package at.antSim.guiWrapper;

import org.lwjgl.util.vector.Vector3f;

import at.antSim.graphics.models.RawModel;
import at.antSim.graphics.textures.GuiTexture;

/**Represents an image to be drawn in the GUI.
 * 
 * @author Flo
 *
 */
public class GuiImage extends GuiElement {

	/**Constructs a new {@link GuiImage}.
	 * 
	 * @param id - the {@link GuiElement}'s id as String
	 * @param parent - the {@link GuiElement}'s parenting {@link GuiContainer}
	 * @param model - the {@link GuiElement}'s geometric model
	 * @param texture - the {@link GuiElement}'s {@link GuiTexture}
	 * @param desiredWidth - desired width of the Gui element in pixels
	 * @param desiredHeight - desired height of the Gui element in pixels
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
	public GuiImage(String id, GuiContainer parent, RawModel model, GuiTexture texture, int desiredWidth, int desiredHeight, 
			HorReference horRef, HorPositions horPos, int horOffset, VerReference verRef, VerPositions verPos, int verOffset, float transparency, Vector3f blendColor, float blendFactor) {
		
		super(id, parent, model, texture.getTextureId(), texture.getWidth(), texture.getHeight(), desiredWidth, desiredHeight, 
				horRef, horPos, horOffset, verRef, verPos, verOffset, transparency, blendColor, blendFactor);
	}
	
	/**Constructs a new {@link GuiImage}.<br>
	 * <br>
	 * Transparency and blendFactor will be set to 0 by default.
	 * 
	 * @param id - the {@link GuiElement}'s id as String
	 * @param parent - the {@link GuiElement}'s parenting {@link GuiContainer}
	 * @param model - the {@link GuiElement}'s geometric model
	 * @param texture - the {@link GuiElement}'s {@link GuiTexture}
	 * @param desiredWidth - desired width of the Gui element in pixels
	 * @param desiredHeight - desired height of the Gui element in pixels
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
	public GuiImage(String id, GuiContainer parent, RawModel model, GuiTexture texture, int desiredWidth, int desiredHeight, 
			HorReference horRef, HorPositions horPos, int horOffset, VerReference verRef, VerPositions verPos, int verOffset) {
		
		super(id, parent, model, texture.getTextureId(), texture.getWidth(), texture.getHeight(), desiredWidth, desiredHeight, 
				horRef, horPos, horOffset, verRef, verPos, verOffset, 0f, new Vector3f(0,0,0), 0f);
	}
}
