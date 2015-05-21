package at.antSim.objectsKI;

import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 18.05.2015.
 *
 * @author Clemens
 */
public abstract class Entity {

	static final Map<PhysicsObject, ObjectType> physicsObjectTypeMap = new HashMap<PhysicsObject, ObjectType>();
	
	final GraphicsEntity graphicsEntity;
	final PhysicsObject physicsObject;
	
	public Entity (GraphicsEntity graphicsEntity, PhysicsObject physicsObject) {
		this.graphicsEntity = graphicsEntity;
		this.physicsObject = physicsObject;
	}

	public abstract void react(StaticPhysicsObject staticPhysicsObject);

	public abstract void react(DynamicPhysicsObject dynamicPhysicsObject);

}
