package models;

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
	
	private int vaoID;
	private int vertexCount; //how many vertexes are inside the model
	
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
}
