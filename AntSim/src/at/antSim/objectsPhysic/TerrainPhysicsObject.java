package at.antSim.objectsPhysic;

import at.antSim.Globals;
import at.antSim.objectsKI.Entity;

import com.bulletphysics.collision.broadphase.CollisionFilterGroups;
import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.dynamics.RigidBody;

/**
 * Created on 18.05.2015.
 *
 * @author Clemens
 */
public class TerrainPhysicsObject extends PositionablePhysicsObjectImpl {

	final RigidBody body;
	final String type;
	short collisionFilterGroup;
	short collisionFilterMask;

	public TerrainPhysicsObject(RigidBody body, String type) {
		this.body = body;
		this.type = type;
		this.body.setCollisionFlags(this.body.getCollisionFlags() & CollisionFlags.STATIC_OBJECT);
		this.body.setUserPointer(this);
		this.collisionFilterGroup = Globals.COL_TERRAIN;
		short tempFilterMask = 0;
		tempFilterMask = (short) (tempFilterMask | Globals.COL_KINEMATIC);
		this.collisionFilterMask = tempFilterMask;
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

	@Override
	public short getCollisionFilterGroup() {
		return collisionFilterGroup;
	}

	@Override
	public short getCollisionFilterMask() {
		return collisionFilterMask;
	}
	
	@Override
	public void setCollisionFilterGroup(short collisionFilterGroup) {
		this.collisionFilterGroup = collisionFilterGroup;
	}

	@Override
	public void setCollisionFilterMask(short collisionFilterMask) {
		this.collisionFilterMask = collisionFilterMask;
	}
}
