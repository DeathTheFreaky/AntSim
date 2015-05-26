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
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import at.antSim.Globals;
import at.antSim.graphics.models.RawModel;
import at.antSim.graphics.textures.TextureData;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

/**OpenGLLoader loads 3D Models and textures into OpenGL and memory (buffer) by storing data (positions, textureCoords, normals) about the model in a VAO.
 * 
 * @author Flo
 *
 */
public class OpenGLLoader {
	
	/* For more information about GL function calls used in this class to load java objects into OpenGL,
	 * refer to OPENGL Programming Guide:
	 * 
	 * - "Initializing Our Vertex-Array Objects": p.16
	 * - "Allocating Vertex-Buffer Objects": p.19
	 * - "Table 3.2 Buffer Binding Targets": p.93
	 * - "Table 3.3 Buffer Usage Tokens": p.96
	 * - "Linear Filtering": p.330
	 * - "Using and Generating Mipmaps": p.333 
	 * - "Calculating the Mipmap Level": p.338
	 * - "Creating and Initializing Textures": p.263
	 * - "Texture Targets and Corresponding Sampler Types": p.263
	 * - "Texture Formats": p.282
	 * */
	
	private List<Integer> vaos = new ArrayList<Integer>(); //list holding all vaos ids to be cleaned at program exit
	private List<Integer> vbos = new ArrayList<Integer>(); //list holding all vbos ids to be cleaned at program exit
	private List<Integer> textures = new ArrayList<Integer>(); //list holding all texture ids to be cleaned at program exit
	private List<Integer> tempVaos = new ArrayList<Integer>(); //list holding all vaos ids for temp data to be cleaned after every render call
	private List<Integer> tempVbos = new ArrayList<Integer>(); //list holding all vbos ids for temp data to be cleaned after every render call
	
	/**Takes in position coordinates, texture coordinates and the indices of positions of a model's vertexes, loads this data into a VAO and then returns information about the VAO as a RawModel object.
	 * 
	 * @param positions - an array of x,y,z positions stored as floats
	 * @param textureCoords - an array of texture coordinates stored as floats
	 * @param normals - an array of normals stored as floats
	 * @param indices - an array of integer indices indicating the positions of vertexes
	 * @return - a RawModel object storing positions', texture coordinate's and normals' data inside a VAO
	 */
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO(false); //creates and activates a VAO: the following VBOs will all be stored to this activated VAO
		bindIndicesBuffer(indices); //bind indices to the activated VAO
		storeDataInAttributeList(0, 3, positions, false); //store positional data into attribute list 0 of the activated VAO
		storeDataInAttributeList(1, 2, textureCoords, false); //store texture coordinates into attribute list 1 of the activated VAO
		storeDataInAttributeList(2, 3, normals, false); //store normals into attribute list 2 of the activated VAO
		unbindVAO(); //now that we finished using the our VAO bound in createVAO(), we need to unbind it - deactivate it
		return new RawModel(vaoID, indices.length); //number of indices equals vertex count
	}
	
	/**Takes in position coordinates of a model's vertexes, loads this data into a VAO and then returns information about the VAO as a RawModel object.<br>
	 * No texture Coordinates, normals or indices are passed, making this function ideal for simple 3d models (such as a skybox cube with no duplicate vertices)
	 * or 2D GUI Elements.
	 *  
	 * @param positions - the vertice's 2D positions
	 * @param dimensions - can be either 2 or 3 (2D quads, 3D Skybox cubes)
	 * @return - a RawModel object storing positions' data inside a VAO
	 */
	public RawModel loadToVAO(float [] positions, int dimensions) {
		int vaoID = createVAO(false);
		this.storeDataInAttributeList(0, dimensions, positions, false); //store positional data into attribute list 0 of the activated VAO
		unbindVAO();
		return new RawModel(vaoID, positions.length/dimensions); //for 2d models there are 2 number per vertex, for 3d models there are 3
	}
	
	/**Takes in position coordinates of a model's vertexes, loads this data into a VAO and then returns information about the VAO as a RawModel object.<br>
	 * No texture Coordinates, normals or indices are passed, making this function ideal for simple 3d models (such as a skybox cube with no duplicate vertices)
	 * or 2D GUI Elements.
	 *  
	 * @param positions - the vertice's 2D positions
	 * @param textureCoorder - texture coordinates of the vertices
	 * @param dimensions - can be either 2 or 3 (2D quads, 3D Skybox cubes)
	 * @param temporary - true if data shall only be stored for one render loop (eg. stats text which changes at every render loop)
	 * @return - a RawModel object storing positions' data inside a VAO
	 */
	public RawModel loadToVAO(float [] positions, float[] textureCoords, int dimensions, boolean temporary) {
		int vaoID = createVAO(temporary);
		storeDataInAttributeList(0, dimensions, positions, temporary); //store positional data into attribute list 0 of the activated VAO
		storeDataInAttributeList(1, 2, textureCoords, temporary); //store texture coordinates into attribute list 1 of the activated VAO
		unbindVAO();
		return new RawModel(vaoID, positions.length/dimensions); //for 2d models there are 2 number per vertex, for 3d models there are 3
	}
	
	/**Loads up a texture into memory to be used by OpenGL.<br>
	 * 
	 * @param filename - the filename of the texture 
	 * @return - the ID of the newly loaded texture
	 */
	public int loadTexture(String fileName) {
		
		//load a texture in .png format from /res/models directory and store it in raw format
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream(Globals.TEXTURES + fileName + ".png")); //texture is being bound for GL30.glGenerateMipMap(int target)
			/* 
			 * To avoid artifacts produced by the undersampling of textures, with the texture changed abruptly at certain transition points,
			 * we create lower res versions of the texture, called mipmaps. OpenGL automatically chooses the right texture version,
			 * according to the distance from and hence the effective size of a texture.
			 * The GL30.glGenerateMipmap() method automatically creates all needed smaller resolution version of the original texture.
			 * Since we are using 2D textures only, we can set the target parameter to GL11.GL_TEXTURE_2D.
			 * 
			 * For more information, see OpenGL Programming Guide on page 333.
			 */
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			 /* 
			  * tell openGl to use these lower res textures:
			  * param1: texture type,
			  * param2: defining openGl's behaviour for when the texture is rendered onto a surface with smaller dimensions than the texture
			  * param3: when the above happens, use the mipmaps that we generated -> linear: transition smoothly between different resolution versions
			  */
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
			/*
			 * Set texture's level of detail bias: texture will be rendered in more detail (higher mipmap levels) for higher negative numbers.
			 * 
			 * See OpenGL Programming Guide on page 338.
			 */
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int textureID = texture.getTextureID();
		return textureID;
	}
	
	/**Loads up a texture into memory to be used by OpenGL, providing the texture's height and width for gui positioning.<br>
	 * 
	 * @param filename - the filename of the texture 
	 * @return - a {@link Texture}
	 */
	public Texture loadGuiTexture(String fileName) {
			
		//load a texture in .png format from /res/models directory and store it in raw format
		Texture texture = null;

		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream(Globals.TEXTURES + fileName + ".png")); //texture is being bound for GL30.glGenerateMipMap(int target)
			/* 
			 * To avoid artifacts produced by the undersampling of textures, with the texture changed abruptly at certain transition points,
			 * we create lower res versions of the texture, called mipmaps. OpenGL automatically chooses the right texture version,
			 * according to the distance from and hence the effective size of a texture.
			 * The GL30.glGenerateMipmap() method automatically creates all needed smaller resolution version of the original texture.
			 * Since we are using 2D textures only, we can set the target parameter to GL11.GL_TEXTURE_2D.
			 * 
			 * For more information, see OpenGL Programming Guide on page 333.
			 */
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			 /* 
			  * tell openGl to use these lower res textures:
			  * param1: texture type,
			  * param2: defining openGl's behaviour for when the texture is rendered onto a surface with smaller dimensions than the texture
			  * param3: when the above happens, use the mipmaps that we generated -> linear: transition smoothly between different resolution versions
			  */
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
			/*
			 * Set texture's level of detail bias: texture will be rendered in more detail (higher mipmap levels) for higher negative numbers.
			 * 
			 * See OpenGL Programming Guide on page 338.
			 */
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return texture;
	}
	
	/**Loads up a CubeMap's textures into OpenGL.<br>
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
		
		//for more information on cubemaps, see OpenGL Programming Guide p.309
		
		int texID = GL11.glGenTextures(); //generates completely empty texture - see OpenGL Programming Guide p.263
		GL13.glActiveTexture(texID); //change the selector referring the active texture unit - see OpenGL Programming Guide p.265
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID); //bind active texture as a cubemap - see OpenGL Programming Guide p.264
		
		for (int i = 0; i < textureFiles.length; i++) {
			TextureData data = decodeTextureFile(Globals.TEXTURES + textureFiles[i] + ".png"); //store the texture in OpenGL's internal Format - see OpenGl Programming Guide p.270
			/*
			 * Allocate and load texture into non-immutable texture storage to allow for texture objects of cube map to be redefined.
			 * 
			 * params:
			 * - target: The textures in the cubemap are to be loaded in the following order: "right", "left", "top", "bottom", "back", "front".
			 *   At an offset of i = 0, the texture "right" will be loaded, at an offset of i = 1, the texture "left" will be loaded...
			 * - level: mipmap level this texture shall be used as
			 * - internalformat: OpenGl's internal format to store our texture
			 * - width: the texture's width
			 * - height: the texture's height
			 * - border: define a texture border
			 * - format: format of the initial texel data (byte buffer data format) 
			 * - type: type of the initial texel data (byte buffer data type)
			 * - pixels: the actual pixels as byteBuffer data
			 * 
			 */
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), //first param is actually an integer, the other faces are consecutive integers
						data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		}
		
		/* Define how OpenGl filters textures.
		 * 
		 * "Texture maps may be linear, square, or rectangular, or even 3D, but after
		 *	being mapped to a polygon or surface and transformed into screen
		 *	coordinates, the individual texels of a texture rarely correspond directly to
		 *	individual pixels of the final screen image. Depending on the transformations
		 *	used and the texture mapping applied, a single pixel on the
		 *	screen can correspond to anything from a tiny portion of a single texel
		 *	(magnification) to a large collection of texels (minification).
		 *
		 * In either case, it’s unclear exactly which texel values should be
		 * used and how they should be averaged or interpolated. Consequently,
		 * OpenGL allows you to specify any of several filtering options to determine
		 * these calculations."
		 * 
		 * GL11.GL_TEXTURE_MAG_FILTER is used when the level-of-detail required is of a higher resolution than the highest resolution mipmap level (level 0 by default).
		 * GL11.GL_TEXTURE_MIN_FILTER is used when mipmapping takes effect - when one of the different lower resolution mipmap levels can be applied.
		 * GL11.GL_LINEAR tells OpenGl to apply a linear filtering method on textures.
		 * 
		 * see OpenGL Programming Guide p.329 and following on Filtering
		 */
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		
		//set clamp to edge to avoid visible seam on the edges of your cubemap textures
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		
		textures.add(texID); //make sure textures get deleted when closing the program
		return texID;
	}
	
	/**Decodes a texture file and stores the raw byte data in a byte buffer within {@link TextureData}.<br>
	 * To decode the png image, the following PNG Decoder library is used: http://hg.l33tlabs.org/twl/file/tip/src/de/matthiasmann/twl/utils/PNGDecoder.java
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
			PNGDecoder decoder = new PNGDecoder(in); //use png decoder library to gather width and height information of png file
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height); //a pixel in a png image consists of: 8bits RGB (3*8 = 24), 8bits alpha channel
			/*
			 * Decode the image into the specified buffer. The last line is placed at the current 
			 * position. After decode the buffer position is at the end of the first line.
			 * 
			 * params:
			 * - buffer: the buffer where the image data is to be stored at
			 * - stride: the stride in bytes from the start of a line to start of the next line
			 * - fmt: the target format into which the image should be decoded
			 *
			 */
			decoder.decode(buffer,  width * 4, Format.RGBA);
			//decoder.decodeFlipped(buffer, width * 4, Format.RGBA);
			buffer.flip(); //change from buffer write to buffer read access
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Loading texture " + fileName + " failed");
			System.exit(-1);
		}
		return new TextureData(buffer, width, height);
	}
	
	/**Creates an empty VAO.
	 * 
	 * @param temporary - true if data shall only be stored for one render loop (eg. stats text which changes at every render loop)
	 * @return - the ID of the newly created VAO
	 */
	private int createVAO(boolean temporary){
		int vaoID = GL30.glGenVertexArrays(); //creates empty VAO returning its ID
		if (temporary) {
			tempVaos.add(vaoID);
		} else {
			vaos.add(vaoID); //add vaoID to list of vaoIDs for cleanup at program exit
		}
		GL30.glBindVertexArray(vaoID); //VAO needs to be activated by binding it before it can be used
		return vaoID;
	}
	
	/**Unbinds the VAO when we're done using the VAO.
	 * 
	 */
	private void unbindVAO(){
		GL30.glBindVertexArray(0); //unbind currently bound VAO by passing ID of 0
	}
	
	/**Loads an array of integer vertex indices into a VBO and loads the this VBO to a VAO.
	 * 
	 * @param indices - array of integer vertex indices for indicating the positions used by vertexes
	 */
	private void bindIndicesBuffer(int[] indices) {
		
		/* Other then for GL_ARRAY_BUFFER, which uses glVertexAttribPointer() 
		 * to store vertex data in one of the 16 arrays in a VAO,
		 * the GL_ELEMENT_ARRAY_BUFFER target is unique for a VAO*/
		
		int vboID = GL15.glGenBuffers(); //create empty VBO and return its id
		vbos.add(vboID); //add VBO to list of vbos so it gets deleted when the game is closed
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);//bind VBO as GL_ELEMENT_ARRAY_BUFFER -> tells OpenGL to use an Index Buffer
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);//data is vertex indices, usage of static means we will not be editing the data later on
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
	
	/**Stores data into a VBO and loads this VBO into one of the attribute lists of a VAO.
	 * 
	 * @param attributeNumber - number of the attribute list in which the data shall be stored
	 * @param coordinateSize - eg. 3 for x,y,z or 2 for texture coordinates
	 * @param data - the data which shall be stored inside an attribute list of a VAO
	 * @param temporary - true if data shall only be stored for one render loop (eg. stats text which changes at every render loop)
	 */
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data, boolean temporary) {
		int vboID = GL15.glGenBuffers(); //creates an empty VBO, returning the empty VBO's ID
		if (temporary) {
			tempVbos.add(vboID);
		} else {
			vbos.add(vboID);
		}
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID); //VBO needs to be bound before it can be used; type of VBA is GL_ARRAY_BUFFER -> vertex-attribute data
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);//usage of static means we will not be editing the data later on
		
		/* puts VBO into the VAO (see OpenGL Programming Guide p.26):
		 * - Data is to be stored at index (shader attribute location) of attributeNumber
		 * - coordinateSize is the number of values for each vertex in our array
		 * - GL11.GL_FLOAT means that our data matches the GLfloat type
		 * - false refers to our positional coordinates not being restrained to the range [-1, 1]
		 * - the first 0 (stride) means that one set of data values is immediately contiguous in memory to the next (no -1 separator)
		 * - the second 0 (pointer) tells OpenGL that our data starts at the first byte (offset)
		 */
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); //unbind VBO when we're done using it by binding with 0 instead of an actual VBO ID
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
	
	/**Deletes all VAOS, VBOS and textures stored in the vaos, vbos and textures lists when they are not needed any longer.
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
	
	/**Deletes all temporary VAOS, VBOS and textures stored in the vaos, vbos and textures lists when they are not needed any longer.
	 * 
	 */
	public void tempCleanUp() {
		for(int vao:tempVaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo:tempVbos) {
			GL15.glDeleteBuffers(vbo);
		}
	}
}
