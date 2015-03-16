package renderEngine;

import java.util.List;

import models.RawModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import shaders.TerrainShader;
import terrains.Terrain;
import textures.ModelTexture;
import toolbox.Maths;

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
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	/**Renders all terrains in terrains list to the screen.
	 * 
	 * @param terrains - a list of {@link Terrain}s
	 */
	public void render(List<Terrain> terrains) {
		for(Terrain terrain:terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			
			// render triangles, draw all vertexes, 																					 
			// the indices are stored as unsigned ints and start rendering at the beginning of the data
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0); 
			unbindTerrain();
		}
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
		
		//load shine variables for specular lighting; load, activate and bind model texture
		ModelTexture texture = terrain.getTexture();
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity()); //load shine damper and reflectivity needed for specular lighting in the shader program
		GL13.glActiveTexture(GL13.GL_TEXTURE0); //activate first texture bank 0 -> bank that's used by default by Sampler2d from fragment shader
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getTexture().getID()); //bind texture so it can be used
	}
	
	/**Unbinds an active {@link Terrain} once it's finished rendering.
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
	private void loadModelMatrix(Terrain terrain) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(),0,terrain.getZ()), 
				0, 0, 0, 1); //transformation matrix to be applied in the shader program
		shader.loadTransformationMatrix(transformationMatrix); //load transformation matrix into the shader program
	}
}
