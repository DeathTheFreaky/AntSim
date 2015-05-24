package at.antSim.graphics.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import at.antSim.Globals;
import at.antSim.MainApplication;
import at.antSim.graphics.entities.Camera;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.graphics.entities.Light;
import at.antSim.graphics.graphicsUtils.DisplayManager;
import at.antSim.graphics.graphicsUtils.Loader;
import at.antSim.graphics.models.TexturedModel;
import at.antSim.graphics.shaders.EntityShader;
import at.antSim.graphics.shaders.GuiShader;
import at.antSim.graphics.shaders.TerrainShader;
import at.antSim.graphics.terrains.Terrain;
import at.antSim.guiWrapper.GuiWrapper;

/**MasterRenderer handles all of the rendering code in the application.
 * 
 * @author Flo
 *
 */
public class MasterRenderer {
	
	//set projection matrix parameters
	private static final float FOV = 70; //horizontal (y-Axis) camera opening angle
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
	private GuiRenderer guiRenderer;
	private GuiShader guiShader = new GuiShader();
	
	private float timeFactor = Globals.TIMECYCLE_MULTIPLIER / MainApplication.getInstance().getSpeed();
	
	//all terrains to be rendered
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
		guiRenderer = new GuiRenderer(guiShader);
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
	public void render(List<Light> lights, Camera camera, boolean worldLoaded) {
		
		if (worldLoaded) {
			
			dayNightCycle(); //simulate a day/night cycle, using a blend factor to blend the two settings
			prepare(); //enable depth test and clear screen and depth buffer
				
			//run all sub-renderers' render methods

			entityRenderer.render(blendFactor, DAY_FOG, NIGHT_FOG, lights, camera);		
			terrainRenderer.render(terrains, blendFactor, DAY_FOG, NIGHT_FOG, lights, camera);
			skyboxRenderer.render(camera, blendFactor, DAY_FOG, NIGHT_FOG);
			
			//clear list of terrains and map of entities each frame so they do not build up and up
			terrains.clear();
		}
		
		guiRenderer.render(GuiWrapper.getInstance().getCurrentState());
	}
	
	/**Processed a terrain, adding it to the list of used terrains.
	 * @param terrain - terrain to be processed
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
	
	/**Creates a Projection Matrix with the parameters set in Renderer.<br>
	 * <br>
	 * The following guide on how to set up a projection matrix originates from http://www.songho.ca/opengl/gl_projectionmatrix.html<br>
	 * For additional information on creating a projection matrix see: http://ogldev.atspace.co.uk/www/tutorial12/tutorial12.html<br>
	 * <br>
	 * In perspective projection, a 3D point in a truncated pyramid frustum (eye coordinates) is mapped to a cube (NDC);
	 * the range of x-coordinate from [l, r] to [-1, 1], the y-coordinate from [b, t] to [-1, 1] and the z-coordinate from [n, f] to [-1, 1].<br>
	 * <img src="doc-files/gl_projectionmatrix01.png">
	 * <br>
	 * Note that the eye coordinates are defined in the right-handed coordinate system, but NDC uses the left-handed coordinate system. 
	 * That is, the camera at the origin is looking along -Z axis in eye space, but it is looking along +Z axis in NDC. 
	 * Since glFrustum() accepts only positive values of near and far distances, we need to negate them during the construction of GL_PROJECTION matrix.<br>
	 * <br>
	 * In OpenGL, a 3D point in eye space is projected onto the near plane (projection plane). 
	 * The following diagrams show how a point (xe, ye, ze) in eye space is projected to (xp, yp, zp) on the near plane.<br>
	 * <img src="doc-files/gl_projectionmatrix03.png">
	 * <img src="doc-files/gl_projectionmatrix04.png">
	 * <br>
	 * From the top view of the frustum, the x-coordinate of eye space, xe is mapped to xp, which is calculated by using the ratio of similar triangles;<br><br> 
	 * <img src="doc-files/gl_projectionmatrix_eq01.png"><br>
	 * <br>
	 * From the side view of the frustum, yp is also calculated in a similar way;<br>
	 * <br>
	 * <img src="doc-files/gl_projectionmatrix_eq02.png"><br>
	 * <br>
	 * Note that both xp and yp depend on ze; they are inversely propotional to -ze. In other words, they are both divided by -ze. 
	 * It is a very first clue to construct GL_PROJECTION matrix. 
	 * After the eye coordinates are transformed by multiplying GL_PROJECTION matrix, the clip coordinates are still a homogeneous coordinates. 
	 * It finally becomes the normalized device coordinates (NDC) by dividing by the w-component of the clip coordinates. <br>
	 * <br>
	 * <img src="doc-files/gl_transform08.png"><img src="doc-files/gl_transform12.png"><br>
	 * <br>
	 * Therefore, we can set the w-component of the clip coordinates as -ze. And, the 4th of GL_PROJECTION matrix becomes (0, 0, -1, 0).<br>
	 * <br>
	 * <img src="doc-files/gl_projectionmatrix_eq03.png"><br>
	 * <br>
	 * Next, we map xp and yp to xn and yn of NDC with linear relationship; [l, r] ⇒ [-1, 1] and [b, t] ⇒ [-1, 1].<br>
	 * <br>
	 * <img src="doc-files/gl_projectionmatrix05.png">
	 * <img src="doc-files/gl_projectionmatrix_eq04.png">
	 * <br><br>
	 * <img src="doc-files/gl_projectionmatrix06.png">
	 * <img src="doc-files/gl_projectionmatrix_eq05.png"><br>
	 * <br>
	 * Then, we substitute xp and yp into the above equations.<br>
	 * <br>
	 * <img src="doc-files/gl_projectionmatrix_eq06.png">
	 * <img src="doc-files/gl_projectionmatrix_eq07.png"><br>
	 * <br>
	 * Note that we make both terms of each equation divisible by -ze for perspective division (xc/wc, yc/wc). 
	 * And we set wc to -ze earlier, and the terms inside parentheses become xc and yc of the clip coordinates.<br>
	 * <br>
	 * From these equations, we can find the 1st and 2nd rows of GL_PROJECTION matrix. <br>
	 * <br>
	 * <img src="doc-files/gl_projectionmatrix_eq08.png"><br>
	 * <br>
	 * Now, we only have the 3rd row of GL_PROJECTION matrix to solve. 
	 * Finding zn is a little different from others because ze in eye space is always projected to -n on the near plane. 
	 * But we need unique z value for the clipping and depth test. Plus, we should be able to unproject (inverse transform) it. 
	 * Since we know z does not depend on x or y value, we borrow w-component to find the relationship between zn and ze. 
	 * Therefore, we can specify the 3rd row of GL_PROJECTION matrix like this.<br>
	 * <br>
	 * <img src="doc-files/gl_projectionmatrix_eq10.png"><br>
	 * <br>
	 * In eye space, we equals to 1. Therefore, the equation becomes <br>
	 * <br>
	 * <img src="doc-files/gl_projectionmatrix_eq11.png"><br>
	 * <br>
	 * To map the eyespace coordinates to normalized device coordinates, we need to use a mapping operation composed of two parts: 
	 * First we scale down the range [NearZ, FarZ] down to any range with a width of 2. Then we move (or translate) the range such that it will start at -1. 
	 * Scaling the Z value and then translating it is represented by the general function:<br><br>
	 * <img src="doc-files/12_07.png"><br>
	 * <br>
	 * We know that when Z equals NearZ the result must be -1 and that when Z equals FarZ the result must be 1.<br>
	 * So to find the coefficients, A and B, we use the (ze, zn) relation: (-n, -1) and (-f, 1), and put them into the above equation: <br>
	 * (substituting zn with -1 and ze with -n and substituting zn with 1 and ze with -f; 
	 * notice that we have to negate near n and far f because in eye coordinates the negative z-axis equals the positive z-axis in normalized device coordinates) <br>
	 * <br>
	 * <img src="doc-files/gl_projectionmatrix_eq12.png"><br>
	 * <br>
	 * To solve the equations for A and B, rewrite eq.(1) for B:<br>
	 * <br>
	 * <img src="doc-files/gl_projectionmatrix_eq13.png"><br>
	 * <br>
	 * Substitute eq.(1') to B in eq.(2), then solve for A: <br>
	 * <br>
	 * <img src="doc-files/gl_projectionmatrix_eq14.png"><br>
	 * <br>
	 * Put A into eq.(1) to find B: <br>
	 * <br>
	 * <img src="doc-files/gl_projectionmatrix_eq15.png"><br>
	 * <br>
	 * We found A and B. Therefore, the relation between ze and zn becomes: <br>
	 * <br>
	 * <img src="doc-files/gl_projectionmatrix_eq17.png"><br>
	 * <br>
	 * Finally, we found all entries of GL_PROJECTION matrix. The complete projection matrix is:<br>
	 * <br>
	 * <img src="doc-files/gl_projectionmatrix_eq16.png"><br>
	 * <br>
	 * This projection matrix is for a general frustum. If the viewing volume is symmetric, which is r = -l and t = -b, then it can be simplified as: <br>
	 * <br>
	 * <img src="doc-files/gl_projectionmatrix_eq20.png"><br>
	 * <br>
	 * Finally, we take a look at the relation between ze and zn, once again. 
	 * The relationship between ze and zn is a rational function and is non-linear. 
	 * This means there is very high precision at the near plane, but very little precision at the far plane. 
	 * If the range [-n, -f] is getting larger, it causes a depth precision problem (z-fighting): 
	 * a small change of ze around the far plane does not affect on zn value. 
	 * The distance between n and f should be short as possible to minimize the depth buffer precision problem.<br>
	 * <br> 
	 * <img src="doc-files/gl_projectionmatrix07.png">
	 */
	private void createProjectionMatrix() {
					
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight(); //get aspect ratio defined by the screen's dimensions
				
		float near = NEAR_PLANE; //n
		float far = FAR_PLANE; //f
		
		//looking at frustum from above, right and near span a triangle under the angle FOV/2 with near being the adjacent and right being the opposite
		float right = (float) (near * Math.tan(Math.toRadians(FOV/2))); //t
		float top = right/aspectRatio; //t: according to screen size, height = width/aspectRatio -> top = height/2, right = width/2 for symmetric frustum
		
		projectionMatrix = new Matrix4f(); //creates a new 4x4 matrix, setting all elements to 0 by default
		projectionMatrix.m00 = near/right;
		projectionMatrix.m11 = near/top;
		projectionMatrix.m22 = -(far + near) / (far - near);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -2 * far * near / (far - near);
	}
	
	/**Call cleanUp in MasterRenderer before closing the game to free resources.
	 * 
	 */
	public void cleanUp() {
		entitiyShader.cleanUp();
		terrainShader.cleanUp();
		guiShader.cleanUp();
	}
	
	/**Simulates a simple day/night cycle by calculating the blendFactor.<br>
	 * A blendFactor of 1 means day, 0 means night. Dusk and dawn are in between.
	 * 
	 */
	private void dayNightCycle() {
		
		if (MainApplication.getInstance().isPaused()) {
			time += 0;
		} else {
			time += DisplayManager.getFrameTimeSeconds() * 1000 / timeFactor;
		}
		time %= 24000;
					
		if(time >= 0 && time < 5000) {
			blendFactor = 1f;
		}else if(time >= 5000 && time < 8000) {
			blendFactor = 1 - (time - 5000 )/(8000 - 5000);
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
	
	public void adaptTime(float multiplier, float speed) {
		timeFactor = Globals.TIMECYCLE_MULTIPLIER / speed;
	} 
}
