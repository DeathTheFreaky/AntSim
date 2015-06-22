package at.antSim;

import at.antSim.eventSystem.EventManager;
import at.antSim.graphics.entities.Camera;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.graphics.entities.Light;
import at.antSim.graphics.graphicsUtils.*;
import at.antSim.graphics.models.RawModel;
import at.antSim.graphics.models.TexturedModel;
import at.antSim.graphics.renderer.MasterRenderer;
import at.antSim.graphics.terrains.Terrain;
import at.antSim.guiWrapper.GuiWrapper;
import at.antSim.guiWrapper.states.*;
import at.antSim.objectsKI.Ant;
import at.antSim.objectsKI.Enemy;
import at.antSim.objectsKI.Entity;
import at.antSim.objectsKI.EntityBuilder;
import at.antSim.objectsKI.EntityBuilderImpl;
import at.antSim.objectsKI.Hive;
import at.antSim.objectsPhysic.PhysicsManager;
import at.antSim.objectsPhysic.Movement.MovementManager;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.PositionablePhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.bulletphysics.linearmath.Transform;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

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
	
	private static MainApplication INSTANCE = null;
	
	private AbstractGuiState startMenuState;
	private AbstractGuiState optionsDisplayState;
	private AbstractGuiState optionsControlsState;
	private AbstractGuiState mainGameState;
	private AbstractGuiState loadingState;
	private AbstractGuiState pauseState;
	
	private MasterRenderer renderer;
	
	private Camera camera;
	private Terrain terrain;
	private MousePicker picker;
	private List<Light> lights;
	
	private MovingEntity movingEntity;
	private GraphicsEntity movingLamp;
	private Light movingLight;
	
	private Hive hive;
	private int antFoodCtr;
	private int antFoodThreshold = 100;
	
	private boolean glLoaded = false;
	private boolean worldLoaded = false;
	private boolean triggeredLoading = false;
	private boolean quit = false;
	private boolean paused = false;
	
	private float normalSpeedTime = 1/60f; //update logic 60times a second on normal speed
	private float speed = 1f;
	private float timeStep = normalSpeedTime;
	private float timeAccumulator = 0;
	private float statsTimeStep = 1f;
	private float statsTimeAccumulator = 0;

	private long timeLastLogicUpdate = System.currentTimeMillis();
	
	private int cycleCtr = 0;

	private HashMap<String, Integer> stats = new HashMap<>();
	
	private EntityBuilder defaultEntityBuilder;
	private Random defaultRandom;
		
	private MainApplication() {};
	
	static {
		INSTANCE = new MainApplication();
	}
	
	/**Launches the simulation loop.<br>
	 * Logic will be updated according according to FPS_CAP * speed,
	 * while rendering will only occur at FPS_CAP.
	 * 
	 * @param loader
	 * @param renderer
	 */
	public void launch(OpenGLLoader loader, MasterRenderer renderer) {
		
		this.renderer = renderer;
		defaultEntityBuilder = new EntityBuilderImpl();
		
		GuiWrapper.getInstance().setLoader(loader);
		loadGui(loader);
		
		movingEntity = new MovingEntity();
		
		//camera for navigating in the world
		camera = new Camera(new Vector3f(Globals.WORLD_SIZE/2, 0, -Globals.WORLD_SIZE/2), pauseState.getName()); 
		EventManager.getInstance().registerEventListener(camera);
		
		//mousepicker to interact with the world
		picker = new MousePicker(camera, renderer.getProjectionMatrix());
		EventManager.getInstance().registerEventListener(picker);
						
		//main game loop
		while(!Display.isCloseRequested() && !quit) {
			
			//regulate rate of calculating logic according to game speed, keeping the rendering untouched at a constant frame rate
			timeAccumulator += DisplayManager.getFrameTimeSeconds();
			
			//update logic and events according to game speed
			while (timeAccumulator >= timeStep) {
				timeAccumulator -= timeStep;
				update();
			}
			
			//regulate rate of calculating logic according to game speed, keeping the rendering untouched at a constant frame rate
			statsTimeAccumulator += DisplayManager.getFrameTimeSeconds();
			
			//update stats (population, food...) and remove temporary vao and vbo data once a second
			while (statsTimeAccumulator >= statsTimeStep) {
				
				statsTimeAccumulator -= statsTimeStep;
				
				if (worldLoaded && !paused) {
					loader.tempCleanUp(); //remove temporary data (like text stats models) after each rendering frame
					((MainGameState)mainGameState).updateStatus(); //update stats in main game state (population, food...)
				}
			}
			
			//trigger loading screen
			if (triggeredLoading) {
				GuiWrapper.getInstance().setCurrentState(loadingState.getName());
			}
			
			//update camera and mouse picker 
			//camera movement should not be affected by game speed since it works with movement speeds multiplied with time elapsed since last frame update
			if (worldLoaded) {
				
				camera.move(terrain); //every single frame check for key inputs which move the camera
				picker.update();
				Vector3f terrainPoint = picker.getCurrentTerrainPoint();
				if (terrainPoint != null && movingEntity.getEntity() != null) {
					PhysicsObject phyObj = movingEntity.getEntity().getPhysicsObject();
					GraphicsEntity graphicsEntity = movingEntity.getEntity().getGraphicsEntity();
					Vector3f correctedTerrainPoint = new Vector3f(terrainPoint.x, terrainPoint.y + graphicsEntity.getModel().getRawModel().getyLength() / 2 * graphicsEntity.getScale(), terrainPoint.z);
					PositionablePhysicsObject posPhyObj = (PositionablePhysicsObject) phyObj;
					posPhyObj.setPosition(new javax.vecmath.Vector3f(correctedTerrainPoint.x, correctedTerrainPoint.y, correctedTerrainPoint.z));
				}
				
				renderer.processTerrain(terrain);
			}
			
			//render and update display
			renderer.render(lights, camera, worldLoaded);				
			DisplayManager.updateDisplay();
			
			//load world after loading screen has been rendered to support single threaded architecture
			if (triggeredLoading) {
				loadWorld(loader, renderer);
				triggeredLoading = false;
			}			
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
	
	/**Updates game logic and events.
	 * 
	 */
	private void update() {
		
		long timeCurrentUpdate = System.currentTimeMillis();
		
		movingEntity.setColliding(false); //assume that moving Entity does not collide by default, if it collides, a collision event...
		EventManager.getInstance().workThroughQueue();
		
		//game logic
		if (!paused && worldLoaded) {
//			System.out.println("update cycle: " + cyclyCtr);
			float timeSinceLastUpdate = (timeCurrentUpdate - timeLastLogicUpdate) / 1000f;
			timeSinceLastUpdate *= speed;
//			System.out.println("time since last updates: " + timeSinceLastUpdate);
			Enemy.updatePositionLocators(); //ensure the PositionLocators follow their target's position
			MovementManager.getInstance().moveAllEntries();
			Entity.resetUndergroundEntities();
//			Ant.printAllPositionLocatorsAndPheromones();
			Ant.resetPheromonesAndPositionLocators();
//			PhysicsManager.getInstance().printAllCollisionObjects();
			PhysicsManager.getInstance().performCollisionDetection(timeSinceLastUpdate); //... will be triggered here and registered by the movingEntity's Collision event listener
			
			cycleCtr++;
			antFoodCtr++;
			if(antFoodCtr > antFoodThreshold){
				antFoodCtr = 0;
				hive.foodChain();
				hive.layPheromones();
			}
//			System.out.println();
		}
		
		timeLastLogicUpdate = timeCurrentUpdate;
	}

	/**Loads the Gui and all its states.
	 * 
	 * @param loader
	 */
	private void loadGui(OpenGLLoader loader) {
		
		//MEINS
		startMenuState = new StartMenuState(loader, "startMenuState");
		optionsDisplayState = new OptionsDisplayState(loader, "optionsDisplayState");
		optionsControlsState = new OptionsControlState(loader, "optionsControlState");
		mainGameState = new MainGameState(loader, "mainGameState");
		loadingState = new LoadingState(loader, "loadingState");
		pauseState = new PauseState(loader, "pauseState");
				
		startMenuState.initializeState(optionsDisplayState.getName());
		loadingState.initializeState();
		optionsDisplayState.initializeState(startMenuState.getName(), optionsControlsState.getName());
		optionsControlsState.initializeState(startMenuState.getName(), optionsDisplayState.getName());
		pauseState.initializeState(mainGameState.getName(), startMenuState.getName(), optionsDisplayState.getName());
		
		GuiWrapper.getInstance().setCurrentState(startMenuState.getName());
	}

	/**Loads the 3d world. Needs to run in existing GL context.
	 * 
	 * @param loader
	 * @param renderer
	 */
	private void loadWorld(OpenGLLoader loader, MasterRenderer renderer) {
		
		if (!worldLoaded) {
						
			/* Using index buffers will help to use less data in total by not specifying positions shared by 
			 * different vertexes multiple times and instead using indices defining which vertexes use which positions.
			 */
			
			//open GL stuff needs to be loaded only once, even when starting a new game to speed up loading times, only released at application exit
			if (!glLoaded) {
				ModelLoader.loadTexturedModels(loader); //model's geometry and textures need to be loaded at first
				glLoaded = true;
			}
			
			terrain = WorldLoader.loadTerrain(loader); // loads the world's terrain
			WorldLoader.loadWorldEntities(loader, terrain); //loads the world's "material" contents
			hive = Hive.getInstance();
			lights = WorldLoader.loadLights();
			
			// a lamp freely positionable on the map
			/*movingLamp = new GraphicsEntity(WorldLoader.texturedModels.get("lamp"), 1, new Vector3f(293, -6.8f, -305), 0, 0, 0, 1);
			entities.add(movingLamp);
			movingLight = new Light(new Vector3f(293, 7, -305), new Vector3f(0, 2, 2), new Vector3f(1, 0.01f, 0.002f));
			lights.add(movingLight);*/
			
			camera.triggerReset();
			picker.setTerrain(terrain);
			
			//indicate to main game loop that world has finished loadings
			worldLoaded = true;
			
			mainGameState.initializeState(); //commands in mainGameState need data from this method 
						
			//sets gui to main game state once world has finished loading
			GuiWrapper.getInstance().setCurrentState(mainGameState.getName());
		}
	}

	/**Trigger world to be loaded.
	 * 
	 */
	public void triggerWorldLoad() {
		triggeredLoading = true;
	}
	
	/**Quits the game.
	 * 
	 */
	public void quitApplication() {
		quit = true;
	}
	
	/**Quits current game and returns to start menu state.
	 * 
	 */
	public void quitCurrentGame() {
		EventManager.getInstance().reset();
		worldLoaded = false;
		paused = false;
		hive.reset();
		Entity.deleteAllEntities();
	}
	
	public void pause() {
		paused = true;
	}
	
	public void unpause() {
		paused = false;
	}

	public void togglePlay() {
		if (paused) {
			paused = false;
		} else {
			paused = true;
		}
	}

	public boolean isPaused() {
		return paused;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
		float prevSpeed = normalSpeedTime/timeStep;
		timeStep = normalSpeedTime / speed;
		renderer.adaptTime(speed/prevSpeed, speed);
	}
	
	public float getSpeed() {
		return normalSpeedTime/timeStep;
	}

	public HashMap<String, Integer> getStats() {
		stats.put("Population", hive.getAntsNum());
		stats.put("Food", hive.getFoodNum());
		stats.put("Eggs", hive.getEggsNum());
		stats.put("Larvae", hive.getLarvaeNum());
		return stats;
	}
	
	public MovingEntity getMovingEntity() {
		return movingEntity;
	}

	public Camera getCamera() {
		return camera;
	}
	
	public EntityBuilder getDefaultEntityBuilder() {
		return defaultEntityBuilder;
	}
	
	public Terrain getTerrain() {
		return terrain;
	}
	
	public int getCycleCtr() {
		return cycleCtr;
	}
	
	public void setSunLight(Vector3f sunLight) {
		lights.get(0).setColor(sunLight);
	}
	
	public void setMoonLight(Vector3f moonLight) {
		lights.get(1).setColor(moonLight);
	}
	
	public void setSunPosition(Vector3f sunPosition) {
		lights.get(0).setPosition(sunPosition);
	}
	
	public void setMoonPosition(Vector3f moonPosition) {
		lights.get(1).setPosition(moonPosition);
	}
	
	/**
	 * @return - the one and only instance of {@link MainApplication}
	 */
	public static MainApplication getInstance() {
		return INSTANCE;
	}
}
