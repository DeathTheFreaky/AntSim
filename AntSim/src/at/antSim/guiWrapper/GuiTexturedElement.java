package at.antSim.guiWrapper;

import org.lwjgl.util.vector.Vector2f;

import at.antSim.graphics.models.RawModel;

public class GuiTexturedElement extends GuiElement {
	
	int textureId;
	
	public GuiTexturedElement(Vector2f position, Vector2f scale, RawModel model, String id, int textureId) {
		super(position, scale, model, id);
		this.textureId = textureId;
	}
	
	public int getTextureId() {
		return textureId;
	}

}
