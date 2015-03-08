package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

/**Manages creation, updating and closing of display.
 * 
 * @author Flo
 *
 */
public class DisplayManager {
	
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int FPS_CAP = 120;
	
	/**Opens display when game starts.
	 * 
	 */
	public static void createDisplay(){
		
		ContextAttribs attribs = new ContextAttribs(3, 2) //OpenGL version 3.2
									.withForwardCompatible(true)
									.withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("AntSim");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT); //where to render game within the display
	};
	
	/**Updates display every single frame.
	 * 
	 */
	public static void updateDisplay(){
		
		Display.sync(FPS_CAP); //set frame cap for smooth rendering
		Display.update();
	};
	
	/**Closes display when game is exited.
	 * 
	 */
	public static void closeDisplay(){
		
		Display.destroy();
	};
}
