package at.antSim.graphics.shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import at.antSim.Globals;

/**GuiShader is used to start up a Gui Shader Program and load uniform variables into this shader program.
 * 
 * @author Alex
 *
 */
public class GuiShader extends ShaderProgram {

	private static final String VERTEX_FILE = Globals.SHADERS + "guiVertexShader.vsh";
    private static final String FRAGMENT_FILE = Globals.SHADERS + "guiFragmentShader.fsh";
    
  //location of uniform variables in shader code
  	private int location_transformationMatrix; 
  	private int location_transparency;
  	private int location_blendColor;
  	private int location_blendFactor;
  	private int location_isFont;
 
    /**Creates a new {@link GuiShader}.
     * 
     */
    public GuiShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
     
    /**Loads 4x4 transformation Matrix into shader uniform variable for making model transformations.
	 * 
	 * @param matrix - the matrix we want to load into the uniform variable
     */
    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }
    
    /**Loads a blend factor into shader uniform variable.
	 * 
	 * @param blend - the blend factor to load into the shader uniform variable
	 */
	public void loadBlendFactor(float blend) {
		super.loadFloat(location_blendFactor, blend);
	}
	
	/**Loads a transparency into shader uniform variable.
	 * 
	 * @param transparency - the transparency to load into the shader uniform variable
	 */
	public void loadTransparency(float transparency) {
		super.loadFloat(location_transparency, transparency);
	}
	
	/**Loads blendColor into shader uniform variable.
	 * 
	 * @param blendColor - color to be blended with original texture color
	 */
	public void loadBlendColor(Vector3f blendColor) {
		super.loadVector(location_blendColor, blendColor);
	}
	
	/**Loads isFont switch into shader uniform variable.
	 * 
	 * @param isFont - true if rendered texture is a font texture
	 */
	public void loadIsFont(boolean isFont) {
		super.loadBoolean(location_isFont, isFont);
	}
 
    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_transparency = super.getUniformLocation("transparency");
        location_blendColor = super.getUniformLocation("blendColor");
        location_blendFactor = super.getUniformLocation("blendFactor");
        location_isFont = super.getUniformLocation("isFont");
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position"); //bind texture coordinates from attribute list 0 to the shader's position input parameter
        super.bindAttribute(1, "textureCoords"); //bind texture coordinates from attribute list 1 to the shader's texture coordinates input parameter
    }
}
