package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.ObjLongConsumer;

import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

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
	 * OpenGL Commands syntax for lots of 3 param - commands:
	 * 1.: what is affected, 2.: which behaviour to define, 3.: the actual value to set
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
	 * 18.) Player Movement: http://www.youtube.com/watch?v=d-kuzyCkjoQ
	 * 19.) 3rd Person Camera: http://www.youtube.com/watch?v=PoxDDZmctnU
	 * 20.) MipMapping: http://www.youtube.com/watch?v=yGjMzIePKNQ //use low - res textures for objects that are far away -> speeds up game
	 * 21.) Terrain Height Maps: http://www.youtube.com/watch?v=O9v6olrHPwI
	 * 22.) Terrain Collision Detection: http://www.youtube.com/watch?v=6E2zjfzMs7c
	 * 23.) Texture Atlases: http://www.youtube.com/watch?v=6T182r4F6J8
	 * 24.) Rendering GUIs: http://www.youtube.com/watch?v=vOmJ1lyiJ4A
	 * 25.) Multiple Lights: http://www.youtube.com/watch?v=95WAAYsOifQ
	 * 26.) Point Lights: http://www.youtube.com/watch?v=KdY0aVDp5G4
	 * 27.) Skybox: http://www.youtube.com/watch?v=_Ix5oN8eC1E
	 * 28.) Day/Night: http://www.youtube.com/watch?v=rqx9IDLKV28&list=PLRIWtICgwaX0u7Rf9zkZhLoLuZVfUksDP&index=28
	 * 
	 * Animations: http://www.wazim.com/Collada_Tutorial_2.htm
	 * */
	
	private static final float WORLD_SIZE = 800; //square
	
	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		
		MasterRenderer renderer = new MasterRenderer(loader);
		
		/* Using index buffers will help to use less data in total by not specifying positions shared by 
		 * different vertexes multiple times and instead using indices defining which vertexes use which positions.
		 */
		
		//load 3d models from .obj files into ModelData objects
		ModelData dragonModelData = OBJFileLoader.loadOBJ("dragon");
		ModelData treeModelData = OBJFileLoader.loadOBJ("tree");
		ModelData grassModelData = OBJFileLoader.loadOBJ("grass");
		ModelData fernModelData = OBJFileLoader.loadOBJ("fern");
		ModelData playerModelData = OBJFileLoader.loadOBJ("person");
		ModelData lampModelData = OBJFileLoader.loadOBJ("lamp");
		
		//load ModelData objects in a VAO and return RawModels
		RawModel dragonRawModel = loader.loadToVAO(dragonModelData.getVertices(), dragonModelData.getTextureCoords(), 
				dragonModelData.getNormals(), dragonModelData.getIndices());
		RawModel treeRawModel = loader.loadToVAO(treeModelData.getVertices(), treeModelData.getTextureCoords(), 
				treeModelData.getNormals(), treeModelData.getIndices());
		RawModel grassRawModel = loader.loadToVAO(grassModelData.getVertices(), grassModelData.getTextureCoords(), 
				grassModelData.getNormals(), grassModelData.getIndices());
		RawModel fernRawModel = loader.loadToVAO(fernModelData.getVertices(), fernModelData.getTextureCoords(), 
				fernModelData.getNormals(), fernModelData.getIndices());
		RawModel playerRawModel = loader.loadToVAO(playerModelData.getVertices(), playerModelData.getTextureCoords(), 
				playerModelData.getNormals(), playerModelData.getIndices());
		RawModel lampRawModel = loader.loadToVAO(lampModelData.getVertices(), lampModelData.getTextureCoords(), 
				lampModelData.getNormals(), lampModelData.getIndices());
		
		//load textures from png files
		ModelTexture dragonTexture = new ModelTexture(loader.loadTexture("dragon"));
		ModelTexture treeTexture = new ModelTexture(loader.loadTexture("tree")); 
		ModelTexture grassTexture = new ModelTexture(loader.loadTexture("grass"));
		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
		ModelTexture playerTexture = new ModelTexture(loader.loadTexture("playerTexture"));
		ModelTexture lampTexture = new ModelTexture(loader.loadTexture("lamp"));
		
		fernTextureAtlas.setNumberOfRows(2); //set number of rows inside texture atlas
		
		//set parameters for specular lighting
		dragonTexture.setShineDamper(10); //set shine damper for specular lighting
		dragonTexture.setReflectivity(1); //set reflectivity for specular lighting
		
		//set transparency and fake lighting for grass and fern (to avoid weird shadow look)
		grassTexture.setHasTransparency(true);
		grassTexture.setUseFakeLighting(true);
		fernTextureAtlas.setHasTransparency(true);
		fernTextureAtlas.setUseFakeLighting(true);
		
		//finally, create the TexturedModel sticking ModelTextures to RawModels
		TexturedModel dragonTexturedModel = new TexturedModel(dragonRawModel, dragonTexture); 
		TexturedModel treeTexturedModel = new TexturedModel(treeRawModel, treeTexture);
		TexturedModel grassTexturedModel = new TexturedModel(grassRawModel, grassTexture);
		TexturedModel fernTexturedModel = new TexturedModel(fernRawModel, fernTextureAtlas);
		TexturedModel playerTexturedModel = new TexturedModel(playerRawModel, playerTexture);
		TexturedModel lampTexturedModel = new TexturedModel(lampRawModel, lampTexture);
		
		//create player
		Player player = new Player(playerTexturedModel, 1, new Vector3f(WORLD_SIZE/2, 0, -WORLD_SIZE/2), 0, 0, 0, 1);
		
		
		//load the different terrain textures
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		//store different terrain textures in a TerrainTexturePack
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		
		//create a blend map texture
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		/*Terrain terrain = new Terrain(0, 0, loader, texturePack, blendMap);
		Terrain terrain2 = new Terrain(0, 0, loader, texturePack, blendMap);*/
		
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");
		//Terrain terrain2 = new Terrain(-1, -1, loader, texturePack, blendMap); //if we need more terrains... thats how to add them as tiles
				
		
		List<Entity> entities = new ArrayList<Entity>(); //holds all entities to be rendered
		Random random = new Random(676452);
		
		entities.add(new Entity(dragonTexturedModel, 1, new Vector3f(0,0,0),0,0,0,1)); 
		
		for (int i = 0; i < 1200; i++) {
			if (i % 20 == 0) {
				float x = random.nextFloat() * WORLD_SIZE;
				float z = random.nextFloat() * -WORLD_SIZE;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(fernTexturedModel, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 
						0, 0.9f));
			}
			if (i % 5 == 0) {
				float x = random.nextFloat() * WORLD_SIZE;
				float z = random.nextFloat() * -WORLD_SIZE;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(grassTexturedModel, 1, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 
						0, random.nextFloat() * 0.1f + 0.6f));
				x = random.nextFloat() * WORLD_SIZE;
				z = random.nextFloat() * -WORLD_SIZE;
				y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(treeTexturedModel, 1, new Vector3f(x, y, z), 0, 0,
						0, random.nextFloat() * 1 + 4));
			}
		} 
		
		List<Light> lights = new ArrayList<Light>();
		lights.add(new Light(new Vector3f(0, 1000, -7000), new Vector3f(0.4f, 0.4f, 0.4f))); //sun
		lights.add(new Light(new Vector3f(185, 10, -293), new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f))); //lamp
		lights.add(new Light(new Vector3f(370, 17, -300), new Vector3f(0, 2, 2), new Vector3f(1, 0.01f, 0.002f))); //lamp
		lights.add(new Light(new Vector3f(293, 7, -305), new Vector3f(2, 2, 0), new Vector3f(1, 0.01f, 0.002f))); //lamp
		
		entities.add(new Entity(lampTexturedModel, 1, new Vector3f(185, -4.7f, -293), 0, 0, 0, 1));
		entities.add(new Entity(lampTexturedModel, 1, new Vector3f(370, 4.2f, -300), 0, 0, 0, 1));
		entities.add(new Entity(lampTexturedModel, 1, new Vector3f(293, -6.6f, -305), 0, 0, 0, 1));
		
		Camera camera = new Camera(player); //player camera - make a "ghost" player to simulate cool camera movement
		
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture gui = new GuiTexture(loader.loadTexture("health"), new Vector2f(-0.8f, 0.95f), new Vector2f(0.2f, 0.25f));
		
		//order matters: second texture will be drawn in fron of first texture
		guis.add(gui);
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		//main game loop
		while(!Display.isCloseRequested()) {
			
			//game logic
			entities.get(0).increaseRotation(0, 0.5f, 0); //rotate the dragon
			
			player.move(terrain);
			camera.move(); //every single frame check for key inputs which move the camera
			renderer.processEntity(player);
			renderer.processTerrain(terrain);
			//renderer.processTerrain(terrain2);
			for (Entity entity : entities) {
				renderer.processEntity(entity); //needs to be called for every single entity that shall be rendered
			}
			
			renderer.render(lights, camera);
			guiRenderer.render(guis);
			
			DisplayManager.updateDisplay();
		}
		
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

	public static float getWorldSize() {
		return WORLD_SIZE;
	}
}
