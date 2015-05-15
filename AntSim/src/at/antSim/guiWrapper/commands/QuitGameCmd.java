package at.antSim.guiWrapper.commands;
import org.lwjgl.opengl.Display;

import at.antSim.EngineTester;

/**Quits the game.
 * 
 * Created by Alexander on 12.05.2015.
 */
public class QuitGameCmd implements Command {
	
	EngineTester tester;
	
	public QuitGameCmd(EngineTester tester) {
		this.tester = tester;
	}

	@Override
	public void execute() {
		tester.quit();
	}
}
