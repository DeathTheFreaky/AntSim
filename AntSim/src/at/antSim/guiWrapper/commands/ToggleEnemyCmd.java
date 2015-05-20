package at.antSim.guiWrapper.commands;

import at.antSim.guiWrapper.states.MainGameState;

/**
 * Created by Alexander on 19.05.2015.
 */
public class ToggleEnemyCmd implements Command {
	
	MainGameState state;
	
	public ToggleEnemyCmd(MainGameState state) {
		this.state = state;
	}
	
	@Override
	public void execute() {
		state.resetState();
		state.toggleEnemyContainer();
	}
}
