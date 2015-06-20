package at.antSim.guiWrapper.commands;

import at.antSim.Globals;
import at.antSim.config.ConfigWriter;
import at.antSim.graphics.graphicsUtils.DisplayManager;
import at.antSim.guiWrapper.GuiImage;
import at.antSim.guiWrapper.GuiUtils;
import at.antSim.guiWrapper.GuiWrapper;

/**
 * @author Alexander
 *
 */
public class FullScreenCmd implements Command {

	GuiImage img;
	
	public FullScreenCmd(GuiImage img) {
		this.img = img;
	}
	
	@Override
	public void execute() {
		Globals.fullscreen = !Globals.fullscreen;
		ConfigWriter.writeConfig();
		img.setTextureId(GuiWrapper.getInstance().getGuiTexture(GuiUtils.getCheckboxTexStr(Globals.fullscreen)).getTextureID());
		DisplayManager.setDisplayMode(Globals.displayWidth, Globals.displayHeight, Globals.fullscreen);
	}
}
