package at.antSim.objectsKI;

import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.GhostPhysicsObject;
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

	public abstract void react(StaticPhysicsObject staticPhysicsObject);

	public abstract void react(DynamicPhysicsObject dynamicPhysicsObject);

	public abstract void react(GhostPhysicsObject ghostPhysicsObject);

}
