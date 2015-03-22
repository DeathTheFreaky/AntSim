package skybox;
 
import org.lwjgl.util.vector.Matrix4f;
 
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
 
import renderEngine.DisplayManager;
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
     
    private static final float ROTATE_SPEED = 1f; //rotation speed of skybox to simulate cloud movement
    
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_fogColor1;
    private int location_fogColor2;
    private int location_cubeMap;
    private int location_cubeMap2;
    private int location_blendFactor;
    
    private float rotation;
     
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
		rotation += ROTATE_SPEED * DisplayManager.getFrameTimeSeconds();
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0),  viewMatrix,  viewMatrix);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	/**Loads a blend factor into shader uniform variable.
	 * 
	 * @param blend - the blend factor to load into the shader uniform variable
	 */
	public void loadBlendFactor(float blend) {
		super.loadFloat(location_blendFactor, blend);
	}
	
	/**Loads up integers into 2Dsamplers to tell them in which texture units to look in.
	 * 
	 */
	public void connectTextureUnits() {
		super.loadInt(location_cubeMap, 0);
		super.loadInt(location_cubeMap2, 1);
	}
	
	/**Loads fog colors for day and night into shader uniform variable.
	 * 
	 * @param dayFog - a Vector3f of r,g,b fog color for daytime
	 * @param nightFog - a Vector3f of r,g,b fog color for nighttime
	 */
	public void loadFogColors(Vector3f dayFog, Vector3f nightFog) {
		super.loadVector(location_fogColor1, dayFog);
		super.loadVector(location_fogColor2, nightFog);
	}
     
    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_fogColor1 = super.getUniformLocation("fogColor1");
        location_fogColor2 = super.getUniformLocation("fogColor2");
        location_cubeMap = super.getUniformLocation("cubeMap");
        location_cubeMap2 = super.getUniformLocation("cubeMap2");
        location_blendFactor = super.getUniformLocation("blendFactor");
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
 
}