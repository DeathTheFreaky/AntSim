package at.antSim.objectsKI;

import javax.vecmath.Quat4f;

import org.lwjgl.util.vector.Vector3f;

import com.bulletphysics.linearmath.Transform;

import at.antSim.MainApplication;
import at.antSim.MovingEntity;
import at.antSim.GTPMapper.GTPCone;
import at.antSim.GTPMapper.GTPCuboid;
import at.antSim.GTPMapper.GTPCylinder;
import at.antSim.GTPMapper.GTPMapper;
import at.antSim.GTPMapper.GTPObject;
import at.antSim.GTPMapper.GTPSphere;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.graphics.graphicsUtils.Maths;
import at.antSim.graphics.graphicsUtils.ModelLoader;
import at.antSim.graphics.models.TexturedModel;
import at.antSim.objectsPhysic.PhysicsManager;
import at.antSim.objectsPhysic.PhysicsFactorys.PhysicsObjectFactory;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;

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
	ObjectType objectType;
	
	float mass;
	Vector3f position = new Vector3f();
	Vector3f rotation = null;
	Quat4f quat = new Quat4f();
	Transform transform = new Transform();
	String type = null;
	
	public EntityBuilderImpl() {
		mass = 0;
	}
	
	@Override
	public EntityBuilder buildGraphicsEntity(String type, int textureIndex, float scale) {
		
		scale = scale/2; //use scale/2 because entity has length of 2 on longest axis but scale shall refer to actual size of object
		
		graphicsEntity = new GraphicsEntity(ModelLoader.texturedModels.get(type), textureIndex, scale); 
		gtpObject = GTPMapper.getObject(graphicsEntity, scale, graphicsEntity.getModel().getPrimitiveType());
		mass = graphicsEntity.getModel().getMass(); //set default mass
		this.type = type;
		objectType = graphicsEntity.getModel().getObjectType();
		position.y = position.y + graphicsEntity.getModel().getRawModel().getyLength()/2 * scale;
		if (rotation != null) {
			transform.set(Maths.createTransformationMatrix(position, rotation.x, rotation.y, rotation.z));
		} else {
			transform.set(Maths.createTransformationMatrix(position, 0, 0, 0));
			transform.setRotation(quat);
		}
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
		physicsObject = factory.createCone(type, mass, cone.getHeight(), cone.getRadius(), cone.getOrienation(), transform);
	}

	@Override
	public void createCuboid(GTPCuboid cuboid) {
		physicsObject = factory.createCuboid(type, mass, cuboid.getxLength(), cuboid.getyLength(), cuboid.getzLength(), transform);
	}

	@Override
	public void createCylinder(GTPCylinder cylinder) {
		physicsObject = factory.createCylinder(type, mass, cylinder.getHeight(), cylinder.getRadius(), cylinder.getOrientation(), transform);
	}

	@Override
	public void createSphere(GTPSphere sphere) {
		physicsObject = factory.createSphere(type, mass, sphere.getRadious(), transform);
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
		rotation = new Vector3f(rx, ry, rz);
		return this;
	}

	@Override
	public Entity registerResult() {
		if (graphicsEntity != null) {
			((ReadOnlyPhysicsObject) physicsObject).setDebugId(MainApplication.getInstance().getIdCtr());
			PhysicsManager.getInstance().registerPhysicsObject(physicsObject);
			switch (objectType) {
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
			case MOVING:
				return new Moving(graphicsEntity, physicsObject);
			}
		}
		return null;
	}

	@Override
	public EntityBuilder setFactory(PhysicsObjectFactory factory) {
		this.factory = factory;
		return this;
	}

	@Override
	public EntityBuilder setType(String type) {
		this.type = type;
		return this;
	}

	@Override
	public EntityBuilder setObjectType(ObjectType objectType) {
		this.objectType = objectType;
		return this;
	}

	@Override
	public EntityBuilder setRotation(Quat4f quat) {
		this.quat = quat;
		rotation = null;
		return this;
	}
}
