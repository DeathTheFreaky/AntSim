package at.antSim.guiWrapper.states;

import at.antSim.Globals;
import at.antSim.eventSystem.EventManager;
import at.antSim.graphics.graphicsUtils.OpenGLLoader;
import at.antSim.graphics.graphicsUtils.OpenGLTextDrawer;
import at.antSim.guiWrapper.*;
import at.antSim.guiWrapper.commands.Command;
import at.antSim.guiWrapper.commands.NewGameCmd;
import at.antSim.guiWrapper.commands.QuitGameCmd;
import at.antSim.guiWrapper.commands.SwitchStateCmd;
import org.lwjgl.util.vector.Vector3f;

/**Start Menu.
 * 
 * @author Alexander
 *
 */
public class StartMenuState extends AbstractGuiState {
	
	Command newGameCmd;
	Command optionsCmd;
	Command quitGameCmd;

	public StartMenuState(OpenGLLoader loader, String name) {
		super(loader, name);
	}

	@Override
	public void initializeState(String... args) {
		
		newGameCmd = new NewGameCmd();
		optionsCmd = new SwitchStateCmd(args[0], state, this);
		quitGameCmd = new QuitGameCmd();

		GuiContainer mainContainer = new GuiContainer("mainContainer", loader, null, null, wrapper.getGuiTexture("background"),
				Globals.displayWidth, Globals.displayHeight, HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(1,1,1), 0f);



		OpenGLTextDrawer textDrawer = new OpenGLTextDrawer(loader, wrapper.getGuiTexture("font"));
		GuiImage logo = new GuiImage("logo", loader, mainContainer, null, wrapper.getGuiTexture("logo"), 580, 130,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.TOP, 30, 0f, new Vector3f(0, 0, 0), 0f);

		// container for menu elements
		GuiContainer startMenuContainer = new GuiContainer("startMenuContainer", loader, mainContainer, null, wrapper.getGuiTexture("startMenu/startMenuContainer"), 600, 300,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0.3f, new Vector3f(0, 0, 0), 0f);
		GuiImage startMenuContainerBorder = new GuiImage("startMenuContainerBorder", loader, startMenuContainer, null, wrapper.getGuiTexture("startMenu/startMenuContainerBorder"), 600, 300,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0), 0f);

		// new game button
		GuiContainer newGameContainer = new GuiContainer("newGameButton", loader, startMenuContainer, newGameCmd, wrapper.getGuiTexture("startMenu/startMenuButton"), 500, 40,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.TOP, 50);
		GuiText newGameText = new GuiText("newGame", textDrawer.createTextQuad("Start new game"), newGameContainer, null, 32,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.BOTTOM, 1);
		EventManager.getInstance().registerEventListener(newGameContainer);
		
		// options button
		GuiContainer optionsContainer = new GuiContainer("optionsButton", loader, startMenuContainer, optionsCmd, wrapper.getGuiTexture("startMenu/startMenuButton"), 500, 40,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 40);
		GuiText optionsText = new GuiText("optionsGame", textDrawer.createTextQuad("Options"), optionsContainer, null, 32,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.BOTTOM, 1);
		EventManager.getInstance().registerEventListener(optionsContainer);
		
		// quit button
		GuiContainer quitContainer = new GuiContainer("quitButton", loader, startMenuContainer, quitGameCmd, wrapper.getGuiTexture("startMenu/startMenuButton"), 500, 40,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 40);
		GuiText quitText = new GuiText("optionsGame", textDrawer.createTextQuad("Quit"), quitContainer, null, 32,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.BOTTOM, 1);
		EventManager.getInstance().registerEventListener(quitContainer);

		// Foreground-Ant
		GuiImage antForeground = new GuiImage("antForeground", loader, mainContainer, null, wrapper.getGuiTexture("ant_foreground"), 440, 360,
				HorReference.PARENT, HorPositions.RIGHT, 0, VerReference.PARENT, VerPositions.BOTTOM, -5, 0f, new Vector3f(0, 0, 0), 0f);
				
		state.addContainer(mainContainer);
		
		GuiWrapper.getInstance().addState(state);	
	}

	@Override
	public void resetState() {
		
	}
}