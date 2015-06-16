package at.antSim.objectsPhysic.Movement;

import java.util.HashMap;
import java.util.LinkedList;

import at.antSim.objectsKI.Entity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;

/**Handles movement of Entities - either to specific locations or randomly.
 * 
 * @author Flo
 *
 */
public class MovementManager {

	private static MovementManager INSTANCE = null;
	
	HashMap<DynamicPhysicsObject, MovementMode> entries = new HashMap<>();
	
	static {
		INSTANCE = new MovementManager();
	}
	
	protected MovementManager() {};
	
	public void setMovementEntry(DynamicPhysicsObject physicsObject, MovementMode mode) {
		entries.put(physicsObject, mode);
	}
	
	public void removeMovementEntry(DynamicPhysicsObject physicsObject) {
		entries.remove(physicsObject);
	}
	
	public void moveAllEntries() {
		for (MovementMode entry : entries.values()) {
			entry.move();
		}
	}
	
	public static MovementManager getInstance() {
		return INSTANCE;
	}
}
