package at.antSim.objectsPhysic.Movement;

import java.util.LinkedList;

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
	
	Vector3f previousDirection;
	Vector3f previousPosition;
	float stuckCtr = 0;
	float stuckInterval = 100;
	
	float movementLimit = 1;
		
	int collidingResetter = 20;
	int stillColliding;	
	
	boolean justCollided = false;
	boolean checkStillCollides = false;		
	
	int orTurnAngle = -10;
	int turnAngle;
	
	int debugCtr = 0;
	
	int turnCtr = 0;
		
	LinkedList<ReadOnlyPhysicsObject> previousObstacles = new LinkedList<>();
	
	public Dodge(DynamicPhysicsObject physicsObject, ReadOnlyPhysicsObject obstacle, float speed) {
		super(MovementModeType.DODGE, physicsObject, speed);
		
		this.obstacle = obstacle;				
	    stillColliding = collidingResetter;
	    turnAngle = orTurnAngle;
	}
	
	public void move() {
				
		updateOriginalDirection();
		
		if (!detectBeingStuck()) { //avoid ants from getting stuck if they are for whatever reason unable to move
			
			stillColliding--;
			
			if (!targetExists()) {	
				physicsObject.setAlignedMovement(originalDirection, speed);
				MovementManager.getInstance().removeLastMovementEntry(physicsObject);
				return;
			}
			if (reachedOriginalDirection()) {
				if (turnCtr >= 3) {
					System.out.println(" finih ");
					MovementManager.getInstance().removeLastMovementEntry(physicsObject);
					turnCtr = 0;
				}
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
						turnCtr++;
											
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
					System.out.println(" DOM E ");
					MovementManager.getInstance().removeLastMovementEntry(physicsObject);
				} else {
					
					int currentTurnAngle = -orTurnAngle/2;
										
					dodgeDirection = Maths.turnDirectionVector(currentDirection, currentTurnAngle);
					currentDirection = dodgeDirection;
					
					physicsObject.setAlignedMovement(dodgeDirection, speed/2);
					turnCtr++;
				}
			}
			
			justCollided = false;
		}
	}
	
	private boolean detectBeingStuck() {
//		if (previousDirection != null && previousPosition != null) {
//			if (stuckCtr >= stuckInterval) {
//				Vector3f diffDirection = new Vector3f(dodgeDirection.x - previousDirection.x, 0, dodgeDirection.z - previousDirection.z);
//				Vector3f diffPosition = new Vector3f(physicsObject.getPosition().x - previousPosition.x, 0, physicsObject.getPosition().z - previousPosition.z);
//				if (diffDirection.length() < 0.001f && diffPosition.length() < 0.1f) {
//					System.out.println("diffDirection: " + diffDirection);
//					System.out.println("diffPosition: " + diffPosition);
//					dodgeDirection = Maths.turnDirectionVector(currentDirection, -90);
//					currentDirection = dodgeDirection;
//					
//					physicsObject.setAlignedMovement(dodgeDirection, speed);
//				}
//				previousDirection = dodgeDirection;
//				previousPosition = physicsObject.getPosition();
//			}
//		} else {
//			previousDirection = dodgeDirection;
//			previousPosition = physicsObject.getPosition();
//		}	
//		stuckCtr++;
		return false;
	}

	private void updateOriginalDirection() {
//		System.out.println("previousMode: " + previousMode);
//		if (previousMode != null) {
		originalDirection = previousMode.getDirection();
		originalDirection.normalize();
//		}
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
//		System.out.println("previousMovementMode: " + previousMode);
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
