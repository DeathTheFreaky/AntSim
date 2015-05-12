package at.antSim.guiWrapper.commands;

import at.antSim.AntSim;
import at.antSim.EngineTester;
import at.antSim.graphics.graphicsUtils.Loader;
import at.antSim.graphics.renderer.MasterRenderer;
import at.antSim.guiWrapper.GuiWrapper;

import org.lwjgl.Sys;

/**Starts a new game.
 * 
 * Created by Alexander on 12.05.2015.
 */
public class NewGameCmd implements Command {

	private EngineTester tester;
	private Loader loader;
	private MasterRenderer renderer;

	public NewGameCmd(EngineTester tester, Loader loader, MasterRenderer renderer) {
		this.tester = tester;
		this.loader = loader;
		this.renderer = renderer;
	}

	@Override
	public void execute() {
		
		tester.triggerWorldLoad();
	}
}
