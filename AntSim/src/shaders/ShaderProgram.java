package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**This class represents a generic shader program containing all attributes and methods that every shader program needs to have.
 * 
 * @author Flo
 *
 */
public abstract	class ShaderProgram {

	private int programID; 
	private int vertexShaderID;
	private int fragmentShaderID;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16); //buffer reused everytime we want to load up a matrix into shader code
	
	public ShaderProgram(String vertexFile, String fragmentFile) {
		
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER); //load vertex shader into OpenGL
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER); //load fragment shader into OpenGL
		
		//create new program that will tie vertex shader and fragment shader together
		programID = GL20.glCreateProgram(); 
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
	}
	
	/**Makes sure that all ShaderProgram classes have a method that gets all the uniform locations.
	 * 
	 */
	protected abstract void getAllUniformLocations();
	
	/**Finds the location of a uniform variable in the shader code.
	 * 
	 * @param uniformName - name of uniform variable
	 * @return - an int representing the location of the uniform variable in the shader code
	 */
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	/**Starts the shader program.
	 * 
	 */
	public void start() {
		
		GL20.glUseProgram(programID);
	}
	
	/**Stops the shader program.
	 * 
	 */
	public void stop() {
		
		GL20.glUseProgram(0);
	}
	
	/**Deletes both the vertex and the fragment shader. 
	 * 
	 */
	public void cleanUp() {
		
		stop(); //make sure program is not running
		
		//detach shaders from program, delete shaders and finally delete program
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	/**Links up the inputs to the shader programs to one of the VAOs of a RawModel.
	 * 
	 */
	protected abstract void bindAttributes();
	
	/**Binds an attribute from a VAO's attribute list to the input parameters of the shader program.
	 * 
	 * @param attribute - number of an attribute list in the VAO that we want to bind
	 * @param variableName - variableName in the shader code that we want to bind that attribute to
	 */
	protected void bindAttribute(int attribute, String variableName) {
		
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	/**Loads a float value into a uniform.
	 * 
	 * @param location - location of uniform variable in shader code
	 * @param value - value that we want to load into the uniform
	 */
	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}
	
	/**Loads a 3floated vector into a uniform.
	 * 
	 * @param location - location of uniform variable in shader code
	 * @param vector - vector that we want to load into the uniform
	 */
	protected void loadVector(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	/**Since OpenGL does not know boolean, we are going to load up either a 0f (false) or a 1f (true).
	 * 
	 * @param location - location of uniform variable in shader code
	 * @param value - 0f (false) or a 1f (true) to load into the uniform
	 */
	protected void loadBoolean(int location, boolean value) {
		float toLoad = 0;
		if (value) {
			toLoad = 1;
		}
		GL20.glUniform1f(location, toLoad);
	}
	
	/**Loads a 4x4 matrix into a uniform.
	 * 
	 * @param location - location of uniform variable in shader code
	 * @param matrix - 4x4 matrix that we want to load into the uniform
	 */
	protected void loadMatrix(int location, Matrix4f matrix) {
		matrix.store(matrixBuffer); //store matrix into matrix buffer
		matrixBuffer.flip(); //change from write to read access
		GL20.glUniformMatrix4(location, false, matrixBuffer); //loads matrix from buffer into uniform location 
	}
	
	/**Opens up source code files, reads in all the lines and connects them all together into one long string.<br>
	 * This long string will then be attached to the new vertex or fragment shader, depending on the passed type.<br>
	 * Finally, this method will compile the shader and print any errors that were found in the shader code.<br> 
	 * 
	 * @param file - a shader file
	 * @param type - indicates whether the shader is a vertex or a fragment shader
	 * @return - the shaderID
	 */
	private static int loadShader(String file, int type) {
		
		//read contents of Shader into one long string
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Could not read file!");
			e.printStackTrace();
			System.exit(-1);
		}
		
		//create new shader and attach source code from the shader file
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		
		//compile the shader and print any errors in the shader code
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader.");
			System.exit(-1);
		}
		
		return shaderID;
	}
}
