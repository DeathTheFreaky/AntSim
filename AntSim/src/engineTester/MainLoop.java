package engineTester;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

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
	 * 6.) Texturing: http://www.youtube.com/watch?v=SPt-aogu72A
	 * 7.) Matrices & Uniform Variables: http://www.youtube.com/watch?v=oc8Yl4ZruCA -> Uniform Variables are used to send variables from Java code to the shaders
	 * 8.) Model, View & Projection Matrices: http://www.youtube.com/watch?v=50Y9u7K0PZo
	 * 9.) OBJ File Format: http://www.youtube.com/watch?v=KMWUjNE0fYI
	 * 10.) Loading 3D Models: http://www.youtube.com/watch?v=YKFYtekgnP8
	 * */
	
	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);
		
		/* Using index buffers will help to use less data in total by not specifying positions shared by 
		 * different vertexes multiple times and instead using indices defining which vertexes use which positions.
		 */
		
		RawModel model = OBJLoader.loadObjModel("models/stall", loader); //loads a 3D model from an .obj file
		
		ModelTexture texture = new ModelTexture(loader.loadTexture("models/stallTexture")); //load a texture from a png file
		
		TexturedModel staticModel = new TexturedModel(model, texture); //stick the texture on a RawModel
		
		Entity entity = new Entity(staticModel, new Vector3f(0,0,-30),0,0,0,1); 
		
		Camera camera = new Camera();
		
		//main game loop
		while(!Display.isCloseRequested()) {
			
			//game logic
			entity.increaseRotation(0, 1, 0);
			camera.move(); //every single frame check for key inputs which move the camera
		
			renderer.prepare(); //basically clears screen from previous frame
			shader.start();
			shader.loadViewMatrix(camera); //all objects in the world need to be moved every single frame to simulate the camera
			renderer.render(entity, shader);
			shader.stop();
			DisplayManager.updateDisplay();
		}
		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
