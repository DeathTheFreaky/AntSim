package at.antSim.objectsPhysic;

import at.antSim.Globals;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import com.bulletphysics.BulletGlobals;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.broadphase.Dispatcher;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.SimpleDynamicsWorld;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;

import javax.vecmath.Vector3f;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

	private final Map<CollisionObject, PhysicsObject> physicsObjectMap = new HashMap<>();

	protected PhysicsManager() {
		BulletGlobals.setContactProcessedCallback(new ContactProcessedCallbackImpl());
		CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
		Dispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);
		BroadphaseInterface broadphaseInterface = new DbvtBroadphase();
		ConstraintSolver constraintSolver = new SequentialImpulseConstraintSolver();
		physicsWorld = new SimpleDynamicsWorld(dispatcher, broadphaseInterface, constraintSolver, collisionConfiguration);
		physicsWorld.getPairCache().setOverlapFilterCallback(new CollisionFilterCallback());
		physicsWorld.setGravity(new Vector3f(0, Globals.gravity, 0));
	}

	public void registerPhysicsObject(PhysicsObject physicsObject) {
		RigidBody rig = RigidBody.upcast(physicsObject.getCollisionBody());
		if (rig != null) {
			physicsWorld.addRigidBody(rig);
		} else {
			physicsWorld.addCollisionObject(physicsObject.getCollisionBody());
		}
		physicsObjectMap.put(physicsObject.getCollisionBody(), physicsObject);
	}

	public void registerPhysicsObject(Collection<PhysicsObject> physicsObjects) {
		for (PhysicsObject object : physicsObjects) {
			registerPhysicsObject(object);
		}
	}

	public void unregisterPhysicsObject(PhysicsObject physicsObject) {
		physicsWorld.removeCollisionObject(physicsObject.getCollisionBody());
		physicsObjectMap.remove(physicsObject.getCollisionBody());
	}

	public void unregisterPhysicsObject(Collection<PhysicsObject> physicsObjects) {
		for (PhysicsObject object : physicsObjects) {
			unregisterPhysicsObject(object);
		}
	}

	public void performCollisionDetection(float timeStep) {
		physicsWorld.performDiscreteCollisionDetection();
		physicsWorld.stepSimulation(timeStep, 7);


	}

	public PhysicsObject getPhysicsObject(CollisionObject colObj) {
		return physicsObjectMap.get(colObj);
	}
}