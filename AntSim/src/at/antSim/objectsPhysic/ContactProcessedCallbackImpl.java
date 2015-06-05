package at.antSim.objectsPhysic;

import at.antSim.MainApplication;
import at.antSim.eventSystem.EventManager;
import at.antSim.eventSystem.events.CollisionEvent;
import at.antSim.objectsPhysic.basics.PhysicsObject;

import com.bulletphysics.ContactProcessedCallback;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.narrowphase.ManifoldPoint;

/**
 * Created on 26.05.2015.
 *
 * @author Clemens
 */
public class ContactProcessedCallbackImpl extends ContactProcessedCallback {
	@Override
	public boolean contactProcessed(ManifoldPoint manifoldPoint, Object colObj, Object colObj1) {
		if (colObj instanceof CollisionObject && colObj1 instanceof CollisionObject) {
			PhysicsObject phyObj = (PhysicsObject)((CollisionObject) colObj).getUserPointer(); //PhysicsManager.getInstance().getPhysicsObject((CollisionObject)colObj);
			PhysicsObject phyObj1 = (PhysicsObject)((CollisionObject) colObj1).getUserPointer(); //PhysicsManager.getInstance().getPhysicsObject((CollisionObject)colObj1);
			
			if (phyObj != null && phyObj1 != null) {
				EventManager.getInstance().addEventToQueue(new CollisionEvent(phyObj, phyObj1));
				return true;
			}
		}
		return false;
	}
}
