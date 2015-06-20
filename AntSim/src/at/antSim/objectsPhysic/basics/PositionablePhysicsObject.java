package at.antSim.objectsPhysic.basics;

import javax.vecmath.Vector3f;

/**
 * Created on 12.05.2015.
 *
 * @author Clemens
 */
public interface PositionablePhysicsObject {
	/**
	 * Sets the Position of the Object.
	 * @param posX
	 * @param posZ
	 * @param posY
	 */
	void setPosition(float posX, float posZ, float posY);

	/**
	 * Sets the Position of the Object.
	 * @param position
	 */
	void setPosition(Vector3f position);

	/**
	 * Sets the Rotation of the Object.
	 * @param yaw
	 * @param pitch
	 * @param roll
	 */
	void setRotation(float yaw, float pitch, float roll);
}
