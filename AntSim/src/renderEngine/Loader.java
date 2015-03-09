package renderEngine;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**Loader loads 3D Models into memory by storing positional data about the model in a VAO.
 * 
 * @author Flo
 *
 */
public class Loader {
	
	private List<Integer> vaos = new ArrayList<Integer>(); //list holding all vaos
	private List<Integer> vbos = new ArrayList<Integer>(); //list holding all vbos
	
	/**Takes in position coordinates and the indices of positions of a model's vertexes, loads this data into a VAO and then returns information about the VAO as a RawModel object.
	 * 
	 * @param positions - an array of x,y,z positions stored as floats
	 * @param indices - an array integer indices indicating the positions of vertexes
	 * @return - a RawModel object storing positional data inside a VAO
	 */
	public RawModel loadToVAO(float[] positions, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices); //bind indices to the VAO
		storeDataInAttributeList(0, positions); //store positional data into attribute list 0 of the VAO
		unbindVAO(); //now that we finished using the VAO, we need to unbind it
		return new RawModel(vaoID, indices.length); //number of indices equals vertex count
	}
	
	/**Deletes all VAOS and VBOS stored in the vaos and vbos lists.
	 * 
	 */
	public void cleanUp(){
		for(int vao:vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo:vbos) {
			GL15.glDeleteBuffers(vbo);
		}
	}
	
	/**Creates an empty VAO.
	 * 
	 * @return - the ID of the newly created VAO
	 */
	private int createVAO(){
		int vaoID = GL30.glGenVertexArrays(); //creates empty VAO returning its ID
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID); //VAO needs to be activated by binding it before it can be used
		return vaoID;
	}
	
	/**Stores data into one of the attribute lists of a VAO.
	 * 
	 * @param attributeNumber - number of the attribute list in which the data shall be stored
	 * @param data - the data which shall be stored inside an attribute list of a VAO
	 */
	private void storeDataInAttributeList(int attributeNumber, float[] data) {
		int vboID = GL15.glGenBuffers(); //creates an empty VBO, returning the empty VBO's ID
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID); //VBO needs to be bound before it can be used; type of VBA is GL_ARRAY_BUFFER
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);//usage of static means we will not be editing the data later on
		GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0, 0);//puts VBO into the VAO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); //unbind VBO when we're done using it by binding with 0 instead of an actual VBO ID
	}
	
	/**Unbinds the VAO when we're done using the VAO.
	 * 
	 */
	private void unbindVAO(){
		GL30.glBindVertexArray(0); //unbind currently bound VAO by passing ID of 0
	}
	
	/**Loads an array of integer indices into an index buffer and bind the index buffer to a VAO.
	 * 
	 * @param indices - array of integer indices for indicating the positions used by vertexes
	 */
	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers(); //create empty VBO
		vbos.add(vboID); //add VBO to list of vbos so it gets deleted when the game is closed
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);//bind VBO as GL_ELEMENT_ARRAY_BUFFER -> tells OpenGL to use an Index Buffer
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);//usage of static means we will not be editing the data later on
	};
	
	/**Converts array of integer indices into an IntBuffer.
	 * 
	 * @param data - integer array of indices that shall be stored to the IntBuffer
	 * @return - an IntBuffer containing the indices used to indicate the positions of vertexes
	 */
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length); //create an empty IntBuffer with the size of data
		buffer.put(data); //put data into the buffer
		buffer.flip(); //change from write to read access mode
		return buffer;
	}
	
	/**Converts a float array into a float buffer, so that it can be stored as a VBO.
	 * 
	 * @param data - float array that should be converted into a float buffer
	 * @return - a FloatBuffer filled with an array of floats
	 */
	private FloatBuffer storeDataInFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length); //create empty float buffer with the size of data
		buffer.put(data); //put data into buffer
		buffer.flip(); //change from write to read access mode
		return buffer;
	}
}
