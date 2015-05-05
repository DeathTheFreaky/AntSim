package at.antSim.eventSystem.events;

import at.antSim.eventSystem.Event;
import com.sun.istack.internal.NotNull;

/**
 * Created on 24.04.2015.<br />
 * Represents a mouse button pressed.
 *
 * @author Clemens
 */
public class MouseButtonPressedEvent extends AbstractMouseButtonEvent {

	public MouseButtonPressedEvent(@NotNull int button, @NotNull int posX, @NotNull int posY) {
		super(button, posX, posY);
	}

	@Override
	public Class<? extends Event> getType() {
		return MouseButtonPressedEvent.class;
	}
}
