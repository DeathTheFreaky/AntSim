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
	 * @param texturedModel
	 * @param textureIndex
	 * @param scale
	 */
	public EntityBuilder buildGraphicsEntity(TexturedModel texturedModel, int textureIndex, float scale);
	
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
	public Entity getResult();
}
