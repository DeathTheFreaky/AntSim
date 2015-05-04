package at.antSim.graphics.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import at.antSim.graphics.entities.Camera;
import at.antSim.graphics.entities.Entity;
import at.antSim.graphics.entities.Light;
import at.antSim.graphics.graphicsUtils.DisplayManager;
import at.antSim.graphics.graphicsUtils.Loader;
import at.antSim.graphics.models.TexturedModel;
import at.antSim.graphics.shaders.EntityShader;
import at.antSim.graphics.shaders.TerrainShader;
import at.antSim.graphics.terrains.Terrain;

/**MasterRenderer handles all of the rendering code in the application.
 * 
 * @author Flo
 *
 */
public class MasterRenderer {
	
	//set projection matrix parameters
	private static final float FOV = 70; 
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	//define sky colors = frame clear color (like a background color)
	private static final float RED = 0.5444f;
	private static final float GREEN = 0.62f;
	private static final float BLUE = 0.69f;
	private static final float NIGHT_RED = 0.03f;
	private static final float NIGHT_GREEN = 0.04f;
	private static final float NIGHT_BLUE = 0.05f;
	private static final Vector3f DAY_FOG = new Vector3f(RED, GREEN, BLUE);
	private static final Vector3f NIGHT_FOG = new Vector3f(NIGHT_RED, NIGHT_GREEN, NIGHT_BLUE);
	private static float time = 0;
	private float blendFactor; //used for blending day/night cycle
	
	private Matrix4f projectionMatrix;
	
	//sub renderers and shaders
	private EntityRenderer entityRenderer;
	private EntityShader entitiyShader = new EntityShader();
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	private SkyboxRenderer skyboxRenderer;
	
	//all entities and terrains to be rendered
	/* map of texturedModels, each containing a list of entities using this TexturedModel - 
	 * so a texture needs to be loaded once and then can be applied to all entities using this same texture */
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>(); 
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	
	/**Constructs a {@link MasterRenderer} which manages all rendering and controls sub-renderer classes.
	 * 
	 * @param loader - a {@link Loader} used to load data into OpenGL
	 */
	public MasterRenderer(Loader loader) {
		enableCulling(); //enable backface culling by default
		createProjectionMatrix(); //needs to be created only once
		entityRenderer = new EntityRenderer(entitiyShader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
	}
	
	/**Enables back face culling for non-transparent textures.<br>
	 *(prevent rendering of triangles whose normals point away/face away from the camera)
	 */
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE); 
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	/**Disables back face culling for transparent textures.
	 * 
	 */
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE); 
	}
	
	/**Renders a frame to the screen.
	 * 
	 * @param lights - a list of lightsources
	 * @param camera - for creating a viewMatrix
	 */
	public void render(List<Light> lights, Camera camera) {
		
		dayNightCycle(); //simulate a day/night cycle, using a blend factor to blend the two settings
		prepare(); //enable depth test and clear screen and depth buffer
		
		//run all sub-renderers' render methods
		entityRenderer.render(entities, blendFactor, DAY_FOG, NIGHT_FOG, lights, camera);		
		terrainRenderer.render(terrains, blendFactor, DAY_FOG, NIGHT_FOG, lights, camera);
		skyboxRenderer.render(camera, blendFactor, DAY_FOG, NIGHT_FOG);
		
		//clear list of terrains and map of entities each frame so they do not build up and up
		terrains.clear();
		entities.clear(); 
	}
	
	/**
	 * @param terrain
	 */
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	/**Prepares OpenGL to render a frame of the game.<br>
	 * Should be called every frame.<br>
	 * 
	 */
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST); //enable depth test -> to only show closest vertices (and not the hidden ones)
		GL11.glClearColor(RED, GREEN, BLUE, 1f); //represents skycolor: choose color (R,G,B,A) with which the screen will be cleared
		GL11.glClearDepth(1.0d); //clear depth buffer/reset depth buffer with values of 1
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT); //clears color of screen and depth buffer with values set above
	}
	
	/**Sort entity in the entities Hashmap by their correct {@link TexturedModel} for more efficient rendering.<br>
	 * This way, a texture has to be loaded only once and can then be applied for all entities using the same texture.
	 * 
	 * @param entity - the {@link Entity} to be sorted in
	 */
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch != null) {
			batch.add(entity);
		}
		else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	/**Creates a Projection Matrix with the parameters set in Renderer.
	 * 
	 */
	private void createProjectionMatrix(){
		
		//for further information on creating a projection matrix see: http://ogldev.atspace.co.uk/www/tutorial12/tutorial12.html
		
		/* When we look at our screen, we usually cannot see all the 3D - World at once,
		 * but a rectangular area (called projection window) that has the same proportions as our screen.
		 * 
		 * The aspect ration for this window can be calculated as: aspectRatio = screenWidth / screenHeight.
		 *  
		 * We can choose one dimension (width or height) of our projection window as we wish, and need to adapt the second dimension
		 * according to the aspect ratio (at first). We do so by conveniently defining the projection window's height as 2 (-1 to 1),
		 * since the supposed output of our projection are normalized device coordinates, which range from -1 to 1 
		 * in both the vertical and the horizontal dimension.
		 * 
		 * But wait... that sounds strange! If both dimensions of our output normalized device coordinates range from -1 to 1, 
		 * doesn't this mean that our screen has to be a square? 
		 * Well obviously, no, I cannot think of any screen ratio being 1:1. But how can we then achieve normalized device coordinates?
		 * The answer is not too difficult: we simply have to fit in more information in one of the dimensions, so that a 1 in vertical dimension
		 * does not match a 1 in horizontal dimension in "real" units. The normalized device coordinate units rather function as relative 
		 * relations rather than absolute measured data. This means that a horizontal value of 0.5 does not mean that the resulting coordinate 
		 * is 0.5 centimeters, or inches or whatever to the right of the middle of the screen, but it means that the resulting coordinate
		 * is located at half of the right half of the screen, or 3/4 from the left border of the screen.
		 * 
		 * But we will deal with this mystery later on.
		 * 
		 * First we need to find the distance of the projection window (near plane) from the camera,
		 * since we will need this value for further calculations.
		 * If we look at our projection window from the side (Z-Y plane), we notice that a triangle is spanned by
		 * the distance and half of the window's height (which we defined as 1), denoted by half of the opening angle of alpha.
		 * We can freely chose alpha (we go with 45°), and using some basic trigonometry, we find that 
		 * in our side-way looking projection plane triangle, the distance from the camera to the projection window
		 * functions as the adjacent and half of the window's height as opposite of half our opening angle alpha.
		 * 
		 * Thus, tan(alpha/2) = 1/distance -> and distance = 1/tan(alpha/2).
		 * 
		 * Now we need to find the projected coordinates of a point in the 3D world.
		 * Again we look at our Projection Plane from the side (Z-Y plane). 
		 * We can place our point anywhere inside the triangle spanned by the opening angle,
		 * and between the near plane (which has the distance previously calculated from the camera) and the far plane.
		 * 
		 * According to the rule of similar triangle's, we know that the relations of a triangle's sides to each other do not change,
		 * regardless of the triangle's size, of the triangle's angles stay the same (which is the case here).
		 * 
		 * So our point's y coordinate divided by its distance from the camera equals the y coordinate where the ray from our point to our camera
		 * intersects the Projection window (near plane) divided by the Projection window's distance from the camera, 
		 * or a little more clearly visualized:
		 * 
		 * Point p; (some point in our 3D world visible in our projection frustum)
		 * Point i; (intersection of ray connection Point p and the camera with the near plane)
		 * p.z; (horizontal distance from our camera to the Point p)
		 * i.z = distance d = 1/tan(alpha/2); (horizontal distance from our camera to the near plane)
		 * 
		 * -> i.y / i.z = p.y / p.z = i.y * tan(alpha/2) (they both are spanned under the same angle).
		 * -> i.x / i.z = p.x / p.z = i.x * tan(alpha/2)
		 * 
		 * Now we can calculate p.y and p.z as:
		 * i.y = p.y * i.z / p.z = p.y / (p.z * tan(alpha/2)
		 * i.x = p.x * i.z / p.z = p.x / (p.z * tan(alpha/2)
		 * 
		 * "Since our projection window is 2*aspectRatio (width) by 2 (height) in size we know that a point in the 3D world is inside the window
		 * if it is projected to a point whose projected X component is between -ar and +ar and the projected Y component is between -1 and +1.
		 * So on the Y component we are normalized but on the X component we are not. 
		 * We can get Xp normalized as well by further dividing it by the aspect ratio. 
		 * This means that a point whose projected X component was +ar is now +1 which places it on the right hand side of the normalized box. 
		 * If its projected X component was +0.5 and the aspect ratio was 1.333 (which is what we get on an 1024x768 screen) the new projected X component is 0.375. 
		 * To summarize, the division by the aspect ratio has the effect of condensing the points on the X axis." 
		 * 
		 * We have reached the following projection equations for the X and Y components:
		 * 
		 * i.x = p.x / (aspectRatio * p.z * tan(alpha/2))
		 * i.y = p.y / (p.z * tan(alpha/2))
		 * 
		 * "Before completing the full process let's try to see how the projection matrix would look like at this point. 
		 * This means representing the above using a matrix. Now we run into a problem. 
		 * In both equations we need to divide X and Y by Z which is part of the vector that represents position. 
		 * However, the value of Z changes from one vertex to the next so you cannot place it into one matrix that projects all vertices. 
		 * To understand this better think about the top row vector of the matrix (a, b, c, d). 
		 * We need to select the values of the vector such that the following will hold true: "
		 * 
		 * a*x + b*y + c*z + d*w = x / (z * tan(alpha/2))
		 * 
		 * "This is the dot product operation between the top row vector of the matrix with the vertex position which yields the final X component. 
		 * We can select 'b' and 'd' to be zero but we cannot find an 'a' and 'c' that can be plugged into the left hand side and provide the results on the right hand side. 
		 * The solution adopted by OpenGL is to seperate the transformation into two parts: a multiplication by a projection matrix followed by a division by the Z value as an independant step. 
		 * The matrix is provided by the application and the shader must include the multiplication of the position by it. 
		 * The division by the Z is hard wired into the GPU and takes place in the rasterizer (somewhere between the vertex shader and the fragment shader). 
		 * How does the GPU know which vertex shader output to divide by its Z value? simple - the built-in variable gl_Position is designated for that job. 
		 * Now we only need to find a matrix that represents the projection equations of X & Y above.
		 * 
		 * After multiplying by that matrix the GPU can divide by Z automatically for us and we get the result we want. 
		 * But here's another complexity: if we multiply the matrix by the vertex position and then divide it by Z we literally loose the Z value because it becomes 1 for all vertices. 
		 * The original Z value must be saved in order to perform the depth test later on. 
		 * So the trick is to copy the original Z value into the W component of the resulting vector and divide only XYZ by W instead of Z. 
		 * W maintains the original Z which can be used for depth test. The automatic step of dividing gl_Position by its W is called 'perspective divide'. 
		 * 
		 * As mentioned earlier, we want to include the normalization of the Z value as well to make it easier for the clipper to work without knowing the near and far Z values. 
		 * However, the matrix above turns Z into zero. Knowing that after transforming the vector the system will automatically do perspective divide we need to select the values
		 * of the third row of the matrix such that following the division any Z value within viewing range (i.e. NearZ <= Z <= FarZ) will be mapped to the [-1,1] range. 
		 * Such a mapping operation is composed of two parts. 
		 * First we scale down the range [NearZ, FarZ] down to any range with a width of 2. 
		 * Then we move (or translate) the range such that it will start at -1. 
		 * Scaling the Z value and then translating it is represented by the general function: "
		 * 
		 * f(z) = A*z + B
		 * 
		 * "But following perspective divide the right hand side of the function becomes:"
		 * 
		 * A + B/z
		 * 
		 * "Now we need to find the values of A and B that will perform the maping to [-1,1]. 
		 * We know that when Z equals NearZ the result must be -1 and that when Z equals FarZ the result must be 1. 
		 * Therefore we can write: "
		 * 
		 * A + B/NearZ = -1 -> A = -1 - B /NearZ
		 * A + B/FarZ = 1 -> B/FarZ - 1 - B/NearZ = 1 -> (B*NearZ - B*FarZ) /  (FarZ*NearZ) = 2 
		 * -> (B * (NearZ - FarZ)) / (FarZ * NearZ) = 2 -> B * (NearZ - FarZ) = 2 * FarZ * NearZ -> 
		 * B = (2 * FarZ * NearZ) / (NearZ - FarZ)
		 * 
		 * A = -1 - B/NearZ = -1 - (2 * FarZ * NearZ) / (NearZ * (NearZ - FarZ)) = 
		 * -1 - (2 * FarZ) / (NearZ - FarZ) = (-NearZ + FarZ - 2*FarZ) / (NearZ - FarZ) ->
		 * A = (-NearZ - FarZ) / (NearZ - FarZ)
		 * 
		 * "Now we need to select the third row of the matrix as the vector (a b c d) that will satisfy:"
		 * 
		 * a*X + b*Y + c*Z + d*W = A*Z + B
		 * 
		 * "We can immediately set 'a' and 'b' to be zero because we don't want X and Y to have any effect on the transformation of Z. 
		 * Then our A value can become 'c' and the B value can become 'd' (since W is known to be 1).
		 * Therefore, the final transformation matrix is:"
		 * 
		 * (all except the explicitly stated elements are 0)
		 * m00 = 1 / (aspectRatio * tan(alpha/2))
		 * m11 = 1 / tan(alpha/2)
		 * m22 = (-NearZ - FarZ) / (NearZ - FarZ)
		 * m23 = (2 * FarZ * NearZ) / (NearZ - FarZ)
		 * m32 = 1
		 * 
		 * "After multiplying the vertex position by the projection matrix the coordinates are said to be in Clip Space 
		 * and after performing the perspective divide the coordinates are in NDC Space (Normalized Device Coordinates)."
		 */
		
		float openingAngle = 45; //vertical opening angle of our camera
		
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight(); //get aspect ratio defined by the screen's dimensions
		
		float distanceFromCamera = (float) (1 / Math.tan(Math.toRadians(openingAngle)/2));
		
		System.out.println("distanceFromCamera: " + distanceFromCamera);
		
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f(); //creates a new 4x4 matrix, setting all elements to 0 by default
		projectionMatrix.m00 = (float) (1 / (aspectRatio * Math.tan(Math.toRadians(openingAngle/2))));
		projectionMatrix.m11 = (float) (1 / Math.tan(Math.toRadians(openingAngle/2)));
		projectionMatrix.m22 = (-NEAR_PLANE - FAR_PLANE) / (FAR_PLANE - NEAR_PLANE);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = (2 * FAR_PLANE * NEAR_PLANE) / (NEAR_PLANE - FAR_PLANE);
		
		/*projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = (float) (1 / (aspectRatio * Math.tan(Math.toRadians(openingAngle/2))));
		projectionMatrix.m11 = (float) (1 / Math.tan(Math.toRadians(openingAngle/2)));
	    projectionMatrix.m22 = (-NEAR_PLANE - FAR_PLANE) / (NEAR_PLANE - FAR_PLANE);
	    projectionMatrix.m23 = (2 * FAR_PLANE * NEAR_PLANE) / (NEAR_PLANE - FAR_PLANE);
	    projectionMatrix.m32 = 1.0f;*/
		
	}
	
	/**Call cleanUp in MasterRenderer before closing the game to free resources.
	 * 
	 */
	public void cleanUp() {
		entitiyShader.cleanUp();
		terrainShader.cleanUp();
	}
	
	/**Simulates a simple day/night cycle by calculating the blendFactor.<br>
	 * A blendFactor of 1 means day, 0 means night. Dusk and dawn are in between.
	 * 
	 */
	private void dayNightCycle() {
		
		time += DisplayManager.getFrameTimeSeconds() * 1000;
		time %= 24000;
					
		if(time >= 0 && time < 5000) {
			blendFactor = 1f;
		}else if(time >= 5000 && time < 8000) {
			blendFactor = 1 - (time - 5000)/(8000 - 5000);
		}else if(time >= 8000 && time < 19000) {
			blendFactor = 0f;
		}else if (time >= 19000 && time < 22000) {
			blendFactor = (time - 19000)/(22000 - 19000);
		} else {
			blendFactor = 1f;
		}
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
}
