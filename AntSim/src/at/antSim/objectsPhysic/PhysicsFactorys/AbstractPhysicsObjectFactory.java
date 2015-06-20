package at.antSim.objectsPhysic.PhysicsFactorys;

import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObjectOrientation;
import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
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
		RigidBodyConstructionInfo info = new RigidBodyConstructionInfo(mass, motionState, shape);
		info.restitution = 0;
		return new RigidBody(info);
	}

	CollisionShape createSphereShape(float radius) {
		return new SphereShape(radius);
	}

	RigidBody createCuboidRigid(float mass, float xLength, float yLength, float zLength, Transform position) {
		MotionState motionState = new DefaultMotionState(position);
		CollisionShape shape = createCuboidShape(xLength, yLength, zLength);
		RigidBodyConstructionInfo info = new RigidBodyConstructionInfo(mass, motionState, shape);
		info.restitution = 0;
		return new RigidBody(info);
	}

	CollisionShape createCuboidShape(float xLength, float yLength, float zLength) {
		return new BoxShape(new Vector3f(xLength / 2, yLength / 2, zLength / 2));
	}

	RigidBody createCylinderRigid(float mass, float height, float radius, PhysicsObjectOrientation orientation, Transform position) {
		MotionState motionState = new DefaultMotionState(position);
		CollisionShape shape = createCylinderShape(height, radius, orientation);
		RigidBodyConstructionInfo info = new RigidBodyConstructionInfo(mass, motionState, shape);
		info.restitution = 0;
		return new RigidBody(info);
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
		MotionState motionState = new DefaultMotionState(position);
		CollisionShape shape = createConeShape(height, radius, orientation);
		RigidBodyConstructionInfo info = new RigidBodyConstructionInfo(mass, motionState, shape);
		info.restitution = 0;
		return new RigidBody(info);
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

	RigidBody createExactRigid(float mass, IndexedMesh mesh, Transform position) {
		MotionState motionState = new DefaultMotionState(position);
		CollisionShape shape = createExactShape(mesh);
		RigidBodyConstructionInfo info = new RigidBodyConstructionInfo(mass, motionState, shape);
		info.restitution = 0;
		return new RigidBody(info);
	}

	CollisionShape createExactShape(IndexedMesh mesh) {
		TriangleIndexVertexArray indexVertexArray = new TriangleIndexVertexArray();
		indexVertexArray.addIndexedMesh(mesh);
		return new BvhTriangleMeshShape(indexVertexArray, true);
	}

}
