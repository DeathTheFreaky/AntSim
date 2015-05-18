package at.antSim.objectsPhysic.PhysicsFactorys;

import at.antSim.objectsPhysic.basics.PhysicsObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 12.05.2015.
 *
 * @author Clemens
 */
public class PhysicsFactoryCollection {

	private static Map<Class<? extends PhysicsObject>, PhysicsObjectFactory<? extends PhysicsObject>> factorys = new HashMap<>();

	static void register(Class<? extends PhysicsObject> physicsObjectClass, PhysicsObjectFactory<? extends PhysicsObject> physicsObjectFactory) {
		factorys.put(physicsObjectClass, physicsObjectFactory);
	}

	/**
	 * Returns the Factory for the passed PhysicsObject-Class if available.
	 *
	 * @param physicsObjectClass
	 * @return Returns a {@link PhysicsObjectFactory} if and only if a PhysicsObjectFactory was registered for this PhysicsObject-Class, otherwise returns null;
	 */
	public static PhysicsObjectFactory getFactory(Class<? extends PhysicsObject> physicsObjectClass) {
		return factorys.get(physicsObjectClass);
	}

}
