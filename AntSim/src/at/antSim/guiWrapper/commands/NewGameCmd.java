package at.antSim.guiWrapper.commands;

import at.antSim.MainApplication;
import at.antSim.eventSystem.EventManager;

/**Starts a new game.
 * 
 * Created by Alexander on 12.05.2015.
 */
public class NewGameCmd implements Command {

	@Override
	public void execute() {
		EventManager.getInstance().registerEventListener(MainApplication.getInstance().getCamera());
		MainApplication.getInstance().triggerWorldLoad();
	}
}
