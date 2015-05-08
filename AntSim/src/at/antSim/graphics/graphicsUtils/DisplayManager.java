package at.antSim.graphics.graphicsUtils;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

import at.antSim.Globals;

/**Manages creation, updating and closing of display.
 * 
 * @author Flo
 *
 */
public class DisplayManager {
	
	private static long lastFrameTime; //time at the end of the last frame
	private static float delta; //time taken to render previous frame
	
	/**Opens display when game starts.
	 * 
	 */
	public static void createDisplay(){
				
		try {
			
			//setup up and open Window
			Display.setDisplayMode(new DisplayMode(Globals.displayWidth, Globals.displayHeight)); 
			Display.setTitle("AntSim");
			
			//setup OpenGL to and run in forward-compatible mode, Ensure that OpenGL is being used
			PixelFormat pixelFormat = new PixelFormat();
			/*ContextAttribs attribs = new ContextAttribs(3,2) //OpenGL version 3.2
			.withForwardCompatible(true)
			.withProfileCore(true);
			Display.create(pixelFormat, attribs);*/
			Display.create(); //cannot use fixed function gui in 3.2 context :-(
			
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		//initialize glViewport to stretch from upper left to lower right corner of display
		GL11.glViewport(0, 0, Globals.displayWidth, Globals.displayHeight); //where to render game within the display
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		lastFrameTime = getCurrentTime();
	};
	
	/**Updates display every single frame.
	 * 
	 */
	public static void updateDisplay(){
		Display.sync(Globals.FPS_CAP); //set frame cap for smooth rendering
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime)/1000f; //delta in seconds
		lastFrameTime = currentFrameTime;
	};
	
	/**Closes display when game is exited.
	 * 
	 */
	public static void closeDisplay(){
		Display.destroy();
	};
	
	/**
	 * @return - time taken to render previous frame
	 */
	public static float getFrameTimeSeconds() {
		return delta;
	}
	
	/**
	 * @return - time in milliseconds
	 */
	private static long getCurrentTime() {
		return Sys.getTime()*1000/Sys.getTimerResolution();
	}
}
