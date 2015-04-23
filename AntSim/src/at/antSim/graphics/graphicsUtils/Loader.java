package at.antSim.graphics.graphicsUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import at.antSim.graphics.models.RawModel;
import at.antSim.graphics.textures.TextureData;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

/**Loader loads 3D Models into memory by storing positional data about the model in a VAO.
 * 
 * @author Flo
 *
 */
public class Loader {
	
	private List<Integer> vaos = new ArrayList<Integer>(); //list holding all vaos ids to be cleaned at program exit
	private List<Integer> vbos = new ArrayList<Integer>(); //list holding all vbos ids to be cleaned at program exit
	private List<Integer> textures = new ArrayList<Integer>(); //list holding all texture ids to be cleaned at program exit
	
	/**Takes in position coordinates, texture coordinates and the indices of positions of a model's vertexes, loads this data into a VAO and then returns information about the VAO as a RawModel object.
	 * 
	 * @param positions - an array of x,y,z positions stored as floats
	 * @param textureCoords - an array of texture coordinates stored as floats
	 * @param normals - an array of normals stored as floats
	 * @param indices - an array integer indices indicating the positions of vertexes
	 * @return - a RawModel object storing positions', texture coordinate's and normals' data inside VAOs
	 */
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices); //bind indices to the VAO
		storeDataInAttributeList(0, 3, positions); //store positional data into attribute list 0 of the VAO
		storeDataInAttributeList(1, 2, textureCoords); //store texture coordinates into attribute list 1 of the VAO
		storeDataInAttributeList(2, 3, normals); //store normals into attribute list 2 of the VAO
		unbindVAO(); //now that we finished using the VAO, we need to unbind it
		return new RawModel(vaoID, indices.length); //number of indices equals vertex count
	}
	
	/**Takes in position coordinates of a model's vertexes, loads this data into a VAO and then returns information about the VAO as a RawModel object.<br>
	 * To be used with <b>GUI Elements</b>.
	 * 
	 * @param positions - the vertice's 2D positions
	 * @param dimensions - can be either 2 or 3 (2D quads, 3D Skybox cubes)
	 * @return - a RawModel object storing positions' data inside a VAO
	 */
	public RawModel loadToVAO(float [] positions, int dimensions) {
		int vaoID = createVAO();
		this.storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length/dimensions); //each vertex has 2 coordinates in 2D -> positions.length/2
	}
	
	/**Loads up a texture into memory to be used by OpenGL.
	 * 
	 * @param filename - the filename of the texture 
	 * @return - the ID of the newly loaded texture
	 */
	public int loadTexture(String fileName) {
		
		//load a texture in .png format from /res directory and store it in raw format
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/models/"+fileName+".png"));
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D); //generates lower res versions of the texture
			 /* tell openGl to use these lower res textures:
			  * param1: texture type,
			  * param2: defining openGl's behaviour for when the texture is rendered onto a surface with smaller dimensions than the texture
			  * param3: when the above happens, use the mipmaps that we generated -> linear: transition smoothly between different resolution versions
			  */
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f); //set texture's level of detail bias: texture will be rendered in more detail for higher negative numbers
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int textureID = texture.getTextureID();
		
		return textureID;
	}
	
	/**Loads up a CubeMap into OpenGL.<br>
	 * The textures used to create the CubeMap must be in the following order:
	 * <ol>
	 * 	<li>Right Face</li>
	 * 	<li>Left Face</li>
	 * 	<li>Top Face</li>
	 * 	<li>Bottom Face</li>
	 * 	<li>Back Face</li>
	 * 	<li>Front Face</li>
	 * </ol>
	 * 
	 * @param textureFiles - names of the textureFiles to be loaded into the CubeMap
	 * @return - ID of the CubeMap texture
	 */
	public int loadCubeMap(String[] textureFiles) {
		int texID = GL11.glGenTextures(); //generates completely empty texture
		GL13.glActiveTexture(texID);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID); //tell OpenGL that this texture is a cubemap
		
		for (int i = 0; i < textureFiles.length; i++) {
			TextureData data = decodeTextureFile("res/models/" + textureFiles[i] + ".png"); 
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), //first param is actually an integer, the other faces are consecutive integers
						data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		}
		
		//make textures appear smooth
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		
		textures.add(texID); //make sure textures get deleted when closing the program
		return texID;
	}
	
	/**Decodes a texture file and stores the raw byte data in a byte buffer within {@link TextureData}.
	 * copied from tutorial
	 * 
	 * @param fileName - name of the texture file
	 * @return - a new {@link TextureData}
	 */
	private TextureData decodeTextureFile(String fileName) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			FileInputStream in = new FileInputStream(fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decodeFlipped(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Loading texture " + fileName + " failed");
			System.exit(-1);
		}
		return new TextureData(buffer, width, height);
	}
	
	/**Deletes all VAOS, VBOS and textures stored in the vaos, vbos and textures lists.
	 * 
	 */
	public void cleanUp(){
		for(int vao:vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo:vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		for (int texture:textures) {
			GL15.glDeleteBuffers(texture);
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
	 * @param coordinateSize - eg. 3 for x,y,z or 2 for texture coordinates
	 * @param data - the data which shall be stored inside an attribute list of a VAO
	 */
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers(); //creates an empty VBO, returning the empty VBO's ID
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID); //VBO needs to be bound before it can be used; type of VBA is GL_ARRAY_BUFFER
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);//usage of static means we will not be editing the data later on
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);//puts VBO into the VAO
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
