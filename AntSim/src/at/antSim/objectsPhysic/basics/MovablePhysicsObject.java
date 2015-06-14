package at.antSim.objectsPhysic.basics;

import javax.vecmath.Vector3f;

/**
 * Created on 18.05.2015.
 *
 * @author Clemens
 */
public interface MovablePhysicsObject {

	/**
	 * Sets linear velocity of Object. (Defines move speed.)
	 *
	 * @param linearVelocity
	 */
	void setLinearVelocity(Vector3f linearVelocity);

	/**
	 * Sets angular Velocity of Object. (Defines rotation speed.)
	 * @param angularVelocity
	 */
	void setAngularVelocity(Vector3f angularVelocity);
	
	/**Moves physicsObject into given direction at desired speed and aligns the object accordingly.
	 * 
	 * @param direction
	 * @param speed
	 */
	void setAlignedMovement(Vector3f direction, float speed);

}
