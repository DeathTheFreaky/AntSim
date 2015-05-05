package at.antSim.eventSystem.events;

import at.antSim.eventSystem.AbstractEvent;
import com.sun.istack.internal.NotNull;

/**
 * Created on 04.05.2015.<br />
 * Represents an interaction with the Mouse.
 *
 * @author Clemens
 */
public abstract class AbstractMouseEvent extends AbstractEvent{

	//Cursor position
	final int posX;
	final int posY;

	protected AbstractMouseEvent(@NotNull int posX, @NotNull int posY) {
		this.posX = posX;
		this.posY = posY;
	}

	/**
	 * @return returns the X-position of the Mouse as the Event triggered.
	 */
	public int getPosX() {
		return posX;
	}

	/**
	 * @return returns the Y-position of the Mouse as the Event triggered.
	 */
	public int getPosY() {
		return posY;
	}
}
