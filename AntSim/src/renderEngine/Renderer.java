package renderEngine;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import shaders.StaticShader;
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
	
	public Renderer(StaticShader shader) {
		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	/**Prepares OpenGL to render the game.<br>
	 * Should be called every frame.<br>
	 * 
	 */
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST); //enable depth test -> to only show closest vertices (and not the hidden ones)
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT); //clears color of last frame and depth buffer
		GL11.glClearColor(1f, 0, 0, 1); //choose color (R,G,B,A) with which the screen will be cleared
	}
	
	/**Renders an Entity to the screen, applying the given shader program.
	 * 
	 * @param entity - the Entity to render to the screen
	 * @param shader - the StaticShader to apply 
	 * @see Entity
	 * @see StaticShader
	 */
	public void render(Entity entity, StaticShader shader) {
		TexturedModel model = entity.getModel();
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID()); //VAO always need to be bound before it can be used
		GL20.glEnableVertexAttribArray(0); //enable the attributelist with ID of 0 to access its data
		GL20.glEnableVertexAttribArray(1); //enable the attributelist with ID of 1 to access its data
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), 
				entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale()); //transformation matrix to be applied in the shader program
		shader.loadTransformationMatrix(transformationMatrix); //load transformation matrix into the shader program
		GL13.glActiveTexture(GL13.GL_TEXTURE0); //activate first texture bank 0 -> bank that's used by default by Sampler2d from fragment shader
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID()); //bind texture so it can be used
		GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0); // render triangles, draw all vertexes, 																					 // the indices are stored as unsigned ints and
																								 // start rendering at the beginning of the data
		GL20.glDisableVertexAttribArray(0); //disable the activated attribute list 0
		GL20.glDisableVertexAttribArray(1); //disable the activated attribute list 1
		GL30.glBindVertexArray(0); //unbind our active VAO
		
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
