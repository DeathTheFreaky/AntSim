package at.antSim.eventSystem.events;

import com.sun.istack.internal.NotNull;

/**
 * Created on 24.04.2015.<br />
 * Represents a mouse button released.
 *
 * @author Clemens
 */
public class MouseButtonReleasedEvent extends AbstractMouseButtonEvent {

	public MouseButtonReleasedEvent(@NotNull int button, @NotNull int posX, @NotNull int posY) {
		super(button, posX, posY);
	}

}
