package at.antSim.guiWrapper.commands;

import at.antSim.AntSim;
import at.antSim.MainApplication;
import at.antSim.graphics.graphicsUtils.OpenGLLoader;
import at.antSim.graphics.renderer.MasterRenderer;
import at.antSim.guiWrapper.GuiWrapper;

import org.lwjgl.Sys;

/**Starts a new game.
 * 
 * Created by Alexander on 12.05.2015.
 */
public class NewGameCmd implements Command {

	@Override
	public void execute() {
		MainApplication.getInstance().triggerWorldLoad();
	}
}
