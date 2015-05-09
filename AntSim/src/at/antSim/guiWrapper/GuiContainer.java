package at.antSim.guiWrapper;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import at.antSim.graphics.models.RawModel;

public class GuiContainer extends GuiElement {
	
	List<GuiTexturedElement> children = new LinkedList<>();

	public GuiContainer(Vector2f position, Vector2f scale, RawModel model,
			String id) {
		super(position, scale, model, id);
	}
	
	public List<GuiTexturedElement> getChildren() {
		return children;
	}
}
