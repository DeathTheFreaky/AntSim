package skybox;
 
import org.lwjgl.util.vector.Matrix4f;
 
import entities.Camera;
 
import shaders.ShaderProgram;
import toolbox.Maths;
 
/**SkyboxShader is used as a shader program when rendering the skybox.
 * 
 * @author Flo
 *
 */
public class SkyboxShader extends ShaderProgram{
 
    private static final String VERTEX_FILE = "src/skybox/skyboxVertexShader.vsh";
    private static final String FRAGMENT_FILE = "src/skybox/skyboxFragmentShader.fsh";
     
    private int location_projectionMatrix;
    private int location_viewMatrix;
     
    /**Creates a new {@link SkyboxShader}.
     * 
     */
    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
    
    /**Loads 4x4 projection Matrix into shader uniform variable for setting the projection.
	 * 
	 * @param projection - the projection matrix to load into the uniform variable
	 */
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	/**Loads 4x4 view Matrix into shader uniform variable for moving the skybox when the camera is moved.
	 * 
	 * @param camera - the Camera to use for creating the view matrix
	 */
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		//only rotate skybox 
		viewMatrix.m30 = 0;
		viewMatrix.m31 = 0;
		viewMatrix.m32 = 0;
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
     
    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
 
}