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
import at.antSim.enums.Dimension;

/**Manages creation, updating and closing of display.
 * 
 * @author Flo
 *
 */
public class DisplayManager {
	
	private static long lastFrameTime; //time at the end of the last frame
	private static float delta; //time taken to render previous frame
	private static Dimension currentRenderDimension;
	
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
	public static long getCurrentTime() {
		return Sys.getTime()*1000/Sys.getTimerResolution();
	}
	
	public static void make2D() {
	    GL11.glEnable(GL11.GL_BLEND);
	    GL11.glMatrixMode(GL11.GL_PROJECTION);
	    GL11.glLoadIdentity();
	    GL11.glOrtho(0.0f, Globals.displayWidth, Globals.displayHeight, 0.0f, 0.0f, 1.0f);

	    GL11.glMatrixMode(GL11.GL_MODELVIEW);
	    GL11.glLoadIdentity();
	}

	public static void make3D() {
	    GL11.glDisable(GL11.GL_BLEND);
	    GL11.glMatrixMode(GL11.GL_PROJECTION);
	    GL11.glLoadIdentity(); // Reset The Projection Matrix
	    GLU.gluPerspective(45.0f, ((float) Globals.displayWidth / (float) Globals.displayHeight), 0.1f, 100.0f); // Calculate The Aspect Ratio Of The Window

	    GL11.glMatrixMode(GL11.GL_MODELVIEW);
	    GL11.glLoadIdentity();
	}
	
	public static void initializeOpenGL() {

	    //Enabling OpenGL functions
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);

	    //Using them
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public static void switchTo2D() {

	    if (currentRenderDimension != Dimension.DIM_2) {

	        //GL11.glMatrixMode(GL11.GL_MODELVIEW);
	        //GL11.glMatrixMode(GL11.GL_PROJECTION);
	        //GL11.glLoadIdentity();
	        GLU.gluOrtho2D(0, Display.getWidth(), Display.getHeight(), 0);
	        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
	        //GL11.glMatrixMode(GL11.GL_MODELVIEW);

	        //GL11.glPushMatrix();
	        //GL11.glLoadIdentity();

	        currentRenderDimension = Dimension.DIM_2;

	    }
	}

	public static void switchTo3D() {

	    if (currentRenderDimension != Dimension.DIM_3) {

	    	GL11.glPopMatrix(); //From 2D
	    	GL11.glMatrixMode(GL11.GL_PROJECTION);
	    	GL11.glLoadIdentity();
	        GLU.gluPerspective(45, (float) Display.getWidth() / Display.getHeight(), 0.1f, 1000);
	        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
	        GL11.glMatrixMode(GL11.GL_MODELVIEW);

	        currentRenderDimension = Dimension.DIM_3;
	    }
	}
}
