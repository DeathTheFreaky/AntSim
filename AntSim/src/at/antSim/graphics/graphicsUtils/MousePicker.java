package at.antSim.graphics.graphicsUtils;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import at.antSim.graphics.entities.Camera;
import at.antSim.graphics.terrains.Terrain;

/**MousePicker can interact with objects in the 3d World using a 3d ray.<br>
 * <br>
 * CODE EXAMPLES AND THEORY EXPLAINED: http://antongerdelan.net/opengl/raycasting.html
 * <br>
 * To check intersection with 3D models, we might be using 3d spheres from physics objects to determine intersections,
 * but for now (until physics spheres are finished), we will check with vertex triangles.<br>
 * <br>
 * However, using barycentric coordinates to find the exact intersection of a ray with a 
 * point inside a triangle gives us the best accuracy and is well-suited for terrain intersection.
 * 
 * @author Flo
 *
 */
public class MousePicker {
	
	private static final int RECURSION_COUNT = 200; //how often the binary search shall be recursed at a maximum
	private static final float RAY_RANGE = 600; //maximum range for following the mouse ray

	private Vector3f currentRay = new Vector3f(); //a 3d ray used to determine where the mouse ray intersects with the 3d world's surface

	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Camera camera;
	
	private Terrain terrain;
	private Vector3f currentTerrainPoint;
	
	/**Creates a new {@link MousePicker}.
	 * 
	 * @param cam - the {@link Camera} associated with this {@link MousePicker}
	 * @param projection - the projection matrix associated with this {@link MousePicker}
	 * @param terrain - the {@link Terrain} used for intersection calculation
	 */
	public MousePicker(Camera cam, Matrix4f projection, Terrain terrain) {
		camera = cam;
		projectionMatrix = projection;
		viewMatrix = Maths.createViewMatrix(camera);
		this.terrain = terrain;
	}
	
	/**Updates the currentRay of the MousePicker according to the camera position.<br>
	 * Also updates intersecting terrainPoints.
	 * 
	 */
	public void update() {
		viewMatrix = Maths.createViewMatrix(camera); //update view matrix resulting from current camera position
		currentRay = calculateMouseRay();
		if (intersectionInRange(0, RAY_RANGE, currentRay)) {
			/*
			 * Only if intersection lies in given ray range, perform a binary search to proximate the intersection of the mouse ray and the terrain.
			 */
			currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, currentRay); 
		} else {
			currentTerrainPoint = null;
		}
	}

	/**Calculates mouse ray converting position of the mouse from Viewport Space to World Space.
	 * 
	 * @return - the new currentRay 
	 */
	private Vector3f calculateMouseRay() {
		float mouseX = Mouse.getX(); //eg 1274 - x position of pixel where mouse is currently pointing at
		float mouseY = Mouse.getY(); //eg 863 - y position of pixel where mouse is currently pointing at
		Vector2f normalizedCoords = SpaceTransforms.toNormalisedDeviceCoords(mouseX, mouseY); //calculate the normalized mouse pointer device coordinates within a range of -1 to 1
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1f, 1f); //make the ray pointing inwards the screen (z value of -1)
		Vector4f eyeCoords = SpaceTransforms.toEyeCoords(clipCoords, projectionMatrix);
		Vector3f worldRay = SpaceTransforms.toWorldCoords(eyeCoords, viewMatrix);
		return worldRay;
	}
	
	/**Finds point on the mouse ray that intersect with the terrain. 
	 * 
	 * @param count - how many times the binarySearch algorithm has been applied recursively
	 * @param start - start length of a part of the mouse ray to perform binarySearch on
	 * @param finish - end length of a part of the mouse ray to perform binarySearch on
	 * @param ray - the mouse ray
	 * @return - the intersection point or null if no intersection could be found
	 */
	private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
		/*
		 * Recursively split the ray into two halfs and check in which of the two halfs the intersection lies.
		 * Continue recursive function calls until maximum recursion count is reached. 
		 * The end result will be an approximation of the real intersection point.
		 */
		float half = start + ((finish - start) / 2f);
		if (count >= RECURSION_COUNT) {
			Vector3f endPoint = getPointOnRay(ray, half);
			Terrain terrain = getTerrain(endPoint.getX(), endPoint.getZ());
			if (terrain != null) {
				return endPoint;
			} else {
				return null;
			}
		}
		if (intersectionInRange(start, half, ray)) { //check if intersection is in first half
			return binarySearch(count + 1, start, half, ray); //restart process within first half
		} else {
			return binarySearch(count + 1, half, finish, ray); //restart process within second half
		}
	}
	
	/**Returns a 3D point on a mouse ray at a given distance from the viewer's perspective.
	 * 
	 * @param ray - the mouse ray pointing from the viewer to the mouse pointer's position on screen
	 * @param distance - distance from the viewer's perspective at which a 3D on the mouse ray shall be returned
	 * @return - a point on the mouse ray at a given distance from the viewer's perspective.
	 */
	private Vector3f getPointOnRay(Vector3f ray, float distance) {
		Vector3f camPos = camera.getPosition();
		Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
		/*
		 * Since ray is stored as a normalized direction vector, we can simple multiply its coordinates with the desired distance 
 		 * and add it to a given start point to get a specific end point on the ray.
		 */
		Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance); 
		return Vector3f.add(start, scaledRay, null);
	}

	/**Checks if ray intersects with terrain between start and finish.
	 * 
	 * @param start - length measured from the viewer's perspective from which to start intersection check
	 * @param finish - length measured from the viewer's perspective at which to stop intersection check
	 * @param ray - the mouse ray pointing from the viewer to the mouse pointer's position on screen
	 * @return - true the mouse ray intersects with terrain within start and finish
	 */
	private boolean intersectionInRange(float start, float finish, Vector3f ray) {
		Vector3f startPoint = getPointOnRay(ray, start);
		Vector3f endPoint = getPointOnRay(ray, finish);
		if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
			return true;
		} else {
			return false;
		}
	}

	/**Checks if a certain point in the world lies below the terrain's ground.
	 * 
	 * @param testPoint - a 3D point in world coordinate system
	 * @return - true if testPoint lies below the terrain's ground
	 */
	private boolean isUnderGround(Vector3f testPoint) {
		Terrain terrain = getTerrain(testPoint.getX(), testPoint.getZ());
		float height = 0;
		if (terrain != null) {
			height = terrain.getHeightOfTerrain(testPoint.getX(), testPoint.getZ());
		}
		if (testPoint.y < height) {
			return true;
		} else {
			return false;
		}
	}

	/**Returns one of multiple terrains used in the world.
	 * 
	 * @param worldX - x coordinate in world space
	 * @param worldZ - z coordinate in world space
	 * @return - the Terrain at the position specified in world space
	 */
	private Terrain getTerrain(float worldX, float worldZ) {
		return terrain; //at the moment, we only use one terrain - if we used more than one we would include our terrain spotting logic here
	}
	
	public Vector3f getCurrentTerrainPoint() {
		return currentTerrainPoint;
	}

	public Vector3f getCurrentRay() {
		return currentRay;
	}

}
