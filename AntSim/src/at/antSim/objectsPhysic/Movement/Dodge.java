package at.antSim.objectsPhysic.Movement;

import java.util.LinkedList;

import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;

import at.antSim.objectsKI.Entity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObject;
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
	MovementMode previousMode;
	
	float movementLimit = 1;
		
	int collidingResetter = 20;
	int stillColliding;	
	
	boolean justCollided = false;
	boolean checkStillCollides = false;		
	
	int orTurnAngle = -10;
	int turnAngle;
	
	int debugCtr = 0;
		
	LinkedList<ReadOnlyPhysicsObject> previousObstacles = new LinkedList<>();
	
	public Dodge(DynamicPhysicsObject physicsObject, ReadOnlyPhysicsObject obstacle, float speed) {
		super(MovementModeType.DODGE, physicsObject, speed);
		
		this.obstacle = obstacle;				
	    stillColliding = collidingResetter;
	    turnAngle = orTurnAngle;
	}
	
	public void move() {
				
		updateOriginalDirection();
				
		stillColliding--;
		
		if (!targetExists()) {	
			physicsObject.setAlignedMovement(originalDirection, speed);
			MovementManager.getInstance().removeLastMovementEntry(physicsObject);
			return;
		}
				
		if (stillColliding > 0) {
													
			if (justCollided) { //turn if just collided
								
				if (currentDirection.length() > 0) {
					currentDirection.normalize();
					
					int currentTurnAngle;
					
					if (!checkStillCollides) { //normal turn
						currentTurnAngle = orTurnAngle;
					} else { //assurance turn to see if object still collides -> 2 "steps" forward, 1 step back
						currentTurnAngle = -orTurnAngle/4*3;
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
				
				physicsObject.setAlignedMovement(dodgeDirection, speed/2);
			}
		}
		
		justCollided = false;
	}
	
	private void updateOriginalDirection() {
		originalDirection = previousMode.getDirection();
		originalDirection.normalize();
	}

	private boolean reachedOriginalDirection() {
		
		Vector3f change = new Vector3f();
		change.sub(currentDirection, originalDirection);
		change.y = 0;
						
		if (change.length() < 0.1f) {
			return true;
		}
		
		return false;
	}

	public Vector3f getDodgeDirection() {
		return dodgeDirection;
	}
	
	public void setOriginalDirection(Vector3f originalDirection) {
		this.originalDirection = originalDirection;
		originalDirection.normalize();
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
	
	public void setPreviousMovementMode(MovementMode previousMode) {
		this.previousMode = previousMode;
	}
	
	private boolean targetExists() {
		if (Entity.getParentEntity((PhysicsObject) obstacle) == null) {
			return false;
		}
		return true;
	}

	@Override
	public Vector3f getDirection() {
		return currentDirection;
	}	
	
	public ReadOnlyPhysicsObject getTarget() {
		return obstacle;
	}
}
