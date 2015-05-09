package at.antSim.graphics.shaders;

import org.lwjgl.util.vector.Matrix4f;

import at.antSim.Globals;

/**GuiShader is used to start up a Gui Shader Program and load uniform variables into this shader program.
 * 
 * @author Flo
 *
 */
public class GuiShader extends ShaderProgram {

	private static final String VERTEX_FILE = Globals.SHADERS + "guiVertexShader.vsh";
    private static final String FRAGMENT_FILE = Globals.SHADERS + "guiFragmentShader.fsh";
     
    private int location_transformationMatrix;
 
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
 
    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }
 
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position"); //bind texture coordinates from attribute list 0 to the shader's position input parameter
        super.bindAttribute(1, "textureCoords"); //bind texture coordinates from attribute list 1 to the shader's texture coordinates input parameter
    }
}
