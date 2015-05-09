package at.antSim.guiWrapper;

import org.lwjgl.util.vector.Vector2f;

import at.antSim.graphics.models.RawModel;

public class GuiTexturedElement extends GuiElement {
	
	int textureId;
	GuiContainer parent;
	
	public GuiTexturedElement(Vector2f position, Vector2f scale, RawModel model, String id, int textureId, GuiContainer parent) {
		super(position, scale, model, id);
		this.textureId = textureId;
		this.parent = parent;
		
		parent.getChildren().add(this);
	}
	
	public int getTextureId() {
		return textureId;
	}
	
	public GuiContainer getParent() {
		return parent;
	}
}
