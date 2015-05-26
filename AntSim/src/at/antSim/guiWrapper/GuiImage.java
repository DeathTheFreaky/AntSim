package at.antSim.guiWrapper;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

import at.antSim.graphics.graphicsUtils.Loader;
import at.antSim.graphics.models.RawModel;
import at.antSim.guiWrapper.commands.Command;

/**Represents an image to be drawn in the GUI.
 * 
 * @author Flo
 *
 */
public class GuiImage extends GuiElement {

	/**Constructs a new {@link GuiImage}.
	 * 
	 * @param id - the {@link GuiElement}'s id as String
	 * @param loader - the {@link Loader} used for loading texture coords and positions into a vao
	 * @param parent - the {@link GuiElement}'s parenting {@link GuiContainer}
	 * @param command - a {@link Command} to be executed when the mouse is released on this {@link GuiElement}
	 * @param texture - the {@link GuiElement}'s {@link Texture}
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
	public GuiImage(String id, Loader loader, GuiContainer parent, Command command, Texture texture, int desiredWidth, int desiredHeight, 
			HorReference horRef, HorPositions horPos, int horOffset, VerReference verRef, VerPositions verPos, int verOffset, float transparency, Vector3f blendColor, float blendFactor) {
		
		super(id, loader, parent, command, texture, desiredWidth, desiredHeight, horRef, horPos, horOffset, verRef, verPos, verOffset, transparency, blendColor, blendFactor);
	}
	
	/**Constructs a new {@link GuiImage}.<br>
	 * <br>
	 * Transparency and blendFactor will be set to 0 by default.
	 * 
	 * @param id - the {@link GuiElement}'s id as String
	 * @param loader - the {@link Loader} used for loading texture coords and positions into a vao
	 * @param parent - the {@link GuiElement}'s parenting {@link GuiContainer}
	 * @param command - a {@link Command} to be executed when the mouse is released on this {@link GuiElement}
	 * @param texture - the {@link GuiElement}'s {@link Texture}
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
	public GuiImage(String id, Loader loader, GuiContainer parent, Command command, Texture texture, int desiredWidth, int desiredHeight, 
			HorReference horRef, HorPositions horPos, int horOffset, VerReference verRef, VerPositions verPos, int verOffset) {
		
		super(id, loader, parent, command, texture, desiredWidth, desiredHeight, horRef, horPos, horOffset, verRef, verPos, verOffset, 0f, new Vector3f(0,0,0), 0f);
	}
}
