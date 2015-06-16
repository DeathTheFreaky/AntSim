package at.antSim.guiWrapper.commands;

import at.antSim.AntSim;
import at.antSim.MainApplication;

/**Quits the game.
 * 
 * @author Alexander
 *
 */
public class QuitGameCmd implements Command {
	
	@Override
	public void execute() {
		MainApplication.getInstance().quitApplication();
	}
}
