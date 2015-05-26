package at.antSim.objectsPhysic.basics;

import at.antSim.objectsKI.Entity;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.dynamics.RigidBody;

/**
 * Created on 12.05.2015.
 *
 * @author Clemens
 */
public interface PhysicsObject {
	/**
	 * @return Returns the {@link RigidBody} represented in the PhysicsObject
	 */
	CollisionObject getCollisionBody();

	void receive(Entity entity);

}
