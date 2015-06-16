package at.antSim.guiWrapper.commands;

import at.antSim.guiWrapper.GuiState;
import at.antSim.guiWrapper.GuiWrapper;
import at.antSim.guiWrapper.states.AbstractGuiState;

/**Opens the option menu.
 * 
 * @author Alexander
 *
 */
public class SwitchStateCmd implements Command {
	
	private String name;
	private GuiState prevState;
	private AbstractGuiState orState;

	public SwitchStateCmd(String name, GuiState prevState, AbstractGuiState orState) {
		this.name = name;
		this.prevState = prevState;
		this.orState = orState;
	}

	@Override
	public void execute() {
		orState.resetState();
		GuiWrapper.getInstance().setCurrentState(name);
		GuiWrapper.getInstance().getCurrentState().setPrevState(prevState);
	}
}
