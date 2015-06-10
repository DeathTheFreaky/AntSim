package at.antSim.objectsKI;

import at.antSim.eventSystem.EventListener;
import at.antSim.eventSystem.events.CollisionEvent;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.TerrainPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObject;

/**For enemies...
 * 
 * @author Flo
 *
 */
public class Enemy extends Entity {

	public Enemy(GraphicsEntity graphicsEntity,
			PhysicsObject physicsObject) {
		super(graphicsEntity, physicsObject, ObjectType.ENEMY);
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
		
			System.out.println("      event in enemy: " + ce.getPhyObj1() + " collided with " + ce.getPhyObj2());
			System.out.println("me: " + physicsObject);
		}
	}
}
