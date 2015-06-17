package at.antSim.objectsPhysic.Movement;

import javax.vecmath.Vector3f;

import at.antSim.Globals;
import at.antSim.objectsKI.Entity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;

/**To be stored in a collection inside MovementManager.
 * 
 * @author Flo
 *
 */
public class MoveToTarget implements MovementMode {

	DynamicPhysicsObject physicsObject;
	ReadOnlyPhysicsObject target;
	MovementMode mode;
	float speed;
	
	public MoveToTarget(DynamicPhysicsObject physicsObject, ReadOnlyPhysicsObject target, float speed) {
		this.physicsObject = physicsObject;
		this.target = target;
		this.speed = speed;
	}
	
	public void move() {
		Vector3f direction = new Vector3f(target.getPosition().x - physicsObject.getPosition().x, 0, target.getPosition().z - physicsObject.getPosition().z);
		physicsObject.setAlignedMovement(direction, speed);
	}
}
