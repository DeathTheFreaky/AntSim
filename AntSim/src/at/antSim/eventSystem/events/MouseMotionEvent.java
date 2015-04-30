package at.antSim.eventSystem.events;

import at.antSim.eventSystem.AbstractEvent;
import at.antSim.eventSystem.Event;

/**
 * Created on 24.04.2015.
 * @author Clemens
 */
public class MouseMotionEvent extends AbstractEvent {

	//Cursorposition befor moved
	final int lastPosX;
	final int lastPosY;

	//Cursorposition after moved
	final int currentPosX;
	final int currentPosY;

	public MouseMotionEvent(int lastPosX, int lastPosY, int currentPosX, int currentPosY) {
		this.lastPosX = lastPosX;
		this.lastPosY = lastPosY;
		this.currentPosX = currentPosX;
		this.currentPosY = currentPosY;
	}

	@Override
	public Class<? extends Event> getType() {
		return MouseMotionEvent.class;
	}

	public int getLastPosX() {
		return lastPosX;
	}

	public int getLastPosY() {
		return lastPosY;
	}

	public int getCurrentPosX() {
		return currentPosX;
	}

	public int getCurrentPosY() {
		return currentPosY;
	}
}
