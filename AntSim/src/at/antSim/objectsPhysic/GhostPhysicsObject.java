package at.antSim.objectsPhysic;

import at.antSim.objectsKI.Entity;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.GhostObject;

/**
 * Created on 18.05.2015.
 *
 * @author Clemens
 */
public class GhostPhysicsObject extends PositionablePhysicsObjectImpl {

	final CollisionObject body;

	public GhostPhysicsObject(GhostObject body) {
		this.body = body;
	}

	@Override
	public CollisionObject getCollisionBody() {
		return body;
	}

	@Override
	public void receive(Entity entity) {
		entity.react(this);
	}
}
