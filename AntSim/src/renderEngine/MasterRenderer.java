package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;
import shaders.TerrainShader;
import skybox.SkyboxRenderer;
import terrains.Terrain;

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
	private static final float RED = 0.4f;
	private static final float GREEN = 0.75f;
	private static final float BLUE = 1.0f;
	
	private Matrix4f projectionMatrix;
	
	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;
	
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	private SkyboxRenderer skyboxRenderer;
	
	/**Constructs a {@link MasterRenderer} which manages all rendering and controls sub-renderer classes.
	 * 
	 * @param loader - a {@link Loader} used to load data into OpenGL
	 */
	public MasterRenderer(Loader loader) {
		enableCulling();
		createProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
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
		prepare();
		shader.start();
		shader.loadSkyColor(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		terrainShader.start();
		terrainShader.loadSkyColor(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		skyboxRenderer.render(camera);
		terrains.clear();
		entities.clear(); //entities needs to be clear every frame, otherwise the entities we build up and up every frame
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
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT); //clears color of last frame and depth buffer
		GL11.glClearColor(RED, GREEN, BLUE, 1f); //represents skycolor: choose color (R,G,B,A) with which the screen will be cleared
	}
	
	/**Sort entity in the entities Hashmap by their correct {@link TexturedModel} for more efficient rendering.
	 * 
	 * @param entity - the {@link Entity} to be sorted in
	 */
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch!=null) {
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
		
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
	
	/**Call cleanUp in MasterRenderer before closing the game to free resources.
	 * 
	 */
	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
	}
}
