package at.antSim.guiWrapper.commands;

import at.antSim.guiWrapper.states.MainGameState;

/**
 * @author Alexander
 *
 */
public class ToggleFoodCmd implements Command {
	
	MainGameState state;
	
	public ToggleFoodCmd(MainGameState state) {
		this.state = state;
	}
	
	@Override
	public void execute() {
		state.resetState();
		state.toggleFoodContainer();
	}
}
