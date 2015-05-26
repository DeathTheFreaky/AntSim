package at.antSim.graphics.models;

/**The RawModel stores 3D models as VAOs (Vertex Array Objects).<br><br>
 * VAOs are the modern way of storing and rendering models in OpenGl.<br>
 * A VAO stores data about 3D Models in slots which are called attribute lists.<br>
 * Usually, each attribute list holds different kinds of data sets (vertex positions, vertex colors, normal vectors...).<br>
 * <br>
 * These data sets are stored inside the VAOs as VBOs (Vertex Buffer Objects).<br>
 * A VBO is basically an array of numbers, and these numbers represent any form of data (vertex positions...).<br>
 * <br>
 * The data stored as VBOs inside VAOs can be accessed via VAO IDs.<br>
 * <br>
 * Watch http://www.youtube.com/watch?v=WMiggUPst-Q for more information.
 * 
 * @author Flo
 *
 */
public class RawModel {
	
	private int vaoID; //id or "name" of the vao used to store this model's vertex indices, positional coordinates, texture coordinates and normals
	private int vertexCount; //how many vertexes are stored inside the model
	private float furthestDistance; //furthest distance from a point to the origin of a geometry model
	private float xLength;
    private float yLength;
    private float zLength;
	
	public RawModel(int vaoID, int vertexCount) {
		
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;	
	}

	/**
	 * @return - a RawModel's Vertex Array Object ID
	 */
	public int getVaoID() {
		return vaoID;
	}

	/**
	 * @return - the number of vertexes inside a RawModel
	 */
	public int getVertexCount() {
		return vertexCount;
	}
	
	/**
	 * @param distance - furthest distance from a point to the origin of a geometry model
	 */
	public void setFurthestPoint(float distance) {
		furthestDistance = distance;
	}
	
	public void setLenghts(float xLength, float yLength, float zLength) {
		this.xLength = xLength;
		this.yLength = yLength;
		this.zLength = zLength;
	}
	
	/**
	 * @return - furthest distance from a point to the origin of a geometry model
	 */
	public float getFurthestPoint() {
		return furthestDistance;
	}
	
	public float getxLength() {
		return xLength;
	}

	public float getyLength() {
		return yLength;
	}

	public float getzLength() {
		return zLength;
	}
}
