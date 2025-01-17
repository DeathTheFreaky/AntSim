package at.antSim.guiWrapper.commands;

import at.antSim.guiWrapper.states.MainGameState;

/**
 * @author Alexander
 *
 */
public class ToggleFoodSubCmd implements Command {
	
	MainGameState state;
	
	public ToggleFoodSubCmd(MainGameState state) {
		this.state = state;
	}
	
	@Override
	public void execute() {
		state.resetState();
		state.toggleFoodSubContainer();
	}
}
