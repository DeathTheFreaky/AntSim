package at.antSim.graphics.renderer;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import at.antSim.graphics.entities.Entity;
import at.antSim.graphics.graphicsUtils.Maths;
import at.antSim.graphics.models.RawModel;
import at.antSim.graphics.models.TexturedModel;
import at.antSim.graphics.shaders.EntityShader;
import at.antSim.graphics.textures.ModelTexture;

/**EntityRenderer renders static models.
 * 
 * @author Flo
 *
 */
public class EntityRenderer {
	
	private EntityShader shader;
	
	/**Creates a new EntityRenderer which can be used by {@link MasterRenderer} to delegate rendering functions.
	 * 
	 * @param shader - a {@link EntityShader} used for rendering
	 * @param projectionMatrix - a 4x4 projectionMatrix
	 */
	public EntityRenderer(EntityShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix); //need to make another entity class for moving objects...
		shader.stop();
	}
	
	/**Renders all entities in an efficient way by performing certain operations for the same 3d model only once for all instances.
	 * 
	 * @param entities - a Hashmap of {@link TexturedModel}s mapped to their entity instances
	 * @param blendfactor - 0 means nighttime, 1 means daytime
	 * @param dayFog - a Vector3f of r,g,b fog color for daytime
	 * @param nightFog - a Vector3f of r,g,b fog color for nighttime
	 */
	public void render(Map<TexturedModel, List<Entity>> entities, float blendFactor, Vector3f dayFog, Vector3f nightFog) {
		
		shader.start();
		shader.loadFogColors(dayFog, nightFog);
		shader.loadBlendFactor(blendFactor);
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
		shader.stop();
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
		shader.loadNumberOfRows(texture.getNumberOfRows());
		if (texture.isHasTransparency()) {
			MasterRenderer.disableCulling(); //disable back face culling for transparent textures
		}
		shader.loadFakeLightingVariable(texture.isUseFakeLighting());
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity()); //load shine damper and reflectivity needed for specular lighting in the shader program
		GL13.glActiveTexture(GL13.GL_TEXTURE0); //activate first texture bank 0 -> bank that's used by default by Sampler2d from fragment shader
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID()); //bind texture so it can be used
	}
	
	/**Unbinds an active {@link TexturedModel} once it's finished rendering.
	 * 
	 */
	private void unbindTexturedModel() {
		MasterRenderer.enableCulling(); //be sure to enable culling before next model
		//disable attribute lists and unbind VAO
		GL20.glDisableVertexAttribArray(0); 
		GL20.glDisableVertexAttribArray(1); 
		GL20.glDisableVertexAttribArray(2); 
		GL30.glBindVertexArray(0); 
	}
	
	/**Creates and loads an entity's transformation matrix and the texture offset in a texture atlas into the shader program. 
	 * 
	 * @param entity - the {@link Entity} to be rendered
	 */
	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), 
				entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale()); //transformation matrix to be applied in the shader program
		shader.loadTransformationMatrix(transformationMatrix); //load transformation matrix into the shader program
		shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset()); //offsets could be different for each entity
	}
}
