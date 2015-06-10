package at.antSim.objectsPhysic;

import at.antSim.Globals;

import com.bulletphysics.collision.broadphase.BroadphasePair;
import com.bulletphysics.collision.broadphase.DispatcherInfo;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.NearCallback;

public class CollisionNearCallback extends NearCallback{

	@Override
	public void handleCollision(BroadphasePair collisionPair, CollisionDispatcher dispatcher, DispatcherInfo dispatchInfo) {
		
	}
}
