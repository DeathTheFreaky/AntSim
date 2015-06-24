package at.antSim.objectsPhysic.Movement;

import javax.vecmath.Vector3f;

import at.antSim.objectsKI.Entity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;

public class MoveToHive extends MovementMode {

	public ReadOnlyPhysicsObject origin;
	public ReadOnlyPhysicsObject target;
	Vector3f direction;
	
	public MoveToHive(DynamicPhysicsObject physicsObject, ReadOnlyPhysicsObject target, ReadOnlyPhysicsObject origin, float speed) {
		super(MovementModeType.HIVE, physicsObject, speed);
		this.target = target;
		this.origin = origin;
		direction = new Vector3f(target.getPosition().x - physicsObject.getPosition().x, 0, target.getPosition().z - physicsObject.getPosition().z);
	}
	
	public void move() {
		if (!targetExists()) {
			MovementManager.getInstance().topDeleteables.add(physicsObject);
			return;
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
	
	public ReadOnlyPhysicsObject getOrigin() {
		Entity ret = Entity.getParentEntity((PhysicsObject) origin);
		if (ret != null) {
			return (ReadOnlyPhysicsObject) ret.getPhysicsObject();
		} else {
			return null;
		}
	}

	@Override
	public Vector3f getDirection() {
		direction = new Vector3f(target.getPosition().x - physicsObject.getPosition().x, 0, target.getPosition().z - physicsObject.getPosition().z);
		return direction;
	}
}
