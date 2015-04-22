package graphicsUtils;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import terrains.Terrain;
import entities.Camera;

/**MousePicker can interact with objects in the 3d World using a 3d ray.<br>
 * <br>
 * CODE EXAMPLES AND THEORY EXPLAINED: http://antongerdelan.net/opengl/raycasting.html
 * 
 * @author Flo
 *
 */
public class MousePicker {
	
	private static final int RECURSION_COUNT = 200; //how often the binary search shall be recursed at a maximum
	private static final float RAY_RANGE = 600; //maximum range for following the mouse ray

	private Vector3f currentRay = new Vector3f();

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
		viewMatrix = Maths.createViewMatrix(camera);
		currentRay = calculateMouseRay();
		if (intersectionInRange(0, RAY_RANGE, currentRay)) {
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
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		Vector2f normalizedCoords = getNormalisedDeviceCoords(mouseX, mouseY);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1f, 1f); //make the ray pointing inwards the screen
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		Vector3f worldRay = toWorldCoords(eyeCoords);
		return worldRay;
	}
	
	/**Converts a 4d coordinates from eyespace to a 3d coordinates in world space.
	 * 
	 * @param eyeCoords - 4d eyespace coordinates
	 * @return - the mouse Ray as a 3d Vector in worldspace
	 */
	private Vector3f toWorldCoords(Vector4f eyeCoords) {
		Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise(); //because it is a direction
		return mouseRay;
	}
	
	/**Converts from clip space 4d coordinates to eye space 4d coordinates.
	 * 
	 * @param clipCoords - 4d coordinates in clip space
	 * @return - 4d coordinates in eye space
	 */
	private Vector4f toEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f); //make z-coordinate point into the screen
	}
	
	/**Converts mouse coordinates from screen coordinates to OpenGL coordinate system (-1 to 1).
	 * 
	 * @param mouseX - x position of mouse in screen coordinates
	 * @param mouseY - y position of mouse in screen coordinates
	 * @return - a Vector2f containing the mouse's x and y coordinates in OpenGL's coordinate system
	 */
	private Vector2f getNormalisedDeviceCoords(float mouseX, float mouseY) {
		float x = (2f * mouseX) / Display.getWidth() - 1f;
		float y = (2f * mouseY) / Display.getHeight() - 1f; //lwjgl uses coordinate system with 0,0 in bottom left corner
		return new Vector2f(x, y);
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
		return terrain;
	}
	
	public Vector3f getCurrentTerrainPoint() {
		return currentTerrainPoint;
	}

	public Vector3f getCurrentRay() {
		return currentRay;
	}

}
