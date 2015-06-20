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
	
	Vector3f previousPosition;
	
	int collidingResetter = 10;
	int stillColliding;
	int turnCtr = 0;
	double turnAngleDegree = -5;
	
	int turnAngleSum = 0;
	
	boolean movesAlongTangent = false;
	boolean movedPassTarget = false;
	boolean justCollided = false;
			
	double orTurnAngle = Math.toRadians(turnAngleDegree);
	double turnAngle;
	
	LinkedList<ReadOnlyPhysicsObject> previousObstacles = new LinkedList<>();
	
	int littleHelper = 0;
	
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
				
				System.out.println("justCollided && !movesAlongTangent");
				
				if (currentDirection.length() > 0) {
					currentDirection.normalize();
										
					dodgeDirection = Maths.turnDirectionVector(currentDirection, turnAngle);
					System.out.println(" DIR1 " + dodgeDirection);
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
				
				System.out.println("!justCollided && !movesAlongTangent");
				
				physicsObject.setAlignedMovement(dodgeDirection, speed);
				
			} else if (movesAlongTangent && !movement) {
				
				System.out.println("movesAlongTangent && !movement, colliding: " + stillColliding + " helper " + littleHelper);				
				if(littleHelper > 3 ){
					MovementManager.getInstance().removeLastMovementEntry(physicsObject);
					return;
				}
				physicsObject.setAlignedMovement(dodgeDirection, speed);
				littleHelper++;
				//let object move, do not reset to get hitback on collision?
			} else if (movesAlongTangent && movement) {
				littleHelper = 0;
				System.out.println("movement && movesAlongTangent");
							
				if (currentDirection.length() > 0) {
					currentDirection.normalize();
										
					dodgeDirection = Maths.turnDirectionVector(currentDirection, turnAngle);
					System.out.println(" DIR2 " + dodgeDirection);
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
			System.out.println("big else, justcollided " + justCollided + " turn " + turnAngleSum);
			if (movesAlongTangent) {
				System.out.println("movesAlongTangent");
				movedPassTarget = true;
								
				if (turnAngleSum == 0) {
					System.out.println("removed");
					MovementManager.getInstance().removeLastMovementEntry(physicsObject);
				} else {
					System.out.println("still colliding");
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
//		System.out.println("length: " + change.length() + " at currentPosition: " + currentPosition + " and previousPosition " + previousPosition + " movesAlongTangent: " + movesAlongTangent);
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
//		if (movedPassTarget) {
//			movesAlongTangent = false;
//			movedPassTarget = false;
//		}
	}
	
	public void reset(ReadOnlyPhysicsObject newObstacle) {
		
		if (!previousObstacles.contains(newObstacle)) {
			previousObstacles.add(obstacle);
			obstacle = newObstacle;
			System.out.println("   RESETTED");
			movesAlongTangent = false;
			movedPassTarget = false;
		}
	}

	@Override
	public Vector3f getDirection() {
		return currentDirection;
	}
}
