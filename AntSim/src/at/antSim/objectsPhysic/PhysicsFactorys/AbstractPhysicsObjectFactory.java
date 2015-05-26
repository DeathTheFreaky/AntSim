package at.antSim.objectsPhysic.PhysicsFactorys;

import at.antSim.GTPMapper.*;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObjectOrientation;
import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

/**
 * Created on 18.05.2015.
 *
 * @author Clemens
 */
public abstract class AbstractPhysicsObjectFactory<E extends PhysicsObject> implements PhysicsObjectFactory<E> {
	
	/**Creates a new StaticPhysicsObject. 
	 * 
	 * @param gtpObject
	 * @param mass
	 * @return - a {@link StaticPhysicsObject}
	 */
	public E createPrimitive(GTPObject gtpObject, float mass) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		return createPrimitive(gtpObject, mass, new Transform(matrix));
	}
	
	/**Creates a new StaticPhysicsObject. 
	 * 
	 * @param gtpObject
	 * @param mass
	 * @param transform - the object's initial position and rotation in the world
	 * @return - a {@link StaticPhysicsObject}
	 */
	public E createPrimitive(GTPObject gtpObject, float mass, Transform transform) {
		if (gtpObject instanceof GTPCone) {
			GTPCone cone = (GTPCone) gtpObject;
			return createCone(mass, cone.getHeight(), cone.getRadius(), cone.getOrienation(), transform);
		} else if (gtpObject instanceof GTPCuboid) {
			GTPCuboid cuboid = (GTPCuboid) gtpObject;
			return createCuboid(mass, cuboid.getxLength(), cuboid.getyLength(), cuboid.getzLength(), transform);
		} else if (gtpObject instanceof GTPCylinder) {
			GTPCylinder cylinder = (GTPCylinder) gtpObject;
			return createCylinder(mass, cylinder.getHeight(), cylinder.getRadius(), cylinder.getOrientation(), transform);
		} else if (gtpObject instanceof GTPSphere) {
			GTPSphere sphere = (GTPSphere) gtpObject;
			return createSphere(mass, sphere.getRadious(), transform);
		}
		return null;
	}

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
		MotionState motionState = new DefaultMotionState(position);
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

	RigidBody createExactRigid(float mass, IndexedMesh mesh, Transform position) {
		MotionState motionState = new DefaultMotionState(position);
		CollisionShape shape = createExactShape(mesh);
		return new RigidBody(mass, motionState, shape);
	}

	CollisionShape createExactShape(IndexedMesh mesh) {
		TriangleIndexVertexArray indexVertexArray = new TriangleIndexVertexArray();
		indexVertexArray.addIndexedMesh(mesh);
		return new BvhTriangleMeshShape(indexVertexArray, true);
	}

}
