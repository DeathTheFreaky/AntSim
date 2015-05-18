package at.antSim.guiWrapper.commands;

import at.antSim.Globals;
import at.antSim.config.ConfigWriter;
import at.antSim.guiWrapper.GuiImage;
import at.antSim.guiWrapper.GuiUtils;
import at.antSim.guiWrapper.GuiWrapper;

public class InvertVerCmd implements Command {
	
	GuiImage img;
	
	public InvertVerCmd(GuiImage img) {
		this.img = img;
	}

	@Override
	public void execute() {
		Globals.invertVerticalAxis = Globals.invertVerticalAxis * -1;
		ConfigWriter.writeConfig();
		img.setTextureId(GuiWrapper.getInstance().getGuiTexture(GuiUtils.getInvertCheckboxTexStr(Globals.invertVerticalAxis)).getTextureId());
	}
}
