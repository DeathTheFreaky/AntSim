package at.antSim.guiWrapper.states;

import at.antSim.guiWrapper.*;
import org.lwjgl.util.vector.Vector3f;

import at.antSim.eventSystem.EventManager;
import at.antSim.graphics.graphicsUtils.OpenGLLoader;
import at.antSim.graphics.graphicsUtils.OpenGLTextDrawer;
import at.antSim.guiWrapper.commands.Command;
import at.antSim.guiWrapper.commands.ContinueCmd;
import at.antSim.guiWrapper.commands.QuitToMainMenuCmd;
import at.antSim.guiWrapper.commands.SwitchStateCmd;

/**
 * @author Alexander
 *
 */
public class PauseState extends AbstractGuiState {
	
	Command backToGameCmd;
	Command quitToMainMenuCmd;
	Command optionsCmd;

	public PauseState(OpenGLLoader loader, String name) {
		super(loader, name);
	}

	@Override
	public void initializeState(String... args) {
		
		backToGameCmd = new ContinueCmd(args[0]);
		quitToMainMenuCmd = new QuitToMainMenuCmd(args[1]);
		optionsCmd = new SwitchStateCmd(args[2], state, this);

		//
		// Big AntSim Logo!!
		//

		// Big container
		GuiContainer mainContainer = new GuiContainer("mainContainer", loader, null, null, wrapper.getGuiTexture("pauseMenu/pauseMenuContainer"),
				600, 300, HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0.3f, new Vector3f(1,1,1), 0f);
		GuiImage pauseMenuContainerBorder = new GuiImage("startMenuContainerBorder", loader, mainContainer, null, wrapper.getGuiTexture("pauseMenu/pauseMenuContainerBorder"), 600, 300,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0), 0f);
		
		OpenGLTextDrawer textDrawer = new OpenGLTextDrawer(loader, loader.loadGuiTexture("font"));
		
		// back button
		GuiContainer backToGameContainer = new GuiContainer("backToGameButton", loader, mainContainer, backToGameCmd, wrapper.getGuiTexture("pauseMenu/pauseMenuButton"), 500, 40,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.TOP, 50);
		GuiText backToGameText = new GuiText("backText", textDrawer.createTextQuad("Back to Game"), backToGameContainer, null, 32,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.BOTTOM, 1);
		EventManager.getInstance().registerEventListener(backToGameContainer);
		
		// options button
		GuiContainer optionsContainer = new GuiContainer("optionsButton", loader, mainContainer, optionsCmd, wrapper.getGuiTexture("pauseMenu/pauseMenuButton"), 500, 40,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 40);
		GuiText optionsText = new GuiText("optionsGame", textDrawer.createTextQuad("Options"), optionsContainer, null, 32,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.BOTTOM, 1);
		EventManager.getInstance().registerEventListener(optionsContainer);
		
		// quit button
		GuiContainer quitToStartMenuContainer = new GuiContainer("backToStartMenuButton", loader, mainContainer, quitToMainMenuCmd, wrapper.getGuiTexture("pauseMenu/pauseMenuButton"), 500, 40,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 40);
		GuiText quitToStartMenuText = new GuiText("backText", textDrawer.createTextQuad("Start Menu"), quitToStartMenuContainer, null, 32,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.BOTTOM, 1);
		EventManager.getInstance().registerEventListener(quitToStartMenuContainer);
				
		state.addContainer(mainContainer);
		
		GuiWrapper.getInstance().addState(state);
	}

	@Override
	public void resetState() {
		
	}
}