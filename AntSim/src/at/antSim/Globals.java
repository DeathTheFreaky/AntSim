package at.antSim;

import java.util.HashMap;
import java.util.Map.Entry;

/**Stores global variables, mainly config file entries and constants like resource folder, fps_cap...
 * 
 * @author Flo, Clemens
 *
 */
public final class Globals {
	
	//global Constants
	public static final String WORKING_DIR = System.getProperty("user.dir");
	public static final String RESOURCES = WORKING_DIR + "\\res\\";
	public static final String MODELS = RESOURCES + "models\\";
	public static final String TEXTURES = RESOURCES + "textures\\";
	public static final String CONFIG = WORKING_DIR + "\\config";
	public static final String SHADERS = WORKING_DIR + "\\src\\at\\antSim\\graphics\\shaders\\";
	public static final int FPS_CAP = 120;
	/**
	 * duration of 1/FPS second in nanoseconds
	 */
	public static final int MILIS_TO_NANOS_RATIO = 1000 * 1000;
	public static final int FPS_DURATION_NANONS = (int) ((1.0 / FPS_CAP) * 1000 * MILIS_TO_NANOS_RATIO);
	public static final float WORLD_SIZE = 5; //square
	public static final float TIMECYCLE_MULTIPLIER = 4.0f;
	
	//config file entries set by config reader
	public static int displayWidth; 
	public static int displayHeight;
	public static int displaySaveWidth;
	public static int displaySaveHeight;
	public static boolean fullscreen;
	public static int fontRows = 16;
	public static int fontCols = 16;
	public static float gravity = -0.5f;
	
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
		
		boolean used = false;
		
		for (Entry<String, Integer> entry : keyMappings.entrySet()) {
			if (entry.getValue() == key && !entry.getKey().equals(fieldname)) {
				used = true;
			}
		}
		
		return used;
	}
}
