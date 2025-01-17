package at.antSim.guiWrapper.commands;

import at.antSim.MainApplication;
import at.antSim.guiWrapper.states.MainGameState;

/**
 * @author Alexander
 *
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
		mainGameState.resetState();
		mainGameState.updateSpeedButtons(speedCount);
		MainApplication.getInstance().setSpeed(speed);
		mainGameState.setPauseButton();
		MainApplication.getInstance().unpause();
	}
}
