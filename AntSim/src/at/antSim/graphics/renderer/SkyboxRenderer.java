package at.antSim.graphics.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import at.antSim.graphics.entities.Camera;
import at.antSim.graphics.graphicsUtils.OpenGLLoader;
import at.antSim.graphics.models.RawModel;
import at.antSim.graphics.shaders.SkyboxShader;

/**SkyboxRenderer is used to render a skybox to the screen.
 * 
 * @author Flo
 *
 */
public class SkyboxRenderer {
	
	private static final float SIZE = 500f; //length of the skybox cube's sides
	
	//the skybox vertices creation code has been taken from https://www.dropbox.com/sh/trn3cugng9ahec1/AAARqfvCzsEcu7mxocjqPT39a/Skybox%20Vertex%20Positions.txt?dl=0
	//6 sides of a cube, each side made up of 2 triangles
	private static final float[] VERTICES = {        
	    -SIZE,  SIZE, -SIZE,
	    -SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	    -SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE
	};
	
	//in OpenGL's CubeMap, the different textures have to be loaded in the order defined below
	private static String[] TEXTURE_FILES = {"skyboxes/right", "skyboxes/left", "skyboxes/top", "skyboxes/bottom", "skyboxes/back", "skyboxes/front"};
	private static String[] NIGHT_TEXTURE_FILES = {"skyboxes/nightRight", "skyboxes/nightLeft", "skyboxes/nightTop", "skyboxes/nightBottom", "skyboxes/nightBack", "skyboxes/nightFront"};
	
	private RawModel cube;
	private int dayTexture;
	private int nightTexture;
	private SkyboxShader shader;
	
	
	/**Creates a new {@link SkyboxRenderer}.
	 * 
	 * @param loader - a {@link OpenGLLoader} to load data into OpenGL
	 * @param projectionMatrix
	 */
	public SkyboxRenderer(OpenGLLoader loader, Matrix4f projectionMatrix) {
		
		//load cube's geometric data into VBO and the VBO into a VAO
		cube = loader.loadToVAO(VERTICES, 3);
		
		//load cube map textures into OpenGL
		dayTexture = loader.loadCubeMap(TEXTURE_FILES);
		nightTexture = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
		
		shader = new SkyboxShader();
		shader.start();
		
		//tell samplers in shaderprogram in which texture banks the cube map's textures are located
		shader.connectTextureUnits(); 
		
		//load the projection matrix into the shader program - needs to be done only once, since projection matrix does not change
		shader.loadProjectionMatrix(projectionMatrix);

		shader.stop();
	}
	
	/**Renders the skybox to the screen.
	 * 
	 * @param camera - the {@link Camera} the skybox is following
	 * @param blendfactor - 0 means nighttime, 1 means daytime
	 * @param dayFog - a Vector3f of r,g,b fog color for daytime
	 * @param nightFog - a Vector3f of r,g,b fog color for nighttime
	 */
	public void render(Camera camera, float blendFactor, Vector3f dayFog, Vector3f nightFog) {		
		
		shader.start();
		
		//load uniform variables into shader program for every frame -> cause view matrix and fog colors can change
		shader.loadViewMatrix(camera);
		shader.loadFogColors(dayFog, nightFog);
		
		//bind the cube map's VAO (set it as "active"), enable the cube map's positions VBO, bind and activate the cube map's textures
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		bindTextures(blendFactor);
		
		//draw the cubemap interpreting the position data in the vbo as triangles, starting at the first vertex and drawing all vertices
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		
		//disable active VBO and unbind active VAO
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		
		shader.stop();
	}
	
	/**Binds textures to texture units, activates the associated texture banks and loads up the blend factor into the shader program.<br>
	 * 
	 * @param blendFactor - 0 (night) to 1 (day)
	 */
	private void bindTextures(float blendFactor){
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, dayTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, nightTexture);
		shader.loadBlendFactor(blendFactor);
	}
}
