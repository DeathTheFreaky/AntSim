package at.antSim.objectsKI;

import java.util.LinkedList;

import javax.vecmath.Vector3f;

import at.antSim.Globals;
import at.antSim.eventSystem.EventManager;
import at.antSim.eventSystem.events.LocatorLockEvent;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.graphics.graphicsUtils.Maths;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.PhysicsManager;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.TerrainPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.PositionablePhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;

/**Used as a sphere around Food and Enemy entities to tell the ant where to go to once in a specified range around the Food or Enemy to actually find their target.
 * @author Flo
 *
 */
public class PositionLocator extends Entity {
	
	private Entity target;
	
	private LinkedList<Ant> activeAnts = new LinkedList<>(); //used to indicate which ants are currently trying to get to this locator's target - to avoid "traffic jam"
	
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
	 */
	public void activateAnt(Ant ant) {
		if (activeAnts.contains(ant)) {
			Vector3f antPosition = ((ReadOnlyPhysicsObject) ant.getPhysicsObject()).getPosition();
			Vector3f targetPosition = getTargetPosition();
			Vector3f linVelocity = new Vector3f((targetPosition.x - antPosition.x)  * Globals.LOCATOR_SPEED_MULT, 0, (targetPosition.z - antPosition.z) * Globals.LOCATOR_SPEED_MULT);
			System.out.println("sending linVelocity " + linVelocity + " to " + ant);
			EventManager.getInstance().addEventToQueue(new LocatorLockEvent(linVelocity, ant, this));
		}
		else if (activeAnts.size() < Globals.MAX_LOCATOR_ANTS) {
			if (!activeAnts.contains(ant)) {
				activeAnts.add(ant);
				Vector3f antPosition = ((ReadOnlyPhysicsObject) ant.getPhysicsObject()).getPosition();
				Vector3f targetPosition = getTargetPosition();
				Vector3f linVelocity = new Vector3f((targetPosition.x - antPosition.x)  * Globals.LOCATOR_SPEED_MULT, 0, (targetPosition.z - antPosition.z) * Globals.LOCATOR_SPEED_MULT);
				System.out.println("sending linVelocity " + linVelocity + " to " + ant);
				EventManager.getInstance().addEventToQueue(new LocatorLockEvent(linVelocity, ant, this));
			}
		} 
		else {
			Vector3f linVelocity = new Vector3f(0,0,0); //causes ant to wait
			EventManager.getInstance().addEventToQueue(new LocatorLockEvent(linVelocity, ant, this));
		}
	}
	
	public boolean containsActiveAnt(Ant ant) {
		return activeAnts.contains(ant);
	}
	
	/**Deactivate an Ant which has left the locator or does not want to get to its target any more.
	 * @param ant
	 */
	public void deactivateAnt(Ant ant) {
		activeAnts.remove(ant);
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
}
