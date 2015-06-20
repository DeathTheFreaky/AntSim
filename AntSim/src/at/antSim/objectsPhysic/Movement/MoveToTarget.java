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
public class MoveToTarget extends MovementMode {

	ReadOnlyPhysicsObject target;
	Vector3f direction;
	
	public MoveToTarget(DynamicPhysicsObject physicsObject, ReadOnlyPhysicsObject target, float speed) {
		super(MovementModeType.TARGET, physicsObject, speed);
		this.target = target;
		direction = new Vector3f(target.getPosition().x - physicsObject.getPosition().x, 0, target.getPosition().z - physicsObject.getPosition().z);
	}
	
	public void move() {
		direction = new Vector3f(target.getPosition().x - physicsObject.getPosition().x, 0, target.getPosition().z - physicsObject.getPosition().z);
		physicsObject.setAlignedMovement(direction, speed);
	}

	@Override
	public Vector3f getDirection() {
		return direction;
	}
}
