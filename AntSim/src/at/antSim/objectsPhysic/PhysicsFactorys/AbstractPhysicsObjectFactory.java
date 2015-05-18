package at.antSim.objectsPhysic.PhysicsFactorys;

import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObjectOrientation;
import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

import javax.vecmath.Vector3f;

/**
 * Created on 18.05.2015.
 *
 * @author Clemens
 */
public abstract class AbstractPhysicsObjectFactory<E extends PhysicsObject> implements PhysicsObjectFactory<E> {

	RigidBody createSphereRigid(float mass, float radius, Transform position) {
		MotionState motionState = new DefaultMotionState(position);
		CollisionShape shape = createSphereShape(radius);
		return new RigidBody(mass, motionState, shape);
	}

	CollisionShape createSphereShape(float radius) {
		return new SphereShape(radius);
	}

	RigidBody createCuboidRigid(float mass, float xLength, float yLength, float zLength, Transform position) {
		MotionState motionState = new DefaultMotionState(position);
		CollisionShape shape = createCuboidShape(xLength, yLength, zLength);
		return new RigidBody(mass, motionState, shape);
	}

	CollisionShape createCuboidShape(float xLength, float yLength, float zLength) {
		return new BoxShape(new Vector3f(xLength / 2, yLength / 2, zLength / 2));
	}

	RigidBody createCylinderRigid(float mass, float height, float radius, PhysicsObjectOrientation orientation, Transform position) {
		MotionState motionState = new DefaultMotionState(position);
		CollisionShape shape = createCylinderShape(height, radius, orientation);
		return new RigidBody(mass, motionState, shape);
	}

	CollisionShape createCylinderShape(float height, float radius, PhysicsObjectOrientation orientation) {
		switch (orientation) {
			case X:
				return new CylinderShapeX(new Vector3f(height / 2, radius, 0));
			case Y:
				return new CylinderShape(new Vector3f(radius, height / 2, 0));
			case Z:
				return new CylinderShapeZ(new Vector3f(radius, 0, height / 2));
			default:
				throw new IllegalArgumentException("Invalid Orientation");
		}
	}

	RigidBody createConeRigid(float mass, float height, float radius, PhysicsObjectOrientation orientation, Transform position) {
		MotionState motionState = new DefaultMotionState();
		CollisionShape shape = createConeShape(height, radius, orientation);
		return new RigidBody(mass, motionState, shape);
	}

	CollisionShape createConeShape(float height, float radius, PhysicsObjectOrientation orientation) {
		switch (orientation) {
			case X:
				return new ConeShapeX(radius, height);
			case Y:
				return new ConeShape(radius, height);
			case Z:
				return new ConeShapeZ(radius, height);
			default:
				throw new IllegalArgumentException("Invalid Orientation");
		}
	}

}
