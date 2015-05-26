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
		Transform transform = new Transform();
		getRigidBody().getMotionState().getWorldTransform(transform);
		Matrix4f transformMatrix = new Matrix4f();
		transform.getMatrix(transformMatrix);
		out.x = transformMatrix.m03/transformMatrix.m33;
		out.y = transformMatrix.m13/transformMatrix.m33;
		out.z = transformMatrix.m23/transformMatrix.m33;
		//getRigidBody().getMotionState().getWorldTransform(new Transform(new Matrix4f(new Quat4f(), out, 1))); //does not work
		return out;
	}

	@Override
	public Vector3f getLinearVelocity() {
		Vector3f out = new Vector3f();
		getRigidBody().getLinearVelocity(out);
		return out;
	}

	@Override
	public Quat4f getRotationQuaternions() {
		Quat4f out = new Quat4f();
		Transform transform = new Transform();
		getRigidBody().getMotionState().getWorldTransform(transform);
		transform.getRotation(out);
//		getRigidBody().getMotionState().getWorldTransform(new Transform(new Matrix4f(out, new Vector3f(), 1))); //does not work
		return out;
	}
	
	@Override
	public Vector3f getRotationAngles() {
		//quaternions to euler angles: http://gamedev.stackexchange.com/questions/80831/jbullet-quaternion-to-euler-angle-conversion-causes-strange-flipping-behavior
		Quat4f q = new Quat4f();
		getRigidBody().getOrientation(q);
		float roll = (float) Math.atan2(2d * (q.x * q.y + q.w * q.z), (double) q.w * q.w + q.x * q.x - q.y * q.y - q.z * q.z);
		float pitch = (float) Math.atan2(2d * (q.y * q.z + q.w * q.x), (double) q.w * q.w - q.x * q.x - q.y * q.y + q.z * q.z);
		float yaw = (float) Math.asin(-2d * (q.x * q.z - q.w * q.y));
		Vector3f out = new Vector3f((float) Math.toDegrees(pitch), (float) Math.toDegrees(yaw), (float) Math.toDegrees(roll));
		return out;
	}

	@Override
	public Vector3f getAngularVelocity() {
		Vector3f out = new Vector3f();
		getRigidBody().getAngularVelocity(out);
		return out;
	}
}
