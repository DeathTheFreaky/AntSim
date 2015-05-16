package at.antSim.guiWrapper.states;

import org.lwjgl.util.vector.Vector3f;

import at.antSim.Globals;
import at.antSim.eventSystem.EventManager;
import at.antSim.graphics.graphicsUtils.Loader;
import at.antSim.graphics.graphicsUtils.OpenGLTextDrawer;
import at.antSim.graphics.textures.GuiTexture;
import at.antSim.guiWrapper.GuiContainer;
import at.antSim.guiWrapper.GuiImage;
import at.antSim.guiWrapper.GuiText;
import at.antSim.guiWrapper.GuiUtils;
import at.antSim.guiWrapper.GuiWrapper;
import at.antSim.guiWrapper.HorPositions;
import at.antSim.guiWrapper.HorReference;
import at.antSim.guiWrapper.VerPositions;
import at.antSim.guiWrapper.VerReference;
import at.antSim.guiWrapper.commands.BackCmd;
import at.antSim.guiWrapper.commands.Command;
import at.antSim.guiWrapper.commands.FullScreenCmd;
import at.antSim.guiWrapper.commands.InvertHorCmd;
import at.antSim.guiWrapper.commands.InvertVerCmd;
import at.antSim.guiWrapper.commands.SwitchStateCmd;

/**Display SubMenu of Options Menu.
 * 
 * @author Flo
 *
 */
public class OptionsDisplayState extends AbstractGuiState {
	
	Command backCmd;
	Command invertHorCmd;
	Command invertVerCmd;
	Command fullScreenCmd;
	
	public OptionsDisplayState(Loader loader, String name) {
		super(loader, name);
	}

	@Override
	public void initializeState(String... args) {
		
		backCmd = new BackCmd(state);
		
		GuiContainer mainContainer = new GuiContainer("mainContainer", null, null, standardQuad, wrapper.getGuiTexture("white"),
				Globals.displayWidth, Globals.displayHeight, HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0, new Vector3f(1,1,1), 1);

		OpenGLTextDrawer textDrawer = new OpenGLTextDrawer(loader, wrapper.getGuiTexture("font"));
		
		//invert mouse
		GuiContainer invertContainer = new GuiContainer("invertContainer", mainContainer, null, standardQuad,  wrapper.getGuiTexture("white"), 410, 105,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.TOP, 35, 0f, new Vector3f(1, 0, 0), 0.5f);
		GuiText invertHeadline = new GuiText("invertHeadline", textDrawer.createTextQuad("Invert mouse axis"), invertContainer, null, 32,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.TOP, 0);
		
		GuiContainer invertHorContainer = new GuiContainer("invertHorContainer", invertContainer, null, standardQuad, wrapper.getGuiTexture("white"), 410, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 5);
		GuiText invertHorAxisText = new GuiText("invertHorAxisText", textDrawer.createTextQuad("Horizontal axis"), invertHorContainer, null, 24,
				HorReference.PARENT, HorPositions.LEFT, 5, VerReference.SIBLING, VerPositions.TOP, 0);
		GuiImage checkedHorImage = new GuiImage("checkedHorImage", invertHorContainer, null, standardQuad, wrapper.getGuiTexture(GuiUtils.getInvertCheckboxTexStr(Globals.invertHorizontalAxis)), 24, 24, 
				HorReference.PARENT, HorPositions.RIGHT, 5, VerReference.PARENT, VerPositions.TOP, 0, 0f, new Vector3f(0f, 0f, 0f), 1f);
		invertHorCmd = new InvertHorCmd(checkedHorImage);
		invertHorContainer.setCommand(invertHorCmd);
		EventManager.getInstance().registerEventListener(invertHorContainer);
		
		GuiContainer invertVerContainer = new GuiContainer("invertVerContainer", invertContainer, null, standardQuad, wrapper.getGuiTexture("white"), 410, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 5);
		GuiText invertVerAxisText = new GuiText("invertVerAxisText", textDrawer.createTextQuad("Vertical axis"), invertVerContainer, null, 24,
				HorReference.PARENT, HorPositions.LEFT, 5, VerReference.SIBLING, VerPositions.TOP, 0);
		GuiImage checkedVerImage = new GuiImage("checkedVerImage", invertVerContainer, null, standardQuad, wrapper.getGuiTexture(GuiUtils.getInvertCheckboxTexStr(Globals.invertVerticalAxis)), 24, 24, 
				HorReference.PARENT, HorPositions.RIGHT, 5, VerReference.PARENT, VerPositions.TOP, 0, 0f, new Vector3f(0f, 0f, 0f), 1f);
		invertVerCmd = new InvertVerCmd(checkedVerImage);
		invertVerContainer.setCommand(invertVerCmd);
		EventManager.getInstance().registerEventListener(invertVerContainer);
		
		//change resolution
		GuiContainer resContainer = new GuiContainer("resContainer", mainContainer, null, standardQuad, null, 300, 100,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 35);
		GuiText resHeadline = new GuiText("resHeadline", textDrawer.createTextQuad("Change resolution"), resContainer, null, 32,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.TOP, 0);
		
		GuiContainer fullScreenContainer = new GuiContainer("fullScreenContainer", resContainer, null, standardQuad, wrapper.getGuiTexture("white"), 410, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 5);
		GuiText fullScreenAxisText = new GuiText("fullScreenAxisText", textDrawer.createTextQuad("Fullscreen"), fullScreenContainer, null, 24,
				HorReference.PARENT, HorPositions.LEFT, 5, VerReference.SIBLING, VerPositions.TOP, 0);
		GuiImage checkedFullScreenImage = new GuiImage("checkedFullscreenImage", fullScreenContainer, null, standardQuad, wrapper.getGuiTexture(GuiUtils.getCheckboxTexStr(Globals.fullscreen)), 24, 24, 
				HorReference.PARENT, HorPositions.RIGHT, 5, VerReference.PARENT, VerPositions.TOP, 0, 0f, new Vector3f(0f, 0f, 0f), 1f);
		fullScreenCmd = new FullScreenCmd(checkedFullScreenImage);
		fullScreenContainer.setCommand(fullScreenCmd);
		EventManager.getInstance().registerEventListener(fullScreenContainer);
	
				
		//back button
		GuiContainer backContainer = new GuiContainer("backButton", mainContainer, backCmd, standardQuad, wrapper.getGuiTexture("white"), 450, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 35);
		GuiText backText = new GuiText("backText", textDrawer.createTextQuad("Back"), backContainer, null, 32,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
		EventManager.getInstance().registerEventListener(backContainer);
		
		state.addContainer(mainContainer);
		
		GuiWrapper.getInstance().addState(state);
	}
}
