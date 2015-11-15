package at.antSim.graphics.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import at.antSim.MainApplication;
import at.antSim.graphics.entities.Camera;
import at.antSim.graphics.entities.Light;
import at.antSim.graphics.graphicsUtils.OpenGLLoader;
import at.antSim.graphics.graphicsUtils.TransparentTriangle;
import at.antSim.graphics.models.ModelData;
import at.antSim.graphics.models.RawModel;
import at.antSim.graphics.models.TexturedModel;
import at.antSim.graphics.shaders.EntityShader;
import at.antSim.graphics.textures.ModelTexture;
import at.antSim.objectsKI.Entity;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;
import at.antSim.utils.Maths;
import at.antSim.utils.Pair;

/**EntityRenderer renders static models.
 * 
 * @author Flo
 *
 */
public class EntityRenderer {
	
	private EntityShader shader;
	private final Vector3f COLLIDING_COLOR = new Vector3f(1,0,0);
	private final Vector3f NOT_COLLIDING_COLOR = new Vector3f(0,1,0);
	private HashMap<Entity, Matrix4f> transforms = new HashMap<>();
	
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
		
		renderTransparents();
		
		transforms.clear();
		
		shader.stop();
	}

	/**Renders transparent triangles sorted from the furthest away from to the closest to the camera.
	 * In order to reduce number of state changes, indices from the same Entity are accumulated and drawn in one draw call when the currently rendered Entity changes.
	 * 
	 * I tried two approaches: 
	 * 	using single draw calls for every triangle (works just fine)
	 * 	cumulating the triangles from one entity and draw them in one draw call, by rewriting the indices buffer each time (causes triangles to flicker)
	 * 
	 * So I used the single draw approach but the other approach is still commented out for demonstration purposes.
	 * 
	 * In order to achieve perfect transparency for overlapping transparent entities, one would have to split triangles but that would be a little too much complication for this demo.
	 * 
	 */
	private void renderTransparents()
	{
		// enable blending and set blend function to simulate transparency in openGL
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		// disable coloring of ghost sphere with moving entity indicator blends (red if collides, green if does not collide)
		shader.loadMovingEntityBlend(0.0f);

		Entity currentEntity = null;
		/*ArrayList<Integer> cumulatedIndices = new ArrayList<Integer>();*/
		
		// disable depth mask so that transparent objects behind other transparent objects are not discarded due to z-buffer test
		GL11.glDepthMask(false);
								
		for (Pair<Entity, TransparentTriangle> triangle : Entity.getTransparentTriangles())
		{
			int idxOffset = triangle.getValue().getIndexBufferOffset();
			ModelData data = triangle.getKey().getGraphicsEntity().getModel().getRawModel().getModelData();
			
			if (currentEntity != triangle.getKey()) // entity has changed -> load new VAO and shader variables
			{
				/*if (currentEntity != null)
				{
					rewriteIndicesBufferAndDraw(cumulatedIndices, triangle.getKey().getGraphicsEntity().getModel().getRawModel().getIndicesID());
					cumulatedIndices.clear();
				}*/
				
				unbindTexturedModel(); // unbind previously used Textured Model and VAO
				
				// bind new Textured Model and VAO
				prepareTexturedModel(triangle.getKey().getGraphicsEntity().getModel()); //lots of state changes, but unavoidable...
				prepareInstance(triangle.getKey()); //load transformation matrix and texture atlas offset
			}
			
			currentEntity = triangle.getKey();
			
			/*cumulatedIndices.add(data.getIndices()[idxOffset]);
			cumulatedIndices.add(data.getIndices()[idxOffset + 1]);
			cumulatedIndices.add(data.getIndices()[idxOffset + 2]);*/
						
			GL11.glDrawElements(GL11.GL_TRIANGLES, 3, GL11.GL_UNSIGNED_INT, idxOffset * 4);			
		}
		
		unbindTexturedModel();
		
		GL11.glDepthMask(true);
				
		GL11.glDisable(GL11.GL_BLEND); //disable alpha blending after we're done with our (transparent) textures
	}
	
	/**To reduce number of draw calls, rewrite the index buffer bound to the active VAO for as many triangles in a row as the transparent Entity stays the same.
	 * 
	 * @param cumulatedIndices
	 */
	private void rewriteIndicesBufferAndDraw(ArrayList<Integer> cumulatedIndices, int indicesID)
	{
		int[] indicesBuffer = new int[cumulatedIndices.size()];
		
		for (int i = 0; i < cumulatedIndices.size(); i++)
		{
			indicesBuffer[i] = cumulatedIndices.get(i);
		}
				
		OpenGLLoader.setNewIndicesBuffer(indicesBuffer, indicesID);
		
		//so this is a little confusing... the offset is measured in bytes -> an int has 4 bytes, its mentioned nowhere in the lwjgl documentation
		GL11.glDrawElements(GL11.GL_TRIANGLES, cumulatedIndices.size() * 3, GL11.GL_UNSIGNED_INT, 0);
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
	
	private void prepareTexturedModel(TexturedModel model)
	{
		prepareTexturedModel(model, false);
	}

	/**Prepares a {@link TexturedModel} for rendering ONCE for all entity instances of that model.
	 * 
	 * @param model - the {@link TexturedModel} to be rendered
	 * @param disableBackfaceCulling - true to disable Backface Culling for transparent entities
	 */
	private void prepareTexturedModel(TexturedModel model, boolean disableBackfaceCulling) {
		
		//bind VAO and enable attributelists
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID()); //VAO always need to be bound before it can be used
		GL20.glEnableVertexAttribArray(0); //enable the attributelist with ID of 0 to access positions
		GL20.glEnableVertexAttribArray(1); //enable the attributelist with ID of 1 to access texture coords
		GL20.glEnableVertexAttribArray(2); //enable the attributelist with ID of 2 to access normals
		
		//load, activate and bind model texture 
		ModelTexture texture = model.getTexture();
		shader.loadNumberOfRows(texture.getNumberOfRows());
		if (texture.isHasTransparency() || disableBackfaceCulling) {
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
		if (!transforms.containsKey(entity))
		{
			ReadOnlyPhysicsObject physicsObject = (ReadOnlyPhysicsObject) entity.getPhysicsObject();
			Matrix4f transformationMatrix = Maths.createTransformationMatrix(Maths.vec3fToSlickUtil(physicsObject.getPosition()), 
					physicsObject.getRotationDegrees().x, physicsObject.getRotationDegrees().y, physicsObject.getRotationDegrees().z, 
					entity.getGraphicsEntity().getScale()); //transformation matrix to be applied in the shader program
			transforms.put(entity, transformationMatrix);
		}
		shader.loadTransformationMatrix(transforms.get(entity)); //load transformation matrix into the shader program
		shader.loadOffset(entity.getGraphicsEntity().getTextureXOffset(), entity.getGraphicsEntity().getTextureYOffset()); //offsets could be different for each entity
		shader.loadTransparency(entity.getGraphicsEntity().getTransparency());
	}
}
