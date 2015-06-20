package at.antSim.guiWrapper.commands;

import at.antSim.MainApplication;
import at.antSim.guiWrapper.GuiWrapper;

/**
 * @author Alexander
 *
 */
public class QuitToMainMenuCmd implements Command {

	String targetName;
	
	public QuitToMainMenuCmd(String targetName) {
		this.targetName = targetName;
	}
	
	@Override
	public void execute() {
		//destroy current game
		GuiWrapper.getInstance().setCurrentState(targetName);
		MainApplication.getInstance().quitCurrentGame();
	}
}
