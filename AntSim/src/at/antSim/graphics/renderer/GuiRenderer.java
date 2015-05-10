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
import at.antSim.graphics.textures.GuiTexturedModel;
import at.antSim.guiWrapper.GuiContainer;
import at.antSim.guiWrapper.GuiElement;
import at.antSim.guiWrapper.GuiState;

/**GuiRenderer is used to render Gui Elements to the screen.
 * 
 * @author Flo
 *
 */
public class GuiRenderer {

	private GuiShader shader;
	
	/**Create a new {@link GuiRenderer}, loading a quad to hold the gui element's texture.
	 * 
	 * @param loader - an instance of {@link Loader} class
	 */
	public GuiRenderer(Loader loader) {		 
		shader = new GuiShader();
	}
	
	/**Renders a list of {@link GuiTexturedModel}s to the screen.
	 * 
	 * @param guis - a list of {@link GuiTexturedModel}s to be rendered to the screen.
	 */
	public void render(GuiState state) {
		
		shader.start();
		
		//enable alpha blending for transparency
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		//disable depth test for gui drawing -> otherwise a texture behind a transparent texture will not be rendered
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		//gui does not need a view matrix -> view on gui elements stays the same / they are shown in a "static 2d plane"
		for (GuiContainer container : state.getElements()) {
						
			drawGuiElement(container);
			
			for (GuiElement element : container.getChildren()) {
				drawGuiElement(element);
			}
		}
		
		GL11.glEnable(GL11.GL_DEPTH_TEST); //reenable depth test once we're done with drawing our textures
		GL11.glDisable(GL11.GL_BLEND); //disable alpha blending after we're done with our (transparent) textures
		
		//disable vertexAttributeArrays (VBOS holding positions and texture coords) and unbind VAO
		GL20.glDisableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
		
		shader.stop();
	}
	
	/**Draws a gui element on the screen.
	 * 
	 * @param container
	 */
	private void drawGuiElement (GuiElement element) {
		
		System.out.println("drawing " + element + " with textureID " + element.getTextureId());
		
		if (element.getTextureId() >= 0) {
						
			//bind the gui element's VAO (set it as "active"), enable the gui element's positions VBO, bind and activate the cube map's textures
			GL30.glBindVertexArray(element.getRawModel().getVaoID());
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			
			//activate first texture bank 0 -> bank that's used by default by Sampler2d from fragment shader
			GL13.glActiveTexture(GL13.GL_TEXTURE0); 
			//bind texture so it can be used
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, element.getTextureId());
			
			//create and load the texture's transformation matrix
			Matrix4f matrix = Maths.createTransformationMatrix(element.getPosition(), element.getScale());
			shader.loadTransformationMatrix(matrix);
			
			//Render vertices as triangle strip, draw all vertexes, indices are stored as unsigned ints and start rendering at the beginning of the data
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, element.getRawModel().getVertexCount()); //treat positions array as triangle strips
		}
	}

	/**Cleans up the shader program for this renderer.
	 * 
	 */
	public void cleanUp() {
		shader.cleanUp();
	}
}
