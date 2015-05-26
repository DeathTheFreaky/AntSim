package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;

/**Maths contains useful mathematical methods.
 * 
 * @author Flo
 *
 */
public class Maths {
	
	/**Creates a transformation matrix to be used for the transformation of 2D gui element.
	 * 
	 * @param translation - an x,y translation
	 * @param scale - scale for transformation of the gui element
	 * @return - a 4x4 transformation matrix
	 */
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}
	
	/**Creates a transformation matrix to be used for the transformation of 3D models.
	 * 
	 * @param translation - an x,y,z translation
	 * @param rx - rotation value for x - axis
	 * @param ry - rotation value for y - axis
	 * @param rz - rotation value for z - axis
	 * @param scale - scale for transformation of the model
	 * @return - a 4x4 transformation matrix
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translation, 
			float rx, float ry, float rz, float scale) {
		
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity(); //resets matrix to identity matrix
		
		//translate, rotate around all 3 axis, scale the matrix
		Matrix4f.translate(translation, matrix, matrix); 
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix); //scale uniformly in all 3 axis
		
		// this process resembles: TransformedVector = TranslationMatrix * RotationMatrix * ScaleMatrix * OriginalVector;
		
		return matrix;
	}
	
	/**Creates a view matrix to move the view on the world.<br>
	 * <br>
	 * To make the world seem to move, although the OpenGL coordinates system stays fixed,
	 * every object in the world needs to move in the opposite direction of the desired camera direction.<br>
	 * <br>
	 * Hence, the viewMatrix's translation is calculated by multiplying the camera's position with -1.
	 * 
	 * @param camera - the Camera used to create the view matrix
	 * @return - a 4x4 view matrix
	 */
	public static Matrix4f createViewMatrix(Camera camera) {
		
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity(); //resets matrix to identity matrix
		
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1,0,0), viewMatrix, viewMatrix); //rotate viewMatrix around x-Axis by the camera's pitch
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0,1,0), viewMatrix, viewMatrix); //rotate viewMatrix around y-Axis by the camera's yaw
		Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0,0,1), viewMatrix, viewMatrix); //rotate viewMatrix around z-Axis by the camera's yaw
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z); //camera needs to move in opposite direction compared to the desired view direction
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix); //move the viewMatrix in opposite direction of camera's position 
		
		return viewMatrix;
	}
	
	/**Returns height of a triangle at a specific position inside the triangle.
	 * 
	 * @param p1 - first corner point of triangle
	 * @param p2 - second corner point of triangle
	 * @param p3 - third corner point of triangle
	 * @param pos - x,z position inside the triangle at which to calculate the height (interpolated)
	 * @return - the height of a triangle at a specific position inside the triangle
	 */
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
}
