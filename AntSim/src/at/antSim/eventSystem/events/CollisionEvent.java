/**
 * 
 */
package at.antSim.eventSystem.events;

import at.antSim.eventSystem.AbstractEvent;
import at.antSim.objectsPhysic.basics.PhysicsObject;

/**
 * 
 * Collision Events:
 * 	- Ant colliding with Ghostobject
 *  - Ant colliding with obstacle
 *  - Ant colliding with Food
 *  - Ant colliding with Enemy

 *
 * @author Martin
 *
 */
public class CollisionEvent extends AbstractEvent {
	
	private PhysicsObject po1;
	private PhysicsObject po2;

	public CollisionEvent(PhysicsObject po1, PhysicsObject po2){
		this.po1 = po1;
		this.po2 = po2;
	}

	
	
	public PhysicsObject getPo1() {
		return po1;
	}

	public void setPo1(PhysicsObject po1) {
		this.po1 = po1;
	}

	public PhysicsObject getPo2() {
		return po2;
	}

	public void setPo2(PhysicsObject po2) {
		this.po2 = po2;
	}
	
}
