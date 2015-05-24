package at.antSim.guiWrapper.states;

import org.lwjgl.util.vector.Vector3f;

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
import at.antSim.guiWrapper.commands.ContinueCmd;
import at.antSim.guiWrapper.commands.QuitToMainMenuCmd;
import at.antSim.guiWrapper.commands.SwitchStateCmd;

public class PauseState extends AbstractGuiState {
	
	Command backToGameCmd;
	Command quitToMainMenuCmd;
	Command optionsCmd;

	public PauseState(Loader loader, String name) {
		super(loader, name);
	}

	@Override
	public void initializeState(String... args) {
		
		backToGameCmd = new ContinueCmd(args[0]);
		quitToMainMenuCmd = new QuitToMainMenuCmd(args[1]);
		optionsCmd = new SwitchStateCmd(args[2], state, this);
		 
		GuiContainer mainContainer = new GuiContainer("mainContainer", loader, null, backToGameCmd, wrapper.getGuiTexture("white"),
				600, 245, HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0.2f, new Vector3f(1,1,1), 1);
		
		OpenGLTextDrawer textDrawer = new OpenGLTextDrawer(loader, loader.loadGuiTexture("font"));
		
		//back button
		GuiContainer backToGameContainer = new GuiContainer("backToGameButton", loader, mainContainer, backToGameCmd, wrapper.getGuiTexture("white"), 580, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.TOP, 35);
		GuiText backToGameText = new GuiText("backText", textDrawer.createTextQuad("Back to Game"), backToGameContainer, null, 32,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
		EventManager.getInstance().registerEventListener(backToGameContainer);
		
		//options button
		GuiContainer optionsContainer = new GuiContainer("optionsButton", loader, mainContainer, optionsCmd, wrapper.getGuiTexture("white"), 580, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 35);
		GuiText optionsText = new GuiText("optionsGame", textDrawer.createTextQuad("Options"), optionsContainer, null, 32,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
		EventManager.getInstance().registerEventListener(optionsContainer);
		
		//quit button
		GuiContainer quitToStartMenuContainer = new GuiContainer("backToGameButton", loader, mainContainer, quitToMainMenuCmd, wrapper.getGuiTexture("white"), 580, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 35);
		GuiText quitToStartMenuText = new GuiText("backText", textDrawer.createTextQuad("Quit to Start Menu"), quitToStartMenuContainer, null, 32,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
		EventManager.getInstance().registerEventListener(quitToStartMenuContainer);
				
		state.addContainer(mainContainer);
		
		GuiWrapper.getInstance().addState(state);
	}

	@Override
	public void resetState() {
		
	}
}
