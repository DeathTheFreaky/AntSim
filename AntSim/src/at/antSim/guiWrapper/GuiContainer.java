package at.antSim.guiWrapper;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import at.antSim.graphics.models.RawModel;

public class GuiContainer extends GuiElement {
	
	List<GuiElement> children = new LinkedList<>();

	/**Creates a new Gui Container. 
	 * 
	 * @param position
	 * @param scale
	 * @param model
	 * @param textureId - -1 if no texture shall be applied
	 * @param id
	 */
	public GuiContainer(Vector2f position, Vector2f scale, RawModel model, int textureId,
			String id, GuiContainer parent) {
		super(position, scale, model, textureId, id, parent);
	}
	
	public List<GuiElement> getChildren() {
		return children;
	}
}
