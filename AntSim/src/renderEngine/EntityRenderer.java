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

/**EntityRenderer renders static models.
 * 
 * @author Flo
 *
 */
public class EntityRenderer {
	
	private StaticShader shader;
	
	/**Creates a new EntityRenderer which can be used by {@link MasterRenderer} to delegate rendering functions.
	 * 
	 * @param shader - a {@link StaticShader} used for rendering
	 * @param projectionMatrix - a 4x4 projectionMatrix
	 */
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
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
				loadModelMatrix(entity);
				
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
	
	/**Creates and loads an entity's transformation matrix into the shader program. 
	 * 
	 * @param entity - the {@link Entity} to be rendered
	 */
	private void loadModelMatrix(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), 
				entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale()); //transformation matrix to be applied in the shader program
		shader.loadTransformationMatrix(transformationMatrix); //load transformation matrix into the shader program
	}
}
