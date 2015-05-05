package at.antSim.eventSystem.events;

import at.antSim.eventSystem.AbstractEvent;
import com.sun.istack.internal.NotNull;

/**
 * Created on 04.05.2015.<br />
 * Represents an interaction with a Key.
 *
 * @author Clemens
 */
public abstract class AbstractKeyEvent extends AbstractEvent {

	final int key;
	final char keyCharacter;


	protected AbstractKeyEvent(@NotNull int key, @NotNull char keyCharacter) {
		this.key = key;
		this.keyCharacter = keyCharacter;
	}

	/**
	 * @return Returns the int representation of the pressed Key.<br />
	 * This this value is not effected by different Keyboard-Layouts. If you want the character pressed on the keyboard according to the Layout you should use {@link #getKeyCharacter() getKeyCharacter}.
	 */
	public int getKey() {
		return key;
	}

	/**
	 * @return Returns the char representation of the pressed Key.
	 */
	public char getKeyCharacter() {
		return keyCharacter;
	}
}
