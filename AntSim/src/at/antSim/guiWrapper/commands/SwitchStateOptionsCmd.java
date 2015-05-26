package at.antSim.guiWrapper.commands;

import at.antSim.guiWrapper.GuiState;
import at.antSim.guiWrapper.GuiWrapper;
import at.antSim.guiWrapper.states.AbstractGuiState;

/**Opens the option menu.
 * 
 * Created by Alexander on 12.05.2015.
 */
public class SwitchStateOptionsCmd implements Command {
	
	private String name;
	private GuiState displayState;
	private AbstractGuiState orState;

	public SwitchStateOptionsCmd(String name, GuiState displayState, AbstractGuiState orState) {
		this.name = name;
		this.displayState = displayState;
		this.orState = orState;
	}

	@Override
	public void execute() {
		orState.resetState();
		GuiWrapper.getInstance().setCurrentState(name);
		GuiWrapper.getInstance().getCurrentState().setPrevState(displayState.getPrevState());
	}
}
