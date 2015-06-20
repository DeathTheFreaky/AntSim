package at.antSim.objectsPhysic.Movement;

import java.util.LinkedList;

import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;

import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;
import at.antSim.utils.Maths;

/**Lets an ant move around an obstacle in 90 degrees.
 * @author Flo
 *
 */
public class Dodge extends MovementMode {

	ReadOnlyPhysicsObject obstacle;
	Vector3f originalDirection;
	Vector3f currentDirection;
	Vector3f dodgeDirection;
	
	float movementLimit = 1;
		
	int collidingResetter = 20;
	int stillColliding;	
	
	boolean justCollided = false;
	boolean checkStillCollides = false;		
	
	int orTurnAngle = -20;
	int turnAngle;
		
	LinkedList<ReadOnlyPhysicsObject> previousObstacles = new LinkedList<>();
	
	public Dodge(DynamicPhysicsObject physicsObject, ReadOnlyPhysicsObject obstacle, float speed) {
		super(MovementModeType.DODGE, physicsObject, speed);
		
		this.obstacle = obstacle;				
	    stillColliding = collidingResetter;
	    turnAngle = orTurnAngle;
	}
	
	public void move() {
				
		stillColliding--;
				
		if (stillColliding > 0) {
													
			if (justCollided) { //turn if just collided
				
				System.out.println("justCollided");
				
				if (currentDirection.length() > 0) {
					currentDirection.normalize();
					
					int currentTurnAngle;
					
					if (!checkStillCollides) { //normal turn
						currentTurnAngle = orTurnAngle;
						System.out.println("normal turn by " + currentTurnAngle);
					} else { //assurance turn to see if object still collides -> 2 "steps" forward, 1 step back
						currentTurnAngle = -orTurnAngle/2;
						System.out.println("checkStillCollides turn by " + currentTurnAngle);
					}
										
					dodgeDirection = Maths.turnDirectionVector(currentDirection, currentTurnAngle);
					currentDirection = dodgeDirection;
					
					physicsObject.setAlignedMovement(dodgeDirection, speed);
										
					if (checkStillCollides) {
						checkStillCollides = false;
					} else {
						checkStillCollides = true;
					}
				}
			} else {
				physicsObject.setAlignedMovement(dodgeDirection, speed);
			} 
		} else {
						
			if (reachedOriginalDirection()) {
				MovementManager.getInstance().removeLastMovementEntry(physicsObject);
			} else {
				
				int currentTurnAngle = -orTurnAngle/2;
									
				dodgeDirection = Maths.turnDirectionVector(currentDirection, currentTurnAngle);
				currentDirection = dodgeDirection;
				
				physicsObject.setAlignedMovement(dodgeDirection, speed);
			}
		}
		
		justCollided = false;
	}
	
	private boolean reachedOriginalDirection() {
		
		System.out.println();
		Vector3f change = new Vector3f();
		change.sub(currentDirection, originalDirection);
		change.y = 0;
				
		if (change.length() < 0.001f) {
			return true;
		}
		
		return false;
	}

	public Vector3f getDodgeDirection() {
		return dodgeDirection;
	}
	
	public void setOriginalDirection(Vector3f originalDirection) {
		this.originalDirection = originalDirection;
	}
	
	public void setCurrentDirection(Vector3f currentDirection) {
		this.currentDirection = currentDirection;
		dodgeDirection = currentDirection;
	}
	
	public void setStillColliding() {
		stillColliding = collidingResetter;
		justCollided = true;
	}
	
	public void reset(ReadOnlyPhysicsObject newObstacle) {
		
		if (!previousObstacles.contains(newObstacle)) {
			previousObstacles.add(obstacle);
			obstacle = newObstacle;
		}
	}

	@Override
	public Vector3f getDirection() {
		return currentDirection;
	}	
}
