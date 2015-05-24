package at.antSim.guiWrapper.states;

import org.lwjgl.util.vector.Vector3f;

import at.antSim.Globals;
import at.antSim.eventSystem.EventManager;
import at.antSim.graphics.graphicsUtils.Loader;
import at.antSim.graphics.graphicsUtils.OpenGLTextDrawer;
import at.antSim.graphics.renderer.MasterRenderer;
import at.antSim.guiWrapper.GuiContainer;
import at.antSim.guiWrapper.GuiText;
import at.antSim.guiWrapper.GuiWrapper;
import at.antSim.guiWrapper.HorPositions;
import at.antSim.guiWrapper.HorReference;
import at.antSim.guiWrapper.VerPositions;
import at.antSim.guiWrapper.VerReference;
import at.antSim.guiWrapper.commands.Command;
import at.antSim.guiWrapper.commands.NewGameCmd;
import at.antSim.guiWrapper.commands.QuitGameCmd;
import at.antSim.guiWrapper.commands.SwitchStateCmd;

/**Start Menu.
 * 
 * @author Flo
 *
 */
public class StartMenuState extends AbstractGuiState {
	
	Command newGameCmd;
	Command optionsCmd;
	Command quitGameCmd;

	public StartMenuState(Loader loader, String name) {
		super(loader, name);
	}

	@Override
	public void initializeState(String... args) {
		
		newGameCmd = new NewGameCmd();
		optionsCmd = new SwitchStateCmd(args[0], state, this);
		quitGameCmd = new QuitGameCmd();
		 
		GuiContainer mainContainer = new GuiContainer("mainContainer", loader, null, null, wrapper.getGuiTexture("white"),
				Globals.displayWidth, Globals.displayHeight, HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0, new Vector3f(1,1,1), 1);

		OpenGLTextDrawer textDrawer = new OpenGLTextDrawer(loader, wrapper.getGuiTexture("font"));
		GuiText antSim1 = new GuiText("antSim1", textDrawer.createTextQuad("Ant"), mainContainer, null, 52, HorReference.PARENT, HorPositions.LEFT, Globals.displayWidth/2 - 150, 
				VerReference.SIBLING, VerPositions.TOP, 20,  0.5f, new Vector3f(1f, 0f, 0f), 0.0f);
		GuiText antSim2 = new GuiText("antSim2", textDrawer.createTextQuad("Sim"), mainContainer, null, 36, HorReference.PARENT, HorPositions.RIGHT, Globals.displayWidth/2 - 100, VerReference.SIBLING, VerPositions.BELOW, -15);

		//new game button
		GuiContainer newGameContainer = new GuiContainer("newGameButton", loader, mainContainer, newGameCmd, wrapper.getGuiTexture("white"), 450, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 35);
		GuiText newGameText = new GuiText("newGame", textDrawer.createTextQuad("Start new game"), newGameContainer, null, 32,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
		EventManager.getInstance().registerEventListener(newGameContainer);
		
		//options button
		GuiContainer optionsContainer = new GuiContainer("optionsButton", loader, mainContainer, optionsCmd, wrapper.getGuiTexture("white"), 450, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 35);
		GuiText optionsText = new GuiText("optionsGame", textDrawer.createTextQuad("Options"), optionsContainer, null, 32,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
		EventManager.getInstance().registerEventListener(optionsContainer);
		
		//quit button
		GuiContainer quitContainer = new GuiContainer("quitButton", loader, mainContainer, quitGameCmd, wrapper.getGuiTexture("white"), 450, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 35);
		GuiText quitText = new GuiText("optionsGame", textDrawer.createTextQuad("Quit"), quitContainer, null, 32,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
		EventManager.getInstance().registerEventListener(quitContainer);
				
		state.addContainer(mainContainer);
		
		GuiWrapper.getInstance().addState(state);	
	}

	@Override
	public void resetState() {
		
	}
}
