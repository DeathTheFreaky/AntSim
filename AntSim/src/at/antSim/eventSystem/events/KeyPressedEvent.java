package at.antSim.eventSystem.events;

import at.antSim.eventSystem.Event;
import com.sun.istack.internal.NotNull;

/**
 * Created on 04.05.2015.
 * Represents a pressed Key.
 *
 * @author Clemens
 */
public class KeyPressedEvent extends AbstractKeyEvent {

	public KeyPressedEvent(@NotNull int key, @NotNull char keyCharacter) {
		super(key, keyCharacter);
	}

	@Override
	public Class<? extends Event> getType() {
		return KeyPressedEvent.class;
	}
}
