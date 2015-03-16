package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import terrains.Terrain;
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
	 * 11.) Per-Pixel/Diffuse Lighting: http://www.youtube.com/watch?v=bcxX0R8nnDs //brightness of objects surface depends on how much the surface faces the light
	 * 12.) Specular Lighting: http://www.youtube.com/watch?v=GZ_1xOm-3qU //used in addition to diffuse lighting -> reflected light on shiny object
	 * 13.) Optimizing and Ambient Lighting: http://www.youtube.com/watch?v=X6KjDwA7mZg //Ambient lighting: add a bit of light to every part of the model
	 * 14.) Simple Terrain: http://www.youtube.com/watch?v=yNYwZMmgTJk
	 * 15.) Transparency (Textures): http://www.youtube.com/watch?v=ZyzXBYVvjsg
	 * */
	
	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		
		/* Using index buffers will help to use less data in total by not specifying positions shared by 
		 * different vertexes multiple times and instead using indices defining which vertexes use which positions.
		 */
		
	//load 3d models from .obj files
		
		RawModel dragonRawModel = OBJLoader.loadObjModel("dragon", loader); //loads a 3D model from an .obj file
		ModelTexture dragonTexture = new ModelTexture(loader.loadTexture("dragon")); //load a texture from a png file
		dragonTexture.setShineDamper(10); //set shine damper for specular lighting
		dragonTexture.setReflectivity(1); //set reflectivity for specular lighting
		TexturedModel dragonTexturedModel = new TexturedModel(dragonRawModel, dragonTexture); //stick the texture on a RawModel
		
		TexturedModel tree = new TexturedModel(OBJLoader.loadObjModel("tree", loader), new ModelTexture(loader.loadTexture("tree")));
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grass", loader), new ModelTexture(loader.loadTexture("grass")));
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), new ModelTexture(loader.loadTexture("fern")));
		
		//set transparency and fake lighting for grass and fern (to avoid weird shadow look)
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		fern.getTexture().setHasTransparency(true);
		fern.getTexture().setUseFakeLighting(true);
		
		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		
		entities.add(new Entity(dragonTexturedModel, new Vector3f(0,0,-30),0,0,0,1)); 
		
		for (int i = 0; i < 500; i++) {
			entities.add(new Entity(tree, new Vector3f(random.nextFloat() * 800 - 400, 0, 
					random.nextFloat() * -800), 0, 0, 0, 3));
			entities.add(new Entity(grass, new Vector3f(random.nextFloat() * 800 - 400, 0, 
					random.nextFloat() * -800), 0, 0, 0, 1));
			entities.add(new Entity(fern, new Vector3f(random.nextFloat() * 800 - 400, 0, 
					random.nextFloat() * -800), 0, 0, 0, 0.6f));
		} //boarders for vegetation: (-400,400),(0),(-800, 0);
		
		Light light = new Light(new Vector3f(3000,2000,2000), new Vector3f(1,1,1));
		
		Terrain terrain = new Terrain(0,0,loader,new ModelTexture(loader.loadTexture("grassTerrain")));
		Terrain terrain2 = new Terrain(0,0,loader,new ModelTexture(loader.loadTexture("grassTerrain")));
		
		Camera camera = new Camera();
		
		MasterRenderer renderer = new MasterRenderer();
		
		//main game loop
		while(!Display.isCloseRequested()) {
			
			//game logic
			entities.get(0).increaseRotation(0, 0.5f, 0); //rotate the dragon
			
			camera.move(); //every single frame check for key inputs which move the camera
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			for (Entity entity : entities) {
				renderer.processEntity(entity); //needs to be called for every single entity that shall be rendered
			}
			renderer.render(light, camera);
			
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
