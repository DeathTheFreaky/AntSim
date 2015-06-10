package at.antSim.objectsPhysic;

import at.antSim.objectsKI.Entity;
import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.GhostObject;

import javax.vecmath.Vector3f;

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
		this.body.setCollisionFlags(CollisionFlags.NO_CONTACT_RESPONSE);
//		this.body.setCollisionFlags(this.body.getCollisionFlags() & CollisionFlags.NO_CONTACT_RESPONSE);
		System.out.println("Flags creating: " + this.body.getCollisionFlags());
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
	public void setPosition(float posX, float posZ, float posY) {
		setPosition(new Vector3f(posX, posY, posZ));
	}

	@Override
	public void setPosition(Vector3f position) {
		PhysicsManager physicsManager = PhysicsManager.getInstance();
		physicsManager.unregisterPhysicsObject(this);
		super.setPosition(position);
		physicsManager.registerPhysicsObject(this);
	}

	@Override
	public void setRotation(float yaw, float pitch, float roll) {
		PhysicsManager physicsManager = PhysicsManager.getInstance();
		physicsManager.unregisterPhysicsObject(this);
		super.setRotation(yaw, pitch, roll);
		physicsManager.registerPhysicsObject(this);
	}

	@Override
	public String getType() {
		return type;
	}
}
