package at.antSim.guiWrapper.states;

import at.antSim.Globals;
import at.antSim.eventSystem.EventManager;
import at.antSim.graphics.graphicsUtils.OpenGLLoader;
import at.antSim.graphics.graphicsUtils.OpenGLTextDrawer;
import at.antSim.guiWrapper.*;
import at.antSim.guiWrapper.commands.BackCmd;
import at.antSim.guiWrapper.commands.Command;
import at.antSim.guiWrapper.commands.KeyChangeCmd;
import at.antSim.guiWrapper.commands.SwitchStateOptionsCmd;
import org.lwjgl.util.vector.Vector3f;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Alexander
 *
 */
public class OptionsControlState extends AbstractGuiState {
	
	Command backCmd;
	Command switchToDisplayCmd;
	
	GuiContainer waitingForKeyContainer;
	GuiImage waitingForKeyText;
	GuiText errText;

	public OptionsControlState(OpenGLLoader loader, String name) {
		super(loader, name);
	}

	@Override
	public void initializeState(String... args) {
		
		backCmd = new BackCmd(state);
		switchToDisplayCmd = new SwitchStateOptionsCmd(args[1], state, this);
		
		GuiContainer mainContainer = new GuiContainer("mainContainer", loader, null, null, wrapper.getGuiTexture("background"),
				Globals.displayWidth, Globals.displayHeight, HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(1,1,1), 0f);

		OpenGLTextDrawer textDrawer = new OpenGLTextDrawer(loader, wrapper.getGuiTexture("font"));

		// Options Container
		GuiContainer optionsContainer = new GuiContainer("optionsContainer", loader, mainContainer, null, wrapper.getGuiTexture("optionsMenu/optionsContainer_controls"), 700, 600,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0.3f, new Vector3f(1, 1, 1), 0f);
		GuiImage startMenuContainerBorder = new GuiImage("startMenuContainerBorder", loader, optionsContainer, null, wrapper.getGuiTexture("optionsMenu/optionsContainer_controlsBorder"), 700, 600,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0), 0f);
		
		// switch containers
		GuiContainer switchContainer = new GuiContainer("switchContainer", loader, optionsContainer, null,  null, 700, 40,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.TOP, 5, 0f, new Vector3f(1, 0, 0), 0.5f);

		// Display Button
		GuiContainer displaySwitchContainer = new GuiContainer("displaySwitchContainer", loader, switchContainer, switchToDisplayCmd,  null, switchContainer.getWidth()/2,
				switchContainer.getHeight(), HorReference.PARENT, HorPositions.LEFT, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 1), 0.5f);
		GuiImage displaySwitchText = new GuiImage("displaySwitch", loader, displaySwitchContainer, null, wrapper.getGuiTexture("optionsMenu/display"), 100, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0), 0f);

		// Controls Button
		GuiContainer controlSwitchContainer = new GuiContainer("controlSwitchContainer", loader, switchContainer, null,  null, switchContainer.getWidth()/2,
				switchContainer.getHeight(), HorReference.PARENT, HorPositions.RIGHT, 0, VerReference.SIBLING, VerPositions.TOP, 0, 0f, new Vector3f(0, 1, 0), 0.5f);
		GuiImage controlsSwitchText = new GuiImage("controlsSwitch", loader, controlSwitchContainer, null, wrapper.getGuiTexture("optionsMenu/controls"), 100, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.BOTTOM, -10, 0f, new Vector3f(0, 0, 0), 0f);

		EventManager.getInstance().registerEventListener(displaySwitchContainer);
		
		// Key Bindings
		// Key container
		GuiContainer keysContainer = new GuiContainer("keysContainer", loader, optionsContainer, null,  null, 700, 455,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 30, 0f, new Vector3f(1, 0, 0), 0.5f);
		
		List<KeyNameBindingWrapper> bindings = new LinkedList<>();
		
		bindings.add(new KeyNameBindingWrapper("Move Forward", "moveForwardKey"));
		bindings.add(new KeyNameBindingWrapper("Move Backward", "moveBackwardKey"));
		bindings.add(new KeyNameBindingWrapper("Move Left", "moveLeftKey"));
		bindings.add(new KeyNameBindingWrapper("Move Right", "moveRightKey"));
		bindings.add(new KeyNameBindingWrapper("Move Up", "moveUpKey"));
		bindings.add(new KeyNameBindingWrapper("Move Down", "moveDownKey"));
		bindings.add(new KeyNameBindingWrapper("Turn Left", "turnLeftKey"));
		bindings.add(new KeyNameBindingWrapper("Turn Right", "turnRightKey"));
		bindings.add(new KeyNameBindingWrapper("Tilt Down", "tiltDownKey"));
		bindings.add(new KeyNameBindingWrapper("Tilt Up", "tiltUpKey"));
		bindings.add(new KeyNameBindingWrapper("Zoom In", "zoomInKey"));
		bindings.add(new KeyNameBindingWrapper("Zoom Out", "zoomOutKey"));
		bindings.add(new KeyNameBindingWrapper("Restore Position", "restoreCameraPosition"));
		bindings.add(new KeyNameBindingWrapper("Show Ghost Spheres", "showGhostSpheres"));
		bindings.add(new KeyNameBindingWrapper("Turn Sorting on/off", "disableSorting"));
		
		int idx = 0;
		for (KeyNameBindingWrapper keyWrapper : bindings) {
			
			int textSize = 18;
			
			GuiContainer keyRowContainer;
			if (idx == 0) {
				keyRowContainer = new GuiContainer("keyRowContainer", loader, optionsContainer, null, null, 690, 30,
						HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.TOP, 0, 0f, new Vector3f(0, 0, 0), 0.5f);
			} else {
				keyRowContainer = new GuiContainer("keyRowContainer", loader, optionsContainer, null,  null, 690, 30,
						HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 0, 0f, new Vector3f(0, 0, 0), 0.5f);
			}
			GuiContainer keyLabelContainer = new GuiContainer("keyLabelContainer", loader, keyRowContainer, switchToDisplayCmd,  null, keyRowContainer.getWidth()/2,
					keyRowContainer.getHeight(), HorReference.PARENT, HorPositions.LEFT, 0, VerReference.SIBLING, VerPositions.TOP, 0, 0f, new Vector3f(0, 0, 1), 0.5f);
			GuiText keyLabelText = new GuiText("keyLabelText" + keyWrapper.getLabel(), textDrawer.createTextQuad(keyWrapper.getLabel()), keyLabelContainer, null, textSize,
					HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 5);
			GuiContainer keyNameContainer = new GuiContainer("keyNameContainer", loader, keyRowContainer, null,  null, keyRowContainer.getWidth()/2,
					keyRowContainer.getHeight(), HorReference.PARENT, HorPositions.RIGHT, 0, VerReference.SIBLING, VerPositions.TOP, 0, 0f, new Vector3f(0, 1, 0), 0.5f);
			GuiText keyNameValue = new GuiText("keyNameValue" + keyWrapper.getLabel(), textDrawer.createTextQuad(keyWrapper.getBindingName()), keyNameContainer, null, textSize,
					HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
			
			keyWrapper.setGuiText(keyNameValue);
			
			KeyChangeCmd keyChangeCmd = new KeyChangeCmd(keyNameContainer, this, keyWrapper.getFieldName(), textDrawer, keyWrapper.getLabel(), textSize);
			keyRowContainer.setCommand(keyChangeCmd);
			
			EventManager.getInstance().registerEventListener(keyRowContainer);
			
			idx++;
		}
		
		//error message
		errText = new GuiText("resNote", textDrawer.createTextQuad("Key is used by another binding!"), 
				optionsContainer, null, 14, HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 10);
		errText.setTransparency(1f);
		
		//back button
		GuiContainer backContainer = new GuiContainer("backButton", loader, optionsContainer, backCmd, null, 400, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.BOTTOM, 10);
		GuiImage backText = new GuiImage("backText", loader, backContainer, null, wrapper.getGuiTexture("optionsMenu/back"), 100, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0), 0f);
		EventManager.getInstance().registerEventListener(backContainer);
		
		//listening for keybinding overlay
		waitingForKeyContainer = new GuiContainer("waitingForKeyContainer", loader, optionsContainer, null,  wrapper.getGuiTexture("optionsMenu/waitingForKeyContainer"), 400, 160,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(1, 0, 0), 0f);
		waitingForKeyText = new GuiImage("waitingForKeyInputText", loader, waitingForKeyContainer, null, wrapper.getGuiTexture("optionsMenu/waitingForKeyInput"), 220, 50,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0), 0f);
		waitingForKeyContainer.setTransparency(1f);
		waitingForKeyText.setTransparency(1f);

		// Foreground-Ant
		GuiImage antForeground = new GuiImage("antForeground", loader, mainContainer, null, wrapper.getGuiTexture("ant_foreground"), 440, 360,
				HorReference.PARENT, HorPositions.RIGHT, 0, VerReference.PARENT, VerPositions.BOTTOM, -5, 0f, new Vector3f(0, 0, 0), 0f);
		
		state.addContainer(mainContainer);
		
		GuiWrapper.getInstance().addState(state);
	}
	
	public void hideWaitingWindow() {
		waitingForKeyContainer.setTransparency(1f);
		waitingForKeyText.setTransparency(1f);
	}
	
	public void showWaitingWindow() {
		waitingForKeyContainer.setTransparency(0f);
		waitingForKeyText.setTransparency(0f);
	}
	
	public void showErrMessage() {
		errText.setTransparency(0f);
	}
	
	public void hideErrMessage() {
		errText.setTransparency(1f);
	}

	@Override
	public void resetState() {
		hideErrMessage();
		hideWaitingWindow();
	}
}