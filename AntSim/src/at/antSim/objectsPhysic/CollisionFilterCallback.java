package at.antSim.objectsPhysic;

import com.bulletphysics.collision.broadphase.BroadphaseProxy;
import com.bulletphysics.collision.broadphase.OverlapFilterCallback;
import com.bulletphysics.collision.dispatch.CollisionObject;

/**
 * Created on 28.05.2015.
 *
 * @author Clemens
 */
public class CollisionFilterCallback extends OverlapFilterCallback {
	@Override
	public boolean needBroadphaseCollision(BroadphaseProxy broadphaseProxy, BroadphaseProxy broadphaseProxy1) {

		if (((CollisionObject) broadphaseProxy.clientObject).isStaticObject() && ((CollisionObject) broadphaseProxy1.clientObject).isStaticObject())
			return false;

		boolean collides = (broadphaseProxy.collisionFilterGroup & broadphaseProxy1.collisionFilterMask) != 0;
		collides = collides && (broadphaseProxy1.collisionFilterGroup & broadphaseProxy.collisionFilterMask) != 0;

		return collides;
	}
}
