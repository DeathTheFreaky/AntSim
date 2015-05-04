package at.antSim.graphics.graphicsUtils;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import at.antSim.graphics.entities.Camera;

/**Maths contains useful mathematical methods.
 * 
 * @author Flo
 *
 */
public class Maths {
	
	/*NOTE ABOUT THE ORDER OF TRANSFORMATIONS IN OPENGL: 
	 * 
	 * OpenGl reads its matrices in a column major order. 
	 * The result is that each subsequent operation is a pre-multiplication of all the operations before it,
	 * not a post-multiplication.
	 * 
	 * Thus, each subsequent call to an OpenGL transformation operation is the first thing that affects on object,
	 * and it goes in reverse order from how it is written.
	 * 
	 * Thus, if we usually perform a complete transform as:
	 * 1. scaling
	 * 2. rotating 
	 * 3. translating
	 * 
	 * we have to change this order to
	 * 1. translating
	 * 2. rotating
	 * 3. scaling
	 * 
	 * The rotation order is defined by the Euler Transform as: 
	 * 1. Roll (rotate around z-Axis)
	 * 2. Pitch (rotate around x-Axis)
	 * 3. Yaw (rotate around y-Axis)
	 * 
	 * Since we need to reverse this order for the reasons mentioned above, we have to perform our rotating operations in the order:
	 * 1. Yaw (rotate around y-Axis)
	 * 2. Pitch (rotate around x-Axis)
	 * 3. Roll (rotate around z-Axis)
	 * 
	 * see: http://www.opengl-tutorial.org/beginners-tutorials/tutorial-3-matrices/
	 * see: http://stackoverflow.com/questions/15993339/opengl-order-of-matrix-transformations
	 * see: http://en.wikipedia.org/wiki/Row-major_order#Column-major_order
	 * see: http://stackoverflow.com/questions/17717600/confusion-between-c-and-opengl-matrix-order-row-major-vs-column-major
	 * see: CG_04_Transforms.pdf
	 * 
	 * For further information, a quote from :
	 * 
	 * Here’s where the confusion starts: https://fgiesen.wordpress.com/2012/02/12/row-major-vs-column-major-row-vectors-vs-column-vectors/ 
	 * To transform a vector by a matrix, you first need to convert the vector to a matrix (i.e. choose whether it’s supposed to be a column or row vector this time), 
	 * then multiply the two. In the usual graphics setting, we have a 4×4 matrix T and a 4-vector v. 
	 * Since the middle dimension in a matrix product must match, we can’t do this arbitrarily. 
	 * If we write v as a 4×1 matrix (column vector), we can compute Tv but not vT. If v is instead a 1×4 matrix (row vector), only vT works and Tv leads to a "dimension mismatch". 
	 * For both column and row vectors, the result of that is again a column or row vector, respectively, which has repercussions: 
	 * if we want to transform the result again by another matrix, again there’s only one place where we can legally put it. 
	 * For column vectors, transforming a vector by T then S leads to the expression S(Tv)=STv=(ST)v, so the matrix that represents "first T then S" is given by the matrix ST. 
	 * For row vectors, we get (vT)S=vTS=v(TS), so the matrix for the concatenation of the two is given by TS. 
	 * Small difference, big repercussions: whether you choose row or column vectors influences how you concatenate transforms.
	 * 
	 * Furthermore, for a "fresh" matrix, we always have to set it to an identity matrix,
	 * since OpenGL works on the principle of the "current matrix" being calculated by multiplying
	 * a previous with an operational matrix. Now at the beginning, the previous matrix obviously has to be an identity matrix
	 * because for a 0-Matrix the multiplication would result in a 0-Result and for all other matrices except for the identity matrix
	 * the results would be distorted.
	 * 
	 * 
	 * Last note: the order of multiplications of matrices is very important.
	 * If we multiply our 4x4 matrix with a vector, we have to ensure the order is like this: M*v.
	 * 
	 * Otherwise we would not be able to calculate the result since a 4x1 matrix (mxn) cannot be multiplied with a 4x4 matrix (!nxp) because the two n's have to match
	 * (remember: row times column... -> does not work if row has 1 element but column has 4 elements).
	 */
	
	/**Creates a transformation matrix to be used for the transformation of 2D gui element with no rotation.<br>
	 * The matrix is going to be applied to the model each frame.
	 * 
	 * @param translation - an x,y translation in world coordinate space
	 * @param scale - scale for transformation of the gui element
	 * @return - a 4x4 transformation matrix
	 */
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity(); //start a "fresh" previous matrix for multiplication
		//perform operations in reverse order due to column-major order post-multiplication equaling row-major order pre-multiplication
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		
		return matrix;
	}
	
	/**Creates a transformation matrix to be used for the transformation of 3D models.<br>
	 * The matrix is going to be applied to the model each frame.
	 * 
	 * @param translation - an x,y,z translation in world coordinate space
	 * @param rx - rotation value for x - axis
	 * @param ry - rotation value for y - axis
	 * @param rz - rotation value for z - axis
	 * @param scale - scale for transformation of the model
	 * @return - a 4x4 transformation matrix
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translation, 
			float rx, float ry, float rz, float scale) {
		
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity(); //start a "fresh" previous matrix for multiplication
		//perform operations in reverse order due to column-major order post-multiplication equaling row-major order pre-multiplication
		Matrix4f.translate(translation, matrix, matrix); 
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix); //scale uniformly in all 3 axis	
		
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
		
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z); //camera needs to move in opposite direction compared to the desired view direction
		
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity(); //resets matrix to identity matrix
		/* ok... so now it gets a little complicated. 
		 * As mentioned earlier, due to openGL using column-major order, post-multiplication is applied.
		 * This means that the last-issued transforms will take effect first. 
		 * Additionally, by the use of a view matrix, we simulate a camera which does not actually move cause the OpenGL coordinate system is fixed (0,0 is middle of screen).
		 * To simulate camera movement, we have to apply the reverse translation direction (mulitply with -1) and perform our transformations in the opposite order.
		 * That is why translate is, in text context, written after rotate (being logically before rotate), although it is normally the other way around.
		 * 
		 * The reason why we don't negate the pitch, yaw and roll is, that we want to have an inverted camera, so when we move the mouse to the left, the camera turns right.
		 * 
		 * The order of the three rotate transformations is defined as an Euler Transformation, in the order: Roll, Pitch, Yaw.
		 * Usually, in a transformation matrix the order would actually have to by Yaw, Pitch, Roll, since due to Column-major order the last column takes effect first.
		 * However, since in a view matrix is kind of an inverted transformation matrix of the world compared to the camera position, 
		 * we once again have to change the order of our rotations which results in Roll, Pitch, Yaw.
		 * 
		 * For more information on matrixes, visit: http://www.opengl-tutorial.org/beginners-tutorials/tutorial-3-matrices/.
		 */
		Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0,0,1), viewMatrix, viewMatrix); //rotate viewMatrix around z-Axis by the camera's roll
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1,0,0), viewMatrix, viewMatrix); //rotate viewMatrix around x-Axis by the camera's pitch
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0,1,0), viewMatrix, viewMatrix); //rotate viewMatrix around y-Axis by the camera's yaw
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix); //move the viewMatrix in opposite direction of camera's position +

		return viewMatrix;
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
		* calculate l3:
		* l3 can be easily obtained by substracting (l1 + l2) from 1
		* */
		 
		float div = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);

        float l1 = ((p2.z - p3.z) * (x - p3.x) + (p3.x - p2.x) * (z - p3.z)) / div;
        float l2 = ((p3.z - p1.z) * (x - p3.x) + (p1.x - p3.x) * (z - p3.z)) / div;
        float l3 = 1.0f - l1 - l2;

        return l1 * p1.y + l2 * p2.y + l3 * p3.y; //use barycentric coordinates to determine value of y
	}	
}
