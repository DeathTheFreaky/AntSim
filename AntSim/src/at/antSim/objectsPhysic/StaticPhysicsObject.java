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
	final String type;

	public StaticPhysicsObject(RigidBody body, String type) {
		this.body = body;
		this.type = type;
		this.body.setCollisionFlags(this.body.getCollisionFlags() & CollisionFlags.STATIC_OBJECT);
		this.body.setUserPointer(this);
	}

	@Override
	public RigidBody getCollisionBody() {
		return body;
	}

	@Override
	public void receive(Entity entity) {
		entity.react(this);
	}

	@Override
	public String getType() {
		return type;
	}
}
