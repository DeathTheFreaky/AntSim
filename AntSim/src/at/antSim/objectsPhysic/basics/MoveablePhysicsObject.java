package at.antSim.objectsPhysic.basics;

import javax.vecmath.Vector3f;

/**
 * Created on 18.05.2015.
 *
 * @author Clemens
 */
public interface MoveablePhysicsObject {

	void setLinearVelocity(Vector3f linearVelocity);

	void setAngularVelocity(Vector3f angularVelocity);

}
