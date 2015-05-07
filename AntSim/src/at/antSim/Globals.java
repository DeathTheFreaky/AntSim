package at.antSim;

import java.awt.*;

/**Stores global variables, mainly config file entries and constants like resource folder, fps_cap...
 * 
 * @author Flo, Clemens
 *
 */
public final class Globals {
	
	//global Constants
	public static String WORKING_DIR = System.getProperty("user.dir");
	public static final String RESOURCES = WORKING_DIR + "\\res\\";
	public static final String MODELS = RESOURCES + "models\\";
	public static final String TEXTURES = RESOURCES + "textures\\";
	public static final String CONFIG = WORKING_DIR + "\\config";
	public static final String SHADERS = WORKING_DIR + "\\src\\at\\antSim\\graphics\\shaders\\";
	public static final int FPS_CAP = 120;
	/**
	 * duration of 1/FPS second in nanoseconds
	 */
	public static final int FPS_DURATION_NANONS = (int) ((1.0 / FPS_CAP) * 1000 * 1000 * 1000);
	public static final float WORLD_SIZE = 800; //square
	
	//config file entries set by config reader
	private static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	public static int displayWidth = gd.getDisplayMode().getWidth();
	public static int displayHeight = gd.getDisplayMode().getHeight();
	
	private Globals() {}; //do not allow instances of this class
}
