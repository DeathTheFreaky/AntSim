package at.antSim.objectsPhysic.Movement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import javax.vecmath.Vector3f;

import at.antSim.objectsKI.Entity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;

/**Handles movement of Entities - either to specific locations or randomly.
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
	
	protected MovementManager() {};
	
	/**Adds a {@link MovementMode} on top of a {@link DynamicPhysicsObject}'s movement stack.
	 * 
	 * @param physicsObject
	 * @param mode
	 */
	public void addMovementEntry(DynamicPhysicsObject physicsObject, MovementMode mode) {
		if (entries.containsKey(physicsObject)) {
			tryAdding(physicsObject, mode);
		} else {
			entries.put(physicsObject, new Stack<MovementMode>());
			entries.get(physicsObject).add(mode);
		}
	}
	
	/**Enforces certain policies in order for a mode to be granted addition to the movement stack.
	 * @param physicsObject
	 * @param mode
	 */
	public void tryAdding(DynamicPhysicsObject physicsObject, MovementMode mode) {
		
		//check if collides with same obstacle
		
		if (entries.get(physicsObject).lastElement().type == MovementModeType.DODGE) {
			
			Dodge lastDodge = (Dodge) entries.get(physicsObject).lastElement();
			Dodge newDodge = (Dodge) mode;
			
			if (lastDodge.obstacle == newDodge.obstacle) {
//				System.out.println("equals last dodge object");
				((Dodge) entries.get(physicsObject).lastElement()).setStillColliding();
			} else {
				
				((Dodge) entries.get(physicsObject).lastElement()).reset(newDodge.obstacle);
				
//				System.out.println("is a now dodge object");
//				System.out.println("Stack size" + entries.get(physicsObject).size());
//				
//				System.out.println();
//				System.out.println("stack content: ");
//				Stack<MovementMode> stacky = entries.get(physicsObject);
//				Iterator<MovementMode> iter = stacky.iterator();
//				boolean contains = false;
//				while (iter.hasNext()) {				
//					MovementMode m = iter.next();
//					if (m.type == MovementModeType.DODGE) {
//						Dodge dodge = (Dodge) m;
//						if (dodge.obstacle == newDodge.obstacle) {
//							contains = true;
//						}
//					}
//					System.out.println(m.toString());
//				}
////				((Dodge) entries.get(physicsObject).lastElement()).
//				
//				
//				
//				if (contains) {
//					System.out.println("already contains dodge " + mode);
//				} else {
//					Vector3f originalDirection = entries.get(physicsObject).lastElement().getDirection();
//					System.out.println(" with direction " + originalDirection);
////					entries.get(physicsObject).pop();
//					entries.get(physicsObject).add(mode);
//					((Dodge) mode).setOriginalDirection(originalDirection);
//				}
			}
//			System.out.println("already dodging");
		} else if (mode.type.equals(MovementModeType.DODGE)){
			System.out.println("new dodge mode");
//			System.out.println("ant hit environmentObject and tries to dodge it");
			Vector3f originalDirection = entries.get(physicsObject).lastElement().getDirection();
			System.out.println(" with direction " + originalDirection);
			entries.get(physicsObject).add(mode);
			((Dodge) mode).setOriginalDirection(originalDirection);
		} else {
			System.out.println("no dodge mode");
			entries.get(physicsObject).add(mode);
		}
	}
	
	/**Removes the last {@link MovementMode} entry from a {@link DynamicPhysicsObject}'s movement stack.
	 * 
	 * @param physicsObject
	 */
	public void removeLastMovementEntry(DynamicPhysicsObject physicsObject) {
		if (entries.containsKey(physicsObject)) {
			entries.get(physicsObject).pop();
			if (entries.get(physicsObject).size() == 0) {
				entries.remove(physicsObject);
			}
		}
	}
	
	/**Removes a {@link DynamicPhysicsObject} from the Collection of handled {@link DynamicPhysicsObject}s, thereby removing all its {@link MovementMode} entries.
	 * 
	 * @param physicsObject
	 */
	public void removeAllMovementEntries(DynamicPhysicsObject physicsObject) {
		entries.remove(physicsObject);
	}
	
	/**Clears all {@link DynamicPhysicsObject}s from the Collection of handled {@link DynamicPhysicsObject}s.
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
	
	public static MovementManager getInstance() {
		return INSTANCE;
	}
}
