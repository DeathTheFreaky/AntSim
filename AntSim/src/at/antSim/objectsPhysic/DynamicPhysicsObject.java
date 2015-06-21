package at.antSim.objectsPhysic;

import javax.vecmath.Vector3f;

import at.antSim.Globals;
import at.antSim.objectsKI.Entity;

import com.bulletphysics.collision.broadphase.CollisionFilterGroups;
import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.dynamics.RigidBody;

/**
 * Created on 18.05.2015.
 *
 * @author Clemens
 */
public class DynamicPhysicsObject extends MovablePhysicsObjectImpl {

	final RigidBody body;
	final String type;
	short collisionFilterGroup;
	short collisionFilterMask;
	
	Vector3f posXDirection = new Vector3f(1,0,0);

	public DynamicPhysicsObject(RigidBody body, String type) {
		this.body = body;
		this.type = type;
		this.body.setCollisionFlags(this.body.getCollisionFlags() & CollisionFlags.KINEMATIC_OBJECT);
		this.body.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
		this.body.setUserPointer(this);
		this.collisionFilterGroup = Globals.COL_KINEMATIC;
		short tempFilterMask = 0;
		tempFilterMask = (short) (tempFilterMask | Globals.COL_KINEMATIC | Globals.COL_STATIC | Globals.COL_SENSOR | Globals.COL_TERRAIN | Globals.COL_MOVING | Globals.COL_BORDER);
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

	@Override
	public void setAlignedMovement(Vector3f direction, float speed) {
		
		//ensure gravity is still applied when setting alignedMovement
		Vector3f currentVelocity = new Vector3f();
		this.getCollisionBody().getLinearVelocity(currentVelocity);
		currentVelocity.y = currentVelocity.y - Globals.gravity/Globals.FPS_CAP; 
		
		if (direction.length() > 0) {
			direction.normalize();
			Vector3f linearVelocity = new Vector3f(direction.x * speed, currentVelocity.y, direction.z * speed);
//			System.out.println("set velocity to  " + linearVelocity);
			setLinearVelocity(linearVelocity);
			//when angles are exactly aligned with axis rotation wont work
			if (direction.x <= 0.000001f && direction.x >= 0) {
				direction.x = 0.000001f;
			} else if (direction.x >= -0.000001f && direction.x < 0) {
				direction.x = -0.000001f;
			}
			if (direction.z <= 0.000001f && direction.z >= 0) {
				direction.z = 0.000001f;
			} else if (direction.z >= -0.000001f && direction.z < 0) {
				direction.z = -0.000001f;
			}

			double alphaRad = direction.dot(posXDirection);
			double alpha = Math.toDegrees(Math.acos(alphaRad));
			//because acos only works between 0 and pi (180 degrees)
			if (direction.z > 0) {
				alpha = alpha * -1;
			}
			if (direction.x < 0) {
				alpha = alpha * -1;
			}
			setRotation((float) alpha, 0, 0);
		} else {
			setLinearVelocity(new Vector3f(0,0,0));
		}
	}

	@Override
	public void setMovement(Vector3f direction, float speed) {
		
		//ensure gravity is still applied when setting alignedMovement
		Vector3f currentVelocity = new Vector3f();
		this.getCollisionBody().getLinearVelocity(currentVelocity);
		currentVelocity.y = currentVelocity.y - Globals.gravity/Globals.FPS_CAP; 
		
		if (direction.length() > 0) {
			direction.normalize();
			Vector3f linearVelocity = new Vector3f(direction.x * speed, currentVelocity.y, direction.z * speed);
			setLinearVelocity(linearVelocity);
		} else {
			setLinearVelocity(new Vector3f(0,0,0));
		}
	}
}
