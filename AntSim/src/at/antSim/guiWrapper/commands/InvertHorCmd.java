package at.antSim.guiWrapper.commands;

import at.antSim.Globals;
import at.antSim.config.ConfigWriter;
import at.antSim.guiWrapper.GuiImage;
import at.antSim.guiWrapper.GuiUtils;
import at.antSim.guiWrapper.GuiWrapper;

/**
 * @author Alexander
 *
 */
public class InvertHorCmd implements Command {
	
	GuiImage img;
	
	public InvertHorCmd(GuiImage img) {
		this.img = img;
	}

	@Override
	public void execute() {
		Globals.invertHorizontalAxis = Globals.invertHorizontalAxis * -1;
		ConfigWriter.writeConfig();
		img.setTextureId(GuiWrapper.getInstance().getGuiTexture(GuiUtils.getInvertCheckboxTexStr(Globals.invertHorizontalAxis)).getTextureID());
	}
}
