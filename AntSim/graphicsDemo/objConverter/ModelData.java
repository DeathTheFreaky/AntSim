package objConverter;
 
/**ModelData is a storage class holding a model's:<br>
 * <ul>
 * 	<li>float[] vertices</li>
 *  <li>float[] textureCoords</li>
 *  <li>float[] normals</li>
 *  <li>int[] indices</li>
 *  <li>float furthestPoint</li>
 * </ul>
 * 
 * @author Flo
 *
 */
public class ModelData {
 
    private float[] vertices;
    private float[] textureCoords;
    private float[] normals;
    private int[] indices;
    private float furthestPoint;
 
    /**Constructs a new {@link ModelData} object.
     * 
     * @param vertices
     * @param textureCoords
     * @param normals
     * @param indices
     * @param furthestPoint
     */
    public ModelData(float[] vertices, float[] textureCoords, float[] normals, int[] indices,
            float furthestPoint) {
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.normals = normals;
        this.indices = indices;
        this.furthestPoint = furthestPoint;
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
 
}