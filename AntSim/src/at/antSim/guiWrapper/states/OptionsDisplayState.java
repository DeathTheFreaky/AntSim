package at.antSim.guiWrapper.states;

import at.antSim.Globals;
import at.antSim.eventSystem.EventManager;
import at.antSim.graphics.graphicsUtils.OpenGLLoader;
import at.antSim.graphics.graphicsUtils.OpenGLTextDrawer;
import at.antSim.guiWrapper.*;
import at.antSim.guiWrapper.commands.*;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.util.LinkedList;

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
	
	public OptionsDisplayState(OpenGLLoader loader, String name) {
		super(loader, name);
	}

	@Override
	public void initializeState(String... args) {
		
		backCmd = new BackCmd(state);
		switchToControlsCmd = new SwitchStateOptionsCmd(args[1], state, this);
		
		GuiContainer mainContainer = new GuiContainer("mainContainer", loader, null, null, wrapper.getGuiTexture("white"),
				Globals.displayWidth, Globals.displayHeight, HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0, new Vector3f(1,1,1), 1);

		OpenGLTextDrawer textDrawer = new OpenGLTextDrawer(loader, wrapper.getGuiTexture("font"));

		// optionsContainer
		GuiContainer optionsContainer = new GuiContainer("optionsContainer", loader, mainContainer, null, wrapper.getGuiTexture("optionsMenu/optionsContainer_display"), 700, 600,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0), 0f);

		// Switching Containers
		GuiContainer switchContainer = new GuiContainer("switchContainer", loader, optionsContainer, null, null, 700, 40,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.TOP, 5, 0f, new Vector3f(1f, 0.5f, 0.5f), 0f);

		// Display Button
		GuiContainer displaySwitchContainer = new GuiContainer("displaySwitchContainer", loader, switchContainer, null,  null, switchContainer.getWidth()/2,
				switchContainer.getHeight(), HorReference.PARENT, HorPositions.LEFT, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0), 0f);
		GuiImage displaySwitchText = new GuiImage("displaySwitch", loader, displaySwitchContainer, null, wrapper.getGuiTexture("optionsMenu/display"), 100, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.BOTTOM, -10, 0f, new Vector3f(0, 0, 0), 0f);

		// Controls Button
		GuiContainer controlSwitchContainer = new GuiContainer("controlSwitchContainer", loader, switchContainer, switchToControlsCmd, null, switchContainer.getWidth()/2,
				switchContainer.getHeight(), HorReference.PARENT, HorPositions.RIGHT, 0, VerReference.SIBLING, VerPositions.TOP, 0, 0f, new Vector3f(0, 0, 0), 0f);
		GuiImage controlsSwitchText = new GuiImage("controlsSwitch", loader, controlSwitchContainer, null, wrapper.getGuiTexture("optionsMenu/controls"), 100, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0), 0f);

		EventManager.getInstance().registerEventListener(controlSwitchContainer);


		// invert mouse
		// Invertion Super Container
		GuiContainer invertContainer = new GuiContainer("invertContainer", loader, optionsContainer, null, null, 400, 105,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 40, 0f, new Vector3f(1, 0, 0), 0.5f);
		GuiImage invertHeadline = new GuiImage("invertHeadline", loader, invertContainer, null, wrapper.getGuiTexture("optionsMenu/invertMouse"), 250, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.TOP, 0, 0f, new Vector3f(0, 0, 0), 0f);

		// Horizontal Axis
		GuiContainer invertHorContainer = new GuiContainer("invertHorContainer", loader, invertContainer, null, null, 400, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 0, 0f, new Vector3f(0.8f, 0.8f, 0.8f), 0.5f);
		GuiImage invertHorAxisText = new GuiImage("invertHorAxisText", loader, invertHorContainer, null, wrapper.getGuiTexture("optionsMenu/horizontal"), 100, 35,
				HorReference.PARENT, HorPositions.LEFT, 5, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0), 0f);
		// Checkbox
		GuiImage checkedHorImage = new GuiImage("checkedHorImage", loader, invertHorContainer, null, wrapper.getGuiTexture(GuiUtils.getInvertCheckboxTexStr(Globals.invertHorizontalAxis)), 22, 22, 
				HorReference.PARENT, HorPositions.RIGHT, 5, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0f, 0f, 0f), 1f);
		invertHorCmd = new InvertHorCmd(checkedHorImage);
		invertHorContainer.setCommand(invertHorCmd);
		EventManager.getInstance().registerEventListener(invertHorContainer);

		// Vertical Axis
		GuiContainer invertVerContainer = new GuiContainer("invertVerContainer", loader, invertContainer, null, null, 400, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 0, 0f, new Vector3f(0.3f, 0.3f, 0.3f), 0.8f);
		GuiImage invertVerAxisText = new GuiImage("invertVerAxisText", loader, invertVerContainer, null, wrapper.getGuiTexture("optionsMenu/vertical"), 100, 35,
				HorReference.PARENT, HorPositions.LEFT, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0), 0f);
		// Checkbox
		GuiImage checkedVerImage = new GuiImage("checkedVerImage", loader, invertVerContainer, null, wrapper.getGuiTexture(GuiUtils.getInvertCheckboxTexStr(Globals.invertVerticalAxis)), 22, 22, 
				HorReference.PARENT, HorPositions.RIGHT, 5, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0f, 0f, 0f), 1f);
		invertVerCmd = new InvertVerCmd(checkedVerImage);
		invertVerContainer.setCommand(invertVerCmd);
		EventManager.getInstance().registerEventListener(invertVerContainer);


		// Change resolution
		GuiContainer resContainer = new GuiContainer("resContainer", loader, optionsContainer, null, null, 300, 360,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 40);
		GuiImage resHeadline = new GuiImage("resHeadline", loader, resContainer, null, wrapper.getGuiTexture("optionsMenu/resolution"), 100, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.TOP, 0, 0f, new Vector3f(0, 0, 0), 0f);

		// FullScreen Container
		GuiContainer fullScreenContainer = new GuiContainer("fullScreenContainer", loader, resContainer, null, null, 400, 30,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 5);
		GuiImage fullScreenText = new GuiImage("fullScreenText", loader, fullScreenContainer, null, wrapper.getGuiTexture("optionsMenu/fullscreen"), 100,35,
				HorReference.PARENT, HorPositions.LEFT, 5, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0), 0f);
		// Checkbox
		GuiImage checkedFullScreenImage = new GuiImage("checkedFullscreenImage", loader, fullScreenContainer, null, wrapper.getGuiTexture(GuiUtils.getCheckboxTexStr(Globals.fullscreen)), 22, 22, 
				HorReference.PARENT, HorPositions.RIGHT, 5, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0f, 0f, 0f), 1f);
		fullScreenCmd = new FullScreenCmd(checkedFullScreenImage);
		fullScreenContainer.setCommand(fullScreenCmd);
		EventManager.getInstance().registerEventListener(fullScreenContainer);

		// Resolutions
		GuiContainer resolutionsContainer = new GuiContainer("fullScreenContainer", loader, resContainer, null, null, 400, 260,
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
		GuiText orResolutionText = new GuiText("resText0", textDrawer.createTextQuad(str4(width) + "x" + str4(height)), resolutionsContainer, orResolutionCmd, 18,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.TOP, 4);
		EventManager.getInstance().registerEventListener(orResolutionText);
		
		int idx = 1;
		for (DisplayMode mode : displayedModes) {
			Command resolutionCmd = new ResCmd(mode.getWidth(), mode.getHeight());
			resCmds.add((ResCmd) resolutionCmd);
			GuiText resolutionText = new GuiText("resText" + idx, textDrawer.createTextQuad(str4(mode.getWidth()) + "x" + str4(mode.getHeight())), resolutionsContainer, resolutionCmd, 18,
					HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 4);
			EventManager.getInstance().registerEventListener(resolutionText);
			idx++;
		}
		
		resNoteText = new GuiText("resNote", textDrawer.createTextQuad("Application needs to be restarted"), 
				resolutionsContainer, orResolutionCmd, 14, HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 10);
		resNoteText2 = new GuiText("resNote2", textDrawer.createTextQuad("in order for resolution changes to take effect!"), 
				resolutionsContainer, orResolutionCmd, 14, HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 4);
		resNoteText.setTransparency(1f);
		resNoteText2.setTransparency(1f);
		
		for (ResCmd cmd : resCmds) {
			cmd.setNoteText(resNoteText);
			cmd.setNoteText2(resNoteText2);
		}
				
		// back button
		GuiContainer backContainer = new GuiContainer("backButton", loader, optionsContainer, backCmd, null, 400, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.BOTTOM, 10);
		GuiImage backText = new GuiImage("backText", loader, backContainer, null, wrapper.getGuiTexture("optionsMenu/back"), 100, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0), 0f);
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
