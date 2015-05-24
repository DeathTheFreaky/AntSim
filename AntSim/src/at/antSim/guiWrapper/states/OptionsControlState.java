package at.antSim.guiWrapper.states;

import java.util.LinkedList;
import java.util.List;

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
import at.antSim.guiWrapper.commands.BackCmd;
import at.antSim.guiWrapper.commands.Command;
import at.antSim.guiWrapper.commands.KeyChangeCmd;
import at.antSim.guiWrapper.commands.SwitchStateOptionsCmd;

public class OptionsControlState extends AbstractGuiState {
	
	Command backCmd;
	Command switchToDisplayCmd;
	
	GuiContainer waitingForKeyContainer;		
	GuiText waitingForKeyText;
	GuiText errText;

	public OptionsControlState(Loader loader, String name) {
		super(loader, name);
	}

	@Override
	public void initializeState(String... args) {
		
		backCmd = new BackCmd(state);
		switchToDisplayCmd = new SwitchStateOptionsCmd(args[1], state, this);
		
		GuiContainer mainContainer = new GuiContainer("mainContainer", loader, null, null, wrapper.getGuiTexture("white"),
				Globals.displayWidth, Globals.displayHeight, HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0, new Vector3f(1,1,1), 1);

		OpenGLTextDrawer textDrawer = new OpenGLTextDrawer(loader, wrapper.getGuiTexture("font"));
		
		//switch between display and control options
		GuiContainer switchContainer = new GuiContainer("switchContainer", loader, mainContainer, null,  wrapper.getGuiTexture("white"), 410, 39,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.TOP, 30, 0f, new Vector3f(1, 0, 0), 0.5f);
		GuiContainer displaySwitchContainer = new GuiContainer("displaySwitchContainer", loader, switchContainer, switchToDisplayCmd,  wrapper.getGuiTexture("white"), switchContainer.getWidth()/2, 
				switchContainer.getHeight(), HorReference.PARENT, HorPositions.LEFT, 0, VerReference.SIBLING, VerPositions.TOP, 0, 0f, new Vector3f(0, 0, 1), 0.5f);
		GuiText displaySwitchText = new GuiText("displaySwitch", textDrawer.createTextQuad("Display"), displaySwitchContainer, null, 25,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
		GuiContainer controlSwitchContainer = new GuiContainer("controlSwitchContainer", loader, switchContainer, null,  wrapper.getGuiTexture("white"), switchContainer.getWidth()/2, 
				switchContainer.getHeight(), HorReference.PARENT, HorPositions.RIGHT, 0, VerReference.SIBLING, VerPositions.TOP, 0, 0f, new Vector3f(0, 1, 0), 0.5f);
		GuiText controlSwitchText = new GuiText("controlSwitch", textDrawer.createTextQuad("Controls"), controlSwitchContainer, null, 25,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
		EventManager.getInstance().registerEventListener(displaySwitchContainer);
		
		//keybindings
		GuiContainer keysContainer = new GuiContainer("keysContainer", loader, mainContainer, null,  wrapper.getGuiTexture("white"), 720, 455,
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
		
		int idx = 0;
		for (KeyNameBindingWrapper keyWrapper : bindings) {
			
			int textSize = 22;
			
			GuiContainer keyRowContainer;
			if (idx == 0) {
				keyRowContainer = new GuiContainer("keyRowContainer", loader, mainContainer, null,  wrapper.getGuiTexture("white"), 720, 35,
						HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.TOP, 0, 0f, new Vector3f(0, 0, 0), 0.5f);
			} else {
				keyRowContainer = new GuiContainer("keyRowContainer", loader, mainContainer, null,  wrapper.getGuiTexture("white"), 720, 35,
						HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 0, 0f, new Vector3f(0, 0, 0), 0.5f);
			}
			GuiContainer keyLabelContainer = new GuiContainer("keyLabelContainer", loader, keyRowContainer, switchToDisplayCmd,  wrapper.getGuiTexture("white"), keyRowContainer.getWidth()/2, 
					keyRowContainer.getHeight(), HorReference.PARENT, HorPositions.LEFT, 0, VerReference.SIBLING, VerPositions.TOP, 0, 0f, new Vector3f(0, 0, 1), 0.5f);
			GuiText keyLabelText = new GuiText("keyLabelText" + keyWrapper.getLabel(), textDrawer.createTextQuad(keyWrapper.getLabel()), keyLabelContainer, null, textSize,
					HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 5);
			GuiContainer keyNameContainer = new GuiContainer("keyNameContainer", loader, keyRowContainer, null,  wrapper.getGuiTexture("white"), keyRowContainer.getWidth()/2, 
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
				mainContainer, null, 18, HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 35);
		errText.setTransparency(1f);
		
		//back button
		GuiContainer backContainer = new GuiContainer("backButton", loader, mainContainer, backCmd, wrapper.getGuiTexture("white"), 450, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 35);
		GuiText backText = new GuiText("backText", textDrawer.createTextQuad("Back"), backContainer, null, 32,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
		EventManager.getInstance().registerEventListener(backContainer);
		
		//listening for keybinding overlay
		waitingForKeyContainer = new GuiContainer("waitingForKeyContainer", loader, mainContainer, null,  wrapper.getGuiTexture("white"), 630, 200,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(1, 0, 0), 0.5f);		
		waitingForKeyText = new GuiText("waitingForKeyText", textDrawer.createTextQuad("Waiting for key input..."), waitingForKeyContainer, null, 25,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
		waitingForKeyContainer.setTransparency(1f);
		waitingForKeyText.setTransparency(1f);
		
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
