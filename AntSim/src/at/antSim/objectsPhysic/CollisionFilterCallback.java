package at.antSim.objectsPhysic;

import javax.swing.text.AbstractDocument.BranchElement;

import at.antSim.Globals;
import at.antSim.MainApplication;
import at.antSim.eventSystem.EventManager;
import at.antSim.eventSystem.events.CollisionEvent;
import at.antSim.objectsKI.Entity;
import at.antSim.objectsPhysic.basics.PhysicsObject;

import com.bulletphysics.collision.broadphase.BroadphaseProxy;
import com.bulletphysics.collision.broadphase.OverlapFilterCallback;
import com.bulletphysics.collision.dispatch.CollisionObject;

/**
 * Created on 28.05.2015.
 *
 * @author Clemens
 */
public class CollisionFilterCallback extends OverlapFilterCallback {
	
	//this callback is assured to be called one time per collision cycle! 
	
	@Override
	public boolean needBroadphaseCollision(BroadphaseProxy broadphaseProxy, BroadphaseProxy broadphaseProxy1) {
		
		boolean collides = (broadphaseProxy.collisionFilterGroup & broadphaseProxy1.collisionFilterMask) != 0;
		collides = collides && (broadphaseProxy1.collisionFilterGroup & broadphaseProxy.collisionFilterMask) != 0;
		
		return collides;
	}
}
