package at.antSim.objectsKI;

import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObject;

/**For ants...
 * 
 * @author Flo
 *
 */
public class Ant extends Entity {

	public Ant(GraphicsEntity graphicsEntity,
			PhysicsObject physicsObject) {
		super(graphicsEntity, physicsObject, ObjectType.ANT);
	}

	@Override
	public void react(StaticPhysicsObject staticPhysicsObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void react(DynamicPhysicsObject dynamicPhysicsObject) {
		// TODO Auto-generated method stub
		
	}

}
