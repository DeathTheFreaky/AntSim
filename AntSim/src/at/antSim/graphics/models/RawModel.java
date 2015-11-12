package at.antSim.graphics.models;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import at.antSim.graphics.graphicsUtils.TransparentTriangle;

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
 * @author Clemens
 *
 */
public class RawModel {
	
	private int vaoID; //id or "name" of the vao used to store this model's vertex indices, positional coordinates, texture coordinates and normals
	private int indicesID;
	private int vertexCount; //how many vertexes are stored inside the model
	private float furthestDistance; //furthest distance from a point to the origin of a geometry model
	private float xLength;
    private float yLength;
    private float zLength;
    private List<TransparentTriangle> transparentTriangles;
    private ModelData modelData; // to be removed - only for debug purposes
	
	public RawModel(int vaoID, int indicesID, int vertexCount) {
		
		this.vaoID = vaoID;
		this.indicesID = indicesID;
		this.vertexCount = vertexCount;	
	}
	
	public RawModel(int vaoID, int vertexCount) {
		
		this.vaoID = vaoID;
		this.indicesID = -1;
		this.vertexCount = vertexCount;	
	}

	/**
	 * @return - a RawModel's Vertex Array Object ID
	 */
	public int getVaoID() {
		return vaoID;
	}
	
	/**
	 * @return - ID of the index buffer vbo
	 */
	public int getIndicesID()
	{
		return indicesID;
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
	
	public List<TransparentTriangle> getTransparentVertices() {
		return transparentTriangles;
	}

	public void loadTransparentVertices(ModelData modelData)
	{
		this.modelData = modelData;
		
		transparentTriangles = new ArrayList<TransparentTriangle>();
		
		for (int i = 0; i < modelData.getIndices().length; i++)
		{
			if (i % 3 == 0) // each triangle has 3 vertices
			{
				int indexBufferOffset = i;
				int p1IndexVal = modelData.getIndices()[i];
				int p2IndexVal = modelData.getIndices()[i + 1];
				int p3IndexVal = modelData.getIndices()[i + 2];
				
				Vector3f p1 = new Vector3f(modelData.getVertices()[p1IndexVal], modelData.getVertices()[p1IndexVal + 1], modelData.getVertices()[p1IndexVal + 2]);
				Vector3f p2 = new Vector3f(modelData.getVertices()[p2IndexVal], modelData.getVertices()[p2IndexVal + 1], modelData.getVertices()[p2IndexVal + 2]);
				Vector3f p3 = new Vector3f(modelData.getVertices()[p3IndexVal], modelData.getVertices()[p3IndexVal + 1], modelData.getVertices()[p3IndexVal + 2]);
				
				transparentTriangles.add(new TransparentTriangle(indexBufferOffset, p1, p2, p3));
			}
		}
		
		System.out.println("VaoID: " + vaoID + " - created " + transparentTriangles.size() + " transparent triangles.");
	}
	
	public void setTransparentVerticesWorldPosition()
	{
		for (TransparentTriangle triangle : transparentTriangles)
		{
			
		}
	}
	
	public ModelData getModelData()
	{
		return modelData;
	}
}
