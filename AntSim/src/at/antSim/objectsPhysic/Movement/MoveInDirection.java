package at.antSim.objectsPhysic.Movement;

import javax.vecmath.Vector3f;

import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;

/**Lets ants move into specific direction, rather than a concrete target.
 * 
 * @author Flo
 *
 */
public class MoveInDirection extends MovementMode {

	Vector3f direction;
	
	public MoveInDirection(DynamicPhysicsObject physicsObject, Vector3f direction, float speed) {
		super(MovementModeType.DIRECTION, physicsObject, speed);
		this.direction = direction;
	}
	
	public void move() {
		physicsObject.setAlignedMovement(direction, speed);
	}

	@Override
	public Vector3f getDirection() {
		return direction;
	}
}
