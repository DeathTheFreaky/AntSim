package at.antSim.eventSystem.events;

/**
 * Created on 24.04.2015.<br />
 * Represents a mouse button pressed.
 *
 * @author Clemens
 */
public class MouseButtonPressedEvent extends AbstractMouseButtonEvent {

	public MouseButtonPressedEvent(int button, int posX, int posY) {
		super(button, posX, posY);
	}

}
