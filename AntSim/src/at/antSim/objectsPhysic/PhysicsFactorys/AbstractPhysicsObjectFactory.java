package at.antSim.objectsPhysic.PhysicsFactorys;

import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObjectOrientation;

import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

/**
 * Created on 18.05.2015.
 *
 * @author Clemens
 */
public abstract class AbstractPhysicsObjectFactory<E extends PhysicsObject> implements PhysicsObjectFactory<E> {

	RigidBody createSphereRigid(float mass, float radius, Transform position) {
		System.out.println("\n\ncreateSphereRigid in AbstractPhysicsObjectFactory: ");
		System.out.println("mass: " + mass);
		System.out.println("radius: " + radius);
		System.out.println("position: " + position);
		Matrix4f translation = new Matrix4f();
		Quat4f rotation = new Quat4f();
		position.getMatrix(translation);
		position.getRotation(rotation);
		System.out.println("position - translation: \n" + translation);
		System.out.println("position - rotation: \n" + rotation);
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
