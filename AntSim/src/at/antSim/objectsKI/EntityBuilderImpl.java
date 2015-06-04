package at.antSim.objectsKI;

import org.lwjgl.util.vector.Vector3f;

import com.bulletphysics.linearmath.Transform;

import at.antSim.MovingEntity;
import at.antSim.GTPMapper.GTPCone;
import at.antSim.GTPMapper.GTPCuboid;
import at.antSim.GTPMapper.GTPCylinder;
import at.antSim.GTPMapper.GTPMapper;
import at.antSim.GTPMapper.GTPObject;
import at.antSim.GTPMapper.GTPSphere;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.graphics.graphicsUtils.Maths;
import at.antSim.graphics.models.TexturedModel;
import at.antSim.objectsPhysic.PhysicsManager;
import at.antSim.objectsPhysic.PhysicsFactorys.PhysicsObjectFactory;
import at.antSim.objectsPhysic.basics.PhysicsObject;

/**Implements {@link EntityBuilder}.
 * 
 * @author Flo
 *
 */
public class EntityBuilderImpl implements EntityBuilder {
	
	GraphicsEntity graphicsEntity;
	GTPObject gtpObject;
	PhysicsObject physicsObject;
	PhysicsObjectFactory factory;
	
	float mass;
	Vector3f position;
	float rx;
	float rz;
	float ry;
	
	public EntityBuilderImpl() {
		mass = 0;
		position = new Vector3f(0, 0, 0);
		rx = 0;
		ry = 0;
		rz = 0;
	}
	
	@Override
	public EntityBuilder buildGraphicsEntity(TexturedModel texturedModel, int textureIndex, float scale) {
		
		scale = scale/2; //use scale/2 because entity has length of 2 on longest axis but scale shall refer to actual size of object
		
		graphicsEntity = new GraphicsEntity(texturedModel, textureIndex, scale); 
		gtpObject = GTPMapper.getObject(graphicsEntity, scale, graphicsEntity.getModel().getPrimitiveType());
		mass = graphicsEntity.getModel().getMass(); //set default mass
		position.y = position.y + graphicsEntity.getModel().getRawModel().getyLength()/2 * scale;
		return this;
	}

	@Override
	public EntityBuilder buildPhysicsObject() {
		if (factory != null) {
			gtpObject.createPrimitive(this);
		}
		return this;
	}

	@Override
	public void createCone(GTPCone cone) {
		physicsObject = factory.createCone(mass, cone.getHeight(), cone.getRadius(), cone.getOrienation(), new Transform(Maths.createTransformationMatrix(position, rx, ry, rz)));
	}

	@Override
	public void createCuboid(GTPCuboid cuboid) {
		physicsObject = factory.createCuboid(mass, cuboid.getxLength(), cuboid.getyLength(), cuboid.getzLength(), new Transform(Maths.createTransformationMatrix(position, rx, ry, rz)));
	}

	@Override
	public void createCylinder(GTPCylinder cylinder) {
		physicsObject = factory.createCylinder(mass, cylinder.getHeight(), cylinder.getRadius(), cylinder.getOrientation(), new Transform(Maths.createTransformationMatrix(position, rx, ry, rz)));
	}

	@Override
	public void createSphere(GTPSphere sphere) {
		physicsObject = factory.createSphere(mass, sphere.getRadious(),  new Transform(Maths.createTransformationMatrix(position, rx, ry, rz)));
	}

	@Override
	public EntityBuilder setMass(float mass) {
		this.mass = mass;
		return this;
	}

	@Override
	public EntityBuilder setPosition(Vector3f position) {
		this.position = position;
		return this;
	}

	@Override
	public EntityBuilder setRotation(float rx, float ry, float rz) {
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
		return this;
	}

	@Override
	public Entity registerResult() {
		if (graphicsEntity != null) {
			PhysicsManager.getInstance().registerPhysicsObject(physicsObject);
			switch (graphicsEntity.getModel().getObjectType()) {
			case ANT:
				return new Ant(graphicsEntity, physicsObject);
			case ENEMY:
				return new Enemy(graphicsEntity, physicsObject);
			case ENVIRONMENT:
				return new EnvironmentObject(graphicsEntity, physicsObject);
			case FOOD:
				return new Food(graphicsEntity, physicsObject);
			case PHEROMONE:
				return new Pheronome(graphicsEntity, physicsObject);
			}
		}
		return null;
	}

	@Override
	public EntityBuilder setFactory(PhysicsObjectFactory factory) {
		this.factory = factory;
		return this;
	}
}
