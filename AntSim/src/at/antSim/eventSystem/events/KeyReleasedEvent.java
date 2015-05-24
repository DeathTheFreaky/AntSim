package at.antSim.eventSystem.events;

/**
 * Created on 04.05.2015.
 * Represents a released Key.
 *
 * @author Clemens
 */
public class KeyReleasedEvent extends AbstractKeyEvent {

	public KeyReleasedEvent(int key, char keyCharacter) {
		super(key, keyCharacter);
	}

}
