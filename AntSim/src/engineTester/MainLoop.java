package engineTester;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import textures.ModelTexture;
import toolbox.Terrain;

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
	 * 11.) Per-Pixel/Diffuse Lighting: http://www.youtube.com/watch?v=bcxX0R8nnDs //brightness of objects surface depends on how much the surface faces the light
	 * 12.) Specular Lighting: http://www.youtube.com/watch?v=GZ_1xOm-3qU //used in addition to diffuse lighting -> reflected light on shiny object
	 * 13.) Optimizing and Ambient Lighting: http://www.youtube.com/watch?v=X6KjDwA7mZg //Ambient lighting: add a bit of light to every part of the model
	 * 14.) Simple Terrain: http://www.youtube.com/watch?v=yNYwZMmgTJk
	 * */
	
	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		
		/* Using index buffers will help to use less data in total by not specifying positions shared by 
		 * different vertexes multiple times and instead using indices defining which vertexes use which positions.
		 */
		
		RawModel model = OBJLoader.loadObjModel("models/dragon", loader); //loads a 3D model from an .obj file
		
		ModelTexture texture = new ModelTexture(loader.loadTexture("models/dragon")); //load a texture from a png file
		texture.setShineDamper(10);
		texture.setReflectivity(1);
		
		TexturedModel staticModel = new TexturedModel(model, texture); //stick the texture on a RawModel
		
		Entity entity = new Entity(staticModel, new Vector3f(0,0,-30),0,0,0,1); 
		Light light = new Light(new Vector3f(3000,2000,2000), new Vector3f(1,1,1));
		
		Terrain terrain = new Terrain(0,0,loader,new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain2 = new Terrain(0,0,loader,new ModelTexture(loader.loadTexture("grass")));
		
		Camera camera = new Camera();
		
		MasterRenderer renderer = new MasterRenderer();
		
		//main game loop
		while(!Display.isCloseRequested()) {
			
			entity.increaseRotation(0, 0.5f, 0);
			
			//game logic
			camera.move(); //every single frame check for key inputs which move the camera
			
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			renderer.processEntity(entity); //needs to be called for every single entity that shall be rendered
			
			renderer.render(light, camera);
			
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
