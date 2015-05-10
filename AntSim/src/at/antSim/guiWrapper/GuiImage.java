package at.antSim.guiWrapper;

import org.lwjgl.util.vector.Vector2f;

import at.antSim.graphics.models.RawModel;

public class GuiImage extends GuiElement {

	public GuiImage(Vector2f position, Vector2f scale, RawModel model, String id, int textureId, GuiContainer parent) {
		super(position, scale, model, textureId, id, parent);
	}
}
