package at.antSim.objectsPhysic.Movement;

import javax.vecmath.Vector3f;

import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;

/**Lets ants move into specific direction, rather than a concrete target.
 * 
 * @author Flo
 *
 */
public class MoveInDirection implements MovementMode {

	DynamicPhysicsObject physicsObject;
	Vector3f direction;
	float speed;
	
	public MoveInDirection(DynamicPhysicsObject physicsObject, Vector3f direction, float speed) {
		this.physicsObject = physicsObject;
		this.direction = direction;
		this.speed = speed;
	}
	
	public void move() {
		physicsObject.setAlignedMovement(direction, speed);
	}
}
