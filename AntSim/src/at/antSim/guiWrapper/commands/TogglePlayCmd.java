package at.antSim.guiWrapper.commands;

import at.antSim.MainApplication;
import at.antSim.guiWrapper.states.MainGameState;
import sun.applet.Main;

/**
 * Created by Alexander on 18.05.2015.
 */
public class TogglePlayCmd implements Command {
	private MainGameState mainGameState;

	public TogglePlayCmd(MainGameState mainGameState) {
		this.mainGameState = mainGameState;
	}
	@Override
	public void execute() {
		mainGameState.hideFoodContainer();
		mainGameState.hideEnemyContainer();
		mainGameState.updatePlayButton();
		MainApplication.getInstance().togglePlay();
	}
}
