package at.antSim.guiWrapper.commands;

import at.antSim.MainApplication;

/**
 * Created by Alexander on 18.05.2015.
 */
public class ToggleSpeedCmd implements Command {
	private float speed;

	public ToggleSpeedCmd(float speed) {
		this.speed = speed;
	}
	@Override
	public void execute() {
		MainApplication.getInstance().setSpeed(speed);
		System.out.println("ToggleSpeedCmd: execute()\n Speed: " + speed);
		//change icon
	}
}
