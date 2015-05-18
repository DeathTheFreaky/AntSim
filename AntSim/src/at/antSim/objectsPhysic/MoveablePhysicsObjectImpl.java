package at.antSim.objectsPhysic;

import at.antSim.objectsPhysic.basics.MoveablePhysicsObject;

import javax.vecmath.Vector3f;

/**
 * Created on 18.05.2015.
 *
 * @author Clemens
 */
public abstract class MoveablePhysicsObjectImpl extends PositionablePhysicsObjectImpl implements MoveablePhysicsObject {
	@Override
	public void setLinearVelocity(Vector3f linearVelocity) {
		getRigidBody().setLinearVelocity(linearVelocity);
	}

	@Override
	public void setAngularVelocity(Vector3f angularVelocity) {
		getRigidBody().setAngularVelocity(angularVelocity);
	}
}
