package at.antSim.graphics.shaders;

import at.antSim.Globals;
import at.antSim.graphics.entities.Camera;
import at.antSim.graphics.entities.Light;
import at.antSim.graphics.graphicsUtils.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

/**StaticShader is used for the rendering of all static models.<br>
 * 
 * @author Flo
 * @see ShaderProgram
 *
 */
public class EntityShader extends ShaderProgram {
	
	private static final int MAX_LIGHTS = 4; //maximum number of different light sources to use
	
	private static final String VERTEX_FILE = Globals.SHADERS + "entityVertexShader.vsh";
	private static final String FRAGMENT_FILE = Globals.SHADERS + "entityFragmentShader.fsh";

	//location of uniform variables in shader code
	private int location_transformationMatrix; 
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition[];
	private int location_lightColor[];
	private int location_attenuation[];
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_useFakeLighting;
	private int location_numberOfRows;
	private int location_offset;
	private int location_fogColor1;
    private int location_fogColor2;
    private int location_blendFactor;
    private int location_movingEntityColor;
    private int location_movingEntityBlendFactor;
	
	/**Creates a new shader program using the shader source files configured in the StaticShader class.
	 * 
	 */
	public EntityShader() {
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
		
		/* Uniform variables do not change for each shader call but stay the same for all vertices of a model.
		 * Basically, all data we pass to the shader are uniform variables, except for the positions, texture coords and normals of the vertices.
		 */
		
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_useFakeLighting = super.getUniformLocation("useFakeLighting");
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");
		
		location_fogColor1 = super.getUniformLocation("fogColor1");
        location_fogColor2 = super.getUniformLocation("fogColor2");
        location_blendFactor = super.getUniformLocation("blendFactor");
		
        location_movingEntityColor = super.getUniformLocation("movingEntityColor");
        location_movingEntityBlendFactor = super.getUniformLocation("movingEntityBlendFactor");
        
		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColor = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];
		for (int i=0; i<MAX_LIGHTS; i++) {
			location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			location_lightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
			location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
	}
	
	/**Loads number of rows of a texture atlas into shader uniform variable numberOfRows.
	 * 
	 * @param numberOfRows - the number of rows in a texture atlas
	 */
	public void loadNumberOfRows(int numberOfRows) {
		super.loadFloat(location_numberOfRows, numberOfRows);
	}
	
	/**Loads x,y offsets for texture's position in texture atlas into shader uniform variable offset.
	 * 
	 * @param x - the x offset in columns
	 * @param y - the y offset in rows
	 */
	public void loadOffset(float x, float y) {
		super.load2DVector(location_offset, new Vector2f(x, y));
	}
	
	/**Loads a boolean (actually will be converted to float) into the shader uniform variable for useFakeLighting.
	 * 
	 * @param useFakeLighting - true enables the use of fake lighting for a transparent texture like grass to avoid weird look
	 */
	public void loadFakeLightingVariable(boolean useFakeLighting) {
		super.loadBoolean(location_useFakeLighting, useFakeLighting);
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
	
	/**Loads lights' x,y,z position,color (RGB) and attenuation into the shader program.
	 * 
	 * @param lights - a List of lights
	 */
	public void loadLights(List<Light> lights) {
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColor[i], lights.get(i).getColor());
				super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
			} else {
				//load up empty lights into shader cause there needs to be some information (lights arrays in shaders are fixed size)
				super.loadVector(location_lightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector(location_lightColor[i], new Vector3f(0, 0, 0));
				super.loadVector(location_attenuation[i], new Vector3f(1, 0, 0)); //light will not get weaker for increased distance
			}
		}
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
	
	/**Loads fog colors for day and night into shader uniform variable.
	 * 
	 * @param dayFog - a Vector3f of r,g,b fog color for daytime
	 * @param nightFog - a Vector3f of r,g,b fog color for nighttime
	 */
	public void loadFogColors(Vector3f dayFog, Vector3f nightFog) {
		super.loadVector(location_fogColor1, dayFog);
		super.loadVector(location_fogColor2, nightFog);
	}
	
	/**Loads a blend factor for fog into shader uniform variable.
	 * 
	 * @param blend - the blend factor to load into the shader uniform variable
	 */
	public void loadBlendFactor(float blend) {
		super.loadFloat(location_blendFactor, blend);
	}
	
	/**Loads moving entity blend color into shader uniform variable.
	 * 
	 * @param blendColor - a Vector3f of r,g,b
	 */
	public void loadMovingEntityColor(Vector3f blendColor) {
		super.loadVector(location_movingEntityColor, blendColor);
	}
	
	/**Loads a blend factor for moving entity into shader uniform variable.
	 * 
	 * @param blend - the blend factor to load into the shader uniform variable
	 */
	public void loadMovingEntityBlend(float blend) {
		super.loadFloat(location_movingEntityBlendFactor, blend);
	}
}
