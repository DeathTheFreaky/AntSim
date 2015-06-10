package at.antSim.objectsKI;

import at.antSim.eventSystem.EventListener;
import at.antSim.eventSystem.EventPriority;
import at.antSim.eventSystem.events.CollisionEvent;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.PhysicsManager;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.TerrainPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObject;

/**Used for moving entity, just to ensure that no ki based behaviour is triggered for a moving entity.
 * 
 * @author Flo
 *
 */
public class Moving extends Entity {

	public Moving(GraphicsEntity graphicsEntity, PhysicsObject physicsObject) {
		super(graphicsEntity, physicsObject, ObjectType.MOVING);
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
}
