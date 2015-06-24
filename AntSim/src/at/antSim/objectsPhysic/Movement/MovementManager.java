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

	HashMap<DynamicPhysicsObject, StackEntry> entries = new HashMap<>();
	LinkedList<DynamicPhysicsObject> topDeleteables = new LinkedList<>();
	private int maxStackMovements = 7;

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
			entries.put(physicsObject, new StackEntry());
			entries.get(physicsObject).stack.add(mode);
		}
	}

	/**
	 * Enforces certain policies in order for a mode to be granted addition to
	 * the movement stack.
	 * 
	 * @param physicsObject
	 * @param modesw
	 */
	public void tryAdding(DynamicPhysicsObject physicsObject, MovementMode mode) {
		
		if (entries.get(physicsObject).stack.size() >= maxStackMovements) {
			return;
		}

		// check if collides with same obstacle
//		System.out.println("size " + entries.get(physicsObject).size());
//		System.out.println(" last " + entries.get(physicsObject).lastElement());
		if (entries.get(physicsObject).stack.lastElement().type == MovementModeType.DODGE
				&& mode.type == MovementModeType.DODGE) {

			Dodge lastDodge = (Dodge) entries.get(physicsObject).stack.lastElement();
			Dodge newDodge = (Dodge) mode;

			if (lastDodge.obstacle == newDodge.obstacle) {
				((Dodge) entries.get(physicsObject).stack.lastElement())
						.setStillColliding();
			} else {
				((Dodge) entries.get(physicsObject).stack.lastElement())
						.reset(newDodge.obstacle);
			}

		} else if (entries.get(physicsObject).stack.lastElement().type == MovementModeType.BORDER && mode.type == MovementModeType.BORDER) {
			// ignore new border movement, wait for previous one to finish
		} else if (mode.type == MovementModeType.BORDER) {
			MovementMode previousMode = entries.get(physicsObject).stack.lastElement();
			((BorderCollisionMovement) mode).setOriginalDirection(previousMode.getDirection());
			entries.get(physicsObject).stack.add(mode);
		} else if (mode.type == MovementModeType.DODGE) {
			MovementMode previousMode = entries.get(physicsObject).stack.lastElement();
			entries.get(physicsObject).stack.add(mode);
			((Dodge) mode).setCurrentDirection(previousMode.getDirection());
			((Dodge) mode).setPreviousMovementMode(previousMode);
		} else if (mode.type == MovementModeType.TARGET) {
			if (getTargetMovementMode(physicsObject) == null && getHiveMovementMode(physicsObject) == null && entries.get(physicsObject).stack.lastElement().type != MovementModeType.DODGE) {
				entries.get(physicsObject).stack.add(mode);
			}
		} else if (mode.type == MovementModeType.DIRECTION ) {
			if (entries.get(physicsObject).stack.lastElement().type != MovementModeType.TARGET && entries.get(physicsObject).stack.lastElement().type != MovementModeType.DODGE) {
				entries.get(physicsObject).moveToDirCtr++;
//				System.out.println("moveToDir " + moveToDir + " size " + entries.get(physicsObject).size());
//				System.out.println(" last " + entries.get(physicsObject).lastElement());
				if(entries.get(physicsObject).moveToDirCtr > 1){
//					System.out.println("add + remove ");
					removeLastMovementEntry(physicsObject);
					if(entries.get(physicsObject) != null)
						entries.get(physicsObject).stack.add(mode);
				} else {
//					System.out.println("add ");
					if(entries.get(physicsObject) != null)
						entries.get(physicsObject).stack.add(mode);
				}	
			}
		} else if (mode.type == MovementModeType.WAIT) {
			entries.get(physicsObject).stack.add(mode);
		} else if (mode.type == MovementModeType.BASIC) {
			if (entries.get(physicsObject).stack.lastElement().type != MovementModeType.BASIC) {
				entries.get(physicsObject).stack.add(mode);
			}
		} else if (mode.type == MovementModeType.HIVE) {
			if (getHiveMovementMode(physicsObject) == null) {
				entries.get(physicsObject).stack.add(mode);
			}
		}
	}

	/**
	 * Removes the last {@link MovementMode} entry from a
	 * {@link DynamicPhysicsObject}'s movement stack.
	 * 
	 * @param physicsObject
	 */
	public boolean removeLastMovementEntry(DynamicPhysicsObject physicsObject) {
		if (entries.containsKey(physicsObject)) {
//			System.out.println( " size " + entries.get(physicsObject).stack.size());
			if (entries.get(physicsObject).stack.lastElement().type != MovementModeType.BASIC) {
				MovementMode m = entries.get(physicsObject).stack.pop();
//				System.out.println(" size " + entries.get(physicsObject).stack.size());
				if(m.type == MovementModeType.DIRECTION){
//					System.out.println("--");
					entries.get(physicsObject).moveToDirCtr--;
				}
				if (entries.get(physicsObject).stack.size() == 0) {
					entries.remove(physicsObject);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes a {@link DynamicPhysicsObject} from the Collection of handled
	 * {@link DynamicPhysicsObject}s, thereby removing all its
	 * {@link MovementMode} entries.
	 * 
	 * @param physicsObject
	 * @param deleteBasic - true if last basic movement mode shall also be deleted
	 */
	public void removeAllMovementEntries(DynamicPhysicsObject physicsObject, boolean deleteBasic) {
		if (deleteBasic) {
			entries.remove(physicsObject);
		}
		if (removeLastMovementEntry(physicsObject)) {
			removeAllMovementEntries(physicsObject, deleteBasic);
		}
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
//		System.out.println();
//		System.out.println("------------------");
		for (StackEntry entry : entries.values()) {
			entry.stack.lastElement().move();

//			System.out.println("ant ");
//			
//			Iterator it = entry.stack.iterator();
//			
//			while(it.hasNext()) {
//				System.out.println(it.next());
//			}
//			
//			System.out.println();
		}
		for (DynamicPhysicsObject po : topDeleteables) {
			removeLastMovementEntry(po);
		}
		topDeleteables.clear();
	}

	public MovementMode getTopMovementMode(PhysicsObject physicsObject) {
		MovementMode retMode = null;
		if (entries.containsKey(physicsObject)) {
			retMode = entries.get(physicsObject).stack.lastElement();
		}
		return retMode;
	}
	
	public MovementMode getBaseMovementMode(PhysicsObject physicsObject) {
		return entries.get(physicsObject).stack.elementAt(0);
	}
	
	/**
	 * @param physicsObject
	 * @return - topMost {@link MoveToTarget}
	 */
	public MoveToTarget getTargetMovementMode(PhysicsObject physicsObject) {
		MoveToTarget ret = null;
		Iterator<MovementMode> it = entries.get(physicsObject).stack.iterator();
		while(it.hasNext()) {
			MovementMode mode = it.next();
			if (mode.type == MovementModeType.TARGET) {
				ret = (MoveToTarget) mode;
			}
		}
		return ret;
	}
	
	/**
	 * @param physicsObject
	 * @return - topMost {@link MoveToTarget}
	 */
	public MoveInDirection getDirectionMovementMode(PhysicsObject physicsObject) {
		MoveInDirection ret = null;
		Iterator<MovementMode> it = entries.get(physicsObject).stack.iterator();
		while(it.hasNext()) {
			MovementMode mode = it.next();
			if (mode.type == MovementModeType.DIRECTION) {
				ret = (MoveInDirection) mode;
			}
		}
		return ret;
	}
	
	/**
	 * @param physicsObject
	 * @return - topMost {@link MoveToHive}
	 */
	public MoveToHive getHiveMovementMode(PhysicsObject physicsObject) {
		MoveToHive ret = null;
		Iterator<MovementMode> it = entries.get(physicsObject).stack.iterator();
		while(it.hasNext()) {
			MovementMode mode = it.next();
			if (mode.type == MovementModeType.HIVE) {
				ret = (MoveToHive) mode;
			}
		}
		return ret;
	}
	
	public void addTopDeleteAble(DynamicPhysicsObject po) {
		topDeleteables.add(po);
	}
	
	class StackEntry {
		
		Stack<MovementMode> stack;
		int moveToDirCtr;
		
		StackEntry() {
			stack = new Stack<MovementMode>();
			moveToDirCtr = 0;
		}
	}

	public static MovementManager getInstance() {
		return INSTANCE;
	}
}
