package at.antSim.guiWrapper.commands;

import at.antSim.MainApplication;
import at.antSim.guiWrapper.states.MainGameState;

/**
 * Created by Alexander on 18.05.2015.
 */
public class ToggleSpeedCmd implements Command {
	private float speed;
	private MainGameState mainGameState;
	private int speedCount;

	public ToggleSpeedCmd(float speed, MainGameState mainGameState, int speedCount) {
		this.speed = speed;
		this.mainGameState = mainGameState;
		this.speedCount = speedCount;
	}
	@Override
	public void execute() {
		mainGameState.updateSpeedButtons(speedCount);
		MainApplication.getInstance().setSpeed(speed);
		mainGameState.setPauseButton();
		MainApplication.getInstance().unpause();
		System.out.println("ToggleSpeedCmd: execute()\n Speed: " + speed);
		//change icon
	}
}
