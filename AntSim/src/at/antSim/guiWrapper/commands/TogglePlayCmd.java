package at.antSim.guiWrapper.commands;

import at.antSim.MainApplication;
import sun.applet.Main;

/**
 * Created by Alexander on 18.05.2015.
 */
public class TogglePlayCmd implements Command {
	@Override
	public void execute() {
		MainApplication.getInstance().togglePause();
		System.out.println("TogglePlayCmd: execute()");
		// change icon
	}
}
