package at.antSim.objectsPhysic.Movement;

import javax.vecmath.Vector3f;

import at.antSim.objectsPhysic.DynamicPhysicsObject;

/**Wait at your current position.
 * 
 * @author Flo
 *
 */
public class Wait extends MovementMode {
	
	Vector3f direction;
	Vector3f position;
	
	int limit;
	
	public Wait(DynamicPhysicsObject physicsObject, float speed) {
		this(physicsObject, speed, -1);
	}

	public Wait(DynamicPhysicsObject physicsObject, float speed, int limit) {
		super(MovementModeType.WAIT, physicsObject, speed);
		
		this.limit = limit;
		position = physicsObject.getPosition();
		direction = new Vector3f(position.x - physicsObject.getPosition().x, 0, position.z - physicsObject.getPosition().z);
	}

	@Override
	public void move() {
		
		direction = new Vector3f(position.x - physicsObject.getPosition().x, 0, position.z - physicsObject.getPosition().z);
		physicsObject.setMovement(direction, speed);
		
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
