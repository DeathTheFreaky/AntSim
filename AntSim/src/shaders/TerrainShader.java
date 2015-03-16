package shaders;

import org.lwjgl.util.vector.Matrix4f;

import terrains.Terrain;
import toolbox.Maths;
import entities.Camera;
import entities.Light;

/**TerrainShader is used for the rendering of {@link Terrain}s.
 * 
 * @author Flo
 *
 */
public class TerrainShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/shaders/terrainVertexShader.vsh";
	private static final String FRAGMENT_FILE = "src/shaders/terrainFragmentShader.fsh";

	//location of uniform variables in shader code
	private int location_transformationMatrix; 
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition;
	private int location_lightColor;
	private int location_shineDamper;
	private int location_reflectivity;
	
	/**Creates a new shader program using the shader source files configured in the StaticShader class.
	 * 
	 */
	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position"); //bind x,y,z positions from attribute list 0 to the shader's position input parameter
		super.bindAttribute(1, "textureCoords"); //bind texture coordinates from attribute list 1 to the shader's texture coordinates input parameter
		super.bindAttribute(2, "normal"); //bind normal from attribute list 2 to the shader's normal input parameter
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColor = super.getUniformLocation("lightColor");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
	}
	
	/**Loads a vertices shineDamper and reflectivity into the shader uniform variable for specular lighting.
	 * 
	 * @param damper - how strong specular lighting appears when camera is not directly facing the reflected light
	 * @param reflectivity - how strongly light is reflected by a surface
	 */
	public void loadShineVariables(float damper, float reflectivity) {
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	/**Loads 4x4 transformation Matrix into shader uniform variable for making model transformations.
	 * 
	 * @param matrix - the matrix we want to load into the uniform variable
	 */
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	/**Loads a lights x,y,z position and color (RGB) into the shader program.
	 * 
	 * @param light 
	 */
	public void loadLight(Light light) {
		super.loadVector(location_lightPosition, light.getPosition());
		super.loadVector(location_lightColor, light.getColor());
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
