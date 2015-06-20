package at.antSim.guiWrapper.commands;

import at.antSim.guiWrapper.states.MainGameState;

/**
 * @author Alexander
 *
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
