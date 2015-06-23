package at.antSim.objectsKI;

import at.antSim.Globals;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.Movement.Dodge;
import at.antSim.objectsPhysic.Movement.MoveInDirection;
import at.antSim.objectsPhysic.Movement.MoveToTarget;
import at.antSim.objectsPhysic.Movement.MovementModeType;
import at.antSim.objectsPhysic.Movement.Wait;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;

public class Forager extends Ant{
	private int threshold;
	private int foodtransport;
	private int maxFoodTransport;
	
	public Forager(GraphicsEntity graphicsEntity,PhysicsObject physicsObject) {
		super(graphicsEntity, physicsObject);
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
			
//			System.out.println(this + " inside a positionLocator and lockedLocator == " + lockedLocator);
			
			PositionLocator locator = (PositionLocator) parentingEntities.get(ghostPhysicsObject);
			positionLocators.increaseCount(locator); //make sure positionLocator remains/is being added to list of all PositionLocators ant is currently in
			
			if (locator != null) {
				if (lockedLocator == null) { //only register ant for one locator at a time
					if ((locator.getTarget().getObjectType().equals(ObjectType.FOOD) && foodtransport < maxFoodTransport)
							|| (locator.getTarget().getObjectType().equals(ObjectType.ENEMY) && foodtransport == 0)
							|| (locator.getTarget().getObjectType().equals(ObjectType.HIVE) && foodtransport > 0)) {
						
						if (locator.getTarget().getObjectType().equals(ObjectType.HIVE) && foodtransport > 0) {
	//						System.out.println(this + " is trying to enter hive locator");
						}
						
						if (locator.getTarget().getObjectType().equals(ObjectType.ENEMY)) {
							movementManager.addMovementEntry((DynamicPhysicsObject) locator.getTarget().physicsObject, new Wait((DynamicPhysicsObject) locator.getTarget().physicsObject, physicsObject, Globals.ANT_SPEED));
						}
						
						if (locator.entryPossible(this)) {
							if (locator.getTarget().getObjectType().equals(ObjectType.HIVE) && foodtransport > 0) {
	//							System.out.println(this + " was allowed entry into hive locator");
							}

							lockedLocator = locator;
							locator.registerAnt(this); //ant will be added to active ants in locator
							movementManager.addMovementEntry(physicsObject, new MoveToTarget(physicsObject, (ReadOnlyPhysicsObject) locator.physicsObject, Globals.ANT_SPEED));
						} else { //entry not possible -> either add to waiting list by calling locator.registerAnt or do something else
							if (locator.getTarget().getObjectType().equals(ObjectType.HIVE) && foodtransport > 0) {
	//							System.out.println(this + " was told to wait for entry into hive locator");
							}

							lockedLocator = locator;
							locator.registerAnt(this); //ant will be added to waiting ants in locator
							movementManager.addMovementEntry(physicsObject, new Wait(physicsObject, (ReadOnlyPhysicsObject) locator.getTarget().physicsObject, Globals.ANT_SPEED));
	//						movementManager.addMovementEntry(physicsObject, new MoveToTarget(physicsObject, physicsObject, Globals.ANT_SPEED));
						}
						
					} else if (locator.getTarget().getObjectType().equals(ObjectType.ENEMY)) {
						if (locator.registerAnt(this)) {
							
						};
					}
				} else if (locator.getTarget().getObjectType().equals(ObjectType.HIVE) && foodtransport == 0) {
					
				} else {
					if (!locator.containsActiveAnt(this)) {
						if(locator.registerAnt(this)) { //try to get access -> if allowed, ant will be added to actives, so method will not be called again
							movementManager.removeLastMovementEntry(physicsObject); //remove wait entry (which forces ant to stay where it is, even when hit by other ants)
							movementManager.addMovementEntry(physicsObject, new MoveToTarget(physicsObject, (ReadOnlyPhysicsObject) locator.physicsObject, Globals.ANT_SPEED));
						};
					}
				}
			}
			
//			System.out.println("i tapped into the sphere of a positionLocator. I need to go to my target at " + locator.getTargetPosition());
		} else if (ghostPhysicsObject.getType().equals("pheromone")) {
			pheromones.increaseCount((Pheromone) parentingEntities.get(ghostPhysicsObject));
			Pheromone p = (Pheromone) parentingEntities.get(ghostPhysicsObject);
//			System.out.println("an ant ran into " + ghostPhysicsObject.getType());
//			System.out.println("pheromone " + p);
			if(p != null)
				movementManager.addMovementEntry(physicsObject, new MoveInDirection(physicsObject, p.getDirection(), Globals.ANT_SPEED));		
		}
	}
	
	
	
	@Override
	public void reactSpecific(StaticPhysicsObject staticPhysicsObject) {
		
		if (lockedLocator != null) {
			if (staticPhysicsObject.equals(lockedLocator.getTarget().physicsObject)) { //ant ran into lockedLocator
				
				if (lockedLocator.getTarget().objectType == ObjectType.FOOD) {
					
					if (foodtransport < maxFoodTransport) {
						
						Food food = (Food) parentingEntities.get(staticPhysicsObject);
						carryMoreFood(food.harvest()); //amount of food which cannot be carried by ant is put back into food resource
						
						if (!Entity.entities.contains(food)) { //food has been deleted cause its foodstacks reached 0
							movementManager.addMovementEntry(physicsObject, new MoveToTarget(physicsObject, (ReadOnlyPhysicsObject) hive.physicsObject, Globals.ANT_SPEED));
							this.setOdorStatus(2);
						}
											
						if (lockedLocator != null) {
							//lock in on lockedLocator
							movementManager.removeLastMovementEntry(physicsObject); //whatfor?
							movementManager.addMovementEntry(physicsObject, new MoveToTarget(physicsObject, (ReadOnlyPhysicsObject) lockedLocator.physicsObject, Globals.LOCKIN_SPEED));							
						}
					} else {
						if (movementManager.getTopMovementMode(physicsObject).getType() != MovementModeType.DODGE) {
							movementManager.removeLastMovementEntry(physicsObject); //whatfor?
							movementManager.addMovementEntry(physicsObject, new MoveToTarget(physicsObject, (ReadOnlyPhysicsObject) hive.physicsObject, Globals.ANT_SPEED));
							movementManager.addMovementEntry(physicsObject, new Dodge(physicsObject, staticPhysicsObject, Globals.ANT_SPEED));
							this.setOdorStatus(2);
						} else {
							movementManager.addMovementEntry(physicsObject, new Dodge(physicsObject, staticPhysicsObject, Globals.ANT_SPEED));
						}
					}
				} else if (lockedLocator.getTarget().objectType == ObjectType.HIVE) {
					if (foodtransport > 0) {
						hive.storeFood(foodtransport);
						foodtransport = 0;
						this.setHp(Globals.antHp);
						this.setOdorStatus(1);
						if (movementManager.getTopMovementMode(physicsObject) != null) {
							if (movementManager.getTopMovementMode(physicsObject).getType() != MovementModeType.BASIC) {
								movementManager.removeLastMovementEntry(physicsObject); // So he doesnt stand at the hive	
							}	
						} 			
					}
				}
			}
		}
	}
	

	@Override
	public void reactSpecific(DynamicPhysicsObject dynamicPhysicsObject) {
		
		if (Entity.parentingEntities.get(dynamicPhysicsObject).objectType == ObjectType.ENEMY) {
			Enemy enemy = (Enemy) Entity.parentingEntities.get(dynamicPhysicsObject);
			enemy.fight(attack);
			fight(enemy.attack);
		}
	}
}
