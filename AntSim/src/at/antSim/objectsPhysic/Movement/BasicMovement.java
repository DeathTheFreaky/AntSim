package at.antSim.objectsPhysic.Movement;

import javax.vecmath.Vector3f;

import at.antSim.objectsPhysic.DynamicPhysicsObject;

public class BasicMovement extends MovementMode {

	Vector3f direction;
	
	public BasicMovement(DynamicPhysicsObject physicsObject, Vector3f direction, float speed) {
		super(MovementModeType.BASIC, physicsObject, speed);
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
