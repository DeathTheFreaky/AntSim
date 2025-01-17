package at.antSim;

import java.util.HashMap;
import java.util.Map.Entry;

import com.bulletphysics.collision.broadphase.CollisionFilterGroups;

/**Stores global variables, mainly config file entries and constants like resource folder, fps_cap...
 * 
 * @author Flo, Clemens
 *
 */
public final class Globals {
	
	//default variables are set in ConfigReader
	//delete config file to write defaults on next start!!!
	
	//global Constants
	public static final String WORKING_DIR = System.getProperty("user.dir");
	public static final String RESOURCES = WORKING_DIR + "\\res\\";
	public static final String MODELS = RESOURCES + "models\\";
	public static final String TEXTURES = RESOURCES + "textures\\";
	public static final String CONFIG = WORKING_DIR + "\\config";
	public static final String SHADERS = WORKING_DIR + "\\shaders\\";
	public static final int FPS_CAP = 120;
	/**
	 * duration of 1/FPS second in nanoseconds
	 */
	public static final int MILIS_TO_NANOS_RATIO = 1000 * 1000;
	public static final int FPS_DURATION_NANONS = (int) ((1.0 / FPS_CAP) * 1000 * MILIS_TO_NANOS_RATIO);
	public static final float WORLD_SIZE = 800; //square
	public static final float TIMECYCLE_MULTIPLIER = 4.0f;
	
	//default additional margin for a positionLocator
	public static final float POSITION_LOCATOR_MARGIN = 50; //25
	public static final float MASS_DUMMIE = 10;
	
	//pheromone size
	public static final float PHERONOME_SIZE = 50;
	
	//bit masks for collision filtering
	public static final short COL_ALL = CollisionFilterGroups.ALL_FILTER;
	public static final short COL_DEFAULT = CollisionFilterGroups.DEFAULT_FILTER;
	public static final short COL_STATIC = CollisionFilterGroups.STATIC_FILTER;
	public static final short COL_KINEMATIC = CollisionFilterGroups.KINEMATIC_FILTER;
	public static final short COL_DEBRIS = CollisionFilterGroups.DEBRIS_FILTER;
	public static final short COL_SENSOR = CollisionFilterGroups.SENSOR_TRIGGER;
	public static final short COL_CHARACTER = CollisionFilterGroups.CHARACTER_FILTER;
	public static final short COL_TERRAIN = 64;
	public static final short COL_MOVING = 128;
	public static final short COL_BORDER = 256;
	
	//transparency of pheromones and positionLocators
	public static final float GHOST_TRANSPARENCY = 0.75f;
	public static float currentGhostTransparency = 0.75f;
	
	//maximum nbr of ants waiting at a position locator
	public static final int MAX_LOCATOR_ANTS = 1;
	
	//speeds in various movement situations
	public static final float ANT_SPEED = 20f;
	public static final float LOCKIN_SPEED = 5f;
	
	//maximum number of food an ant can carry
	public static int maxFoodCarry;
	
	//max lifetime of pheromone
	public static int maxPheromoneLifetime;
	
	//default food resource sizes
	public static int foodResAppleSize;
	public static int foodResAntSize;
	public static int foodResGrasshopperSize;
	public static int foodResSquirrelSize;
	
	//attack and hp of enemies
	public static float enemyAntHp;
	public static float enemyAntAttack;
	public static float enemyGrasshopperHp;
	public static float enemyGrasshopperAttack;
	
	//attack and hp of ants
	public static float antHp;
	public static float antAttack;
	public static float queenHp;
	public static float queenAttack;
	
	public static int hiveFoodStacks; //
	
	//config file entries set by config reader
	public static int displayWidth; 
	public static int displayHeight;
	public static int displaySaveWidth;
	public static int displaySaveHeight;
	public static boolean fullscreen;
	public static int fontRows = 16;
	public static int fontCols = 16;
	public static float gravity = -98.1f; //just some extra gravity to avoid ants jumping like crazy
	
	public static int moveForwardKey;
	public static int moveBackwardKey;
	public static int moveLeftKey;
	public static int moveRightKey;
	public static int moveUpKey;
	public static int moveDownKey;
	public static int turnLeftKey;
	public static int turnRightKey;
	public static int tiltDownKey;
	public static int tiltUpKey;
	public static int zoomInKey;
	public static int zoomOutKey;
	public static int restoreCameraPosition;
	public static int showGhostSpheres;
	
	public static int invertHorizontalAxis; //-1 if inverted, 1 if not inverted
	public static int invertVerticalAxis;
	
	private Globals() {}; //do not allow instances of this class
	
	/**Indicates whether a key is already in use.
	 * @param fieldname
	 * @param key
	 * @return
	 */
	public static boolean isKeyAlreadyUsed(String fieldname, int key) {
		
		HashMap<String, Integer> keyMappings = new HashMap<>();
		
		keyMappings.put("moveForwardKey", moveForwardKey);
		keyMappings.put("moveBackwardKey", moveBackwardKey);
		keyMappings.put("moveLeftKey", moveLeftKey);
		keyMappings.put("moveRightKey", moveRightKey);
		keyMappings.put("moveUpKey", moveUpKey);
		keyMappings.put("moveDownKey", moveDownKey);
		keyMappings.put("turnLeftKey", turnLeftKey);
		keyMappings.put("turnRightKey", turnRightKey);
		keyMappings.put("tiltDownKey", tiltDownKey);
		keyMappings.put("tiltUpKey", tiltUpKey);
		keyMappings.put("zoomInKey", zoomInKey);
		keyMappings.put("zoomOutKey", zoomOutKey);
		keyMappings.put("restoreCameraPosition", restoreCameraPosition);
		keyMappings.put("showGhostSpheres", showGhostSpheres);
		
		boolean used = false;
		
		for (Entry<String, Integer> entry : keyMappings.entrySet()) {
			if (entry.getValue() == key && !entry.getKey().equals(fieldname)) {
				used = true;
			}
		}
		
		return used;
	}
}
