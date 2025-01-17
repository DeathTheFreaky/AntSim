package at.antSim.objectsPhysic.basics;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

/**
 * Created on 12.05.2015.
 *
 * @author Clemens
 */
public interface ReadOnlyPhysicsObject {
	
	/**
	 * @return Returns the position of the Object represented as {@link Vector3f vector}
	 */
	Vector3f getPosition();

	/**
	 * @return Returns the linear velocity of the Object represented as {@link Vector3f vector}
	 */
	Vector3f getLinearVelocity();

	/**
	 * @return Returns the rotation of the object represented as {@link Quat4f quaternion}
	 */
	Quat4f getRotationQuaternions();
	
	/**
	 * @return Returns the rotation of the object represented as {@link Vector3f} in degrees.
	 */
	Vector3f getRotationDegrees();
	
	/**
	 * @return Returns the rotation of the object represented as {@link Vector3f} in radians.
	 */
	Vector3f getRotationRadians();

	/**
	 * @return Returns the angular velocity of the Object represented as {@link Vector3f vector}
	 */
	Vector3f getAngularVelocity();
}
