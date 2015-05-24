package at.antSim.graphics.textures;

/**Used to store a Gui Texture's id, width and height.
 * @author Flo
 *
 */
public class GuiTexture {
	
	int textureId;
	int textureWidth;
	int height;
	
	/**Creates a new Gui Texture.
	 * 
	 * @param textureId
	 * @param width
	 * @param height
	 */
	public GuiTexture(int textureId, int width, int height) {
		this.textureId = textureId;
		this.textureWidth = width;
		this.height = height;
	}

	public int getTextureId() {
		return textureId;
	}

	public int getWidth() {
		return textureWidth;
	}

	public int getHeight() {
		return height;
	}
}
