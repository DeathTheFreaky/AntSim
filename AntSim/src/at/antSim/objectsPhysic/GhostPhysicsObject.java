package at.antSim.objectsPhysic;

import at.antSim.objectsKI.Entity;
import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.GhostObject;

/**
 * Created on 18.05.2015.
 *
 * @author Clemens
 */
public class GhostPhysicsObject extends PositionablePhysicsObjectImpl {

	final CollisionObject body;
	final String type;

	public GhostPhysicsObject(GhostObject body, String type) {
		this.body = body;
		this.type = type;
		this.body.setCollisionFlags(this.body.getCollisionFlags() | CollisionFlags.NO_CONTACT_RESPONSE);
		this.body.setUserPointer(this);
	}

	@Override
	public CollisionObject getCollisionBody() {
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
