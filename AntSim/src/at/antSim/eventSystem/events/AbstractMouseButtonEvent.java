package at.antSim.eventSystem.events;

import com.sun.istack.internal.NotNull;

/**
 * Created on 05.05.2015.<br />
 * Represents an interaction with the mouse button.
 *
 * @author Clemens
 */
public abstract class AbstractMouseButtonEvent extends AbstractMouseEvent{

	final int button;

	protected AbstractMouseButtonEvent(@NotNull int button, @NotNull int posX, @NotNull int posY) {
		super(posX, posY);
		this.button = button;
	}

	/**
	 * @return Returns the int representation of the pressed mouse button.
	 */
	public int getButton() {
		return button;
	}
}
