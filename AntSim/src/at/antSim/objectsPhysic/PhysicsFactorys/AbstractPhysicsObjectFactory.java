package at.antSim.objectsPhysic.PhysicsFactorys;

import at.antSim.GTPMapper.GTPCone;
import at.antSim.GTPMapper.GTPCuboid;
import at.antSim.GTPMapper.GTPCylinder;
import at.antSim.GTPMapper.GTPObject;
import at.antSim.GTPMapper.GTPSphere;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
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
	 * @param position - the object's initial position and rotation in the world 
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
		CollisionShape shape = new SphereShape(radius);
		return new RigidBody(mass, motionState, shape);
	}

	RigidBody createCuboidRigid(float mass, float xLength, float yLength, float zLength, Transform position) {
		MotionState motionState = new DefaultMotionState(position);
		CollisionShape shape = new BoxShape(new Vector3f(xLength / 2, yLength / 2, zLength / 2));
		return new RigidBody(mass, motionState, shape);
	}

	RigidBody createCylinderRigid(float mass, float height, float radius, PhysicsObjectOrientation orientation, Transform position) {
		MotionState motionState = new DefaultMotionState(position);
		CollisionShape shape;
		switch (orientation) {
			case X:
				shape = new CylinderShapeX(new Vector3f(height / 2, radius, 0));
				break;
			case Y:
				shape = new CylinderShape(new Vector3f(radius, height / 2, 0));
				break;
			case Z:
				shape = new CylinderShapeZ(new Vector3f(radius, 0, height / 2));
				break;
			default:
				throw new IllegalArgumentException("Invalid Orientation");
		}
		return new RigidBody(mass, motionState, shape);
	}

	RigidBody createConeRigid(float mass, float height, float radius, PhysicsObjectOrientation orientation, Transform position) {
		MotionState motionState = new DefaultMotionState();
		CollisionShape shape;
		switch (orientation) {
			case X:
				shape = new ConeShapeX(radius, height);
				break;
			case Y:
				shape = new ConeShape(radius, height);
				break;
			case Z:
				shape = new ConeShapeZ(radius, height);
				break;
			default:
				throw new IllegalArgumentException("Invalid Orientation");
		}
		return new RigidBody(mass, motionState, shape);
	}

}
