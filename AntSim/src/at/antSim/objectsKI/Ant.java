package at.antSim.objectsKI;

import javax.vecmath.Vector3f;

import at.antSim.eventSystem.EventListener;
import at.antSim.eventSystem.EventManager;
import at.antSim.eventSystem.EventPriority;
import at.antSim.eventSystem.events.CollisionEvent;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.PhysicsManager;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.TerrainPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObject;

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
public class Ant extends Entity {
	// search 1, food 2, war 3, nothing 0
	private int odorStatus = 0;
	private int hp;
	private int attack;
	// saturate?
	private int hunger;
	private DynamicPhysicsObject physicsObject = null;
	private int velocityX = 10;
	private int velocityZ = 20;
	private float lastposition = 0;
	private int velocityhelper = 0;

	// wahrscheinlich eigene Jobklasse => fuer im Bautätige
	// und Worker/Forager
	private String job;

	public Ant(GraphicsEntity graphicsEntity, PhysicsObject physicsObject) {
		super(graphicsEntity, physicsObject, ObjectType.ANT);
		this.physicsObject = (DynamicPhysicsObject) physicsObject;
		Vector3f v = new Vector3f(velocityX, 0, velocityZ);
		this.physicsObject.setLinearVelocity(v);
		dynamicEntities.add(this);
		// ROTATE WITH THIS Math.toradiant();
		//this.physicsObject.setRotation(0, 0, 0);
		EventManager.getInstance().registerEventListener(this);
	}

	@Override
	public void react(StaticPhysicsObject staticPhysicsObject) {
		// System.out.println("static");
		//Colliding with the ground/terrain
		
		// example
		// if(physicsObject.getPosition().y <
		// staticPhysicsObject.getPosition().y){
		if (staticPhysicsObject.getPosition().y == 0) {
			if (physicsObject.getLinearVelocity().x < velocityX
					|| physicsObject.getLinearVelocity().z < velocityZ) {
				Vector3f v = new Vector3f(velocityX, 0, velocityZ);
				physicsObject.setLinearVelocity(v);
//				 System.out.println("Ant: " + physicsObject.getPosition() +
//				 " Ground: " + staticPhysicsObject.getPosition());
			}
		} else {
			// Not colliding with the ground but something else like a tree 
			if (physicsObject.getPosition().x > staticPhysicsObject
					.getPosition().x) {
				Vector3f v;
				v = new Vector3f(velocityX + 10, 0, velocityZ);
				physicsObject.setLinearVelocity(v);
//				System.out.println("velocity: "
//						+ physicsObject.getLinearVelocity() + " helper "
//						+ velocityhelper);
			} else {
				Vector3f v;
				if (lastposition == physicsObject.getPosition().x) {
					velocityhelper = velocityhelper + 10;
					velocityZ = velocityhelper;
					v = new Vector3f(velocityX, 0, velocityZ);
				} else {
					lastposition = physicsObject.getPosition().x;
					v = new Vector3f(velocityX + 10, 0, velocityZ);
					physicsObject.setLinearVelocity(v);
					velocityhelper = 0;
				}
			}
//			System.out.println("Ant: " + physicsObject.getPosition().x
//					+ " lastposition: " + lastposition + " velocity: "
//					+ physicsObject.getLinearVelocity() + " helper "
//					+ velocityhelper);

		}
	}

	@Override
	public void react(DynamicPhysicsObject dynamicPhysicsObject) {
//		System.out.println("dynamisch");
		ObjectType tp = Entity.physicsObjectTypeMap.get(dynamicPhysicsObject);
		if (tp.equals(ObjectType.ANT)) {
			// interacting with ghost, the ant must not stray from its path!
			// think up smthg to handle it
			Vector3f v = new Vector3f(velocityX, 0, 0);
			physicsObject.setLinearVelocity(v);
			v = new Vector3f(0, 0, velocityZ);
			dynamicPhysicsObject.setLinearVelocity(v);
		} else if (tp.equals(ObjectType.ENEMY)) {
			attackEnemy(dynamicPhysicsObject);
		}
	}

	@Override
	public void react(GhostPhysicsObject ghostPhysicsObject) {
		if (ghostPhysicsObject.getType().equals("positionLocator")) {
//			System.out.println("i tapped into the sphere of a positionLocator. I need to go to my target at " + ghostPhysicsObject.getPosition());
		}
	}
	
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
//			 "\nPhyObj1: " + ce.getPhyObj1().getType() + "\nPhyObj2: " + ce.getPhyObj2().getType());
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
		Hive.foodStacks = Hive.foodStacks - 1;
	}

	public int getOdorStatus() {
		return odorStatus;
	}

	public void setOdorStatus(int odorStatus) {
		this.odorStatus = odorStatus;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getAttack() {
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
	
	@Override
	public void delete() {
		PhysicsManager.getInstance().unregisterPhysicsObject(physicsObject);
		entities.remove(this);
		dynamicEntities.remove(this);
		physicsObjectTypeMap.remove(this);
		renderingMap.get(graphicsEntity.getModel()).remove(this);
	}
}