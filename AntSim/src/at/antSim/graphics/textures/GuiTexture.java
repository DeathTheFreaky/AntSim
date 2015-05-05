package at.antSim.graphics.textures;

import org.lwjgl.util.vector.Vector2f;

/**GuiTexture holds all types of textures which are rendered to the screen as part of the Gui.
 * 
 * @author Flo
 *
 */
public class GuiTexture {

	private int texture; //id or "name" of this texture returned by org.newdawn.slick.opengl.Texture.getTextureID()
	private Vector2f position; //need to store center position of the gui quad since gui Texture has no underlying ModelData storing positional data
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
