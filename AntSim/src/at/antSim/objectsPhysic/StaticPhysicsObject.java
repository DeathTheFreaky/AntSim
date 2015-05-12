package at.antSim.objectsPhysic;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;

/**
 * Created on 12.05.2015.
 *
 * @author Clemens
 */
public class StaticPhysicsObject implements PhysicsObject {

	private final RigidBody body;

	/**
	 * Creates an PhysicsObject representing a Sphere with mass and radius.
	 * @param mass Mass of the Object
	 * @param radius Radius of the Sphere
	 */
	public StaticPhysicsObject(float mass, float radius) {
		MotionState motionState = new DefaultMotionState();
		CollisionShape shape = new SphereShape(radius);
		body = new RigidBody(mass, motionState, shape);
	}
/*
	public StaticPhysicsObject(float mass) {
		MotionState motionState = new DefaultMotionState();
		BoxShape shape = new BoxShape();
		body = new RigidBody(mass, motionState, shape);
	}
*/
	@Override
	public RigidBody getRigidBody() {
		return body;
	}
}
