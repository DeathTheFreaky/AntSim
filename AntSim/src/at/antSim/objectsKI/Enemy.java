package at.antSim.objectsKI;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import com.bulletphysics.linearmath.Transform;

import at.antSim.Globals;
import at.antSim.MainApplication;
import at.antSim.eventSystem.EventListener;
import at.antSim.eventSystem.events.CollisionEvent;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.graphics.graphicsUtils.Maths;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.PhysicsManager;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.TerrainPhysicsObject;
import at.antSim.objectsPhysic.PhysicsFactorys.GhostPhysicsObjectFactory;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;

/**For enemies...
 * 
 * @author Flo
 *
 */
public class Enemy extends Entity {
	
	static final List<Enemy> enemies = new LinkedList<>(); //used to delete all entities
	PositionLocator positionLocator;

	public Enemy(GraphicsEntity graphicsEntity, PhysicsObject physicsObject) {
		super(graphicsEntity, physicsObject, ObjectType.ENEMY);
		
		//set initial position and size of PositionLocator ghost physics object
		javax.vecmath.Vector3f vecMathPos = ((ReadOnlyPhysicsObject) physicsObject).getPosition();
		vecMathPos.y = vecMathPos.y - Globals.POSITION_LOCATOR_MARGIN;
	
		positionLocator = (PositionLocator) MainApplication.getInstance().getDefaultEntityBuilder().setFactory(GhostPhysicsObjectFactory.getInstance())
				.setPosition(new Vector3f(vecMathPos.x, vecMathPos.y, vecMathPos.z))
				.buildGraphicsEntity("positionLocator", 1, graphicsEntity.getScale() + Globals.POSITION_LOCATOR_MARGIN * 2) 
				.setTarget(this)
				.buildPhysicsObject()
				.registerResult();
				
		dynamicEntities.add(this);
		enemies.add(this);
	}

	@Override
	public void react(StaticPhysicsObject staticPhysicsObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void react(DynamicPhysicsObject dynamicPhysicsObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void react(GhostPhysicsObject ghostPhysicsObject) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void react(TerrainPhysicsObject terrainPhysicsObject) {
		// TODO Auto-generated method stub
		
	}
	
	@EventListener
	public void test(CollisionEvent ce) {
		if ((ce.getPhyObj1().equals(physicsObject) && !ce.getPhyObj2().getCollisionBody().isStaticObject()) 
				|| (ce.getPhyObj2().equals(physicsObject) && !ce.getPhyObj1().getCollisionBody().isStaticObject())) {
		
//			System.out.println("      event in enemy: " + ce.getPhyObj1() + " collided with " + ce.getPhyObj2());
//			System.out.println("me: " + physicsObject);
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
		positionLocator.delete();
		dynamicEntities.remove(this);
		enemies.remove(this);
	}
}
