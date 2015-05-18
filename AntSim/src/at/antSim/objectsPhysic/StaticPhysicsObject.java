package at.antSim.objectsPhysic;

import com.bulletphysics.dynamics.RigidBody;

/**
 * Created on 18.05.2015.
 *
 * @author Clemens
 */
public class StaticPhysicsObject extends PositionablePhysicsObjectImpl {

	final RigidBody body;

	public StaticPhysicsObject(RigidBody body) {
		this.body = body;
	}

	@Override
	public RigidBody getRigidBody() {
		return body;
	}
}
