package at.antSim.objectsPhysic;

import at.antSim.objectsKI.Entity;
import at.antSim.objectsPhysic.basics.MovablePhysicsObject;

import com.bulletphysics.dynamics.RigidBody;

import javax.vecmath.Vector3f;

/**
 * Created on 18.05.2015.
 *
 * @author Clemens
 */
public abstract class MovablePhysicsObjectImpl extends PositionablePhysicsObjectImpl implements MovablePhysicsObject {
	@Override
	public void setLinearVelocity(Vector3f linearVelocity) {
		RigidBody body = RigidBody.upcast(getCollisionBody());
		if (body != null) {
			body.setLinearVelocity(linearVelocity);
			
			// update triangle positions
			Entity.setTriangleTransforms(this);
		}
	}

	@Override
	public Vector3f getLinearVelocity() {
		RigidBody body = RigidBody.upcast(getCollisionBody());
		Vector3f linearVelocity = new Vector3f();
		if (body != null) {
			return body.getLinearVelocity(linearVelocity);
		}
		return linearVelocity;
	}

	@Override
	public void setAngularVelocity(Vector3f angularVelocity) {
		RigidBody body = RigidBody.upcast(getCollisionBody());
		if (body != null) {
			body.setAngularVelocity(angularVelocity);
			
			// update triangle positions
			Entity.setTriangleTransforms(this);
		}
	}

	@Override
	public Vector3f getAngularVelocity() {
		RigidBody body = RigidBody.upcast(getCollisionBody());
		Vector3f angularVelocity = new Vector3f();
		if (body != null) {
			body.getAngularVelocity(angularVelocity);
		}
		return angularVelocity;
	}
}
