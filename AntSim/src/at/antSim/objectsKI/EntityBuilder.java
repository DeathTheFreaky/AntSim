package at.antSim.objectsKI;

import org.lwjgl.util.vector.Vector3f;

import at.antSim.GTPMapper.GTPCone;
import at.antSim.GTPMapper.GTPCuboid;
import at.antSim.GTPMapper.GTPCylinder;
import at.antSim.GTPMapper.GTPSphere;
import at.antSim.graphics.models.TexturedModel;
import at.antSim.objectsPhysic.PhysicsFactorys.PhysicsObjectFactory;
import at.antSim.objectsPhysic.basics.PhysicsObject;

/**Provides methods for creating a new {@link Entity}.
 * 
 * @author Flo
 *
 */
public interface EntityBuilder {
	
	/**Builds the {@link GraphicsEntity} for the {@link Entity}.
	 * 
	 * @param type - key used to obtain TexturedModel, also functions as physicsObject.type for reacting to collisions
	 * @param textureIndex
	 * @param scale
	 */
	public EntityBuilder buildGraphicsEntity(String type, int textureIndex, float scale);
	
	/**Builds the {@link PhysicsObject} for the {@link Entity}.
	 * 
	 * @param mass
	 * @param position
	 * @param rx
	 * @param ry
	 * @param rz
	 */
	public EntityBuilder buildPhysicsObject();
	
	/**Sets the {@link PhysicsObjectFactory} to be used for building the {@link Entity}.
	 * 
	 * @param factory
	 */
	public EntityBuilder setFactory(PhysicsObjectFactory factory);
	
	/**Sets the {@link Entity}'s mass.<br>
	 * If not set, the default model's mass will be used.
	 * 
	 * @param mass
	 */
	public EntityBuilder setMass(float mass);
	
	/**Sets the {@link Entity}'s position.
	 * 
	 * @param position
	 */
	public EntityBuilder setPosition(Vector3f position);
	
	/**Used to set a different type for the physicsObject than the default one deriving from the graphicsEntity builder method.
	 * 
	 * @param type
	 * @return
	 */
	public EntityBuilder setType(String type);
	
	/**Used to set ObjectType to a different value than the default derived from the graphicsEntitie's TexturedModel.
	 * 
	 * @param objectType
	 * @return
	 */
	public EntityBuilder setObjectType(ObjectType objectType);
	
	/**Sets the {@link Entity}'s rotation.
	 * 
	 * @param rx
	 * @param ry
	 * @param rz
	 */
	public EntityBuilder setRotation(float rx, float ry, float rz);
	
	public void createCone(GTPCone cone);
	
	public void createCuboid(GTPCuboid cuboid);
	
	public void createCylinder(GTPCylinder cylinder);
	
	public void createSphere(GTPSphere sphere);
	
	/**
	 * @return - the created {@link Entity}
	 */
	public Entity registerResult();
}
