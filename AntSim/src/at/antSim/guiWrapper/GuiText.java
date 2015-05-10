package at.antSim.guiWrapper;

import org.lwjgl.util.vector.Vector2f;

import at.antSim.Globals;
import at.antSim.graphics.models.RawModel;

public class GuiText extends GuiElement {
	
	int size;

	public GuiText(HorPositions horPos, int horVal, VerPositions verPos, int verVal, int size, RawModel model, String id, int textureId, GuiContainer parent) {

		super(parent.getTopLeft()
				
				
				new Vector2f(position.x,  position.y - (float) 0.17 * size/Globals.displayHeight), //by default, our font's characters are not placed in the middle of the screen but a little higher
				new Vector2f((float) (1.0 * size/Globals.displayHeight), (float) (1.0 * size/Globals.displayHeight/9*16)), model, textureId, id, parent);
	}
}
