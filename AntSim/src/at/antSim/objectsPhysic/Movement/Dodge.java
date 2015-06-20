package at.antSim.objectsPhysic.Movement;

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
	
	Vector3f previousPosition;
	
	int collidingResetter = 10;
	int stillColliding;
	int turnCtr = 0;
	double turnAngleDegree = -10;
	
	int turnAngleSum = 0;
	
	boolean movesAlongTangent = false;
	boolean movedPassTarget = false;
	boolean justCollided = false;
			
	double orTurnAngle = Math.toRadians(turnAngleDegree);
	double turnAngle;
	
	public Dodge(DynamicPhysicsObject physicsObject, ReadOnlyPhysicsObject obstacle, float speed) {
		super(MovementModeType.DODGE, physicsObject, speed);
		this.obstacle = obstacle;
		this.originalDirection = physicsObject.getLinearVelocity();
		this.currentDirection = originalDirection;
		
		previousPosition = physicsObject.getPosition();
		
		dodgeDirection = Maths.turnDirectionVector(currentDirection, orTurnAngle);
		currentDirection = dodgeDirection;
	    
	    stillColliding = collidingResetter;
	    turnAngle = orTurnAngle;
	}
	
	public void move() {
				
		stillColliding--;
		
		if (stillColliding > 0) {
			
			boolean movement = checkMovementLength();
										
			if (justCollided && !movesAlongTangent) {
				
				if (currentDirection.length() > 0) {
					currentDirection.normalize();
										
					dodgeDirection = Maths.turnDirectionVector(currentDirection, turnAngle);
					currentDirection = dodgeDirection;
					if (turnAngle > 0) {
						turnAngleSum++;
					} else {
						turnAngleSum--;
					}
					
					if (movesAlongTangent && !movedPassTarget) {
						turnAngle = -turnAngle;
					}
					
					physicsObject.setAlignedMovement(dodgeDirection, speed);
					
				}
				
			} else if (!justCollided && !movesAlongTangent) {
				
				physicsObject.setAlignedMovement(dodgeDirection, speed);
				
			} else if (movesAlongTangent && !movement) {
								
				physicsObject.setAlignedMovement(dodgeDirection, speed);
				
				//let object move, do not reset to get hitback on collision?
			} else if (movesAlongTangent && movement) {
							
				if (currentDirection.length() > 0) {
					currentDirection.normalize();
										
					dodgeDirection = Maths.turnDirectionVector(currentDirection, turnAngle);
					currentDirection = dodgeDirection;
										
					currentDirection = dodgeDirection;
					if (turnAngle > 0) {
						turnAngleSum++;
					} else {
						turnAngleSum--;
					}
					
					if (movesAlongTangent && !movedPassTarget) {
						System.out.println("not moved passtarget");
						turnAngle = -turnAngle;
					} else {
						turnAngle = -orTurnAngle;
					
						if (turnAngleSum == 0) {
							MovementManager.getInstance().removeLastMovementEntry(physicsObject);
						}
					}
					
					physicsObject.setAlignedMovement(dodgeDirection, speed);
				}
			} 
		} else {
			
			if (movesAlongTangent) {
				
				movedPassTarget = true;
								
				if (turnAngleSum == 0) {
					MovementManager.getInstance().removeLastMovementEntry(physicsObject);
				} else {
					setStillColliding();
				}
				
				return;
			}
			
			movesAlongTangent = true;
			turnAngle = -turnAngle;
			setStillColliding();
		}
		
		justCollided = false;
	}
	
	private boolean checkMovementLength() {
		
		Vector3f currentPosition = physicsObject.getPosition();
//		Vector3f change = new Vector3f(currentPosition.x - previousPosition.x, 0, currentPosition.z - previousPosition.z);
		Vector3f change = new Vector3f();
		change.sub(previousPosition, currentPosition);
		change.y = 0;
		System.out.println("length: " + change.length() + " at currentPosition: " + currentPosition + " and previousPosition " + previousPosition + " movesAlongTangent: " + movesAlongTangent);
		if (change.length() > movementLimit) {
			previousPosition = currentPosition;
			return true;
		}
		return false;
	}

	public Vector3f getDodgeDirection() {
		return dodgeDirection;
	}
	
	
	
	public void setOriginalDirection(Vector3f originalDirection) {
		this.currentDirection = originalDirection;
	}
	
	public void setStillColliding() {
		stillColliding = collidingResetter;
		justCollided = true;
	}

	@Override
	public Vector3f getDirection() {
		return currentDirection;
	}
}
