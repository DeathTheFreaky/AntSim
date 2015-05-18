package at.antSim.guiWrapper.commands;

import at.antSim.AntSim;
import at.antSim.MainApplication;

/**Quits the game.
 * 
 * Created by Alexander on 12.05.2015.
 */
public class QuitGameCmd implements Command {
	
	@Override
	public void execute() {
		MainApplication.getInstance().quitApplication();
	}
}
