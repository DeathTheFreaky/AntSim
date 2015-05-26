package at.antSim.guiWrapper;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

import at.antSim.graphics.graphicsUtils.OpenGLLoader;
import at.antSim.graphics.models.RawModel;
import at.antSim.guiWrapper.commands.Command;

/**A {@link GuiContainer} is meant to group {@link GuiElement} in a list of children.
 * 
 * @author Flo
 *
 */
public class GuiContainer extends GuiElement {
	
	List<GuiElement> children = new LinkedList<>();

	/**Constructs a new {@link GuiContainer}.
	 * 
	 * @param id - the {@link GuiElement}'s id as String
	 * @param loader - the {@link OpenGLLoader} used for loading texture coords and positions into a vao
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
	public GuiContainer(String id, OpenGLLoader loader, GuiContainer parent, Command command, Texture texture, int desiredWidth, int desiredHeight, 
			HorReference horRef, HorPositions horPos, int horOffset, VerReference verRef, VerPositions verPos, int verOffset, float transparency, Vector3f blendColor, float blendFactor) {
		
//		super(id, loader, parent, command, (texture != null) ? texture.getTextureId() : -1, (texture != null) ? texture.getWidth() : 0, (texture != null) ? texture.getHeight() : 0, 
//				desiredWidth, desiredHeight, horRef, horPos, horOffset, verRef, verPos, verOffset, transparency, blendColor, blendFactor);
//		
		super(id, loader, parent, command, texture, desiredWidth, desiredHeight, horRef, horPos, horOffset, verRef, verPos, verOffset, transparency, blendColor, blendFactor);
	}
	
	/**Constructs a new {@link GuiContainer}.<br>
	 * <br>
	 * Transparency and blendFactor will be set to 0 by default.<br>
	 * 
	 * @param id - the {@link GuiElement}'s id as String
	 * @param loader - the {@link OpenGLLoader} used for loading texture coords and positions into a vao
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
	 */
	public GuiContainer(String id, OpenGLLoader loader, GuiContainer parent, Command command, Texture texture, int desiredWidth, int desiredHeight, 
			HorReference horRef, HorPositions horPos, int horOffset, VerReference verRef, VerPositions verPos, int verOffset) {
		
		super(id, loader, parent, command, texture, desiredWidth, desiredHeight, horRef, horPos, horOffset, verRef, verPos, verOffset, 0f, new Vector3f(0, 0, 0), 0f);
	}
	
	/**Removes a specific child from this container, identified by its id.
	 * @param id
	 */
	public void removeChild(String id) {
		GuiElement removeElement = null;
		for (GuiElement elem : children) {
			if (elem.getId().equals(id)) {
				removeElement = elem;
			}
		}
		children.remove(removeElement);
	}
	
	/**Removes all children of this container.
	 * 
	 */
	public void removeChildren() {
		children.clear();
	}
	
	/**
	 * @return - number of this container's children
	 */
	public int getChildrenSize() {
		return children.size();
	}
	
	/**Adds a new child also registering its gui state as the parent's gui state.
	 * @param elem
	 */
	public void addChild(GuiElement elem) {
		children.add(elem);
		elem.setGuiState(getGuiState());
	}

	/**Adds a guiState as associated state for all children of this container.
	 * @param guiState
	 */
	public void addStateForAllChildren(GuiState guiState) {
		for (GuiElement element : children) {
			element.setGuiState(guiState);
			if (element instanceof GuiContainer) {
				((GuiContainer) element).addStateForAllChildren(guiState);
			}
		}
	}

	/**
	 * @return - returns an unmodifiable list of all children of this container
	 */
	public List<GuiElement> getAllChildren() {
		return Collections.unmodifiableList(children);
	}
}
