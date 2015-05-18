package at.antSim.graphics.graphicsUtils;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import at.antSim.AntSim;
import at.antSim.Globals;
import at.antSim.MainApplication;

/**Manages creation, updating and closing of display.
 * 
 * @author Flo
 *
 */
public class DisplayManager {
	
	private static long lastFrameTime; //time at the end of the last frame
	private static float delta; //time taken to render previous frame
	private static int speed = 1; //2 is default speed, 1 is half speed, 4 is double...
	
	/**Opens display when game starts.
	 * 
	 */
	public static void createDisplay(){
				
		try {

			setDisplayMode(Globals.displayWidth, Globals.displayHeight, Globals.fullscreen);
			Display.setTitle("AntSim");
			
			//setup OpenGL to and run in forward-compatible mode, Ensure that OpenGL is being used
			PixelFormat pixelFormat = new PixelFormat();
			ContextAttribs attribs = new ContextAttribs(3,2) //OpenGL version 3.2
			.withForwardCompatible(true)
			.withProfileCore(true);
			Display.create(pixelFormat, attribs);
			
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		//initialize glViewport to stretch from upper left to lower right corner of display
		GL11.glViewport(0, 0, Globals.displayWidth, Globals.displayHeight); //where to render game within the display
		lastFrameTime = getCurrentTime();
	};
	
	/**Updates display every single frame.
	 * 
	 */
	public static void updateDisplay(){
		Display.sync((int) (Globals.FPS_CAP)); //set frame cap for smooth rendering
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime)/1000f; //delta in seconds
		lastFrameTime = currentFrameTime;
	};
	
	/**Closes display when game is exited.
	 * 
	 */
	public static void closeDisplay(){
		AntSim.getInstance().onClose();
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
	
	/**
	 * Set the display mode to be used.
	 * <br>
	 * <br>
	 * Code taken from http://wiki.lwjgl.org/index.php?title=LWJGL_Basics_5_%28Fullscreen%29
	 * 
	 * @param width The width of the display required
	 * @param height The height of the display required
	 * @param fullscreen True if we want fullscreen mode
	 */
	public static void setDisplayMode(int width, int height, boolean fullscreen) {
	 
	    // return if requested DisplayMode is already set
	    if ((Display.getDisplayMode().getWidth() == width) && 
	        (Display.getDisplayMode().getHeight() == height) && 
	    (Display.isFullscreen() == fullscreen)) {
	        return;
	    }
	    	 
	    try {
	        DisplayMode targetDisplayMode = null;
	         
	    if (fullscreen) {
	        DisplayMode[] modes = Display.getAvailableDisplayModes();
	        int freq = 0;
	                 
	        for (int i=0;i<modes.length;i++) {
	            DisplayMode current = modes[i];
	                     
	        if ((current.getWidth() == width) && (current.getHeight() == height)) {
	            if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
	                if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
	                targetDisplayMode = current;
	                freq = targetDisplayMode.getFrequency();
	                        }
	                    }
	 
	            // if we've found a match for bpp and frequence against the 
	            // original display mode then it's probably best to go for this one
	            // since it's most likely compatible with the monitor
	            if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
	                        (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
	                            targetDisplayMode = current;
	                            break;
	                    }
	                }
	            }
	        } else {
	            targetDisplayMode = new DisplayMode(width,height);
	        }
	 
	        if (targetDisplayMode == null) {
	            System.out.println("Failed to find value mode: "+width+"x"+height+" fs="+fullscreen);
	            return;
	        }
	 
	        Display.setDisplayMode(targetDisplayMode);
	        Display.setFullscreen(fullscreen);
	             
	    } catch (LWJGLException e) {
	        System.out.println("Unable to setup mode "+width+"x"+height+" fullscreen="+fullscreen + e);
	    }
	}
}
