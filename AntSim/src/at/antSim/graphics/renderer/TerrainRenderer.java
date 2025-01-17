package at.antSim.graphics.renderer;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import at.antSim.graphics.entities.Camera;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.graphics.entities.Light;
import at.antSim.graphics.models.RawModel;
import at.antSim.graphics.models.TexturedModel;
import at.antSim.graphics.shaders.TerrainShader;
import at.antSim.graphics.terrains.Terrain;
import at.antSim.graphics.textures.TerrainTexturePack;
import at.antSim.utils.Maths;

/**TerrainRenderer renders terrains.
 * 
 * @author Flo
 *
 */
public class TerrainRenderer {
	
	private TerrainShader shader;
	
	/**Creates a TerrainRenderer.
	 * 
	 * @param shader - a {@link TerrainShader}
	 * @param projectionMatrix - a 4x4 projectionMatrix
	 */
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix); //projectionMatrix does not change - needs to be calculated only once
		shader.connectTextureUnits(); //connect sample2Ds to texture units only once - stay connected throughout the application
		shader.stop();
	}
	
	/**Renders all terrains in terrains list to the screen.
	 * 
	 * @param terrains - a list of {@link Terrain}s
	 * @param blendfactor - 0 means nighttime, 1 means daytime
	 * @param dayFog - a Vector3f of r,g,b fog color for daytime
	 * @param nightFog - a Vector3f of r,g,b fog color for nighttime
	 * @param lights - a list of lightsources
	 * @param camera - for creating a viewMatrix
	 */
	public void render(List<Terrain> terrains, float blendFactor,  float ambientLightIntensity, Vector3f dayFog, Vector3f nightFog, List<Light> lights, Camera camera) {
		
		shader.start();
		
		//load uniform variable into shader program
		shader.loadFogColors(dayFog, nightFog);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		shader.loadBlendFactor(blendFactor);
		shader.loadAmbientLightIntensity(ambientLightIntensity);
		
		for(Terrain terrain : terrains) {
			
			/*load terrain's textures (by binding them to texture banks) and positions, normals, texture coordinates (as VBOs inside VAO) into OpenGL 
			* and load other model attributes as uniform variables into shader program */
			prepareTerrain(terrain);
			
			/*Create and load a terrain's transformation matrix into the shader program */
			loadTransformationMatrix(terrain);
			
			//Render indexed vertices as triangles, draw all vertexes, indices are stored as unsigned ints and start rendering at the beginning of the data
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0); 
			
			//"restore the defaults" -> disable vertexAttributeArrays (VBOS holding position, normals, texture coords) and unbind VAO
			unbindTerrain();
		}
		
		shader.stop();
	}
	
	/**Prepares a {@link Terrain} for rendering ONCE for all entity instances of that model.
	 * 
	 * @param model - the {@link Terrain} to be rendered
	 */
	private void prepareTerrain(Terrain terrain) {
		
		//bind VAO and enable attributelists
		RawModel rawModel = terrain.getModel();
		GL30.glBindVertexArray(rawModel.getVaoID()); //VAO always need to be bound before it can be used
		GL20.glEnableVertexAttribArray(0); //enable the attributelist with ID of 0 to access positions
		GL20.glEnableVertexAttribArray(1); //enable the attributelist with ID of 1 to access texture coords
		GL20.glEnableVertexAttribArray(2); //enable the attributelist with ID of 2 to access normals
		
		//a terrain may have several different textures and they need to be bound to different texture banks
		bindTextures(terrain);
		
		//load shine variables for specular lighting; load, activate and bind model texture
		shader.loadShineVariables(1, 0); //load shine damper and reflectivity needed for specular lighting in the shader program
	}
	
	/**Binds all the textured used on this terrain, as well as the blend map, to different texture units.
	 * 
	 * @param terrain - the {@link Terrain} holding the blendMap and the {@link TerrainTexturePack}
	 */
	private void bindTextures(Terrain terrain) {
		TerrainTexturePack texturePack = terrain.getTexturePack();
		
		//bind textures to the different texture units
		GL13.glActiveTexture(GL13.GL_TEXTURE0); 
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1); 
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2); 
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE3); 
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE4); 
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());
	}
	
	/**Unbinds an active {@link Terrain} once it's finished rendering by disabled its VertexAttribArrays 
	 * (which hold VBOs storing positions, normals, texture coords) and unbinding the VAO.
	 * 
	 */
	private void unbindTerrain() {
		
		//disable attribute lists and unbind VAO
		GL20.glDisableVertexAttribArray(0); 
		GL20.glDisableVertexAttribArray(1); 
		GL20.glDisableVertexAttribArray(2); 
		GL30.glBindVertexArray(0); 
	}
	
	/**Creates and loads a terrains transformation matrix. 
	 * 
	 * @param entity - the {@link Terrain} to be rendered
	 */
	private void loadTransformationMatrix(Terrain terrain) {
		
		//create transformation matrix to be applied to the terrain
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(),0,terrain.getZ()), 
				0, 0, 0, 1);
		//load transformation matrix into the shader program
		shader.loadTransformationMatrix(transformationMatrix); 
	}
}
