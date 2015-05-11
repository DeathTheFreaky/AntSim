package at.antSim.guiWrapper;

import at.antSim.graphics.models.RawModel;

public class GuiText extends GuiElement {
	
	int size;

//	public GuiText(HorPositions horPos, int horOffset, VerPositions verPos, int verOffset, int textureWidth, int textureHeight, int size, RawModel model, String id, int textureId, GuiContainer parent) {
//
//		/*super(parent.getTopLeft()
//				
//				
//				new Vector2f(position.x,  position.y - (float) 0.17 * size/Globals.displayHeight), //by default, our font's characters are not placed in the middle of the screen but a little higher
//				new Vector2f((float) (1.0 * size/Globals.displayHeight), (float) (1.0 * size/Globals.displayHeight/9*16)), model, textureId, id, parent);*/
//		
//		super(horPos, horOffset, verPos, verOffset, textureWidth, textureHeight, size, size, model, textureId, id, parent);
//	}

	public GuiText(HorPositions horPos, int horOffset, VerPositions verPos, int verOffset, int size, GuiTextData textData, String id, GuiContainer parent) {
		
		super(horPos, horOffset, verPos, verOffset, textData.getCharSize() * textData.getCols(), textData.getCharSize() * textData.getRows(), 
				size * textData.getCols(), size * textData.getRows(), textData.getModel(), textData.getTextureId(), id, parent);
	}
}
