package at.antSim.guiWrapper.commands;

import at.antSim.MainApplication;
import at.antSim.guiWrapper.states.MainGameState;

/**
 * @author Alexander
 *
 */
public class TogglePlayCmd implements Command {
	private MainGameState mainGameState;

	public TogglePlayCmd(MainGameState mainGameState) {
		this.mainGameState = mainGameState;
	}
	@Override
	public void execute() {
		mainGameState.resetState();
		mainGameState.updatePlayButton();
		MainApplication.getInstance().togglePlay();
	}
}
