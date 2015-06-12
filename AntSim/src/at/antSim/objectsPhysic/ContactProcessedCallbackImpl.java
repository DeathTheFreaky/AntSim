package at.antSim.objectsPhysic;

import at.antSim.Globals;
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
	
	//Attention: this callback can be called not at all, or even multiple times per collision cycle; events shall only be fired once however!!!
	PhysicsObject prevPhyObj;
	PhysicsObject prevPhyObj1;
	
	@Override
	public boolean contactProcessed(ManifoldPoint manifoldPoint, Object colObj, Object colObj1) {
		
		if (colObj instanceof CollisionObject && colObj1 instanceof CollisionObject) {
			PhysicsObject phyObj = (PhysicsObject)((CollisionObject) colObj).getUserPointer(); //PhysicsManager.getInstance().getPhysicsObject((CollisionObject)colObj);
			PhysicsObject phyObj1 = (PhysicsObject)((CollisionObject) colObj1).getUserPointer(); //PhysicsManager.getInstance().getPhysicsObject((CollisionObject)colObj1);
			
			if (phyObj != null && phyObj1 != null) {
				
				if (!((phyObj == prevPhyObj && phyObj1 == prevPhyObj1) || (phyObj == prevPhyObj1 && phyObj1 == prevPhyObj))) { //ensure events get only triggered once per different collision pair
//					System.out.println("adding collision between " + phyObj + ", " + phyObj1 + " with ctr " + MainApplication.getInstance().getCycleCtr());
					EventManager.getInstance().addEventToQueue(new CollisionEvent(phyObj, phyObj1));
				}
				
				prevPhyObj = phyObj;
				prevPhyObj1 = phyObj1;
				
				return true;
			}
		}
		return false;
	}
	
	public void reset() {
		prevPhyObj = null;
		prevPhyObj1 = null;
	}
}
