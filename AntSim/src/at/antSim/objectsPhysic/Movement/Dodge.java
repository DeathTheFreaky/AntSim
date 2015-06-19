package at.antSim.objectsPhysic.Movement;

import javax.vecmath.Vector3f;

import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;

/**Lets an ant move around an obstacle in 90 degrees.
 * @author Flo
 *
 */
public class Dodge implements MovementMode {

	DynamicPhysicsObject physicsObject;
	ReadOnlyPhysicsObject obstacle;
	float speed;
	Vector3f originalDirection;
	Vector3f dodgeDirection;
	int tickCtr; //every tickCtr%x, try if Ant still collides with other ant
	int x = 10;
	
	public Dodge(DynamicPhysicsObject physicsObject, ReadOnlyPhysicsObject obstacle, float speed) {
		this.physicsObject = physicsObject;
		this.obstacle = obstacle;
		this.speed = speed;
		originalDirection = physicsObject.getLinearVelocity();
		dodgeDirection = new Vector3f(-originalDirection.z, originalDirection.y, originalDirection.x);
		tickCtr = 0;
	}
	
	public void move() {
		
		if (tickCtr < x) {
			physicsObject.setAlignedMovement(dodgeDirection, speed);
		} else {
			MovementManager.getInstance().removeLastMovementEntry(physicsObject); //remove dodging movement 
		}
		
		tickCtr++;
	}
}
