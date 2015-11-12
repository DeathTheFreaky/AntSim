package at.antSim.graphics.graphicsUtils;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**Used for transparent rendering to sort vertices by their barycentre's distance from the camera.
 * 
 * @author Flo
 *
 */
public class TransparentTriangle {

	int indexBufferOffset; // multiples of 3 -> 3 indices per triangle
	
	Vector4f centroid;
	Vector4f translatedCentroid;
	float cameraSquaredDist; // not actually camera pos, root not considered
	
	public TransparentTriangle(int bufferOffset, Vector3f p1, Vector3f p2, Vector3f p3)
	{
		this.indexBufferOffset = bufferOffset;
				
		// calculate centroid of vertex in Object Space!!!
		Vector3f centroid3f = new Vector3f();
		Vector3f.add(p1, p2, centroid3f);
		Vector3f.add(centroid3f, p3, centroid3f);
		centroid3f.scale(1/3f);
		
		centroid = new Vector4f(centroid3f.x, centroid3f.y, centroid3f.z, 1);
		translatedCentroid = new Vector4f();
		
		// System.out.println("Created TransparentTriangle with indexBufferOffset " + indexBufferOffset + " and centroid " + centroid);
	}

	public int getIndexBufferOffset() {
		return indexBufferOffset;
	}
	
	/**Calculates distance from camera to centroid of this transparent triangle in world space.
	 * 
	 * First, centroid needs to be translated by translationMatrix to be positioned correctly in world space.
	 */
	public void calcCameraSquaredDist(Vector3f cameraPos)
	{	
		Vector3f toCamera = new Vector3f(translatedCentroid.x - cameraPos.x, translatedCentroid.y - cameraPos.y, translatedCentroid.z - cameraPos.z);
		cameraSquaredDist = toCamera.lengthSquared(); // do not consider root cause it doesn't matter
	}
	
	public void updateTransform(Matrix4f transformationMatrix)
	{
		Matrix4f.transform(transformationMatrix, centroid, translatedCentroid); // translate here
	}
	
	public float getCameraSquaredDist()
	{
		return cameraSquaredDist;
	}
}
