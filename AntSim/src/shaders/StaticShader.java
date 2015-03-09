package shaders;

import org.lwjgl.util.vector.Matrix4f;

/**StaticShader will be used to create all our static models.<br>
 * Static Shader implements ShaderProgram.
 * 
 * @author Flo
 * @see ShaderProgram
 *
 */
public class StaticShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "src/shaders/vertexShader.vsh";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.fsh";

	private int location_transformationMatrix; //location of uniform variable in shader code
	
	/**Creates a new shader program using the shader source files configured in the StaticShader class.
	 * 
	 */
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position"); //bind x,y,z positions from attribute list 0 to the shader's position input parameter
		super.bindAttribute(1, "textureCoords"); //bind texture coordinates from attribute list 1 to the shader's texture coordinates input parameter
	}

	@Override
	protected void getAllUniformLocations() {
		super.getUniformLocation("transformationMatrix");
	}
	
	/**Loads 4x4 Matrix into shader uniform variable for making model transformations.
	 * 
	 * @param matrix - the matrix we want to load into the uniform variable
	 */
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
}
