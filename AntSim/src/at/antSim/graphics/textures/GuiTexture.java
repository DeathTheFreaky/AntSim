package at.antSim.graphics.textures;

import org.lwjgl.util.vector.Vector2f;

/**GuiTexture holds all types of texture which are rendered to the screen as part of the Gui.
 * 
 * @author Flo
 *
 */
public class GuiTexture {

	private int texture;
	private Vector2f position;
	private Vector2f scale;
	
	/**Creates a new {@link GuiTexture}.
	 * 
	 * @param texture - id of the texture to use for this gui element
	 * @param position - center of the gui quad in the openGl coordinate system
	 * @param scale - size of the gui quad in relation to the size of the screen
	 */
	public GuiTexture(int texture, Vector2f position, Vector2f scale) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
	}
	
	public int getTexture() {
		return texture;
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public Vector2f getScale() {
		return scale;
	}
}
