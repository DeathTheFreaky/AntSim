package at.antSim.objectsPhysic.PhysicsFactorys;

import at.antSim.objectsPhysic.basics.PhysicsObject;

/**Used to indicate which factory shall be used to create a {@link PhysicsObject}.<br>
 * STATIC is used for objects which do not move once placed in the world,
 * DYNAMIC is used for objects which can move after in the world after they have initially be placed.
 * 
 * @author Flo
 *
 */
public enum PhysicsFactoryType {
	STATIC, DYNAMIC
}
