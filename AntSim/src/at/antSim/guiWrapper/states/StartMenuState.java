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
		optionsCmd = new SwitchStateCmd(args[0]);
		quitGameCmd = new QuitGameCmd();
		 
		GuiContainer startContainer = new GuiContainer("mainContainer", null, null, standardContainerQuad, loader.loadGuiTexture("white"),
				Globals.displayWidth, Globals.displayHeight, HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);

	
		OpenGLTextDrawer textDrawer = new OpenGLTextDrawer(loader, loader.loadGuiTexture("font"));
		GuiText antSim1 = new GuiText("antSim1", textDrawer.createTextQuad("Ant"), startContainer, null, 52, HorReference.PARENT, HorPositions.LEFT, Globals.displayWidth/2 - 150, VerReference.SIBLING, VerPositions.TOP, 20);
		GuiText antSim2 = new GuiText("antSim2", textDrawer.createTextQuad("Sim"), startContainer, null, 36, HorReference.PARENT, HorPositions.RIGHT, Globals.displayWidth/2 - 100, VerReference.SIBLING, VerPositions.BELOW, -15);

		GuiContainer newGameContainer = new GuiContainer("newGamebutton", startContainer, newGameCmd, standardContainerQuad, loader.loadGuiTexture("white"), 300, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 35, 0, new Vector3f(0, 0, 0), 0.8f);
		GuiText newGameText = new GuiText("newGame", textDrawer.createTextQuad("Start new game"), newGameContainer, null, 25,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
		EventManager.getInstance().registerEventListener(newGameContainer);
		
		startContainer.getChildren().add(newGameContainer);
		state.addContainer(startContainer);
		
		GuiWrapper.getInstance().addState(state);	
	}
}
