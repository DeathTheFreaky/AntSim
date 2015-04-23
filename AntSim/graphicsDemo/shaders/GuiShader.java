package shaders;

import org.lwjgl.util.vector.Matrix4f;

/**GuiShader is used to start up a Gui Shader Program and load uniform variables into this shader program.
 * 
 * @author Flo
 *
 */
public class GuiShader extends ShaderProgram {

	private static final String VERTEX_FILE = "graphicsDemo/shaders/guiVertexShader.vsh";
    private static final String FRAGMENT_FILE = "graphicsDemo/shaders/guiFragmentShader.fsh";
     
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
        super.bindAttribute(0, "position");
    }
}
