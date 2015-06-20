package at.antSim.objectsPhysic.Movement;

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
	int x = 2;
	int collidingResetter = 50;
	int stillColliding;
	int turnCtr = 0;
	double turnAngleDegree = 10;
		
	double turnAngle = Math.toRadians(turnAngleDegree);
	
	public Dodge(DynamicPhysicsObject physicsObject, ReadOnlyPhysicsObject obstacle, float speed) {
		super(MovementModeType.DODGE, physicsObject, speed);
		this.obstacle = obstacle;
		this.originalDirection = physicsObject.getLinearVelocity();
		this.currentDirection = originalDirection;
		
		dodgeDirection = Maths.turnDirectionVector(currentDirection, turnAngle);
		currentDirection = dodgeDirection;
	    
	    stillColliding = collidingResetter;
		
//		dodgeDirection = new Vector3f(-originalDirection.z, originalDirection.y, originalDirection.x);
//		tickCtr = 0;
		
	}
	
	public void move() {
		
		stillColliding--;
		
		if (stillColliding > 0) {
						
			if (tickCtr == 0) {
				
				if (currentDirection.length() > 0) {
					currentDirection.normalize();
					
					System.out.println(" turning with currentDirection " + currentDirection);
					
					if (turnAngleDegree * turnCtr < 90) {
						
						dodgeDirection = Maths.turnDirectionVector(currentDirection, turnAngle);
						currentDirection = dodgeDirection;
					    
					    turnCtr++;
					    
					} else {
						
						dodgeDirection = Maths.turnDirectionVector(currentDirection, turnAngle);
						currentDirection = dodgeDirection;
					}
				    
				    System.out.println(" new dodgeDirection " + currentDirection);
					
					physicsObject.setAlignedMovement(dodgeDirection, speed);
					
				}
				
			} else if (tickCtr < x) {
				
//				System.out.println(tickCtr + " returning with dodgeDirection " + dodgeDirection);
				
				physicsObject.setAlignedMovement(dodgeDirection, speed);
				
				//let object move, do not reset to get hitback on collision?
			} else {
				tickCtr = -1;
			}
			
			tickCtr++;
		
		} else {
			
			System.out.println("i am no more colliding");
			
//			if (tickCtr < x) {
//				
//			}
			
			MovementManager.getInstance().removeLastMovementEntry(physicsObject);
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
