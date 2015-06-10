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
	
	@Override
	public boolean needBroadphaseCollision(BroadphaseProxy broadphaseProxy, BroadphaseProxy broadphaseProxy1) {
		
		boolean collides = (broadphaseProxy.collisionFilterGroup & broadphaseProxy1.collisionFilterMask) != 0;
		collides = collides && (broadphaseProxy1.collisionFilterGroup & broadphaseProxy.collisionFilterMask) != 0;
		
		CollisionObject colObj = (CollisionObject) broadphaseProxy.clientObject;
		CollisionObject colObj1 = (CollisionObject) broadphaseProxy1.clientObject;		
		
		//check if moving entity or ghost object collide, register event and return false to avoid a collision to bypass kinematic vs ghost collision no_contact_response not working bug
		//moving entity has own collision group because it is able to collide with static, whereas a normal ghost object is not
		if (collides && ((broadphaseProxy.collisionFilterGroup == Globals.COL_MOVING || broadphaseProxy1.collisionFilterGroup == Globals.COL_MOVING) 
				|| (broadphaseProxy.collisionFilterGroup == Globals.COL_SENSOR || broadphaseProxy1.collisionFilterGroup == Globals.COL_SENSOR))) {
			
			//only use instanceof when there are collision triggered by ghost objects and moving entities
			if (colObj instanceof CollisionObject && colObj1 instanceof CollisionObject) {
				
				PhysicsObject phyObj = (PhysicsObject)((CollisionObject) colObj).getUserPointer(); //PhysicsManager.getInstance().getPhysicsObject((CollisionObject)colObj);
				PhysicsObject phyObj1 = (PhysicsObject)((CollisionObject) colObj1).getUserPointer(); //PhysicsManager.getInstance().getPhysicsObject((CollisionObject)colObj1);
				
//				System.out.println(phyObj + "(" + phyObj.getType() + ") and " + phyObj1 + "(" + phyObj1.getType() + ") -> " + collides);
				
				EventManager.getInstance().addEventToQueue(new CollisionEvent(phyObj, phyObj1));
			}
			
			return false;
		}
		
		return collides;
	}
}
