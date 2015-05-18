package at.antSim.objectsPhysic;

import at.antSim.objectsPhysic.basics.PositionablePhysicsObject;
import com.bulletphysics.dynamics.RigidBody;
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
public class StaticPhysicsObject extends ReadOnlyStaticPhysicsObject implements PositionablePhysicsObject {

	private final RigidBody body;

	/**
	 * Creates an PhysicsObject representing the passed RigidBody
	 *
	 * @param body
	 */
	public StaticPhysicsObject(RigidBody body) {
		this.body = body;
	}

	@Override
	public RigidBody getRigidBody() {
		return body;
	}

	@Override
	public void setPosition(float posX, float posZ, float posY) {
		setPosition(new Vector3f(posX, posY, posZ));
	}

	@Override
	public void setPosition(Vector3f position) {
		Transform bodyTransform = new Transform();
		body.getMotionState().getWorldTransform(bodyTransform);
		Quat4f bodyRotation = new Quat4f();
		bodyTransform.getRotation(bodyRotation);
		body.getMotionState().setWorldTransform(new Transform(new Matrix4f(bodyRotation, position, 1)));
	}

	@Override
	public void setRotation(float yaw, float pitch, float roll) {
		Quat4f rotation = new Quat4f();
		QuaternionUtil.setEuler(rotation, yaw, pitch, roll);
		Transform bodyTransform = new Transform();
		body.getMotionState().getWorldTransform(bodyTransform);
		bodyTransform.setRotation(rotation);
		body.getMotionState().setWorldTransform(bodyTransform);
	}
}
