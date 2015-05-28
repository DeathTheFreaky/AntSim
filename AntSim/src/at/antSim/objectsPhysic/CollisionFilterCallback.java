package at.antSim.objectsPhysic;

import com.bulletphysics.collision.broadphase.BroadphaseProxy;
import com.bulletphysics.collision.broadphase.OverlapFilterCallback;

/**
 * Created on 28.05.2015.
 *
 * @author Clemens
 */
public class CollisionFilterCallback extends OverlapFilterCallback {
	@Override
	public boolean needBroadphaseCollision(BroadphaseProxy broadphaseProxy, BroadphaseProxy broadphaseProxy1) {

		if((broadphaseProxy.clientObject.getClass() == StaticPhysicsObject.class && broadphaseProxy1.clientObject.getClass() == StaticPhysicsObject.class)) {
			System.out.println("static objects " + broadphaseProxy.clientObject + ", " + broadphaseProxy1.clientObject + " collided");
			return false;
		}

		boolean collides = (broadphaseProxy.collisionFilterGroup & broadphaseProxy1.collisionFilterMask) != 0;
		collides = collides && (broadphaseProxy1.collisionFilterGroup & broadphaseProxy.collisionFilterMask) != 0;

		return collides;
	}
}
