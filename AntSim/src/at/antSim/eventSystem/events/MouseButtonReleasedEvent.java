package at.antSim.eventSystem.events;

/**
 * Created on 24.04.2015.<br />
 * Represents a mouse button released.
 *
 * @author Clemens
 */
public class MouseButtonReleasedEvent extends AbstractMouseButtonEvent {

	public MouseButtonReleasedEvent(int button, int posX, int posY) {
		super(button, posX, posY);
	}

}
