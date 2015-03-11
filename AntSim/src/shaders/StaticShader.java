package shaders;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import toolbox.Maths;

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

	//location of uniform variables in shader code
	private int location_transformationMatrix; 
	private int location_projectionMatrix;
	private int location_viewMatrix;
	
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
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
	}
	
	/**Loads 4x4 transformation Matrix into shader uniform variable for making model transformations.
	 * 
	 * @param matrix - the matrix we want to load into the uniform variable
	 */
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	/**Loads 4x4 projection Matrix into shader uniform variable for setting the projection.
	 * 
	 * @param projection - the projection matrix to load into the uniform variable
	 */
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	/**Loads 4x4 view Matrix into shader uniform variable for moving all objects in the world to simulate camera movement.
	 * 
	 * @param camera - the Camera to use for creating the view matrix
	 */
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
}
