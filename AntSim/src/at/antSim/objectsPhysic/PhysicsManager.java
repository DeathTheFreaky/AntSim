package at.antSim.objectsPhysic;

import at.antSim.Globals;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;

import com.bulletphysics.BulletGlobals;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.CollisionFilterGroups;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.broadphase.Dispatcher;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.dispatch.GhostPairCallback;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.SimpleDynamicsWorld;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;

import javax.vecmath.Vector3f;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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

	public DynamicPhysicsObject observingPhysicsObject = null;

	protected PhysicsManager() {
		BulletGlobals.setContactProcessedCallback(new ContactProcessedCallbackImpl());
		CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
		CollisionDispatcher dispatcher = new CustomCollisionDispatcher(collisionConfiguration);
//		dispatcher.setNearCallback(new CollisionNearCallback());
		BroadphaseInterface broadphaseInterface = new DbvtBroadphase();
		ConstraintSolver constraintSolver = new SequentialImpulseConstraintSolver();
		physicsWorld = new SimpleDynamicsWorld(dispatcher, broadphaseInterface, constraintSolver, collisionConfiguration);
		physicsWorld.getPairCache().setOverlapFilterCallback(new CollisionFilterCallback());
		physicsWorld.getPairCache().setInternalGhostPairCallback(new GhostPairCallback());
		physicsWorld.setGravity(new Vector3f(0, Globals.gravity, 0));
	}

	public void registerPhysicsObject(PhysicsObject physicsObject) {
		RigidBody rig = RigidBody.upcast(physicsObject.getCollisionBody());
		if (rig != null) {
			rig.setGravity(new javax.vecmath.Vector3f(0, Globals.gravity, 0));
		} 
		physicsWorld.addCollisionObject(physicsObject.getCollisionBody(), physicsObject.getCollisionFilterGroup(), physicsObject.getCollisionFilterMask());
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
		if (observingPhysicsObject != null) {
			System.out.println("Position: " + observingPhysicsObject.getPosition() + " timeStep: " + timeStep);
		}
		if(timeStep < 0.01 || timeStep > 2.0)
			return;
		physicsWorld.performDiscreteCollisionDetection();
		physicsWorld.stepSimulation(timeStep, 7);
	}

	public PhysicsObject getPhysicsObject(CollisionObject colObj) {
		return physicsObjectMap.get(colObj);
	}
	
	public void printAllCollisionObjects() {
		
		System.out.println();
		System.out.println();
		
		System.out.println("physicsObjectMap has " + physicsObjectMap.size() + " entries");
		for (Entry<CollisionObject, PhysicsObject> entry : physicsObjectMap.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue().getType() + " (" + entry.getValue() + ")");
		}
	}
}
