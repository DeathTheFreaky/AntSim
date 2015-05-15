package at.antSim.guiWrapper.commands;

import at.antSim.guiWrapper.GuiWrapper;

/**Opens the option menu.
 * 
 * Created by Alexander on 12.05.2015.
 */
public class SwitchStateCmd implements Command {
	
	private String name;

	public SwitchStateCmd(String name) {
		this.name = name;
	}

	@Override
	public void execute() {
		GuiWrapper.getInstance().setCurrentState(name);
	}
}
