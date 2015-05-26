package at.antSim.objectsPhysic;

import at.antSim.objectsKI.Entity;
import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.dynamics.RigidBody;

/**
 * Created on 18.05.2015.
 *
 * @author Clemens
 */
public class DynamicPhysicsObject extends MovablePhysicsObjectImpl {

	final RigidBody body;

	public DynamicPhysicsObject(RigidBody body) {
		this.body = body;
		this.body.setCollisionFlags(this.body.getCollisionFlags() | CollisionFlags.KINEMATIC_OBJECT);
	}

	@Override
	public RigidBody getCollisionBody() {
		return body;
	}

	@Override
	public void receive(Entity entity) {
		entity.react(this);
	}
}
