package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrains.Terrain;
import textures.ModelTexture;

/**MainApplication holds the main game loop containing the main game logic.<br>
 * It handles the initialization and destruction of the game and holds main parameters (eg World Size).<br>
 * <br>
 * The main game loop updates all objects and does the rendering for every frame.
 * 
 * @author Flo
 *
 */
public class MainApplication {
	
	/* The OpenGL code so far has been written based on the a youtube tutorial series. 
	 * 
	 * Also, there is a plugin for writing GLSL shaders in Eclipse: http://sourceforge.net/projects/webglsl/
	 * -> Extract .zip file to Eclipse installation directory
	 * 
	 * OpenGL Api: https://www.opengl.org/sdk/docs/man4/index.php
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
	 * 16.) Fog: http://www.youtube.com/watch?v=qslBNLeSPUc
	 * 17.) Multitexturing: http://www.youtube.com/watch?v=-kbal7aGUpk
	 * */
	
	private static final float WORLD_SIZE_X = 800;
	private static final float WORLD_SIZE_Z = 800;
	
	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		
		/* Using index buffers will help to use less data in total by not specifying positions shared by 
		 * different vertexes multiple times and instead using indices defining which vertexes use which positions.
		 */
		
		//load 3d models from .obj files into ModelData objects
		ModelData dragonModelData = OBJFileLoader.loadOBJ("dragon");
		ModelData treeModelData = OBJFileLoader.loadOBJ("tree");
		ModelData grassModelData = OBJFileLoader.loadOBJ("grass");
		ModelData fernModelData = OBJFileLoader.loadOBJ("fern");
		
		//load ModelData objects in a VAO and return RawModels
		RawModel dragonRawModel = loader.loadToVAO(dragonModelData.getVertices(), dragonModelData.getTextureCoords(), 
				dragonModelData.getNormals(), dragonModelData.getIndices());
		RawModel treeRawModel = loader.loadToVAO(treeModelData.getVertices(), treeModelData.getTextureCoords(), 
				treeModelData.getNormals(), treeModelData.getIndices());
		RawModel grassRawModel = loader.loadToVAO(grassModelData.getVertices(), grassModelData.getTextureCoords(), 
				grassModelData.getNormals(), grassModelData.getIndices());
		RawModel fernRawModel = loader.loadToVAO(fernModelData.getVertices(), fernModelData.getTextureCoords(), 
				fernModelData.getNormals(), fernModelData.getIndices());
		
		//load textures from png files
		ModelTexture dragonTexture = new ModelTexture(loader.loadTexture("dragon"));
		ModelTexture treeTexture = new ModelTexture(loader.loadTexture("tree")); 
		ModelTexture grassTexture = new ModelTexture(loader.loadTexture("grass"));
		ModelTexture fernTexture = new ModelTexture(loader.loadTexture("fern"));
		
		//set parameters for specular lighting
		dragonTexture.setShineDamper(10); //set shine damper for specular lighting
		dragonTexture.setReflectivity(1); //set reflectivity for specular lighting
		
		//set transparency and fake lighting for grass and fern (to avoid weird shadow look)
		grassTexture.setHasTransparency(true);
		grassTexture.setUseFakeLighting(true);
		fernTexture.setHasTransparency(true);
		fernTexture.setUseFakeLighting(true);
		
		//finally, create the TexturedModel sticking ModelTextures to RawModels
		TexturedModel dragonTexturedModel = new TexturedModel(dragonRawModel, dragonTexture); 
		TexturedModel treeTexturedModel = new TexturedModel(treeRawModel, treeTexture);
		TexturedModel grassTexturedModel = new TexturedModel(grassRawModel, grassTexture);
		TexturedModel fernTexturedModel = new TexturedModel(fernRawModel, fernTexture);
		
		List<Entity> entities = new ArrayList<Entity>(); //holds all entities to be rendered
		Random random = new Random();
		
		entities.add(new Entity(dragonTexturedModel, new Vector3f(0,0,-30),0,0,0,1)); 
		
		for (int i = 0; i < 500; i++) {
			entities.add(new Entity(treeTexturedModel, new Vector3f(random.nextFloat() * WORLD_SIZE_X - WORLD_SIZE_X/2, 0, 
					random.nextFloat() * -WORLD_SIZE_Z), 0, 0, 0, 3));
			entities.add(new Entity(grassTexturedModel, new Vector3f(random.nextFloat() * WORLD_SIZE_X - WORLD_SIZE_X/2, 0, 
					random.nextFloat() * -WORLD_SIZE_Z), 0, 0, 0, 1));
			entities.add(new Entity(fernTexturedModel, new Vector3f(random.nextFloat() * WORLD_SIZE_X - WORLD_SIZE_X/2, 0, 
					random.nextFloat() * -WORLD_SIZE_Z), 0, 0, 0, 0.6f));
		} //boarders for vegetation and terrain: x,y,z of (-400,400),(0),(-800, 0);
		
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

	public static float getWorldSizeX() {
		return WORLD_SIZE_X;
	}

	public static float getWorldSizeZ() {
		return WORLD_SIZE_Z;
	}
}
