package at.antSim.objectsPhysic.Movement;

import javax.vecmath.Vector3f;

import at.antSim.objectsPhysic.DynamicPhysicsObject;

public class BasicMovement extends MovementMode {

	Vector3f direction;
	int dirChangeBlocker;
	int blockingRange = 10;
	
	public BasicMovement(DynamicPhysicsObject physicsObject, Vector3f direction, float speed) {
		super(MovementModeType.BASIC, physicsObject, speed);
		this.direction = direction;
		dirChangeBlocker = 0;
	}
	
	public void move() {
		physicsObject.setAlignedMovement(direction, speed);
		if (dirChangeBlocker > 0) {
			dirChangeBlocker--;
		}
	}

	@Override
	public Vector3f getDirection() {
		return direction;
	}

	public void setDirection(Vector3f turnDirectionVector) {
		if (dirChangeBlocker <= 0) {
			this.direction = turnDirectionVector;
			dirChangeBlocker = blockingRange;
		}
	}
}
