package at.antSim.objectsPhysic.Movement;

import javax.vecmath.Vector3f;

import at.antSim.objectsPhysic.DynamicPhysicsObject;

/**Lets ants move into specific direction, rather than a concrete target.
 * 
 * @author Flo
 *
 */
public class MoveInDirection extends MovementMode {

	Vector3f direction;
	int limit = -1;
	
	public MoveInDirection(DynamicPhysicsObject physicsObject, Vector3f direction, float speed) {
		super(MovementModeType.DIRECTION, physicsObject, speed);
		this.direction = direction;
if (this.direction == null) {
		System.err.println("direction was null in constructor");	
		}
	}
	
	public MoveInDirection(DynamicPhysicsObject physicsObject, Vector3f direction, float speed, int limit) {
		super(MovementModeType.DIRECTION, physicsObject, speed);
		this.direction = direction;
		this.limit = limit;
if (this.direction == null) {
	System.err.println("direction was null in constructor");
		}
	}
	
	public void move() {
		physicsObject.setAlignedMovement(direction, speed);
		
		if (limit > 0) {
			limit--;
		} else if (limit == 0){
			MovementManager.getInstance().removeLastMovementEntry(physicsObject);
		}
	}

	@Override
	public Vector3f getDirection() {
		return direction;
	}
}
