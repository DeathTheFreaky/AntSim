package at.antSim.guiWrapper.commands;

import at.antSim.guiWrapper.GuiState;
import at.antSim.guiWrapper.GuiWrapper;

/**Opens the option menu.
 * 
 * Created by Alexander on 12.05.2015.
 */
public class SwitchStateCmd implements Command {
	
	private String name;
	private GuiState prevState;

	public SwitchStateCmd(String name, GuiState prevState) {
		this.name = name;
		this.prevState = prevState;
	}

	@Override
	public void execute() {
		GuiWrapper.getInstance().setCurrentState(name);
		GuiWrapper.getInstance().getCurrentState().setPrevState(prevState);
	}
}
