package at.antSim.eventSystem.events;

import at.antSim.eventSystem.AbstractEvent;
import at.antSim.objectsPhysic.basics.PhysicsObject;

/**
 * Created on 26.05.2015.
 *
 * @author Clemens
 */
public class CollisionEvent extends AbstractEvent {

	private final PhysicsObject phyObj1;
	private final PhysicsObject phyObj2;

	public CollisionEvent(PhysicsObject phyObj1, PhysicsObject phyObj2) {
		this.phyObj1 = phyObj1;
		this.phyObj2 = phyObj2;
	}

	public PhysicsObject getPhyObj1() {
		return phyObj1;
	}

	public PhysicsObject getPhyObj2() {
		return phyObj2;
	}
}
