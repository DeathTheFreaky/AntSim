package at.antSim.guiWrapper.commands;

import at.antSim.Globals;
import at.antSim.config.ConfigWriter;
import at.antSim.graphics.graphicsUtils.DisplayManager;
import at.antSim.guiWrapper.GuiText;

public class ResCmd implements Command {

	int width;
	int height;
	GuiText text;
	GuiText text2;
	
	public ResCmd(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void execute() {
		Globals.displaySaveWidth = width;
		Globals.displaySaveHeight = height;
		ConfigWriter.writeConfig();
		if (text != null) {
			text.setTransparency(0f);
		}
		if (text2 != null) {
			text2.setTransparency(0f);
		}
	}

	public void setNoteText(GuiText resNoteText) {
		text = resNoteText;
	}
	
	public void setNoteText2(GuiText resNoteText) {
		text2 = resNoteText;
	}
}
