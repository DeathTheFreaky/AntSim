package at.antSim.objectsPhysic;

import at.antSim.objectsPhysic.basics.PhysicsObject;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.broadphase.Dispatcher;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.SimpleDynamicsWorld;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;

import javax.vecmath.Vector3f;
import java.util.Collection;

/**
 * Created on 12.05.2015.
 *
 * @author Clemens
 */
public class PhysicsManager {
	private static PhysicsManager ourInstance = new PhysicsManager();

	public static PhysicsManager getInstance() {
		return ourInstance;
	}

	private DynamicsWorld physicsWorld;

	protected PhysicsManager() {
		CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
		Dispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);
		BroadphaseInterface broadphaseInterface = new DbvtBroadphase();
		ConstraintSolver constraintSolver = new SequentialImpulseConstraintSolver();
		physicsWorld = new SimpleDynamicsWorld(dispatcher, broadphaseInterface, constraintSolver, collisionConfiguration);
		physicsWorld.setGravity(new Vector3f(0, (float) 8.13, 0));
	}

	public void registerPhysicsObject(PhysicsObject physicsObject) {
		physicsWorld.addRigidBody(physicsObject.getRigidBody());
	}

	public void registerPhysicsObject(Collection<PhysicsObject> physicsObjects) {
		for (PhysicsObject object : physicsObjects) {
			registerPhysicsObject(object);
		}
	}

	public void unregisterPhysicsObject(PhysicsObject physicsObject) {
		physicsWorld.removeRigidBody(physicsObject.getRigidBody());
	}

	public void unregisterPhysicsObject(Collection<PhysicsObject> physicsObjects) {
		for (PhysicsObject object : physicsObjects) {
			unregisterPhysicsObject(object);
		}
	}

	public void performCollisionDetection() {
		physicsWorld.performDiscreteCollisionDetection();
	}
}
