package at.antSim.graphics.renderer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.vecmath.Quat4f;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import at.antSim.MainApplication;
import at.antSim.graphics.entities.Camera;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.graphics.entities.Light;
import at.antSim.graphics.models.RawModel;
import at.antSim.graphics.models.TexturedModel;
import at.antSim.graphics.shaders.EntityShader;
import at.antSim.graphics.textures.ModelTexture;
import at.antSim.objectsKI.Entity;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;
import at.antSim.utils.Maths;

/**EntityRenderer renders static models.
 * 
 * @author Flo
 *
 */
public class EntityRenderer {
	
	private EntityShader shader;
	private final Vector3f COLLIDING_COLOR = new Vector3f(1,0,0);
	private final Vector3f NOT_COLLIDING_COLOR = new Vector3f(0,1,0);
	
	/**Creates a new EntityRenderer which can be used by {@link MasterRenderer} to delegate rendering functions.
	 * 
	 * @param shader - a {@link EntityShader} used for rendering
	 * @param projectionMatrix - a 4x4 projectionMatrix
	 */
	public EntityRenderer(EntityShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix); //projection does not change - load only once
		shader.stop();
	}
	
	/**Renders all entities in an efficient way by performing certain operations for the same 3d model 
	 * (like loading of positions, normals, texture coords, textures) only once for all instances.
	 * 
	 * @param entities - a Hashmap of {@link TexturedModel}s mapped to their entity instances
	 * @param blendfactor - 0 means nighttime, 1 means daytime
	 * @param dayFog - a Vector3f of r,g,b fog color for daytime
	 * @param nightFog - a Vector3f of r,g,b fog color for nighttime
	 * @param lights - a list of lightsources
	 * @param camera - for creating a viewMatrix
	 */
	public void render(float blendFactor, float ambientLightIntensity, Vector3f dayFog, Vector3f nightFog, List<Light> lights, Camera camera) {
		
		shader.start(); 
		
		//load uniform variables into shader program
		shader.loadFogColors(dayFog, nightFog);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		shader.loadBlendFactor(blendFactor);
		shader.loadAmbientLightIntensity(ambientLightIntensity);
		
		LinkedList<TexturedModel> opaques = new LinkedList<TexturedModel>();
		LinkedList<TexturedModel> transparents = new LinkedList<TexturedModel>();
		
		for (TexturedModel model : Entity.getUnmodifiableRenderingMap().keySet()) {
			if (model.usesTransparency()) {
				transparents.add(model);
			} else {
				opaques.add(model);
			}
		}
		
		//first render opaques, then render transparents
		for (TexturedModel model : opaques) {
			renderBatch(model);
		}
		
		LinkedList<Entity> transparentEntities = Entity.getTransparentsList();
		
		renderTransparents(transparentEntities); //does not work quite as intended, but is not that important and no time to change left
		
		shader.stop();
	}
	
	/**Renders all transparent Entities, sorted by WorldPosition z value of their center, starting from 0 to increasing negative values.
	 * @param transparentEntities
	 */
	private void renderTransparents(LinkedList<Entity> transparentEntities) {
				
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		for(Entity entity:transparentEntities) {
			
			prepareTexturedModel(entity.getGraphicsEntity().getModel()); //lots of state changes, but unavoidable...
			
			prepareInstance(entity); //load transformation matrix and texture atlas offset
			
			if (MainApplication.getInstance().getMovingEntity().getEntity() == entity) {
				shader.loadMovingEntityBlend(0.75f);
				if (MainApplication.getInstance().getMovingEntity().isColliding()) {
					shader.loadMovingEntityColor(COLLIDING_COLOR);
				} else {
					shader.loadMovingEntityColor(NOT_COLLIDING_COLOR);
				}
			} else {
				shader.loadMovingEntityBlend(0.0f);
			}
			
			//Render indexed vertices as triangles, draw all vertexes, indices are stored as unsigned ints and start rendering at the beginning of the data
			GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getGraphicsEntity().getModel().getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			
			//"restore the defaults" -> enable culling, disable vertexAttributeArrays (VBOS holding position, normals, texture coords) and unbind VAO
			unbindTexturedModel();
		}
		
		GL11.glDisable(GL11.GL_BLEND); //disable alpha blending after we're done with our (transparent) textures
		
	}

	/**Renders a batch of same models.
	 * 
	 * @param model
	 * @param transparent
	 */
	private void renderBatch(TexturedModel model) {
		
		/*Once for each unique model: load model's texture (by binding it to texture bank) and positions, normals, texture coordinates (as VBOs inside VAO) into OpenGL 
		* and load other model attributes as uniform variables into shader program */
		prepareTexturedModel(model);
		
		/* For every instance of a unique model: prepare the instance by loading its transformation matrix and its texture atlas offset (if needed)
		 * and draw the model.
		 */
		List<Entity> batch = Entity.getUnmodifiableRenderingMap().get(model);		
		
		for(Entity entity:batch) {
			prepareInstance(entity); //load transformation matrix and texture atlas offset
			
			if (MainApplication.getInstance().getMovingEntity().getEntity() == entity) {
				shader.loadMovingEntityBlend(0.75f);
				if (MainApplication.getInstance().getMovingEntity().isColliding()) {
					shader.loadMovingEntityColor(COLLIDING_COLOR);
				} else {
					shader.loadMovingEntityColor(NOT_COLLIDING_COLOR);
				}
			} else {
				shader.loadMovingEntityBlend(0.0f);
			}
			
			//Render indexed vertices as triangles, draw all vertexes, indices are stored as unsigned ints and start rendering at the beginning of the data
			GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0); 
		}
		
		//"restore the defaults" -> enable culling, disable vertexAttributeArrays (VBOS holding position, normals, texture coords) and unbind VAO
		unbindTexturedModel();
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
		
		//load, activate and bind model texture 
		ModelTexture texture = model.getTexture();
		shader.loadNumberOfRows(texture.getNumberOfRows());
		if (texture.isHasTransparency()) {
			MasterRenderer.disableCulling(); //disable back face culling for transparent textures
		}
		
		//load isUseFakeLighting, indicating whether to use fake lighting or not (all normals pointing upwards, eg. for grass which would make weird shadows otherwise)
		shader.loadFakeLightingVariable(texture.isUseFakeLighting());
		//load shine damper and reflectivity needed for specular lighting in the shader program
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity()); 
		
		//activate first texture bank 0 -> bank that's used by default by Sampler2d from fragment shader
		GL13.glActiveTexture(GL13.GL_TEXTURE0); 
		//bind texture so it can be used
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID()); 
	}
	
	/**Unbinds an active {@link TexturedModel} once it's finished rendering by re-enabling culling if it has been disabled for transparent models,
	 * disabling the VertexAttributeArrays (which hold VBOS with positions, normals, texture coords) and unbinding the VAO.
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
		ReadOnlyPhysicsObject physicsObject = (ReadOnlyPhysicsObject) entity.getPhysicsObject();
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(Maths.vec3fToSlickUtil(physicsObject.getPosition()), 
				physicsObject.getRotationDegrees().x, physicsObject.getRotationDegrees().y, physicsObject.getRotationDegrees().z, 
				entity.getGraphicsEntity().getScale()); //transformation matrix to be applied in the shader program
		shader.loadTransformationMatrix(transformationMatrix); //load transformation matrix into the shader program
		shader.loadOffset(entity.getGraphicsEntity().getTextureXOffset(), entity.getGraphicsEntity().getTextureYOffset()); //offsets could be different for each entity
		shader.loadTransparency(entity.getGraphicsEntity().getTransparency());
	}
}
