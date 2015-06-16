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
		if (ghostPhysicsObject.getType().equals("positionLocator")) {
			PositionLocator locator = (PositionLocator) parentingEntities.get(ghostPhysicsObject);
			positionLocators.increaseCount(locator);
			if (locator.getTarget().getObjectType().equals(ObjectType.FOOD) && foodtransport < maxFoodTransport) {
				locator.activateAnt(this); //only activate foodLocator if ant can still carry food
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
			if (staticPhysicsObject.equals(lockedLocator.getTarget().physicsObject)) {
				if (lockedLocator.getTarget().getObjectType().equals(ObjectType.FOOD) && foodtransport >= maxFoodTransport) {
					lockedLocator.deactivateAnt(this);
					movementManager.removeMovementEntry(physicsObject);
				} else {
					Food food = (Food) parentingEntities.get(staticPhysicsObject);
					carryMoreFood(food.harvest()); //amount of food which cannot be carried by ant is put back into food resource
					if (foodtransport >= maxFoodTransport) {
						System.out.println("ant gathered " + foodtransport + " food");
						unlockLocator();
						System.out.println("movementManager: " + movementManager + ", hive: " + hive);
						movementManager.setMovementEntry(physicsObject, new MoveToTarget(physicsObject, (ReadOnlyPhysicsObject) hive.physicsObject));
					}
				}
			}
		}
	}

	@Override
	public void reactSpecific(DynamicPhysicsObject dynamicPhysicsObject) {
		// TODO Auto-generated method stub
		
	}
	
	@EventListener(priority = EventPriority.NORMAL)
	public void locatorLockEvent(LocatorLockEvent le) {
		if (le.getAnt() == this && le.getLocator().getTarget().getObjectType().equals(ObjectType.FOOD)) {
//			System.out.println("tells me to turn to " + le.getDirection() + " with speed " + le.getSpeed());
			MovementManager.getInstance().setMovementEntry(physicsObject, new MoveToTarget(physicsObject, (ReadOnlyPhysicsObject) le.getLocator().getPhysicsObject()));
//			physicsObject.setAlignedMovement(le.getDirection(), le.getSpeed());
			lockedLocator = le.getLocator();
			le.consume();
		}
	}
	
	@Override
	public void run() {
		while(getHp()>0){
			// Wenn er unter dem threshold ist muss die Ameise essen
			// Falls kein Futter vorhanden ist verliert sie HP
			// bei <1 HP stirbt die Ameise aka die while im Thread läuft nicht mehr
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
