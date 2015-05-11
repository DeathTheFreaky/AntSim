package at.antSim.guiWrapper;

import at.antSim.graphics.models.RawModel;
import at.antSim.graphics.textures.GuiTexture;

public class GuiImage extends GuiElement {

	public GuiImage(HorPositions horPos, int horOffset, VerPositions verPos, int verOffset, int desiredWidth, 
			int desiredHeight, RawModel model, GuiTexture texture, String id, GuiContainer parent) {
		
		super(horPos, horOffset, verPos, verOffset, texture.getWidth(), texture.getHeight(), desiredWidth, desiredHeight, model, texture.getTextureId(), id, parent);
	}
}
