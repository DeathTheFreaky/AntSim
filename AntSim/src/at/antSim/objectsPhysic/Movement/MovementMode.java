package at.antSim.objectsPhysic.Movement;

import javax.vecmath.Vector3f;

import at.antSim.objectsPhysic.DynamicPhysicsObject;

/**An abstract class for different movement behavior of Entities.
 * 
 * @author Flo
 *
 */
public abstract class MovementMode {
	
	MovementModeType type;
	DynamicPhysicsObject physicsObject;
	float speed;
	
	public MovementMode(MovementModeType type, DynamicPhysicsObject physicsObject, float speed) {
		this.type = type;
		this.physicsObject = physicsObject;
		this.speed = speed;
	}

	public abstract void move();	
	
	public abstract Vector3f getDirection();
	
	public MovementModeType getType() {
		return type;
	}
}
