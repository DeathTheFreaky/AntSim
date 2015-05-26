package at.antSim.objectsPhysic;

import at.antSim.objectsKI.Entity;
import com.bulletphysics.collision.dispatch.CollisionFlags;
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
		this.body.setCollisionFlags(this.body.getCollisionFlags() | CollisionFlags.STATIC_OBJECT);
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
