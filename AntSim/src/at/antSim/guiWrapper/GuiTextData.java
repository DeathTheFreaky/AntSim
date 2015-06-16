package at.antSim.guiWrapper;

import at.antSim.graphics.models.RawModel;

/**Stores a GuiText's geometric model, maximum number of columns and number of rows, character Size in pixels and the associated font textured id.
 * 
 * @author Alexander
 *
 */
public class GuiTextData {
	
	private RawModel model;
	private int rows;
	private int cols;
	private int charSize;
	private int textureId;
	
	/**Constructs a new {@link GuiTextData}.
	 * 
	 * @param model - the {@link GuiText}'s geometric data
	 * @param rows - the number of rows (lines) of a {@link GuiText}
	 * @param cols - maximum number of letters in a line of a {@link GuiText}
	 * @param charSize - size of a character in pixels
	 * @param textureId - id of associated font texture as assigned by OpenGL
	 */
	public GuiTextData(RawModel model, int rows, int cols, int charSize, int textureId) {
		this.model = model;
		this.rows = rows;
		this.cols = cols;
		this.charSize = charSize;
		this.textureId = textureId;
	}

	public RawModel getModel() {
		return model;
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public int getCharSize() {
		return charSize;
	}
	
	public int getTextureId() {
		return textureId;
	}
}
