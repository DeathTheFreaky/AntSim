package at.antSim.guiWrapper.commands;

import at.antSim.AntSim;
import at.antSim.MainApplication;
import at.antSim.graphics.graphicsUtils.OpenGLLoader;
import at.antSim.graphics.renderer.MasterRenderer;
import at.antSim.guiWrapper.GuiWrapper;
import at.antSim.eventSystem.EventManager;

import org.lwjgl.Sys;

/**Starts a new game.
 * 
 * @author Alexander
 *
 */
public class NewGameCmd implements Command {

	@Override
	public void execute() {
		EventManager.getInstance().registerEventListener(MainApplication.getInstance().getCamera());
		MainApplication.getInstance().triggerWorldLoad();
	}
}
