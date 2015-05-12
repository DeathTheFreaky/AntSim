package at.antSim.guiWrapper.commands;

import at.antSim.guiWrapper.GuiWrapper;

/**
 * Created by Alexander on 12.05.2015.
 */
public class OptionsCmd implements Command {
	private String name;

	public OptionsCmd(String name) {
		this.name = name;
	}

	@Override
	public void execute() {
		GuiWrapper.getInstance().setCurrentState(name);
	}
}
