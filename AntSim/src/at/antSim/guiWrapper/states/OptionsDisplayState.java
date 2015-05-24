package at.antSim.guiWrapper.states;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector3f;

import at.antSim.Globals;
import at.antSim.eventSystem.EventManager;
import at.antSim.graphics.graphicsUtils.Loader;
import at.antSim.graphics.graphicsUtils.OpenGLTextDrawer;
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
import at.antSim.guiWrapper.commands.ResCmd;
import at.antSim.guiWrapper.commands.SwitchStateOptionsCmd;

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
	Command switchToControlsCmd;
	
	GuiText resNoteText;
	GuiText resNoteText2;
	
	public OptionsDisplayState(Loader loader, String name) {
		super(loader, name);
	}

	@Override
	public void initializeState(String... args) {
		
		backCmd = new BackCmd(state);
		switchToControlsCmd = new SwitchStateOptionsCmd(args[1], state, this);
		
		GuiContainer mainContainer = new GuiContainer("mainContainer", loader, null, backCmd, wrapper.getGuiTexture("white"),
				Globals.displayWidth, Globals.displayHeight, HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0, new Vector3f(1,1,1), 1);

		OpenGLTextDrawer textDrawer = new OpenGLTextDrawer(loader, wrapper.getGuiTexture("font"));
		
		//switch between display and control options
		GuiContainer switchContainer = new GuiContainer("switchContainer", loader, mainContainer, null,  wrapper.getGuiTexture("white"), 410, 39,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.TOP, 30, 0f, new Vector3f(1, 0, 0), 0.5f);
		GuiContainer displaySwitchContainer = new GuiContainer("displaySwitchContainer", loader, switchContainer, null,  wrapper.getGuiTexture("white"), switchContainer.getWidth()/2, 
				switchContainer.getHeight(), HorReference.PARENT, HorPositions.LEFT, 0, VerReference.SIBLING, VerPositions.TOP, 0, 0f, new Vector3f(0, 0, 1), 0.5f);
		GuiText displaySwitchText = new GuiText("displaySwitch", textDrawer.createTextQuad("Display"), displaySwitchContainer, null, 25,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
		GuiContainer controlSwitchContainer = new GuiContainer("controlSwitchContainer", loader, switchContainer, switchToControlsCmd,  wrapper.getGuiTexture("white"), switchContainer.getWidth()/2, 
				switchContainer.getHeight(), HorReference.PARENT, HorPositions.RIGHT, 0, VerReference.SIBLING, VerPositions.TOP, 0, 0f, new Vector3f(0, 1, 0), 0.5f);
		GuiText controlSwitchText = new GuiText("controlSwitch", textDrawer.createTextQuad("Controls"), controlSwitchContainer, null, 25,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
		EventManager.getInstance().registerEventListener(controlSwitchContainer);
		
		//invert mouse
		GuiContainer invertContainer = new GuiContainer("invertContainer", loader, mainContainer, null,  wrapper.getGuiTexture("white"), 410, 105,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 30, 0f, new Vector3f(1, 0, 0), 0.5f);
		GuiText invertHeadline = new GuiText("invertHeadline", textDrawer.createTextQuad("Invert mouse axis"), invertContainer, null, 25,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.TOP, 0);
		
		GuiContainer invertHorContainer = new GuiContainer("invertHorContainer", loader, invertContainer, null, wrapper.getGuiTexture("white"), 410, 30,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 5);
		GuiText invertHorAxisText = new GuiText("invertHorAxisText", textDrawer.createTextQuad("Horizontal axis"), invertHorContainer, null, 22,
				HorReference.PARENT, HorPositions.LEFT, 5, VerReference.SIBLING, VerPositions.TOP, 0);
		GuiImage checkedHorImage = new GuiImage("checkedHorImage", loader, invertHorContainer, null, wrapper.getGuiTexture(GuiUtils.getInvertCheckboxTexStr(Globals.invertHorizontalAxis)), 22, 22, 
				HorReference.PARENT, HorPositions.RIGHT, 5, VerReference.PARENT, VerPositions.TOP, 0, 0f, new Vector3f(0f, 0f, 0f), 1f);
		invertHorCmd = new InvertHorCmd(checkedHorImage);
		invertHorContainer.setCommand(invertHorCmd);
		EventManager.getInstance().registerEventListener(invertHorContainer);
		
		GuiContainer invertVerContainer = new GuiContainer("invertVerContainer", loader, invertContainer, null, wrapper.getGuiTexture("white"), 410, 30,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 5);
		GuiText invertVerAxisText = new GuiText("invertVerAxisText", textDrawer.createTextQuad("Vertical axis"), invertVerContainer, null, 22,
				HorReference.PARENT, HorPositions.LEFT, 5, VerReference.SIBLING, VerPositions.TOP, 0);
		GuiImage checkedVerImage = new GuiImage("checkedVerImage", loader, invertVerContainer, null, wrapper.getGuiTexture(GuiUtils.getInvertCheckboxTexStr(Globals.invertVerticalAxis)), 22, 22, 
				HorReference.PARENT, HorPositions.RIGHT, 5, VerReference.PARENT, VerPositions.TOP, 0, 0f, new Vector3f(0f, 0f, 0f), 1f);
		invertVerCmd = new InvertVerCmd(checkedVerImage);
		invertVerContainer.setCommand(invertVerCmd);
		EventManager.getInstance().registerEventListener(invertVerContainer);
		
		//change resolution
		GuiContainer resContainer = new GuiContainer("resContainer", loader, mainContainer, null, null, 300, 360,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 30);
		GuiText resHeadline = new GuiText("resHeadline", textDrawer.createTextQuad("Change resolution"), resContainer, null, 25,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.TOP, 0);
		
		GuiContainer fullScreenContainer = new GuiContainer("fullScreenContainer", loader, resContainer, null, wrapper.getGuiTexture("white"), 410, 30,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 5);
		GuiText fullScreenAxisText = new GuiText("fullScreenAxisText", textDrawer.createTextQuad("Fullscreen"), fullScreenContainer, null, 22,
				HorReference.PARENT, HorPositions.LEFT, 5, VerReference.SIBLING, VerPositions.TOP, 0);
		GuiImage checkedFullScreenImage = new GuiImage("checkedFullscreenImage", loader, fullScreenContainer, null, wrapper.getGuiTexture(GuiUtils.getCheckboxTexStr(Globals.fullscreen)), 22, 22, 
				HorReference.PARENT, HorPositions.RIGHT, 5, VerReference.PARENT, VerPositions.TOP, 0, 0f, new Vector3f(0f, 0f, 0f), 1f);
		fullScreenCmd = new FullScreenCmd(checkedFullScreenImage);
		fullScreenContainer.setCommand(fullScreenCmd);
		EventManager.getInstance().registerEventListener(fullScreenContainer);
	
		GuiContainer resolutionsContainer = new GuiContainer("fullScreenContainer", loader, resContainer, null, wrapper.getGuiTexture("white"), 410, 260,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 5);
		
		LinkedList<DisplayMode> modes = new LinkedList<>();
		modes.add(new DisplayMode(1024, 768));
		modes.add(new DisplayMode(1280, 720));
		modes.add(new DisplayMode(1280, 960));
		modes.add(new DisplayMode(1440, 900));
		modes.add(new DisplayMode(1600, 900));
		modes.add(new DisplayMode(1600, 1200));
		modes.add(new DisplayMode(1680, 1050));
		modes.add(new DisplayMode(1920, 1080));
		modes.add(new DisplayMode(1920, 1200));
		
		LinkedList<DisplayMode> displayedModes = new LinkedList<>();
		
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		
		for (DisplayMode mode : modes) {
			if (mode.getHeight() != height || mode.getWidth() != width) {
				displayedModes.add(mode);
			}
		}
		
		LinkedList<ResCmd> resCmds = new LinkedList<>();
		
		Command orResolutionCmd = new ResCmd(width, height);
		resCmds.add((ResCmd) orResolutionCmd);
		GuiText orResolutionText = new GuiText("resText0", textDrawer.createTextQuad(str4(width) + "x" + str4(height)), resolutionsContainer, orResolutionCmd, 22,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.TOP, 4);
		EventManager.getInstance().registerEventListener(orResolutionText);
		
		int idx = 1;
		for (DisplayMode mode : displayedModes) {
			Command resolutionCmd = new ResCmd(mode.getWidth(), mode.getHeight());
			resCmds.add((ResCmd) resolutionCmd);
			GuiText resolutionText = new GuiText("resText" + idx, textDrawer.createTextQuad(str4(mode.getWidth()) + "x" + str4(mode.getHeight())), resolutionsContainer, resolutionCmd, 22,
					HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 4);
			EventManager.getInstance().registerEventListener(resolutionText);
			idx++;
		}
		
		resNoteText = new GuiText("resNote", textDrawer.createTextQuad("Application needs to be restarted"), 
				resolutionsContainer, orResolutionCmd, 18, HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 46);
		resNoteText2 = new GuiText("resNote2", textDrawer.createTextQuad("in order for resolution changes to take effect!"), 
				resolutionsContainer, orResolutionCmd, 18, HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 4);
		resNoteText.setTransparency(1f);
		resNoteText2.setTransparency(1f);
		
		for (ResCmd cmd : resCmds) {
			cmd.setNoteText(resNoteText);
			cmd.setNoteText2(resNoteText2);
		}
				
		//back button
		GuiContainer backContainer = new GuiContainer("backButton", loader, mainContainer, backCmd, wrapper.getGuiTexture("white"), 450, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 50);
		GuiText backText = new GuiText("backText", textDrawer.createTextQuad("Back"), backContainer, null, 32,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
		EventManager.getInstance().registerEventListener(backContainer);
		
		((BackCmd) backCmd).setNoteText(resNoteText);
		((BackCmd) backCmd).setNoteText2(resNoteText2);
		
		state.addContainer(mainContainer);
		
		GuiWrapper.getInstance().addState(state);
	}
	
	private String str4(int nbr) {
		String retStr = String.valueOf(nbr);
		if (retStr.length() >= 4) {
			return retStr;
		} else {
			for (int i = retStr.length(); i < 4; i++) {
				retStr = retStr.concat(" ");
			}
			return retStr;
		}
	}

	@Override
	public void resetState() {
		resNoteText.setTransparency(1f);
		resNoteText2.setTransparency(1f);
	}
}
