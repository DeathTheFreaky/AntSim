package at.antSim.objectsPhysic.Movement;

import javax.vecmath.Vector3f;

import at.antSim.Globals;
import at.antSim.objectsKI.Entity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;

/**To be stored in a collection inside MovementManager.
 * 
 * @author Flo
 *
 */
public class MoveToTarget extends MovementMode {

	public ReadOnlyPhysicsObject target;
	Vector3f direction;
	
	public MoveToTarget(DynamicPhysicsObject physicsObject, ReadOnlyPhysicsObject target, float speed) {
		super(MovementModeType.TARGET, physicsObject, speed);
		this.target = target;
		direction = new Vector3f(target.getPosition().x - physicsObject.getPosition().x, 0, target.getPosition().z - physicsObject.getPosition().z);
	}
	
	public void move() {
		
		if (!targetExists()) {
			MovementManager.getInstance().removeLastMovementEntry(physicsObject);
		};
		direction = new Vector3f(target.getPosition().x - physicsObject.getPosition().x, 0, target.getPosition().z - physicsObject.getPosition().z);
		physicsObject.setAlignedMovement(direction, speed);
	}

	private boolean targetExists() {
		if (Entity.getParentEntity((PhysicsObject) target) == null) {
			return false;
		}
		return true;
	}

	@Override
	public Vector3f getDirection() {
		direction = new Vector3f(target.getPosition().x - physicsObject.getPosition().x, 0, target.getPosition().z - physicsObject.getPosition().z);
		return direction;
	}
}
