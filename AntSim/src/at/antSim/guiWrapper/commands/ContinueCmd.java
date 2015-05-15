package at.antSim.guiWrapper.commands;

import at.antSim.MainApplication;
import at.antSim.guiWrapper.GuiWrapper;

public class ContinueCmd implements Command {

	String targetName;
	
	public ContinueCmd(String targetName) {
		this.targetName = targetName;
	}
	
	@Override
	public void execute() {
		MainApplication.getInstance().unpause();
		GuiWrapper.getInstance().setCurrentState(targetName);
	}
}
