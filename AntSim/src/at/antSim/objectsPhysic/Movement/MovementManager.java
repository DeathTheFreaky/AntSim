package at.antSim.objectsPhysic.Movement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import javax.vecmath.Vector3f;

import at.antSim.objectsKI.Entity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObject;

/**
 * Handles movement of Entities - either to specific locations or randomly.
 * 
 * @author Flo
 *
 */
public class MovementManager {

	private static MovementManager INSTANCE = null;

	HashMap<DynamicPhysicsObject, Stack<MovementMode>> entries = new HashMap<>();

	static {
		INSTANCE = new MovementManager();
	}

	protected MovementManager() {
	};

	/**
	 * Adds a {@link MovementMode} on top of a {@link DynamicPhysicsObject}'s
	 * movement stack.
	 * 
	 * @param physicsObject
	 * @param mode
	 */
	public void addMovementEntry(DynamicPhysicsObject physicsObject,
			MovementMode mode) {
		if (entries.containsKey(physicsObject)) {
			tryAdding(physicsObject, mode);
		} else {
			entries.put(physicsObject, new Stack<MovementMode>());
			entries.get(physicsObject).add(mode);
		}
	}

	/**
	 * Enforces certain policies in order for a mode to be granted addition to
	 * the movement stack.
	 * 
	 * @param physicsObject
	 * @param mode
	 */
	public void tryAdding(DynamicPhysicsObject physicsObject, MovementMode mode) {

		// check if collides with same obstacle

		if (entries.get(physicsObject).lastElement().type == MovementModeType.DODGE
				&& mode.type == MovementModeType.DODGE) {

			Dodge lastDodge = (Dodge) entries.get(physicsObject).lastElement();
			Dodge newDodge = (Dodge) mode;

			if (lastDodge.obstacle == newDodge.obstacle) {
				((Dodge) entries.get(physicsObject).lastElement())
						.setStillColliding();
			} else {
				((Dodge) entries.get(physicsObject).lastElement())
						.reset(newDodge.obstacle);
			}

		} else if (entries.get(physicsObject).lastElement().type == MovementModeType.BORDER
				&& mode.type == MovementModeType.BORDER) {
			// ignore new border movement, wait for previous one to finish
		} else if (mode.type == MovementModeType.BORDER) {
			MovementMode previousMode = entries.get(physicsObject)
					.lastElement();
			((BorderCollisionMovement) mode).setOriginalDirection(previousMode
					.getDirection());
			entries.get(physicsObject).add(mode);
		} else if (mode.type == MovementModeType.DODGE) {
			MovementMode previousMode = entries.get(physicsObject)
					.lastElement();
			entries.get(physicsObject).add(mode);
			((Dodge) mode).setCurrentDirection(previousMode.getDirection());
			((Dodge) mode).setPreviousMovementMode(previousMode);
		} else if (mode.type == MovementModeType.TARGET) {
			if(entries.get(physicsObject).lastElement().type != MovementModeType.TARGET){
				entries.get(physicsObject).add(mode);
			}
		} else {
			entries.get(physicsObject).add(mode);
		}
	}

	/**
	 * Removes the last {@link MovementMode} entry from a
	 * {@link DynamicPhysicsObject}'s movement stack.
	 * 
	 * @param physicsObject
	 */
	public void removeLastMovementEntry(DynamicPhysicsObject physicsObject) {
		System.out.println("size " + entries.get(physicsObject).size());
		if (entries.containsKey(physicsObject)) {
			entries.get(physicsObject).pop();
			if (entries.get(physicsObject).size() == 0) {
				entries.remove(physicsObject);
			}
		}
		System.out.println("size2 " + entries.get(physicsObject).size());
	}

	/**
	 * Removes a {@link DynamicPhysicsObject} from the Collection of handled
	 * {@link DynamicPhysicsObject}s, thereby removing all its
	 * {@link MovementMode} entries.
	 * 
	 * @param physicsObject
	 */
	public void removeAllMovementEntries(DynamicPhysicsObject physicsObject) {
		entries.remove(physicsObject);
	}

	/**
	 * Clears all {@link DynamicPhysicsObject}s from the Collection of handled
	 * {@link DynamicPhysicsObject}s.
	 * 
	 */
	public void clearAllEntries() {
		entries.clear();
	}

	public void moveAllEntries() {
		for (Stack<MovementMode> stack : entries.values()) {
			stack.lastElement().move();
		}
	}

	public MovementMode getTopMovementMode(PhysicsObject physicsObject) {
		MovementMode retMode = null;
		if (entries.containsKey(physicsObject)) {
			retMode = entries.get(physicsObject).lastElement();
		}
		return retMode;
	}

	public static MovementManager getInstance() {
		return INSTANCE;
	}
}
