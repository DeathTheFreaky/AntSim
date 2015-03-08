package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**Renderer renders models from data stored inside VAOs.
 * 
 * @author Flo
 *
 */
public class Renderer {
	
	/**Prepares OpenGL to render the game.<br>
	 * Should be called every frame.<br>
	 * 
	 */
	public void prepare() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT); //clears color of last frame
		GL11.glClearColor(1, 0, 0, 1); //choose color (R,G,B,A) with which the screen will be cleared
	}
	
	/**Renders a RawModel containing a VBA holding the data that shall be rendered to the screen.
	 * 
	 * @param model - a RawModel that shall be rendered to the screen 
	 */
	public void render(RawModel model) {
		GL30.glBindVertexArray(model.getVaoID()); //VAO always need to be bound before it can be used
		GL20.glEnableVertexAttribArray(0); //enable the attributelist with ID of 0 to access its data
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0); // render triangles, draw all vertexes, 
																								 // the indices are stored as unsigned ints and
																								 // start rendering at the beginning of the data
		GL20.glDisableVertexAttribArray(0); //disable the activated attribute list
		GL30.glBindVertexArray(0); //unbind our active VAO
	}

}
