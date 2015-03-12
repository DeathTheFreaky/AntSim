package renderEngine;

import java.util.List;
import java.util.Map;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;
import entities.Entity;

/**Renderer renders models from data stored inside VAOs.
 * 
 * @author Flo
 *
 */
public class Renderer {
	
	//set projection matrix parameters
	private static final float FOV = 70; 
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	private Matrix4f projectionMatrix;
	private StaticShader shader;
	
	/**Creates a new Renderer which can be used my {@link MasterRenderer} to delegate rendering functions.
	 * 
	 * @param shader - a {@link StaticShader} used for rendering
	 */
	public Renderer(StaticShader shader) {
		
		this.shader = shader;
		
		//prevent rendering of triangles whose normals point away/face away from the camera
		GL11.glEnable(GL11.GL_CULL_FACE); 
		GL11.glCullFace(GL11.GL_BACK);
		
		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	/**Prepares OpenGL to render a frame of the game.<br>
	 * Should be called every frame.<br>
	 * 
	 */
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST); //enable depth test -> to only show closest vertices (and not the hidden ones)
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT); //clears color of last frame and depth buffer
		GL11.glClearColor(0, 0, 0, 0); //choose color (R,G,B,A) with which the screen will be cleared
	}
	
	/**Renders all entities in an efficient way by performing certain operations for the same 3d model only once for all instances.
	 * 
	 * @param entities - a Hashmap of {@link TexturedModel}s mapped to their entity instances
	 */
	public void render(Map<TexturedModel,List<Entity>> entities) {
		for (TexturedModel model:entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for(Entity entity:batch) {
				prepareInstance(entity);
				
				// render triangles, draw all vertexes, 																					 
				// the indices are stored as unsigned ints and start rendering at the beginning of the data
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0); 
			}
			unbindTexturedModel();
		}
	}
	
	/**Prepares a {@link TexturedModel} for rendering ONCE for all entity instances of that model.
	 * 
	 * @param model - the {@link TexturedModel} to be rendered
	 */
	private void prepareTexturedModel(TexturedModel model) {
		
		//bind VAO and enable attributelists
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID()); //VAO always need to be bound before it can be used
		GL20.glEnableVertexAttribArray(0); //enable the attributelist with ID of 0 to access positions
		GL20.glEnableVertexAttribArray(1); //enable the attributelist with ID of 1 to access texture coords
		GL20.glEnableVertexAttribArray(2); //enable the attributelist with ID of 2 to access normals
		
		//load shine variables for specular lighting; load, activate and bind model texture
		ModelTexture texture = model.getTexture();
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity()); //load shine damper and reflectivity needed for specular lighting in the shader program
		GL13.glActiveTexture(GL13.GL_TEXTURE0); //activate first texture bank 0 -> bank that's used by default by Sampler2d from fragment shader
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID()); //bind texture so it can be used
	}
	
	/**Unbinds an active {@link TexturedModel} once it's finished rendering.
	 * 
	 */
	private void unbindTexturedModel() {
		
		//disable attribute lists and unbind VAO
		GL20.glDisableVertexAttribArray(0); 
		GL20.glDisableVertexAttribArray(1); 
		GL20.glDisableVertexAttribArray(2); 
		GL30.glBindVertexArray(0); 
	}
	
	/**Prepares an entity for rendering. 
	 * 
	 * @param entity - the {@link Entity} to be rendered
	 */
	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), 
				entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale()); //transformation matrix to be applied in the shader program
		shader.loadTransformationMatrix(transformationMatrix); //load transformation matrix into the shader program
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

}
