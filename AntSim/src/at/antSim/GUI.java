package at.antSim;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import at.antSim.guiWrapper.commands.*;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import at.antSim.eventSystem.EventManager;
import at.antSim.graphics.entities.Camera;
import at.antSim.graphics.entities.Entity;
import at.antSim.graphics.entities.Light;
import at.antSim.graphics.graphicsUtils.DisplayManager;
import at.antSim.graphics.graphicsUtils.Loader;
import at.antSim.graphics.graphicsUtils.MousePicker;
import at.antSim.graphics.graphicsUtils.OBJFileLoader;
import at.antSim.graphics.graphicsUtils.OpenGLTextDrawer;
import at.antSim.graphics.models.ModelData;
import at.antSim.graphics.models.RawModel;
import at.antSim.graphics.models.TexturedModel;
import at.antSim.graphics.renderer.GuiRenderer;
import at.antSim.graphics.renderer.MasterRenderer;
import at.antSim.graphics.terrains.Terrain;
import at.antSim.graphics.textures.ModelTexture;
import at.antSim.graphics.textures.TerrainTexture;
import at.antSim.graphics.textures.TerrainTexturePack;
import at.antSim.guiWrapper.GuiContainer;
import at.antSim.guiWrapper.GuiImage;
import at.antSim.guiWrapper.GuiState;
import at.antSim.guiWrapper.GuiText;
import at.antSim.guiWrapper.GuiWrapper;
import at.antSim.guiWrapper.HorPositions;
import at.antSim.guiWrapper.HorReference;
import at.antSim.guiWrapper.VerPositions;
import at.antSim.guiWrapper.VerReference;

/**MainApplication holds the main game loop containing the main game logic.<br>
 * It handles the initialization and destruction of the game and holds main parameters (eg World Size).<br>
 * <br>
 * The main game loop updates all objects and does the rendering for every frame.
 *
 * @author Flo
 *
 */
public class GUI {
	
	/* The OpenGL code so far has been written based on the a youtube tutorial series. 
	 * 
	 * Also, there is a plugin for writing GLSL shaders in Eclipse: http://sourceforge.net/projects/webglsl/
	 * -> Extract .zip file to Eclipse installation directory
	 * 
	 * NiftyGUI: http://sourceforge.net/projects/nifty-gui/files/nifty-gui/1.3.2/
	 * 
	 * OpenGL Commands syntax for lots of 3 param - commands:
	 * 1.: what is affected, 2.: which behaviour to define, 3.: the actual value to set
	 * 
	 * Explanation for different spaces in OpenGL: http://antongerdelan.net/opengl/raycasting.html
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
	 * 28.) Day/Night: http://www.youtube.com/watch?v=rqx9IDLKV28
	 * 29.) Mouse Picking: https://youtu.be/DLKN0jExRIM //using 3d ray representing everything under the ray from the viewer's perspective; bounding spheres
	 * 
	 * Sources:
	 * OpenGL Api: https://www.opengl.org/sdk/docs/man4/index.php
	 * Animations: http://www.wazim.com/Collada_Tutorial_2.htm
	 * LWJGL: http://wiki.lwjgl.org/index.php?title=Main_Page
	 * Easy Matrix Explanations: http://www.mathsisfun.com/algebra/matrix-introduction.html
	 * TransformationMatrix: http://en.wikipedia.org/wiki/Transformation_matrix
	 * Conversion Matrix to Euler: http://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToEuler/
	 * More complex matrix explanation: http://www.opengl-tutorial.org/beginners-tutorials/tutorial-3-matrices/
	 * Projection Matrix explained: http://www.songho.ca/opengl/gl_projectionmatrix.html
	 * Vector basics: http://www.bbc.co.uk/bitesize/higher/maths/geometry/vectors/revision/1/
	 * Dot Product: http://betterexplained.com/articles/vector-calculus-understanding-the-dot-product/
	 * Some basic trigonometry: http://www.mathsisfun.com/algebra/trigonometry.html
	 * Open GL Textures: https://open.gl/textures
	 * Mouse Picking with Ray Casting: http://antongerdelan.net/opengl/raycasting.html
	 * 
	 * Code sources:
	 * 14 - Simple Terrain generation code: https://www.dropbox.com/s/47qk4yrz5v9lb61/Terrain%20Generation%20Code.txt?dl=0
	 * 16 - OBJ File Loader: https://www.dropbox.com/sh/x1fyet1otejxk3z/AAAoCqArl4cIx0THdRk2poW3a?dl=0
	 * 17 - Example blend Map: https://www.dropbox.com/s/yfd7so1yg0fq4jo/blendMap.png?dl=0
	 * 18 - Height map: https://www.dropbox.com/s/dcul3fnrnejue7x/heightmap.png?dl=0
	 * 22 - BarryCentric function: https://www.dropbox.com/s/0md240yyc359ik3/code.txt?dl=0
	 * 23 - Fern texture atlas: https://www.dropbox.com/s/4m901nauypnsapn/fern.png?dl=0
	 * 26 - Models and texuters: https://www.dropbox.com/sh/j1zmywbkxqkp0rw/AADx61ZUt48A97xKZUww5YNea?dl=0
	 * 27 - Skybox: https://www.dropbox.com/sh/phslacd8v9i17wb/AABui_-C-yhKvZ1H2wb3NykIa?dl=0
	 * 28 - Night Skybox: https://www.dropbox.com/sh/o7ozx1u5qlg7b5v/AACI3zt1a9ZMw5MG2G_rzbKda?dl=0
	 * 28 - Day/Night example code: https://www.dropbox.com/s/iom1x2c3t0r5owr/Day%20Night%20Example.txt?dl=0
	 * 29 - Mouse Picker example code: https://www.dropbox.com/s/qkslys3p3xzh8av/MousePicker%20Code.txt?dl=0
	 * */

	public static void launch(Loader loader, MasterRenderer renderer) {
		
		/* Using index buffers will help to use less data in total by not specifying positions shared by 
		 * different vertexes multiple times and instead using indices defining which vertexes use which positions.
		 */

		//load 3d models from .obj files into ModelData objects
		ModelData treeModelData = OBJFileLoader.loadOBJ("tree");
		ModelData grassModelData = OBJFileLoader.loadOBJ("grass");
		ModelData fernModelData = OBJFileLoader.loadOBJ("fern");
		ModelData lampModelData = OBJFileLoader.loadOBJ("lamp");

		//load ModelData objects in a VAO and return RawModels
		RawModel treeRawModel = loader.loadToVAO(treeModelData.getVertices(), treeModelData.getTextureCoords(),
				treeModelData.getNormals(), treeModelData.getIndices());
		RawModel grassRawModel = loader.loadToVAO(grassModelData.getVertices(), grassModelData.getTextureCoords(),
				grassModelData.getNormals(), grassModelData.getIndices());
		RawModel fernRawModel = loader.loadToVAO(fernModelData.getVertices(), fernModelData.getTextureCoords(),
				fernModelData.getNormals(), fernModelData.getIndices());
		RawModel lampRawModel = loader.loadToVAO(lampModelData.getVertices(), lampModelData.getTextureCoords(),
				lampModelData.getNormals(), lampModelData.getIndices());

		//load textures from png files
		ModelTexture dragonTexture = new ModelTexture(loader.loadTexture("dragon"));
		ModelTexture treeTexture = new ModelTexture(loader.loadTexture("tree"));
		ModelTexture grassTexture = new ModelTexture(loader.loadTexture("grass"));
		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
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
		TexturedModel treeTexturedModel = new TexturedModel(treeRawModel, treeTexture);
		TexturedModel grassTexturedModel = new TexturedModel(grassRawModel, grassTexture);
		TexturedModel fernTexturedModel = new TexturedModel(fernRawModel, fernTextureAtlas);
		TexturedModel lampTexturedModel = new TexturedModel(lampRawModel, lampTexture);

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

		for (int i = 0; i < 1200; i++) {
			if (i % 20 == 0) {
				float x = random.nextFloat() * Globals.WORLD_SIZE;
				float z = random.nextFloat() * -Globals.WORLD_SIZE;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(fernTexturedModel, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360,
						0, 0.9f));
			}
			if (i % 5 == 0) {
				float x = random.nextFloat() * Globals.WORLD_SIZE;
				float z = random.nextFloat() * -Globals.WORLD_SIZE;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(grassTexturedModel, 1, new Vector3f(x, y, z), 0, random.nextFloat() * 360,
						0, random.nextFloat() * 0.1f + 0.6f));
				x = random.nextFloat() * Globals.WORLD_SIZE;
				z = random.nextFloat() * -Globals.WORLD_SIZE;
				y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(treeTexturedModel, 1, new Vector3f(x, y, z), 0, 0,
						0, random.nextFloat() * 1 + 4));
			}
		}

		List<Light> lights = new ArrayList<Light>();
		lights.add(new Light(new Vector3f(0, 1000, -7000), new Vector3f(0.4f, 0.4f, 0.4f))); //sun
		lights.add(new Light(new Vector3f(185, 10, -293), new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f))); //lamp
		lights.add(new Light(new Vector3f(370, 17, -300), new Vector3f(0, 2, 2), new Vector3f(1, 0.01f, 0.002f))); //lamp
		// lights.add(new Light(new Vector3f(293, 7, -305), new Vector3f(2, 2, 0), new Vector3f(1, 0.01f, 0.002f))); //lamp

		// a lamp freely positionable on the map
		Entity movingLamp = new Entity(lampTexturedModel, 1, new Vector3f(293, -6.8f, -305), 0, 0, 0, 1);
		entities.add(movingLamp);
		Light movingLight = new Light(new Vector3f(293, 7, -305), new Vector3f(0, 2, 2), new Vector3f(1, 0.01f, 0.002f));
		lights.add(movingLight);

		entities.add(new Entity(lampTexturedModel, 1, new Vector3f(185, -4.7f, -293), 0, 0, 0, 1));
		entities.add(new Entity(lampTexturedModel, 1, new Vector3f(370, 4.2f, -300), 0, 0, 0, 1));
		//entities.add(new Entity(lampTexturedModel, 1, new Vector3f(293, -6.6f, -305), 0, 0, 0, 1));

		Camera camera = new Camera(new Vector3f(Globals.WORLD_SIZE/2, 0, -Globals.WORLD_SIZE/2)); //player camera - make a "ghost" player to simulate cool camera movement

		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);

		//gui
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, 1, -1, -1, 1, -1 }; //gui quad positions for images
		float[] textureCoords = {0, 0, 0, 1, 1, 0, 1, 0, 0, 1, 1, 1}; //gui texture coords for images
		RawModel standardContainerQuad = loader.loadToVAO(positions, textureCoords, 2);

		GuiWrapper guiWrapper = GuiWrapper.getInstance();
		GuiState testState = new GuiState("testState");

		//MEINS
		GuiState startMenu = new GuiState("startMenu");
		GuiState optionsDisplay = new GuiState("optionsDisplay");
		GuiState optionsControls = new GuiState("optionsControl");
		GuiState mainGame = new GuiState("mainGame");

		Command newGameCmd = new NewGameCmd("testState");
		Command optionsCmd = new OptionsCmd("optionsDisplay");
		Command quitGameCmd = new QuitGameCmd();

		GuiContainer startContainer = new GuiContainer("startContainer", null, newGameCmd, standardContainerQuad, loader.loadGuiTexture("white"),
				Globals.displayWidth, Globals.displayHeight, HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);

		Command cmd = new TestCommand();

		GuiContainer testContainer = new GuiContainer("testContainer", null, cmd, standardContainerQuad, loader.loadGuiTexture("white"), 640, 360,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0.5f, new Vector3f(0,0,0), 0f);

		//nur einmal, außer für andere Fonts
		OpenGLTextDrawer textDrawer = new OpenGLTextDrawer(loader, loader.loadGuiTexture("font"));
		GuiText testText = new GuiText("testText", textDrawer.createTextQuad("Flo war da!\nWhohoooo"), testContainer, null, 42, HorReference.PARENT, HorPositions.LEFT, 0, VerReference.PARENT, VerPositions.TOP, 0,
				0f, new Vector3f(0f, 1f, 0f), 0.5f);

		RawModel testImageQuad = loader.loadToVAO(positions, textureCoords, 2);
		GuiImage testImage = new GuiImage("testImage", testContainer, null, testImageQuad, loader.loadGuiTexture("health"), 500, 280, HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 0,
				0f, new Vector3f(1f, 0f, 0f), 0.2f);
		EventManager.getInstance().registerEventListener(testContainer);


		GuiText antSim1 = new GuiText("antSim1", textDrawer.createTextQuad("Ant"), startContainer, null, 52, HorReference.PARENT, HorPositions.LEFT, Globals.displayWidth/2 - 150, VerReference.SIBLING, VerPositions.TOP, 20);
		GuiText antSim2 = new GuiText("antSim2", textDrawer.createTextQuad("Sim"), startContainer, null, 36, HorReference.PARENT, HorPositions.RIGHT, Globals.displayWidth/2 - 100, VerReference.SIBLING, VerPositions.BELOW, -15);

		GuiContainer newGameContainer = new GuiContainer("newGamebutton", startContainer, null, standardContainerQuad, loader.loadGuiTexture("white"), 300, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 35, 0, new Vector3f(0, 0, 0), 0.8f);
		GuiText newGameText = new GuiText("newGame", textDrawer.createTextQuad("Start new game"), newGameContainer, null, 25,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
		EventManager.getInstance().registerEventListener(startContainer);

		testState.addContainer(testContainer);
		//MEINS
		startMenu.addContainer(startContainer);
		startContainer.getChildren().add(newGameContainer);

		guiWrapper.addState(testState);
		//MEINS
		guiWrapper.addState(startMenu);
		guiWrapper.setCurrentState("startMenu");

		GuiRenderer guiRenderer = new GuiRenderer(loader);

		//main game loop
		boolean done = false;
		while(!Display.isCloseRequested() && !done) {
			EventManager.getInstance().workThroughQueue();

			//game logic
			entities.get(0).increaseRotation(0, 0.5f, 0); //rotate the dragon

			camera.move(terrain); //every single frame check for key inputs which move the camera

			picker.update(); //update mouse picker's ray
			Vector3f terrainPoint = picker.getCurrentTerrainPoint();
			if (terrainPoint != null) {
				movingLamp.setPosition(terrainPoint);
				movingLight.setPosition(new Vector3f(terrainPoint.x, terrainPoint.y + 12f, terrainPoint.z));
			}

			renderer.processTerrain(terrain);
			//renderer.processTerrain(terrain2);
			for (Entity entity : entities) {
				renderer.processEntity(entity); //needs to be called for every single entity that shall be rendered
			}

			renderer.render(lights, camera);
			guiRenderer.render(guiWrapper.getCurrentState());

			DisplayManager.updateDisplay();
		}

		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
