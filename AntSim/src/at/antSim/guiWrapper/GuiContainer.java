package at.antSim.guiWrapper;

import java.util.LinkedList;
import java.util.List;

import at.antSim.graphics.models.RawModel;
import at.antSim.graphics.textures.GuiTexture;

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
	public GuiContainer(HorPositions horPos, int horOffset, VerPositions verPos, int verOffset,	int desiredWidth, 
			int desiredHeight, RawModel model, GuiTexture texture, String id, GuiContainer parent) {
		
		super(horPos, horOffset, verPos, verOffset, texture.getWidth(), texture.getHeight(), desiredWidth, desiredHeight, model, texture.getTextureId(), id, parent);
	}
	
	public List<GuiElement> getChildren() {
		return children;
	}
}
