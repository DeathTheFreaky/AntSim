package at.antSim.objectsKI;

import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObject;

/**For fauna and flora...
 * 
 * @author Flo
 *
 */
public class EnvironmentObject extends Entity {

	public EnvironmentObject(GraphicsEntity graphicsEntity,
			PhysicsObject physicsObject) {
		super(graphicsEntity, physicsObject, ObjectType.ENVIRONMENT);
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