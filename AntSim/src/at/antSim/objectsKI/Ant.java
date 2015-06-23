package at.antSim.objectsKI;

import java.util.LinkedList;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import at.antSim.Globals;
import at.antSim.MainApplication;
import at.antSim.eventSystem.EventListener;
import at.antSim.eventSystem.EventManager;
import at.antSim.eventSystem.EventPriority;
import at.antSim.eventSystem.events.CollisionEvent;
import at.antSim.eventSystem.events.LocatorLockEvent;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.graphics.graphicsUtils.WorldLoader;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.PhysicsManager;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.TerrainPhysicsObject;
import at.antSim.objectsPhysic.Movement.BorderCollisionMovement;
import at.antSim.objectsPhysic.Movement.Dodge;
import at.antSim.objectsPhysic.Movement.MoveInDirection;
import at.antSim.objectsPhysic.Movement.MoveToTarget;
import at.antSim.objectsPhysic.Movement.MovementManager;
import at.antSim.objectsPhysic.Movement.MovementMode;
import at.antSim.objectsPhysic.Movement.MovementModeType;
import at.antSim.objectsPhysic.PhysicsFactorys.GhostPhysicsObjectFactory;
import at.antSim.objectsPhysic.PhysicsFactorys.StaticPhysicsObjectFactory;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.PositionablePhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;
import at.antSim.utils.CountingLinkedList;
import at.antSim.utils.Maths;

/**
 * 
 * 
 * TODO: -
 * http://stackoverflow.com/questions/2790144/avoiding-instanceof-in-java +
 * Reflection Ueberschreiben verwenden => dadurch muss kein type check gemacht
 * werden
 * 
 * Visitor Pattern
 * 
 * 
 * Physics - Entity inkludieren.
 * 
 * 
 * 
 * @author Martin
 *
 */
public abstract class Ant extends Entity {
	// search 1, food 2, war 3, nothing 0
	protected int odorStatus = 0;
	protected float hp;
	protected float attack;
	// saturate?
	private int hunger;
	protected DynamicPhysicsObject physicsObject = null;
	protected int velocityX = -5; //attention: positive velocity in x-axis moves entity to right side, although x decreases to the right side!
	protected int velocityZ = 0;
	protected float lastposition = 0;
	private int velocityhelper = 0;
	
	protected PositionLocator lockedLocator; //target's positionLocator where ant is currently heading to, all targets must have positionLocators

	// wahrscheinlich eigene Jobklasse => fuer im Bautštige
	// und Worker/Forager
	private String job;
	
	Hive hive;
	
	MovementManager movementManager;
	
	//linked lists for storing which position Locators and Pheromones the ant is currently in
	protected CountingLinkedList<PositionLocator> positionLocators = new CountingLinkedList<>();
	protected CountingLinkedList<Pheromone> pheromones = new CountingLinkedList<>();
	
	public Ant(GraphicsEntity graphicsEntity, PhysicsObject physicsObject) {
		super(graphicsEntity, physicsObject, ObjectType.ANT);
		this.physicsObject = (DynamicPhysicsObject) physicsObject;
		hive = Hive.getInstance();
		hive.addAnt(this);
		Vector3f v = new Vector3f(-1f + 2*(float) Math.random(), 0, -1f + 2*(float) Math.random());
//		this.physicsObject.setLinearVelocity(v);
//		this.physicsObject.setAlignedMovement(new Vector3f(0, 0, -1), Globals.ANT_SPEED*3);
		dynamicEntities.add(this);
		ants.add(this);
		// ROTATE WITH THIS Math.toradiant();
		//this.physicsObject.setRotation(0, 0, 0);
		EventManager.getInstance().registerEventListener(this);
		movementManager = MovementManager.getInstance();
		movementManager.addMovementEntry((DynamicPhysicsObject) physicsObject, new MoveInDirection((DynamicPhysicsObject) physicsObject, v, Globals.ANT_SPEED));
//		movementManager.addMovementEntry((DynamicPhysicsObject) physicsObject, new MoveToTarget((DynamicPhysicsObject) physicsObject, (ReadOnlyPhysicsObject) WorldLoader.hive.physicsObject, Globals.ANT_SPEED));
	}

	@Override
	public void react(StaticPhysicsObject staticPhysicsObject) {
		//Colliding with the ground/terrain
		
		if (staticPhysicsObject.getType().equals("border")) {
			if (MovementManager.getInstance().getTopMovementMode(physicsObject).getType() == MovementModeType.DIRECTION) {
				BorderCollisionMovement borderCollisionMovement = new BorderCollisionMovement(physicsObject, Globals.ANT_SPEED);
				MoveInDirection moveInDirection = new MoveInDirection(physicsObject, borderCollisionMovement.getDirection(), Globals.ANT_SPEED);
				movementManager.removeLastMovementEntry(physicsObject); //makes no sense to keep moving into same direction when hitting a wall
				movementManager.addMovementEntry(physicsObject, moveInDirection);
				movementManager.addMovementEntry(physicsObject, borderCollisionMovement);
			} else {
				movementManager.addMovementEntry(physicsObject, new BorderCollisionMovement(physicsObject, Globals.ANT_SPEED));
			}
		} else {
			ObjectType tp = Entity.physicsObjectTypeMap.get(staticPhysicsObject);
			if (tp.equals(ObjectType.ENVIRONMENT)) { 
				movementManager.addMovementEntry(physicsObject, new Dodge(physicsObject, staticPhysicsObject, Globals.ANT_SPEED));
			}
		}
		
		
		
		// example
		// if(physicsObject.getPosition().y <
		// staticPhysicsObject.getPosition().y){
//		if (staticPhysicsObject.getPosition().y == 0) {
//			if (physicsObject.getLinearVelocity().x < velocityX
//					|| physicsObject.getLinearVelocity().z < velocityZ) {
//				Vector3f v = new Vector3f(velocityX, 0, velocityZ);
//				physicsObject.setLinearVelocity(v);
////				 System.out.println("Ant: " + physicsObject.getPosition() +
////				 " Ground: " + staticPhysicsObject.getPosition());
//			}
//		} else {
//			// Not colliding with the ground but something else like a tree 
//			if (physicsObject.getPosition().x > staticPhysicsObject
//					.getPosition().x) {
//				Vector3f v;
//				v = new Vector3f(velocityX + 10, 0, velocityZ);
//				physicsObject.setLinearVelocity(v);
////				System.out.println("velocity: "
////						+ physicsObject.getLinearVelocity() + " helper "
////						+ velocityhelper);
//			} else {
//				Vector3f v;
//				if (lastposition == physicsObject.getPosition().x) {
//					velocityhelper = velocityhelper + 10;
//					velocityZ = velocityhelper;
//					v = new Vector3f(velocityX, 0, velocityZ);
//				} else {
//					lastposition = physicsObject.getPosition().x;
//					v = new Vector3f(velocityX + 10, 0, velocityZ);
//					physicsObject.setLinearVelocity(v);
//					velocityhelper = 0;
//				}
//			}
//			System.out.println("Ant: " + physicsObject.getPosition().x
//					+ " lastposition: " + lastposition + " velocity: "
//					+ physicsObject.getLinearVelocity() + " helper "
//					+ velocityhelper);

		
		reactSpecific(staticPhysicsObject);
	}
	
	/**Allows different ant types to react differently.
	 * @param staticPhysicsObject
	 */
	public abstract void reactSpecific(StaticPhysicsObject staticPhysicsObject);

	@Override
	public void react(DynamicPhysicsObject dynamicPhysicsObject) {
		
		ObjectType tp = Entity.physicsObjectTypeMap.get(dynamicPhysicsObject);
		if (tp.equals(ObjectType.ANT)) { //ant hit another ant: start dodging procedure
			movementManager.addMovementEntry(physicsObject, new Dodge(physicsObject, dynamicPhysicsObject, Globals.ANT_SPEED));
		} else if (tp.equals(ObjectType.ENEMY)) {
//			attackEnemy(dynamicPhysicsObject);
		}
		reactSpecific(dynamicPhysicsObject);
		
		
//		System.out.println("dynamisch");
//		ObjectType tp = Entity.physicsObjectTypeMap.get(dynamicPhysicsObject);
//		if (tp.equals(ObjectType.ANT)) {
//			// interacting with ghost, the ant must not stray from its path!
//			// think up smthg to handle it
//			Vector3f v = new Vector3f(velocityX, 0, 0);
//			physicsObject.setLinearVelocity(v);
//			v = new Vector3f(0, 0, velocityZ);
//			dynamicPhysicsObject.setLinearVelocity(v);
//		} else if (tp.equals(ObjectType.ENEMY)) {
//			attackEnemy(dynamicPhysicsObject);
//		}
//		reactSpecific(dynamicPhysicsObject);
	}
	
	/**Allows different ant types to react differently.
	 * @param dynamicPhysicsObject
	 */
	public abstract void reactSpecific(DynamicPhysicsObject dynamicPhysicsObject);

	@Override
	public void react(GhostPhysicsObject ghostPhysicsObject) {
		//common reacting behaviour of all ant types
		reactSpecific(ghostPhysicsObject);
	}
	
	/**Allows different ant types to react differently.
	 * @param ghostPhysicsObject
	 */
	public abstract void reactSpecific(GhostPhysicsObject ghostPhysicsObject); 
	
	@Override
	public void react(TerrainPhysicsObject terrainPhysicsObject) {
		// TODO Auto-generated method stub
		
	}

	public void move() {
		// check Odor stacks
		/*
		 * if (oderStacks > 0){ followTrail(); }else{ randomMove(x,y) }
		 */

	}

	public void attackEnemy(DynamicPhysicsObject dynamicPhysicsObject) {

	}

	@EventListener(priority = EventPriority.NORMAL)
	public void decideEvent(CollisionEvent ce) {
		if (!ce.getPhyObj1().getType().equals("terrain") && !ce.getPhyObj2().getType().equals("terrain")) {
//			 System.out.println("Ant: in decideEvent\nAnt:" + physicsObject +
//			 "\nPhyObj1: " + ce.getPhyObj1().getType() + "\nPhyObj2: " + ce.getPhyObj2().getType() + " with ctr " + MainApplication.getInstance().getCycleCtr());
			if (ce.getPhyObj1().equals(physicsObject)) {
				ce.getPhyObj2().receive(this);
				ce.consume();
			} else if (ce.getPhyObj2().equals(physicsObject)) {
				ce.getPhyObj1().receive(this);
				ce.consume();
			}
		}
	}

	public void eat() {
		hunger = hunger + 40;
		hive.takeFood(1);
	}

	public int getOdorStatus() {
		return odorStatus;
	}

	public void setOdorStatus(int odorStatus) {
		this.odorStatus = odorStatus;
	}

	public float getHp() {
		return hp;
	}

	public void setHp(float hp) {
		this.hp = hp;
		if(hp == 1){
			spawnDeadAnt();
		}
	}

	public float getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getHunger() {
		return hunger;
	}

	public void setHunger(int hunger) {
		this.hunger = hunger;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}
	
	public void unlockLocator() {
		System.out.println("unlocking locator: " + lockedLocator);
		//return to default movement behavior - this is just a dummy
//		System.out.println("unlocked locator " + lockedLocator + " for " + this);
		if (lockedLocator != null) {
			lockedLocator.unregisterAnt(this);
			lockedLocator = null;
		}
	}
	
	public static void printAllPositionLocatorsAndPheromones() {
		for (Ant ant : ants) {
			System.out.println("Ant " + ant);
			System.out.println("position locators: ");
			for (PositionLocator loc : ant.positionLocators.getUnmodifiableList()) {
				System.out.println(loc);
			}
			System.out.println("pheromones: ");
			for (Pheromone pher : ant.pheromones.getUnmodifiableList()) {
				System.out.println(pher);
			}
			System.out.println();
		}
	}
	
	/**Called at the end of every update, after events have been processed, to identify if an ant left some pheromones.
	 * 
	 */
	public void resetPheromones() {
		pheromones.update();
	}
	
	/**Called at the end of every update, after events have been processed, to identify if an ant left some PositionLocators.
	 * 
	 */
	public void resetPositionLocators() {
		for (PositionLocator loc : positionLocators.update()) {
			if (loc != null) {
				if (loc.equals(lockedLocator)) {
					unlockLocator();
				} else {
					loc.unregisterAnt(this);
//					System.out.println("unregisterd " + this + " in " + loc);
				}
			} else {
				unlockLocator();
			}
		};
	}
	
	public static void resetPheromonesAndPositionLocators() {
		for (Ant ant : ants) {
			ant.resetPheromones();
			ant.resetPositionLocators();
		}
	}

	@Override
	protected void deleteSpecific() {
		unlockLocator();
		dynamicEntities.remove(this);
		ants.remove(this);
	}
	
	public void fight(float damage) {
		hp -= damage;
		System.out.println(this + " has " + hp + " health left");
		if (hp <= 0) {
			delete(true);
		}
	}
	
	private void spawnDeadAnt() {
		org.lwjgl.util.vector.Vector3f pos = Maths.vec3fToSlickUtil(physicsObject.getPosition());
		Quat4f rot = physicsObject.getRotationQuaternions();
		delete(true);
		Hive.getInstance().addDeleteAnt(this);
		
		Entity movingEntity = MainApplication.getInstance().getDefaultEntityBuilder().setFactory(StaticPhysicsObjectFactory.getInstance())
				.setPosition(pos) //position will be set later anyway in main loop according to mouse position
				.setRotation(rot)
				.buildGraphicsEntity("deadAnt", 1, 10)
				.setObjectType(ObjectType.FOOD)
				.buildPhysicsObject()
				.registerResult();
	}

	public Entity layPheromones() {
		org.lwjgl.util.vector.Vector3f pos = Maths.vec3fToSlickUtil(physicsObject.getPosition());
		Quat4f rot = physicsObject.getRotationQuaternions();
		Entity pheromone = MainApplication.getInstance().getDefaultEntityBuilder().setFactory(GhostPhysicsObjectFactory.getInstance())
				.setPosition(pos)
				.buildGraphicsEntity("pheromone", 1, 50) //enable for debugging just to visualize the pheromones
				.buildPhysicsObject()
				.registerResult();
		return pheromone;
	}
}
