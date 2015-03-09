package engineTester;

import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.RawModel;
import renderEngine.Renderer;
import shaders.StaticShader;

/**MainLoop holds the main game loop containing the main game logic and handles the initialization and destruction of the game.
 * <br>
 * The main game loop updates all objects and does the rendering for every frame.
 * 
 * @author Flo
 *
 */
public class MainLoop {
	
	/* The OpenGL code so far has been written based on the a youtube tutorial series. 
	 * 
	 * Also, there is a plugin for writing GLSL shaders in Eclipse: http://sourceforge.net/projects/webglsl/
	 * -> Extract .zip file to Eclipse installation directory
	 * 
	 * In case of questions, refer to the following videos:
	 * 
	 * 1.) Setting up Display: http://www.youtube.com/watch?v=VS8wlS9hF8E
	 * 2.) VAOs and VBOs: http://www.youtube.com/watch?v=WMiggUPst-Q
	 * 3.) Using index buffers: http://www.youtube.com/watch?v=z2yFlvkBbmk
	 * 4.) Introduction to Shaders: http://www.youtube.com/watch?v=AyNZG_mqGVE
	 * 5.) Coloring using Shaders: http://www.youtube.com/watch?v=4w7lNF8dnYw (shaders.vertexShader, shaders.fragmentShader)
	 * */
	
	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		StaticShader shader = new StaticShader();
		
		// draw a cube for testing purposes
		float[] vertices = {			
				-0.5f,0.5f,0,	//V0
				-0.5f,-0.5f,0,	//V1
				0.5f,-0.5f,0,	//V2
				0.5f,0.5f,0		//V3
		};
		
		int[] indices = {
				0,1,3,	//Top left triangle (V0,V1,V3)
				3,1,2	//Bottom right triangle (V3,V1,V2)
		};
		
		/* Using index buffers will help to use less data in total by not specifying positions shared by 
		 * different vertexes multiple times and instead using indices defining which vertexes use which positions.
		 */
		
		RawModel model = loader.loadToVAO(vertices, indices);
		
		//main game loop
		while(!Display.isCloseRequested()) {
			//game logic
			renderer.prepare(); //basically clears screen from previous frame
			shader.start();
			renderer.render(model);
			shader.stop();
			DisplayManager.updateDisplay();
		}
		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
