package at.antSim.guiWrapper;

import org.lwjgl.util.vector.Vector3f;

/**Represents a text drawn as 2d quad in the GUI.
 * 
 * @author Flo
 *
 */
public class GuiText extends GuiElement {
	
	/**Constructs a new {@link GuiText}.
	 * 
	 * @param id - the {@link GuiElement}'s id as String
	 * @param textData - the {@link GuiText}'s {@link GuiTextData}
	 * @param parent - the {@link GuiElement}'s parenting {@link GuiContainer}
	 * @param size - size of a character in pixels
	 * @param horRef - {@link HorReference} of the {@link GuiElement}
	 * @param horPos - {@link HorPosition} of the {@link GuiElement}
	 * @param horOffset - horizontal offset in pixels
	 * @param verRef - {@link VerReference} of the {@link GuiElement}
	 * @param verPos - {@link VerPosition} of the {@link GuiElement}
	 * @param verOffset - vertical offset in pixels
	 * @param transparency - 0: opaque, 1: fully transparent
	 * @param blendColor - color to blend with the {@link GuiElement}'s texture
	 * @param blendFactor - 0: draw 100% original texture, 1: fully blend texture with blendColor
	 */
	public GuiText(String id, GuiTextData textData, GuiContainer parent, int size, HorReference horRef, HorPositions horPos, int horOffset, 
			VerReference verRef, VerPositions verPos, int verOffset, float transparency, Vector3f blendColor, float blendFactor) {
		
		super(id, parent, textData.getModel(), textData.getTextureId(), textData.getCharSize() * textData.getCols(), textData.getCharSize() * textData.getRows(), 
				size * textData.getCols(), size * textData.getRows(), horRef, horPos, horOffset, verRef, verPos, verOffset, transparency, blendColor, blendFactor);
	}
	
	/**Constructs a new {@link GuiText}.<br>
	 * <br>
	 * Transparency will be set to 0, blendFactor to 1 and blendColor to black by default.
	 * 
	 * @param id - the {@link GuiElement}'s id as String
	 * @param textData - the {@link GuiText}'s {@link GuiTextData}
	 * @param parent - the {@link GuiElement}'s parenting {@link GuiContainer}
	 * @param size - size of a character in pixels
	 * @param horRef - {@link HorReference} of the {@link GuiElement}
	 * @param horPos - {@link HorPosition} of the {@link GuiElement}
	 * @param horOffset - horizontal offset in pixels
	 * @param verRef - {@link VerReference} of the {@link GuiElement}
	 * @param verPos - {@link VerPosition} of the {@link GuiElement}
	 * @param verOffset - vertical offset in pixels
	 * @param transparency - 0: opaque, 1: fully transparent
	 * @param blendColor - color to blend with the {@link GuiElement}'s texture
	 * @param blendFactor - 0: draw 100% original texture, 1: fully blend texture with blendColor
	 */
	public GuiText(String id, GuiTextData textData, GuiContainer parent, int size, HorReference horRef, HorPositions horPos, int horOffset, 
			VerReference verRef, VerPositions verPos, int verOffset) {
		
		super(id, parent, textData.getModel(), textData.getTextureId(), textData.getCharSize() * textData.getCols(), textData.getCharSize() * textData.getRows(), 
				size * textData.getCols(), size * textData.getRows(), horRef, horPos, horOffset, verRef, verPos, verOffset, 0f, new Vector3f(0, 0, 0), 1f);
	}
}
