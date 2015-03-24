package objConverter;
 
import org.lwjgl.util.vector.Vector3f;
 
/**This class stores data about a Vertex, including:
 * <ul>
 * 	<li>Vector3f of x,y,z positions</li>
 * 	<li>int textureIndex</li>
 * 	<li>int normalIndex</li>
 * 	<li>{@link Vertex} duplicateVertex</li>
 * 	<li>int index</li>
 * 	<li>float length</li>
 * </ul>
 * 
 * @author Flo
 *
 */
public class Vertex {
     
    private static final int NO_INDEX = -1;
     
    private Vector3f position; //x,y,z coordinates
    private int textureIndex = NO_INDEX;
    private int normalIndex = NO_INDEX;
    private Vertex duplicateVertex = null;
    private int index;
    private float length;
     
    public Vertex(int index,Vector3f position){
        this.index = index;
        this.position = position;
        this.length = position.length();
    }
     
    public int getIndex(){
        return index;
    }
     
    public float getLength(){
        return length;
    }
     
    /**
     * @return - false if the Vertex's textureIndex and normalIndex have not been set
     */
    public boolean isSet(){
        return textureIndex!=NO_INDEX && normalIndex!=NO_INDEX;
    }
     
    /**Checks if the textureIndex and normalIndex passed as parameters equal those if this Vertex.
     * 
     * @param textureIndexOther - a Vertex's textureIndex
     * @param normalIndexOther - a Vertex's normalIndex
     * @return - true if this Vertex's textureIndex and normalIndex equals the passed parameters
     */
    public boolean hasSameTextureAndNormal(int textureIndexOther,int normalIndexOther){
        return textureIndexOther==textureIndex && normalIndexOther==normalIndex;
    }
     
    public void setTextureIndex(int textureIndex){
        this.textureIndex = textureIndex;
    }
     
    public void setNormalIndex(int normalIndex){
        this.normalIndex = normalIndex;
    }
 
    public Vector3f getPosition() {
        return position;
    }
 
    public int getTextureIndex() {
        return textureIndex;
    }
 
    public int getNormalIndex() {
        return normalIndex;
    }
 
    public Vertex getDuplicateVertex() {
        return duplicateVertex;
    }
 
    public void setDuplicateVertex(Vertex duplicateVertex) {
        this.duplicateVertex = duplicateVertex;
    }
 
}
