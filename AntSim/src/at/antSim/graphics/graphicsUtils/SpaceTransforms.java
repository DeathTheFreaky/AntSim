package at.antSim.graphics.graphicsUtils;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**Methods for conversion between the different spaces used in OpenGL.
 * 
 * @author Flo
 *
 */
public class SpaceTransforms {

	/**Converts mouse coordinates from screen coordinates to OpenGL coordinate system (-1 to 1).
	 * 
	 * @param mouseX - x position of mouse in screen coordinates
	 * @param mouseY - y position of mouse in screen coordinates
	 * @return - a Vector2f containing the mouse's x and y coordinates in OpenGL's coordinate system
	 */
	public static Vector2f toNormalisedDeviceCoords(float mouseX, float mouseY) {
		float x = (2f * mouseX) / Display.getWidth() - 1f; //multiply by 2 since the normalized range has a "length" of 2 stretching from -1 to 1
		/* lwjgl uses coordinate system with 0,0 in bottom left corner, as opposed to 0,0 being the top left corner in viewport space (screen pixels)
		 * Image we have a point with an y-coordinate in viewport space of 2/3 of the screen height (measured from the top).
		 * So the point's y-coordinate would be at 1/3 from the bottom of the screen. 
		 * By multiplying 2/3 with 2, we get 4/3 and if we substract 1 from that (3/3) we get 1/3 which gives us our desired results.
		 */
		float y = (2f * mouseY) / Display.getHeight() - 1f; 
		return new Vector2f(x, y);
	}
	
	/**Converts from clip space 4d coordinates to eye space 4d coordinates.
	 * 
	 * @param clipCoords - 4d coordinates in clip space
	 * @return - 4d coordinates in eye space
	 */
	public static Vector4f toEyeCoords(Vector4f clipCoords, Matrix4f projectionMatrix) {
		/*
		 * Normally, to get into clip space from eye space, we multiply the vector by a projection matrix. 
		 * However, this time we want to get into eye space from clip space, so we need to multiply the vector by the inverted projection matrix.
		 * Furthermore, we also have to reverse the order of multiplication operations for an inverse operation since in Matrix multiplication order is important.
		 */
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null); 
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null); //reverse order of multiplication operations for inverse operation
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f); //make z-coordinate point into the screen, ray is a direction -> w = 0
	}
	
	/**Converts 4d coordinates from eyespace to a 3d coordinates in world space.
	 * 
	 * @param eyeCoords - 4d eyespace coordinates
	 * @return - the mouse Ray as a 3d Vector in worldspace
	 */
	public static Vector3f toWorldCoords(Vector4f eyeCoords, Matrix4f viewMatrix) {
		/*
		 * Normally, to get into eye space from world space, we multiply the vector by a view matrix. 
		 * However, this time we want to get into world space from eye space, so we need to multiply the vector by the inverted view matrix.
		 * Furthermore, we also have to reverse the order of multiplication operations for an inverse operation since in Matrix multiplication order is important.
		 */
		Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null); //reverse order of multiplication operations for inverse operation
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		/* Since we manually specified a -1 for the z component of eyeCoords vector, our ray isn't normalised. 
		 * We should do this before we use it.
		 * 
		 * "This should balance the up-and-down, left-and-right, and forwards components for us. 
		 * So, assuming our camera is looking directly along the -Z world axis, we should get [0,0,-1] when the mouse is in the centre of the screen, 
		 * and less significant z values when the mouse moves around the screen. 
		 * This will depend on the aspect ratio, and field-of-view defined in the view and projection matrices. 
		 * We now have a ray that we can compare with surfaces in world space." - see: http://antongerdelan.net/opengl/raycasting.html -> Step 4: 4d World Coordinates
		 */
		mouseRay.normalise();
		return mouseRay;
	}
}
