package at.antSim.graphics.models;

import at.antSim.graphics.graphicsUtils.OBJLoaderGeometryData;
 
/**ModelData is a storage class holding a model's:<br>
 * <ul>
 * 	<li>float[] vertices positional coordinates</li>
 *  <li>float[] textureCoords</li>
 *  <li>float[] normals</li>
 *  <li>int[] indices</li>
 *  <li>float furthestPoint</li>
 * </ul>
 * The positional coordinates, texture coordinates and normals are stored in the order defined by indices.
 * 
 * @author Flo
 *
 */
public class ModelData {
 
    private float[] vertices;
    private float[] textureCoords;
    private float[] normals;
    private int[] indices;
    private float furthestPoint; //distance of the point furthest away form this model's origin -> useful for creating spheres around the model for collision detection 
    private float xLength;
    private float yLength;
    private float zLength;
 
    /**Constructs a new {@link ModelData} object.
     * 
     * @param vertices
     * @param textureCoords
     * @param normals
     * @param indices
     * @param furthestPoint
     */
    public ModelData(float[] vertices, float[] textureCoords, float[] normals, int[] indices,
            OBJLoaderGeometryData geometryData) {
        this.vertices = vertices; //a vertex's positional coordinates
        this.textureCoords = textureCoords;
        this.normals = normals;
        this.indices = indices;
        this.furthestPoint = geometryData.getFurthestPoint();
        this.xLength = geometryData.getxLength();
        this.yLength = geometryData.getyLength();
        this.zLength = geometryData.getzLength();
    }
 
    public float[] getVertices() {
        return vertices;
    }
 
    public float[] getTextureCoords() {
        return textureCoords;
    }
 
    public float[] getNormals() {
        return normals;
    }
 
    public int[] getIndices() {
        return indices;
    }
 
    public float getFurthestPoint() {
        return furthestPoint;
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