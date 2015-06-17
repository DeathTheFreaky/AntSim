package at.antSim.objectsKI;

import javax.vecmath.Vector3f;

import at.antSim.Globals;
import at.antSim.eventSystem.EventListener;
import at.antSim.eventSystem.EventPriority;
import at.antSim.eventSystem.events.LocatorLockEvent;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.Movement.MoveToTarget;
import at.antSim.objectsPhysic.Movement.MovementManager;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;

public class Forager extends Ant implements Runnable {
	private int threshold;
	private int foodtransport;
	private int maxFoodTransport;
	
	public Forager(GraphicsEntity graphicsEntity,PhysicsObject physicsObject, Hive hive) {
		super(graphicsEntity, physicsObject, hive);
		hp = Globals.antHp;
		attack = Globals.antAttack;
		threshold = 2;
		odorStatus = 1;
		foodtransport = 0;
		maxFoodTransport = Globals.maxFoodCarry;
	}
	
	public int carryMoreFood(int newFoodAmout) {
		if ((foodtransport + newFoodAmout) <= maxFoodTransport) {
			foodtransport += newFoodAmout;
			return 0;
		} else {
			foodtransport = maxFoodTransport;
			return (foodtransport + newFoodAmout - maxFoodTransport);
		}
	}
	
	public void foundFood(){
		setOdorStatus(2);
		//extractfood from Enemy
		foodtransport = 1;
	}
	
	@Override
	public void reactSpecific(GhostPhysicsObject ghostPhysicsObject) {
		
		if (ghostPhysicsObject.getType().equals("positionLocator")) { //ant is inside a positionLocator
			
			PositionLocator locator = (PositionLocator) parentingEntities.get(ghostPhysicsObject);
			positionLocators.increaseCount(locator); //make sure positionLocator remains/is being added to list of all PositionLocators ant is currently in
			
			if (lockedLocator == null) { //only register ant for one locator at a time
				if (locator.getTarget().getObjectType().equals(ObjectType.FOOD) && foodtransport < maxFoodTransport) {
					if (locator.entryPossible(this)) {
						lockedLocator = locator;
						locator.registerAnt(this); //ant will be added to active ants in locator
						movementManager.addMovementEntry(physicsObject, new MoveToTarget(physicsObject, (ReadOnlyPhysicsObject) locator.physicsObject, Globals.ANT_SPEED));
					} else { //entry not possible -> either add to waiting list by calling locator.registerAnt or do something else
						lockedLocator = locator;
						locator.registerAnt(this); //ant will be added to waiting ants in locator
						movementManager.addMovementEntry(physicsObject, new MoveToTarget(physicsObject, physicsObject, Globals.ANT_SPEED));
					}
					
				} else if (locator.getTarget().getObjectType().equals(ObjectType.ENEMY)) {
					if (locator.registerAnt(this)) {
						
					};
				}
			} else {
				if (!locator.containsActiveAnt(this)) {
					if(locator.registerAnt(this)) { //try to get access -> if allowed, ant will be added to actives, so method will not be called again
						movementManager.removeLastMovementEntry(physicsObject); //remove lockin entry (which forces ant to stay where it is, even when hit by other ants)
						movementManager.addMovementEntry(physicsObject, new MoveToTarget(physicsObject, (ReadOnlyPhysicsObject) locator.physicsObject, Globals.ANT_SPEED));
					};
				}
			}
			
//			System.out.println("i tapped into the sphere of a positionLocator. I need to go to my target at " + locator.getTargetPosition());
		} else if (ghostPhysicsObject.getType().equals("pheromone")) {
			pheromones.increaseCount((Pheromone) parentingEntities.get(ghostPhysicsObject));
//			System.out.println("an ant ran into " + ghostPhysicsObject.getType());
		}
	}
	
	
	
	@Override
	public void reactSpecific(StaticPhysicsObject staticPhysicsObject) {
		
		if (lockedLocator != null) {
			if (staticPhysicsObject.equals(lockedLocator.getTarget().physicsObject)) { //ant ran into lockedLocator
				
				if (foodtransport < maxFoodTransport) {
					
					Food food = (Food) parentingEntities.get(staticPhysicsObject);
					carryMoreFood(food.harvest()); //amount of food which cannot be carried by ant is put back into food resource
										
					if (lockedLocator != null) {
						//lock in on lockedLocator
						movementManager.removeLastMovementEntry(physicsObject);
						movementManager.addMovementEntry(physicsObject, new MoveToTarget(physicsObject, (ReadOnlyPhysicsObject) lockedLocator.physicsObject, Globals.LOCKIN_SPEED));
					} else { //lockedLocator has been deleted in the meantime cause it ran out of food
						movementManager.removeLastMovementEntry(physicsObject);
					}
				} else {
					movementManager.removeLastMovementEntry(physicsObject);
					movementManager.addMovementEntry(physicsObject, new MoveToTarget(physicsObject, (ReadOnlyPhysicsObject) hive.physicsObject, Globals.ANT_SPEED));
				}
			}
		}
	}

	@Override
	public void reactSpecific(DynamicPhysicsObject dynamicPhysicsObject) {
		// TODO Auto-generated method stub
		
	}
	
//	@EventListener(priority = EventPriority.NORMAL)
//	public void locatorLockEvent(LocatorLockEvent le) {
//		if (le.getAnt() == this && le.getLocator().getTarget().getObjectType().equals(ObjectType.FOOD)) {
////			System.out.println("tells me to turn to " + le.getDirection() + " with speed " + le.getSpeed());
//			MovementManager.getInstance().addMovementEntry(physicsObject, new MoveToTarget(physicsObject, (ReadOnlyPhysicsObject) le.getLocator().getPhysicsObject()));
////			physicsObject.setAlignedMovement(le.getDirection(), le.getSpeed());
//			lockedLocator = le.getLocator();
//			le.consume();
//		}
//	}
	
	@Override
	public void run() {
		while(getHp()>0){
			// Wenn er unter dem threshold ist muss die Ameise essen
			// Falls kein Futter vorhanden ist verliert sie HP
			// bei <1 HP stirbt die Ameise aka die while im Thread l�uft nicht mehr
			if(getHunger() < threshold){
				try{
					this.eat();
				}catch(Exception e){
					setHp(getHp()-10);
				}	
			}
		}		
	}
}
