package at.antSim.guiWrapper.states;

import org.lwjgl.util.vector.Vector3f;

import at.antSim.Globals;
import at.antSim.eventSystem.EventManager;
import at.antSim.graphics.graphicsUtils.Loader;
import at.antSim.graphics.graphicsUtils.OpenGLTextDrawer;
import at.antSim.guiWrapper.GuiContainer;
import at.antSim.guiWrapper.GuiText;
import at.antSim.guiWrapper.GuiWrapper;
import at.antSim.guiWrapper.HorPositions;
import at.antSim.guiWrapper.HorReference;
import at.antSim.guiWrapper.VerPositions;
import at.antSim.guiWrapper.VerReference;
import at.antSim.guiWrapper.commands.Command;
import at.antSim.guiWrapper.commands.SwitchStateCmd;

/**Display SubMenu of Options Menu.
 * 
 * @author Flo
 *
 */
public class OptionsDisplayState extends AbstractGuiState {
	
	Command startMenuCmd;

	public OptionsDisplayState(Loader loader, String name) {
		super(loader, name);
	}

	@Override
	public void initializeState(String... args) {
		
		startMenuCmd = new SwitchStateCmd(args[0]);
		 
		GuiContainer mainContainer = new GuiContainer("mainContainer", null, null, standardContainerQuad, loader.loadGuiTexture("white"),
				Globals.displayWidth, Globals.displayHeight, HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0, new Vector3f(1,1,1), 1);

		OpenGLTextDrawer textDrawer = new OpenGLTextDrawer(loader, loader.loadGuiTexture("font"));
		
		//back button
		GuiContainer backContainer = new GuiContainer("backButton", mainContainer, startMenuCmd, standardContainerQuad, loader.loadGuiTexture("white"), 450, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.TOP, 35);
		GuiText backText = new GuiText("backText", textDrawer.createTextQuad("Back"), backContainer, null, 32,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
		EventManager.getInstance().registerEventListener(backContainer);
				
		state.addContainer(mainContainer);
		
		GuiWrapper.getInstance().addState(state);
	}
}
