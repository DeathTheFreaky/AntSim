package at.antSim.objectsPhysic.Movement;

import javax.vecmath.Vector3f;

import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.utils.Maths;

public class BorderCollisionMovement extends MovementMode {
	
	int limit;
	Vector3f originalDirection;
	Vector3f direction;

	public BorderCollisionMovement(DynamicPhysicsObject physicsObject, float speed) {
		this(physicsObject, speed, -1);
	}
	
	public BorderCollisionMovement(DynamicPhysicsObject physicsObject, float speed, int limit) {
		super(MovementModeType.BORDER, physicsObject, speed);
		this.limit = 10;
		MovementMode topMode = MovementManager.getInstance().getTopMovementMode(physicsObject);
		if (topMode != null) {
			direction = Maths.turnDirectionVector(topMode.getDirection(), 125); //dont use 135 -> if hitting two walls will always move along walls
			direction.y = 0;
		} else {
			direction = new Vector3f(1, 0, 1);
		}
	}

	@Override
	public void move() {
		
		physicsObject.setAlignedMovement(direction, speed);
		if (limit > 0) {
			limit--;
		} else if (limit == 0){
			MovementManager.getInstance().removeLastMovementEntry(physicsObject);
		}
	}
	
	public void setOriginalDirection(Vector3f originalDirection) {
		this.originalDirection = originalDirection;
	}

	@Override
	public Vector3f getDirection() {
		return direction;	
	}
}
