package at.antSim.eventSystem.events;

import at.antSim.eventSystem.AbstractEvent;
import at.antSim.eventSystem.Event;
import at.antSim.inputHandlers.MouseButtons;
import com.sun.istack.internal.NotNull;

/**
 * Created by Clemens on 24.04.2015.
 */
public class MouseButtonPressedEvent extends AbstractEvent{

	final MouseButtons pressedButton;

	public MouseButtonPressedEvent(@NotNull MouseButtons pressedButton) {
		this.pressedButton = pressedButton;
	}

	@Override
	public Class<? extends Event> getType() {
		return MouseButtonPressedEvent.class;
	}

	public MouseButtons getPressedButton() {
		return pressedButton;
	}
}
