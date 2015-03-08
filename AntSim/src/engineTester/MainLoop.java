package engineTester;

import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.RawModel;
import renderEngine.Renderer;

/**MainLoop holds the main game loop containing the main game logic and handles the initialization and destruction of the game.
 * <br>
 * The main game loop updates all objects and does the rendering for every frame.
 * 
 * @author Flo
 *
 */
public class MainLoop {
	
	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		
		// draw a cube for testing purposes
		float[] vertices = {
				-0.5f, 0.5f, 0, 	//V0
				-0.5f, -0.5f, 0,	//V1
				0.5f, -0.5f, 0,		//V2
				0.5f, 0.5f, 0f		//V3
		};
		
		int[] indices = {
				0, 1, 3,	//Top left triangle (V0, V1, V3)
				3, 1, 2		//Bottom right triangle (V3, V1, V2)
		};
		
		/* Using index buffers will help to use less data in total by not specifying positions shared by 
		 * different vertexes multiple times and instead using indices defining which vertexes use which positions.
		 */
		
		RawModel model = loader.loadToVAO(vertices, indices);
		
		//main game loop
		while(!Display.isCloseRequested()) {
			
			renderer.prepare(); //basically clears screen from previous frame
			
			//game logic
			renderer.render(model);
			DisplayManager.updateDisplay();
		}
		
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
