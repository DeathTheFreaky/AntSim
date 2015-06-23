package at.antSim.objectsKI;

import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Vector3f;

import com.bulletphysics.linearmath.Transform;

import at.antSim.Globals;
import at.antSim.MainApplication;
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
import at.antSim.objectsPhysic.Movement.BorderCollisionMovement;
import at.antSim.objectsPhysic.Movement.Dodge;
import at.antSim.objectsPhysic.Movement.MoveInDirection;
import at.antSim.objectsPhysic.Movement.MovementManager;
import at.antSim.objectsPhysic.Movement.MovementModeType;
import at.antSim.objectsPhysic.PhysicsFactorys.GhostPhysicsObjectFactory;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;
import at.antSim.utils.Maths;

/**For enemies...
 * 
 * @author Flo
 *
 */
public abstract class Enemy extends Entity {
	
	static final List<Enemy> enemies = new LinkedList<>(); //used to delete all entities
	PositionLocator positionLocator;
	
	DynamicPhysicsObject physicsObject;
	
	float hp;
	float attack;
	
	MovementManager movementManager;

	public Enemy(GraphicsEntity graphicsEntity, PhysicsObject physicsObject) {
		super(graphicsEntity, physicsObject, ObjectType.ENEMY);
		
		this.physicsObject = (DynamicPhysicsObject) physicsObject;
		
		//set initial position and size of PositionLocator ghost physics object
		javax.vecmath.Vector3f vecMathPos = ((ReadOnlyPhysicsObject) physicsObject).getPosition();
		vecMathPos.y = vecMathPos.y - Globals.POSITION_LOCATOR_MARGIN;
	
		positionLocator = (PositionLocator) MainApplication.getInstance().getDefaultEntityBuilder().setFactory(GhostPhysicsObjectFactory.getInstance())
				.setPosition(new org.lwjgl.util.vector.Vector3f(vecMathPos.x, vecMathPos.y, vecMathPos.z))
				.buildGraphicsEntity("positionLocator", 1, graphicsEntity.getScale() + Globals.POSITION_LOCATOR_MARGIN * 2) 
				.setTarget(this)
				.buildPhysicsObject()
				.registerResult();
				
		dynamicEntities.add(this);
		enemies.add(this);
		
		movementManager = MovementManager.getInstance();
				
		Vector3f dir = new Vector3f(1,0,0);
		Vector3f rotation = this.physicsObject.getRotationDegrees();
		if (rotation.x == 180) {
			dir.x = -1;
		}
		Vector3f v = Maths.turnDirectionVector(dir, (int) this.physicsObject.getRotationDegrees().y);
		EventManager.getInstance().registerEventListener(this);
		movementManager = MovementManager.getInstance();
		movementManager.addMovementEntry((DynamicPhysicsObject) physicsObject, new MoveInDirection((DynamicPhysicsObject) physicsObject, v, Globals.ANT_SPEED));
	}

	@Override
	public void react(StaticPhysicsObject staticPhysicsObject) {
				
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
	}

	@Override
	public void react(DynamicPhysicsObject dynamicPhysicsObject) {
		
//		if (Entity.parentingEntities.get(dynamicPhysicsObject).objectType == ObjectType.ANT) {
//			Ant ant = (Ant) Entity.parentingEntities.get(dynamicPhysicsObject);
//			ant.fight(attack);
//			fight(ant.attack);
//		}
	}

	@Override
	public void react(GhostPhysicsObject ghostPhysicsObject) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void react(TerrainPhysicsObject terrainPhysicsObject) {
		// TODO Auto-generated method stub
		
	}
	
	@EventListener(priority = EventPriority.NORMAL)
	public void decideEvent(CollisionEvent ce) {
		if (!ce.getPhyObj1().getType().equals("terrain") && !ce.getPhyObj2().getType().equals("terrain")) {
//			 System.out.println("Ant: in decideEvent\nAnt:" + physicsObject +
//			 "\nPhyObj1: " + ce.getPhyObj1().getType() + "\nPhyObj2: " + ce.getPhyObj2().getType() + " with ctr " + MainApplication.getInstance().getCycleCtr());
			
			boolean hitAnt = false;
			
			if (Entity.parentingEntities.containsKey(ce.getPhyObj1())) {
				if (Entity.parentingEntities.get(ce.getPhyObj1()).objectType == ObjectType.ANT) {
					hitAnt = true;
				}
			}
			if (Entity.parentingEntities.containsKey(ce.getPhyObj2())) {
				if (Entity.parentingEntities.get(ce.getPhyObj2()).objectType == ObjectType.ANT) {
					hitAnt = true;
				}
			}
			
			if (hitAnt) { //let ants handle the fighting
				return;
			}
			
			if (ce.getPhyObj1().equals(physicsObject)) {
				ce.getPhyObj2().receive(this);
				ce.consume();
			} else if (ce.getPhyObj2().equals(physicsObject)) {
				ce.getPhyObj1().receive(this);
				ce.consume();
			}
		}
	}
	
	/**Update PositionLocators.
	 * 
	 */
	public static void updatePositionLocators() {
		for (Enemy enemy : enemies) {
			enemy.positionLocator.updatePosition();
		}
	}

	@Override
	protected void deleteSpecific() {
		positionLocator.delete(true);
		dynamicEntities.remove(this);
		enemies.remove(this);
	}

	public void fight(float damage) {
		hp -= damage;
		System.out.println(this + " has " + hp + " health left");
		if (hp <= 0) {
			delete(true);
		}
	}
}
