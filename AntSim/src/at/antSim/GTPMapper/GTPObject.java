package at.antSim.GTPMapper;

import at.antSim.EntityBuilder;
import at.antSim.objectsPhysic.basics.PhysicsObject;

import com.bulletphysics.linearmath.Transform;

/**Just used for nice switch syntax when deciding which type of GTP object to create.
 * 
 * @author Flo
 *
 */
public abstract class GTPObject {

	public abstract void createPrimitive(EntityBuilder builder);
}
