package at.antSim.objectsKI;

import java.util.LinkedList;

import javax.vecmath.Vector3f;

import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector2f;

import com.bulletphysics.linearmath.QuaternionUtil;

import at.antSim.Globals;
import at.antSim.eventSystem.EventManager;
import at.antSim.eventSystem.events.LocatorLockEvent;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.PhysicsManager;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.TerrainPhysicsObject;
import at.antSim.objectsPhysic.Movement.Dodge;
import at.antSim.objectsPhysic.Movement.MovementManager;
import at.antSim.objectsPhysic.Movement.MovementModeType;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.PositionablePhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;
import at.antSim.utils.Maths;

/**Used as a sphere around Food and Enemy entities to tell the ant where to go to once in a specified range around the Food or Enemy to actually find their target.
 * @author Flo
 *
 */
public class PositionLocator extends Entity {
	
	private Entity target;
	
	private LinkedList<Ant> activeAnts = new LinkedList<>(); //used to indicate which ants are currently trying to get to this locator's target - to avoid "traffic jam"
	private LinkedList<Ant> waitingAnts = new LinkedList<>();
	
	PositionLocator(GraphicsEntity graphicsEntity, PhysicsObject physicsObject, Entity target) {
		super(graphicsEntity, physicsObject, ObjectType.LOCATOR);
		this.target = target;
	}
	
	public Entity getTarget() {
		return target;
	}
	
	/**Updates this PositionLocator's position to match its target.
	 * 
	 */
	public void updatePosition() {
		((GhostPhysicsObject) physicsObject).setPosition(((ReadOnlyPhysicsObject)((Entity) target).getPhysicsObject()).getPosition());
	}
	
	public Vector3f getTargetPosition() {
		javax.vecmath.Vector3f position = ((GhostPhysicsObject) physicsObject).getPosition();
		return new Vector3f(position.x, position.y, position.z);
	}
	
	/**Activate an ant to indicate that this ant is currently trying to get to the PositionLocator's target.
	 * @param ant
	 * @return - true if the PositionLocator still has enough "room" for this ant to approach it and false if not
	 */
	public boolean registerAnt(Ant ant) {

		if (activeAnts.contains(ant)) { //ant is already active in positionLocator
			return true;
		} 
		if (!waitingAnts.contains(ant)) { //register ant as waiting for being allowed to enter the positionLocator
			waitingAnts.add(ant);
		}
		if (activeAnts.size() < Globals.MAX_LOCATOR_ANTS){ //some waiting ants may be allowed into positionLocator
						
			int newAllowedAnts = Globals.MAX_LOCATOR_ANTS - activeAnts.size(); //how many new ants are allowed into positionLocator?
			
//			System.out.println("newAllowedAnts: " + newAllowedAnts);
							
			int i = 0;
			for (Ant waitingAnt : waitingAnts) {
				if (i >= newAllowedAnts) {
					break;
				}
				if (waitingAnt.equals(ant)) {
					activeAnts.add(ant);
					waitingAnts.remove(waitingAnt);
					return true; //waiting ant was on top of waiting list - is now allowed entry into positionLocator
				}
				i++;
			}
			return false; //waiting ant was ranked too far behind in waiting list - is not allowed  into positionLocator

		} else { //no more room in position locator for waiting ants
			return false;
		}
	}
	
	public boolean containsActiveAnt(Ant ant) {
		return activeAnts.contains(ant);
	}
	
	/**Used to indicate to an ant if it would be allowed entry to the locator.<br>
	 * If not, and ant is inside another locator too, it could choose the other locator as a target...
	 * 
	 * @param ant
	 * @return
	 */
	public boolean entryPossible(Ant ant) {
		if (activeAnts.size() < Globals.MAX_LOCATOR_ANTS) {
			return true;
		}
		return false;
	}
	
	/**Deactivate an Ant which has left the locator or does not want to get to its target any more.
	 * @param ant
	 */
	public void unregisterAnt(Ant ant) {
		activeAnts.remove(ant);
		waitingAnts.remove(ant);
	}
	
	public int numberOfActiveAnts() {
		return activeAnts.size();
	}

	@Override
	public void react(StaticPhysicsObject staticPhysicsObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void react(DynamicPhysicsObject dynamicPhysicsObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void react(GhostPhysicsObject ghostPhysicsObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void react(TerrainPhysicsObject terrainPhysicsObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void deleteSpecific() {
		// TODO Auto-generated method stub
	}

	public void cancelAnts() {
		for (Ant ant : activeAnts) {
			ant.unlockLocator();
		}
	}
}
