package at.antSim.eventSystem.events;

import at.antSim.eventSystem.AbstractEvent;
import at.antSim.eventSystem.Event;
import at.antSim.inputHandlers.MouseButtons;
import com.sun.istack.internal.NotNull;

/**
 * Created by Clemens on 24.04.2015.
 */
public class MouseButtonReleasedEvent extends AbstractEvent {

	final MouseButtons pressedButton;

	final int x;
	final int y;

	public MouseButtonReleasedEvent(@NotNull MouseButtons pressedButton, int x, int y) {
		this.pressedButton = pressedButton;
		this.x = x;
		this.y = y;
	}

	@Override
	public Class<? extends Event> getType() {
		return MouseButtonReleasedEvent.class;
	}

	public MouseButtons getPressedButton() {
		return pressedButton;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
