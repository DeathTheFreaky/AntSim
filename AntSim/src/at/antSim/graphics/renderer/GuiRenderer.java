package at.antSim.graphics.renderer;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import at.antSim.graphics.graphicsUtils.Loader;
import at.antSim.graphics.graphicsUtils.Maths;
import at.antSim.graphics.models.RawModel;
import at.antSim.graphics.shaders.GuiShader;
import at.antSim.graphics.textures.GuiTexture;

/**GuiRenderer is used to render Gui Elements to the screen.
 * 
 * @author Flo
 *
 */
public class GuiRenderer {

	private final RawModel quad; //gui elements are usually stored as flat quads / rectangles
	private GuiShader shader;
	
	/**Create a new {@link GuiRenderer}, loading a quad to hold the gui element's texture.
	 * 
	 * @param loader - an instance of {@link Loader} class
	 */
	public GuiRenderer(Loader loader) {
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 }; //using triangle strips, only the four corners of a rectangle need to be defined (instead of each corner of both of the two triangles)
		quad = loader.loadToVAO(positions, 2);
		shader = new GuiShader();
	}
	
	/**Renders a list of {@link GuiTexture}s to the screen.
	 * 
	 * @param guis - a list of {@link GuiTexture}s to be rendered to the screen.
	 */
	public void render(List<GuiTexture> guis) {
		
		shader.start();
		
		//bind the gui element's VAO (set it as "active"), enable the gui element's positions VBO, bind and activate the cube map's textures
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		
		//enable alpha blending for transparency
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		//disable depth test for gui drawing -> otherwise a texture behind a transparent texture will not be rendered
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		//gui does not need a view matrix -> view on gui elements stays the same / they are shown in a "static 2d plane"
		
		for (GuiTexture gui: guis) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0); 
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
			Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale());
			shader.loadTransformationMatrix(matrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount()); //treat positions array as triangle strips
		}
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND); //disable alpha blending which we don't to use all the time
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	/**Cleans up the shader program for this renderer.
	 * 
	 */
	public void cleanUp() {
		shader.cleanUp();
	}
}
