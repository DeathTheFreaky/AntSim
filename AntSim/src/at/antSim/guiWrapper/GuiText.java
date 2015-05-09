package at.antSim.guiWrapper;

import org.lwjgl.util.vector.Vector2f;

import at.antSim.graphics.models.RawModel;

public class GuiText extends GuiTexturedElement {

	public GuiText(Vector2f position, Vector2f scale, RawModel model, String id, int textureId) {
		super(position, scale, model, id, textureId);
	}
}
