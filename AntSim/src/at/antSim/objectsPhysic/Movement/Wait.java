package at.antSim.objectsPhysic.Movement;

import javax.vecmath.Vector3f;

import at.antSim.objectsKI.Entity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;

/**Wait at your current position.
 * 
 * @author Flo
 *
 */
public class Wait extends MovementMode {
	
	ReadOnlyPhysicsObject target;
	Vector3f direction;
	Vector3f position;
	
	int limit;
	
	public Wait(DynamicPhysicsObject physicsObject, ReadOnlyPhysicsObject target, float speed) {
		this(physicsObject, target, speed, -1);
	}

	public Wait(DynamicPhysicsObject physicsObject, ReadOnlyPhysicsObject target, float speed, int limit) {
		super(MovementModeType.WAIT, physicsObject, speed);
		
		this.target = target;
		this.limit = limit;
		position = physicsObject.getPosition();
		direction = new Vector3f(position.x - physicsObject.getPosition().x, 0, position.z - physicsObject.getPosition().z);
	}

	@Override
	public void move() {
		if (target != null && !targetExists()) {
			MovementManager.getInstance().topDeleteables.add(physicsObject);
			return;
		};
		
		direction = new Vector3f(position.x - physicsObject.getPosition().x, 0, position.z - physicsObject.getPosition().z);
		physicsObject.setMovement(direction, speed);
		
		if (limit > 0) {
			limit--;
		} else if (limit == 0){
			MovementManager.getInstance().topDeleteables.add(physicsObject);
			return;
		}
	}
	
	private boolean targetExists() {
		if (Entity.getParentEntity((PhysicsObject) target) == null) {
//			System.out.println("target ceased to exist in wait");
			return false;
		}
		return true;
	}

	@Override
	public Vector3f getDirection() {
		return direction;
	}
}
