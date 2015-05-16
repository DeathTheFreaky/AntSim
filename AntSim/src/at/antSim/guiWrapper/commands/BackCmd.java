package at.antSim.guiWrapper.commands;

import at.antSim.guiWrapper.GuiState;
import at.antSim.guiWrapper.GuiWrapper;

public class BackCmd implements Command {

	GuiState state;
	
	public BackCmd(GuiState state) {
		this.state = state;
	}

	@Override
	public void execute() {
		GuiWrapper.getInstance().setCurrentState(state.getPrevState().getName());		
	}
}
