package at.antSim.eventSystem.events;

import javax.vecmath.Vector3f;

import at.antSim.eventSystem.AbstractEvent;
import at.antSim.objectsKI.Ant;
import at.antSim.objectsKI.PositionLocator;

public class LocatorLockEvent extends AbstractEvent {
	
	Vector3f velocity;
	Ant ant;
	PositionLocator locator;
	
	public LocatorLockEvent(Vector3f velocity, Ant ant, PositionLocator locator) {
		this.velocity = velocity;
		this.ant = ant;
		this.locator = locator;
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public Ant getAnt() {
		return ant;
	}

	public PositionLocator getLocator() {
		return locator;
	}
}
