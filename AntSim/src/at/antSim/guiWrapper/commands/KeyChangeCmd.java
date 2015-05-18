package at.antSim.guiWrapper.commands;

import java.lang.reflect.Field;

import org.lwjgl.input.Keyboard;

import at.antSim.Globals;
import at.antSim.config.ConfigWriter;
import at.antSim.eventSystem.EventListener;
import at.antSim.eventSystem.EventManager;
import at.antSim.eventSystem.EventPriority;
import at.antSim.eventSystem.events.KeyPressedEvent;
import at.antSim.eventSystem.events.KeyReleasedEvent;
import at.antSim.graphics.graphicsUtils.OpenGLTextDrawer;
import at.antSim.guiWrapper.GuiContainer;
import at.antSim.guiWrapper.GuiText;
import at.antSim.guiWrapper.HorPositions;
import at.antSim.guiWrapper.HorReference;
import at.antSim.guiWrapper.VerPositions;
import at.antSim.guiWrapper.VerReference;
import at.antSim.guiWrapper.states.OptionsControlState;

/**Used for changing key bindings in options gui.
 * 
 * @author Flo
 *
 */
public class KeyChangeCmd implements Command {
	
	GuiContainer parent;
	OptionsControlState state;
	String fieldname;
	Field field;
	OpenGLTextDrawer textDrawer;
	String label;
	int size;

	/**Creates a new {@link KeyChangeCmd}.
	 * 
	 * @param parent - parent of GuiText to be added for the changed key name
	 * @param state - {@link OptionsControlState} which handles the showing and hiding of the "waiting for key input" window
	 * @param fieldname - name of key binding field in Globals class
	 * @param textDrawer - {@link OpenGLTextDrawer} to be used for creating the new GuiText (of the changed key)
	 * @param label - name of the key functionality to be changed
	 * @param size - desired size of new {@link GuiText}
	 */
	public KeyChangeCmd(GuiContainer parent, OptionsControlState state, String fieldname, OpenGLTextDrawer textDrawer, String label, int size) {
		this.parent = parent;
		this.state = state;
		this.fieldname = fieldname;
		this.textDrawer = textDrawer;
		this.label = label;
		this.size = size;
		try {
			field = Globals.class.getField(fieldname);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute() {
		state.showWaitingWindow();
		state.hideErrMessage();
		EventManager.getInstance().registerEventListener(this);
	}
	
	/**Changes the keybinding in Globals and the GuiText rendering in options menu.<br>
	 * Does not change anything if the ESCAPE key was pressed.
	 * 
	 * @param event
	 */
	@EventListener (priority = EventPriority.HIGH)
	public void onKeyPress(KeyReleasedEvent event) {
		if (!(event.getKey() == Keyboard.KEY_ESCAPE)) {
			if (Globals.isKeyAlreadyUsed(fieldname, event.getKey())) {
				state.showErrMessage();
			} else {
				try {
					field.setInt(null, event.getKey());
					parent.removeChildren();
					GuiText keyNameValue = new GuiText("keyNameValue" + label, textDrawer.createTextQuad(Keyboard.getKeyName(event.getKey())), parent, null, size,
							HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
					parent.addChild(keyNameValue);
					ConfigWriter.writeConfig();
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		state.hideWaitingWindow();
		event.consume();
		EventManager.getInstance().unregisterEventListener(this);
	}
	
	/**Consumes a key press event when key binding change is active.
	 * @param event
	 */
	@EventListener (priority = EventPriority.HIGH)
	public void consumeKeyPress(KeyPressedEvent event) {
		event.consume();
	}
}
