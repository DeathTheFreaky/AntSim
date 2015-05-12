package at.antSim.guiWrapper.commands;

import at.antSim.guiWrapper.GuiWrapper;
import org.lwjgl.Sys;

/**
 * Created by Alexander on 12.05.2015.
 */
public class NewGameCmd implements Command {
	private String name;

	public NewGameCmd(String name) {
		this.name = name;
	}

	@Override
	public void execute() {
		System.out.println("NewGameCmd: execute");
		GuiWrapper.getInstance().setCurrentState(name);
		System.out.println(GuiWrapper.getInstance().getState(name));
		System.out.println(GuiWrapper.getInstance().getCurrentState());
	}
}
