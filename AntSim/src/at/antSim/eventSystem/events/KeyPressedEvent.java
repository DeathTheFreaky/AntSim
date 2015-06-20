package at.antSim.eventSystem.events;

/**
 * Created on 04.05.2015.
 * Represents a pressed Key.
 *
 * @author Clemens
 */
public class KeyPressedEvent extends AbstractKeyEvent {

	public KeyPressedEvent(int key, char keyCharacter) {
		super(key, keyCharacter);
	}

}
