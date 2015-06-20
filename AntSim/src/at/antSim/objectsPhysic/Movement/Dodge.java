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
	int tickCtr; //every tickCtr%x, try if Ant still collides with other ant
	int x = 20; //influences how far ant turns in a circle
	
	float movementLimit = 1;
	
	Vector3f previousPosition;
	
	int collidingResetter = 10;
	int stillColliding;
	int turnCtr = 0;
	double turnAngleDegree = -10;
	
	int turnAngleSum = 0;
	
	boolean movesAlongTangent = false;
	boolean movedPassTarget = false;
			
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
		
//		dodgeDirection = new Vector3f(-originalDirection.z, originalDirection.y, originalDirection.x);
//		tickCtr = 0;
		
	}
	
	public void move() {
		
		previousPosition = physicsObject.getPosition();
		
		stillColliding--;
		
		if (stillColliding > 0) {
						
			if (tickCtr == 0) {
				
				if (currentDirection.length() > 0) {
					currentDirection.normalize();
					
					System.out.println(" turning with currentDirection " + currentDirection);
					
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
					
					
//					if (turnAngleDegree * turnCtr < 90) {
//						
//						
//					    
//					    turnCtr++;
//					    
//					} else {
//						
////						dodgeDirection = Maths.turnDirectionVector(currentDirection, turnAngle);
////						currentDirection = dodgeDirection;
//					}
				    
//				    System.out.println(" new dodgeDirection " + currentDirection);
					
					physicsObject.setAlignedMovement(dodgeDirection, speed);
					
				}
				
//			} else if (tickCtr < x) {
				
			} else if (checkMovementLength()) {
				
//				System.out.println(tickCtr + " returning with dodgeDirection " + dodgeDirection);
				
				physicsObject.setAlignedMovement(dodgeDirection, speed);
				
				//let object move, do not reset to get hitback on collision?
			} else {
				tickCtr = -1;
			}
			
			tickCtr++;
		
		} else {
			
			if (movesAlongTangent) {
				
				movedPassTarget = true;
				
				System.out.println("turnAngleSum: " + turnAngleSum);
				
				if (turnAngleSum == 0) {
					MovementManager.getInstance().removeLastMovementEntry(physicsObject);
					System.out.println("i am no more colliding");
				} else {
					setStillColliding();
				}
				
				return;
			}
			
			System.out.println("i am now moving along the tangent");
			movesAlongTangent = true;
			turnAngle = -turnAngle;
			setStillColliding();
			
			
			
//			if (tickCtr < x) {
//				
//			}
			
			
		}
		
				
//		if (tickCtr < 1) {
////			System.out.println("waiting");
//			physicsObject.setAlignedMovement(dodgeDirection, speed);
////		} else if (tickCtr >= 10 && tickCtr < 50){
////			physicsObject.setAlignedMovement(dodgeDirection, speed);
//////			System.out.println("moving in dodge direction");
////		} else if (tickCtr >= 50 && tickCtr < 100) {
//////			physicsObject.setAlignedMovement(dodgeDirection, speed);
//		} else {
//			System.out.println("deleting");
//			MovementManager.getInstance().removeLastMovementEntry(physicsObject); //remove dodging movement 
//		}
//		
//		tickCtr++;
				
	}
	
	private boolean checkMovementLength() {
		
		Vector3f currentPosition = physicsObject.getPosition();
//		Vector3f change = new Vector3f(currentPosition.x - previousPosition.x, 0, currentPosition.z - previousPosition.z);
		Vector3f change = new Vector3f();
		change.sub(previousPosition, currentPosition);
		change.y = 0;
		if (change.length() > movementLimit) {
			currentPosition = previousPosition;
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
	}

	@Override
	public Vector3f getDirection() {
		return currentDirection;
	}
}
