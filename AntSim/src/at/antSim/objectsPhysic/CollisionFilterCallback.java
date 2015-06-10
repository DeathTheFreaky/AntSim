package at.antSim.objectsPhysic;

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

		if (((CollisionObject) broadphaseProxy.clientObject).isStaticObject() && ((CollisionObject) broadphaseProxy1.clientObject).isStaticObject())
			return false;
		
		CollisionObject colObj = (CollisionObject) broadphaseProxy.clientObject;
		CollisionObject colObj1 = (CollisionObject) broadphaseProxy1.clientObject;
				
		if (colObj instanceof CollisionObject && colObj1 instanceof CollisionObject) {
			
			PhysicsObject phyObj = (PhysicsObject)((CollisionObject) colObj).getUserPointer(); //PhysicsManager.getInstance().getPhysicsObject((CollisionObject)colObj);
			PhysicsObject phyObj1 = (PhysicsObject)((CollisionObject) colObj1).getUserPointer(); //PhysicsManager.getInstance().getPhysicsObject((CollisionObject)colObj1);
			Entity movingEntity = MainApplication.getInstance().getMovingEntity().getEntity();
			PhysicsObject phyMoving = (movingEntity == null) ? null : movingEntity.getPhysicsObject();
			
			if (phyObj != null && phyObj1 != null) {
				
				if (phyObj instanceof GhostPhysicsObject || phyObj1 instanceof GhostPhysicsObject) {
					System.out.println("ghost collision: " + phyObj + "(" + phyObj.getType() + ") and " + phyObj1 + "(" + phyObj1.getType() + ")");
					EventManager.getInstance().addEventToQueue(new CollisionEvent(phyObj, phyObj1));
					return false;
				}
				
				//
					
				//checks for movingEntity explicitly
//				if (phyMoving != null && (phyObj.equals(phyMoving) || phyObj1.equals(phyMoving)) && 
//						(!phyObj.getType().equals("terrain") && !phyObj1.getType().equals("terrain"))) {
//					System.out.println("collision between " + phyObj.getType() + " and " + phyObj1.getType());
//					EventManager.getInstance().addEventToQueue(new CollisionEvent(phyObj, phyObj1));
//					return false;
//				}
			}
		}
		
		boolean collides = (broadphaseProxy.collisionFilterGroup & broadphaseProxy1.collisionFilterMask) != 0;
		collides = collides && (broadphaseProxy1.collisionFilterGroup & broadphaseProxy.collisionFilterMask) != 0;
		
		

		return collides;
	}
}
