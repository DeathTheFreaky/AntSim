package at.antSim.objectsPhysic.PhysicsFactorys;

import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObjectOrientation;
import com.bulletphysics.collision.shapes.*;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

import javax.vecmath.Vector3f;

/**
 * Created on 12.05.2015.
 *
 * @author Clemens
 */
public class StaticPhysicsObjectFactory implements PhysicsObjectFactory<StaticPhysicsObject> {

	static {
		PhysicsFactoryCollection.register(StaticPhysicsObject.class, new StaticPhysicsObjectFactory());
	}

	protected StaticPhysicsObjectFactory() {

	}

	@Override
	public StaticPhysicsObject createSphere(float mass, float radius) {
		return createSphere(mass, radius, new Transform());
	}

	@Override
	public StaticPhysicsObject createSphere(float mass, float radius, Transform position) {
		MotionState motionState = new DefaultMotionState(position);
		CollisionShape shape = new SphereShape(radius);
		return new StaticPhysicsObject(new RigidBody(mass, motionState, shape));
	}

	@Override
	public StaticPhysicsObject createCuboid(float mass, float xLength, float yLength, float zLength) {
		return createCuboid(mass, xLength, yLength, zLength, new Transform());
	}

	@Override
	public StaticPhysicsObject createCuboid(float mass, float xLength, float yLength, float zLength, Transform position) {
		MotionState motionState = new DefaultMotionState(position);
		CollisionShape shape = new BoxShape(new Vector3f(xLength / 2, yLength / 2, zLength / 2));
		return new StaticPhysicsObject(new RigidBody(mass, motionState, shape));
	}

	@Override
	public StaticPhysicsObject createCylinder(float mass, float height, float radius, PhysicsObjectOrientation orientation) {
		return createCylinder(mass, height, radius, orientation, new Transform());
	}

	@Override
	public StaticPhysicsObject createCylinder(float mass, float height, float radius, PhysicsObjectOrientation orientation, Transform position) {
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
		return new StaticPhysicsObject(new RigidBody(mass, motionState, shape));
	}

	@Override
	public StaticPhysicsObject createCone(float mass, float height, float radius, PhysicsObjectOrientation orientation) {
		return createCone(mass, height, radius, orientation, new Transform());
	}

	@Override
	public StaticPhysicsObject createCone(float mass, float height, float radius, PhysicsObjectOrientation orientation, Transform position) {
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
		return new StaticPhysicsObject(new RigidBody(mass, motionState, shape));
	}
}
