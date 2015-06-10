package at.antSim.objectsKI;

import org.lwjgl.util.vector.Vector3f;

import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;

/**Used as a sphere around Food and Enemy entities to tell the ant where to go to once in a specified range around the Food or Enemy to actually find their target.
 * @author Flo
 *
 */
public class PositionLocator {
	
	private GhostPhysicsObject physicsObject;
	private Entity target;
	
	PositionLocator(GhostPhysicsObject physicsObject, Entity target) {
		this.physicsObject = physicsObject;
		this.target = target;
	}
	
	/**Updates this PositionLocator's position to match its target.
	 * 
	 */
	public void updatePosition() {
		physicsObject.setPosition(((ReadOnlyPhysicsObject) target.getPhysicsObject()).getPosition());
	}
	
	public PhysicsObject getPhysicsObject() {
		return physicsObject;
	}
}
