package at.antSim.guiWrapper.commands;

public class TestCommand implements Command {

	@Override
	public void execute() {
		System.out.println("pressed test command button");
	}
}
