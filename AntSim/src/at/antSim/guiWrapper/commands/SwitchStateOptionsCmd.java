package at.antSim.guiWrapper.commands;

import at.antSim.guiWrapper.GuiState;
import at.antSim.guiWrapper.GuiWrapper;

/**Opens the option menu.
 * 
 * Created by Alexander on 12.05.2015.
 */
public class SwitchStateOptionsCmd implements Command {
	
	private String name;
	private GuiState displayState;

	public SwitchStateOptionsCmd(String name, GuiState displayState) {
		this.name = name;
		this.displayState = displayState;
	}

	@Override
	public void execute() {
		GuiWrapper.getInstance().setCurrentState(name);
		GuiWrapper.getInstance().getCurrentState().setPrevState(displayState.getPrevState());
	}
}
