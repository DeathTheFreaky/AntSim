package graphicsUtils;

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
	
	/**Returns height of a triangle at a specific position inside the triangle, using barycentric coordinates of p.<br>
	 * Basically, barycentric coordinates are kinds of "weights".<br>
	 * These weights can be used to calculate the coordinates of any point inside a triangle: 
	 * p = λa*A + λb*B + λc*C.<br>
	 *<br>
	 * Barycentric coordinates explained: http://www.alecjacobson.com/weblog/?p=1596<br>
	 * <br>
	 * I tried to do the calculation myself, but found an apparently much faster code on stack overflow, 
	 * which I however don't understand mathematically. However, for better performance, this code shall be used:
	 * http://stackoverflow.com/questions/5507762/how-to-find-z-by-arbitrary-x-y-coordinates-within-triangle-if-you-have-triangle
	 * 
	 * @param p1 - first corner point of triangle
	 * @param p2 - second corner point of triangle
	 * @param p3 - third corner point of triangle
	 * @param x - x position inside the triangle at which to calculate the height 
	 * @param z - z position inside the triangle at which to calculate the height 
	 * @return - the height of a triangle at a specific position inside the triangle
	 */
	 public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, float x, float z) {
		
		/*We already know the x,y,z coordinates of the three corner points of the triangle: p1, p2, p3
		* We also know the x and z coordinates of the point pos inside this triangle for which we want to calculate its height
		* 
		* We also know that we can calculate any coordinate inside a triangle by its barycentric coordinates using the following formula:
		* p = λa*A + λb*B + λc*C.
		* 
		* For programming simplicity we will call our barycentric coordinates l1, l2, l3:
		* p = l1*p1 + l2*p2 + l3*p3
		* 
		* Given the formula above follows that:
		* I:   px = l1*p1.x + l2*p2.x * l3*p3.x
		* II:  py = l1*p1.y + l2*p2.y * l3*p3.y
		* III: pz = l1*p1.z + l2*p2.z * l3*p3.z
		* 
		* Furthermore, we know that, since App1p2/A + App1p3/A + App2p3/A = (App1p2 + App1p3 + App2p3)/A = A/A = 1:
		* l1 + l2 + l3 = 1
		* 
		* To find py, we can start by eliminiting one onknow variable l3 by substituting
		* l3 = 1 - (l1 + l2) = 1 - l1  - l2
		* 
		* Also, the barycentric coordinates stay the same for the formulas I, II and III.
		* If we substitute l3, we have two unknown variables l1 and l2.
		* So we need to find 2 formulas to solve this problem.
		* We pick formulas I (x - coordinate) and III (z - coordinate), since formula II would introduce another unknown variable py. 
		* 
		* Now we can solve this linear equation system using the cramer rules:
		* I:  px = l1*p1.x + l2*p2.x + (1 - l1 - l2)*p3.x
		* II: pz = l1*p1.z + l2*p2.z + (1 - l1 - l2)*p3.z
		* 
		* I:  px = l1*(p1.x - p3.x) + l2*(p2.x - p3.x) + p3.x
		* II: pz = l1*(p1.z - p3.z) + l2*(p2.z - p3.z) + p3.z
		* 
		* I:  l1*(p1.x - p3.x) + l2*(p2.x - p3.x) + p3.x - px = 0
		* II: l1*(p1.z - p3.z) + l2*(p2.z - p3.z) + p3.z - pz = 0
		* 
		* 
		* Calculate l2:
		* 
		* I:  l1 = (px - p3.x - l2*(p2.x - p3.x)) / (p1.x - p3.x)
		* II: l1 = (pz - p3.z - l2*(p2.z - p3.z)) / (p1.z - p3.z)
		* 
		* (px - p3.x - l2*(p2.x - p3.x)) / (p1.x - p3.x) = (pz - p3.z - l2*(p2.z - p3.z)) / (p1.z - p3.z)
		* (px - p3.x - l2*(p2.x - p3.x)) * (p1.z - p3.z) = (pz - p3.z - l2*(p2.z - p3.z)) * (p1.x - p3.x)
		* (px*p1.z - p3.x*p1.z - l2*(p2.x*p1.z - p3.x*p1.z)) - (px*p3.z - p3.x*p3.z - l2*(p2.x*p3.z - p3.x*p3.z)) = 
		* 		(pz*p1.x - p3.z*p1.x - l2*(p2.z*p1.x - p3.z*p1.x)) - (pz*p3.x - p3.z*p3.x - l2*(p2.z*p3.x - p3.z*p3.x))	
		*   
		* px*p1.z - p3.x*p1.z - l2*(p2.x*p1.z - p3.x*p1.z) - px*p3.z + p3.x*p3.z + l2*(p2.x*p3.z - p3.x*p3.z) = pz*p1.x - p3.z*p1.x - l2*(p2.z*p1.x - p3.z*p1.x) - pz*p3.x + p3.z*p3.x + l2*(p2.z*p3.x - p3.z*p3.x)
		* -l2*(p2.x*p1.z - p3.x*p1.z) + l2*(p2.x*p3.z - p3.x*p3.z) + l2*(p2.z*p1.x - p3.z*p1.x) - l2*(p2.z*p3.x - p3.z*p3.x) = pz*p1.x - p3.z*p1.x - pz*p3.x + p3.z*p3.x - px*p1.z + p3.x*p1.z + px*p3.z - p3.x*p3.z
		* 
		* l2*(-p2.x*p1.z + p3.x*p1.z + p2.x*p3.z - p3.x*p3.z + p2.z*p1.x - p3.z*p1.x - p2.z*p3.x + p3.z*p3.x) = pz*p1.x - p3.z*p1.x - pz*p3.x + p3.z*p3.x - px*p1.z + p3.x*p1.z + px*p3.z - p3.x*p3.z
		* 
		* l2 = p1.x*(pz-p3.z) - p3.x*(pz-p3.z) - p1.z*(px-p3.x) + p3.z*(px-p3.x) / (-p1.z*(p2.x-p3.x) + p3.z*(p2.x-p3.x) + p1.x*(p2.z-p3.z) - p3.x*(p2.z-p3.z)
		* l2 = ((p1.x-p3.x)*(pz-p3.z) - (p1.z-p3.z)*(px-p3.x)) / ((-p1.z+p3.z)*(p2.x-p3.x) + (p1.x-p3.x)*(p2.z-p3.z))
		* 
		* l2 = ((p3.z - p1.z) * (px - p3.x) + (p1.x - p3.x) * (pz - p3.z)) / ((p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z))
		* 
		* Det = ((p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z))
		* 
		* 
		* calculate l1:
		* 
		* I:  l2 = (px - p3.x - l1*(p1.x - p3.x)) / (p2.x - p3.x)
		* II: l2 = (pz - p3.z - l1*(p2.z - p3.z)) / (p1.z - p3.z)
		* 
		* (px - p3.x - l1*(p1.x - p3.x)) / (p2.x - p3.x) = (pz - p3.z - l1*(p1.z - p3.z)) / (p2.z - p3.z)
		* (px - p3.x - l1*(p1.x - p3.x)) * (p2.z - p3.z) = (pz - p3.z - l1*(p1.z - p3.z)) * (p2.x - p3.x)
		* (px*p2.z - p3.x*p2.z - l1*(p1.x*p2.z - p3.x*p2.z)) - (px*p3.z - p3.x*p3.z - l1*(p1.x*p3.z - p3.x*p3.z)) = 
		* 		(pz*p2.x - p3.z*p2.x - l1*(p1.z*p2.x - p3.z*p2.x)) - (pz*p3.x - p3.z*p3.x - l1*(p1.z*p3.x - p3.z*p3.x))	
		*   
		* px*p2.z - p3.x*p2.z - l1*(p1.x*p2.z - p3.x*p2.z) - px*p3.z + p3.x*p3.z + l1*(p1.x*p3.z - p3.x*p3.z) = pz*p2.x - p3.z*p2.x - l1*(p1.z*p2.x - p3.z*p2.x) - pz*p3.x + p3.z*p3.x + l1*(p1.z*p3.x - p3.z*p3.x)
		* -l1*(p1.x*p2.z - p3.x*p2.z) + l1*(p1.x*p3.z - p3.x*p3.z) + l1*(p1.z*p2.x - p3.z*p2.x) - l1*(p1.z*p3.x - p3.z*p3.x) = pz*p2.x - p3.z*p2.x - pz*p3.x + p3.z*p3.x - px*p2.z + p3.x*p2.z + px*p3.z - p3.x*p3.z
		* 
		* l1*(-p1.x*p2.z + p3.x*p2.z + p1.x*p3.z - p3.x*p3.z + p1.z*p2.x - p3.z*p2.x - p1.z*p3.x + p3.z*p3.x) = pz*p2.x - p3.z*p2.x - pz*p3.x + p3.z*p3.x - px*p2.z + p3.x*p2.z + px*p3.z - p3.x*p3.z
		* 
		* l1 = p2.x*(pz-p3.z) - p3.x*(pz-p3.z) - p2.z*(px-p3.x) + p3.z*(px-p3.x) / (-p2.z*(p1.x-p3.x) + p3.z*(p1.x-p3.x) + p2.x*(p1.z-p3.z) - p3.x*(p1.z-p3.z)
		* l1 = ((p2.x-p3.x)*(pz-p3.z) - (p2.z-p3.z)*(px-p3.x)) / ((-p2.z+p3.z)*(p1.x-p3.x) + (p2.x-p3.x)*(p1.z-p3.z))
		* 
		* l1 = ((p2.z - p3.z) * (px - p3.x) + (p3.x - p2.x) * (pz - p3.z)) / ((p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z))
		*  
		* 
		* */
		 
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);

        float l1 = ((p2.z - p3.z) * (x - p3.x) + (p3.x - p2.x) * (z - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (x - p3.x) + (p1.x - p3.x) * (z - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;

        return l1 * p1.y + l2 * p2.y + l3 * p3.y; //use barycentric coordinates to determine value of y
	}
	
}
