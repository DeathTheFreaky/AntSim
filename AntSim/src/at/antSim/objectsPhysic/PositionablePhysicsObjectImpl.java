package at.antSim.objectsPhysic;

import at.antSim.objectsPhysic.basics.PositionablePhysicsObject;
import com.bulletphysics.linearmath.QuaternionUtil;
import com.bulletphysics.linearmath.Transform;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

/**
 * Created on 12.05.2015.
 *
 * @author Clemens
 */
public abstract class PositionablePhysicsObjectImpl extends ReadOnlyPhysicsObjectImpl implements PositionablePhysicsObject {

	@Override
	public void setPosition(float posX, float posZ, float posY) {
		setPosition(new Vector3f(posX, posY, posZ));
	}

	@Override
	public void setPosition(Vector3f position) {
		Transform bodyTransform = new Transform();
		getRigidBody().getMotionState().getWorldTransform(bodyTransform);
		Quat4f bodyRotation = new Quat4f();
		bodyTransform.getRotation(bodyRotation);
		getRigidBody().getMotionState().setWorldTransform(new Transform(new Matrix4f(bodyRotation, position, 1)));
	}

	@Override
	public void setRotation(float yaw, float pitch, float roll) {
		Quat4f rotation = new Quat4f();
		QuaternionUtil.setEuler(rotation, yaw, pitch, roll);
		Transform bodyTransform = new Transform();
		getRigidBody().getMotionState().getWorldTransform(bodyTransform);
		bodyTransform.setRotation(rotation);
		getRigidBody().getMotionState().setWorldTransform(bodyTransform);
	}
}
