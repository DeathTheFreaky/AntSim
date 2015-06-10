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

	String getType();
	
	/**
	 * @return - the Physic Object's Collision Filter
	 */
	short getCollisionFilterGroup();
	
	/**
	 * @return - the Physic Object's Filter Mask
	 */
	short getCollisionFilterMask();
	
	/**Sets the PhyiscsObject's collisionFilterGroup (to use non-default, like for MovingEntity).
	 * @param collisionFilterGroup
	 */
	void setCollisionFilterGroup(short collisionFilterGroup);
	
	/**Sets the PhyiscsObject's collisionFilterMask (to use non-default, like for MovingEntity).
	 * @param collisionFilterMask
	 */
	void setCollisionFilterMask(short collisionFilterMask);

}