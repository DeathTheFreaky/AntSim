package at.antSim.objectsPhysic;

import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;
import com.bulletphysics.linearmath.Transform;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

/**
 * Created on 12.05.2015.
 *
 * @author Clemens
 */
public abstract class ReadOnlyPhysicsObjectImpl implements PhysicsObject, ReadOnlyPhysicsObject {

	@Override
	public Vector3f getPosition() {
		Vector3f out = new Vector3f();
		getRigidBody().getMotionState().getWorldTransform(new Transform(new Matrix4f(new Quat4f(), out, 1)));
		return out;
	}

	@Override
	public Vector3f getLinearVelocity() {
		Vector3f out = new Vector3f();
		getRigidBody().getLinearVelocity(out);
		return out;
	}

	@Override
	public Quat4f getRotation() {
		Quat4f out = new Quat4f();
		getRigidBody().getMotionState().getWorldTransform(new Transform(new Matrix4f(out, new Vector3f(), 1)));
		return out;
	}

	@Override
	public Vector3f getAngularVelocity() {
		Vector3f out = new Vector3f();
		getRigidBody().getAngularVelocity(out);
		return out;
	}
}
