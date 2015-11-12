package at.antSim.objectsKI;

import javax.vecmath.Quat4f;

import org.lwjgl.util.vector.Vector3f;

import com.bulletphysics.linearmath.Transform;

import at.antSim.Globals;
import at.antSim.GTPMapper.GTPCone;
import at.antSim.GTPMapper.GTPCuboid;
import at.antSim.GTPMapper.GTPCylinder;
import at.antSim.GTPMapper.GTPMapper;
import at.antSim.GTPMapper.GTPObject;
import at.antSim.GTPMapper.GTPSphere;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.graphics.graphicsUtils.ModelLoader;
import at.antSim.objectsPhysic.PhysicsManager;
import at.antSim.objectsPhysic.PhysicsFactorys.PhysicsObjectFactory;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObjectOrientation;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;
import at.antSim.utils.Maths;

/**
 * Implements {@link EntityBuilder}.
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
	Quat4f quat;
	Transform transform = new Transform();
	String type = null;

	float height;
	float scale;

	Entity target; // for position locators

	int hiveFoodstackSize;
	int maxAnts; // posiiton locator

	public EntityBuilderImpl() {
		reset();
	}

	@Override
	public void reset() {
		mass = 0;
		position = new Vector3f();
		rotation = null;
		quat = null;
		transform = new Transform();
		type = null;
		graphicsEntity = null;
		gtpObject = null;
		factory = null;
		objectType = null;
		height = 0;
		scale = 0;
		target = null;
		hiveFoodstackSize = 0;
		maxAnts = Globals.MAX_LOCATOR_ANTS; // default
	}

	@Override
	public EntityBuilder buildGraphicsEntity(String type, int textureIndex, float scale) {

		scale = scale / 2; // use scale/2 because entity has length of 2 on longest axis but scale shall refer to actual size of object

		graphicsEntity = new GraphicsEntity(ModelLoader.texturedModels.get(type), textureIndex, scale);
		gtpObject = GTPMapper.getObject(graphicsEntity, scale, graphicsEntity.getModel().getPrimitiveType());
		mass = graphicsEntity.getModel().getMass(); // set default mass
		this.type = type;
		this.height = graphicsEntity.getModel().getRawModel().getyLength() * scale;
		this.scale = scale;
		objectType = graphicsEntity.getModel().getObjectType();
		return this;
	}

	private void setTransform() {
		position.y = position.y + height / 2;
		if (rotation != null) {
			transform.set(Maths.createTransformationMatrix(position, rotation.x, rotation.y, rotation.z));
		} else {
			transform.set(Maths.createTransformationMatrix(position, 0, 0, 0));
			if (quat != null) {
				transform.setRotation(quat);
			}
		}
	}

	@Override
	public EntityBuilder buildPhysicsObject() {
		if (factory != null && gtpObject != null) {
			setTransform();
			gtpObject.createPrimitive(this);
		}
		return this;
	}

	@Override
	public EntityBuilder createCone(GTPCone cone) {
		physicsObject = factory.createCone(type, mass, cone.getHeight(), cone.getRadius(), cone.getOrienation(), transform);
		return this;
	}

	@Override
	public EntityBuilder createCuboid(GTPCuboid cuboid) {
		physicsObject = factory.createCuboid(type, mass, cuboid.getxLength(), cuboid.getyLength(), cuboid.getzLength(), transform);
		return this;
	}

	@Override
	public EntityBuilder createCylinder(GTPCylinder cylinder) {
		physicsObject = factory.createCylinder(type, mass, cylinder.getHeight(), cylinder.getRadius(), cylinder.getOrientation(), transform);
		return this;
	}

	@Override
	public EntityBuilder createSphere(GTPSphere sphere) {
		physicsObject = factory.createSphere(type, mass, sphere.getRadious(), transform);
		return this;
	}

	@Override
	public EntityBuilder createCone(float scale, float height, float radius,
			PhysicsObjectOrientation orientation) {
		this.scale = scale;
		if (radius * 2 > height) {
			height = height / radius / 2 * scale;
			radius = scale / 2;
		} else {
			height = scale;
			radius = radius / height * scale;
		}
		if (orientation == PhysicsObjectOrientation.X) {
			this.height = radius * 2;
		} else if (orientation == PhysicsObjectOrientation.Y) {
			this.height = height;
		} else if (orientation == PhysicsObjectOrientation.Z) {
			this.height = radius * 2;
		}
		setTransform();
		physicsObject = factory.createCone(type, mass, height, radius, orientation, transform);
		return this;
	}

	@Override
	public EntityBuilder createCuboid(float scale, float xLength,
			float yLength, float zLength) {
		this.scale = scale;
		if (xLength > yLength && xLength > zLength) {
			xLength = scale;
			yLength = yLength / xLength * scale;
			zLength = zLength / xLength * scale;
		} else if (yLength > xLength && yLength > zLength) {
			yLength = scale;
			xLength = xLength / yLength * scale;
			zLength = zLength / yLength * scale;
		} else if (zLength > yLength && zLength > xLength) {
			zLength = scale;
			yLength = yLength / zLength * scale;
			xLength = xLength / zLength * scale;
		} else {
			xLength = scale;
			yLength = scale;
			zLength = scale;
		}
		this.height = yLength;
		setTransform();
		physicsObject = factory.createCuboid(type, mass, xLength, yLength, zLength, transform);
		return this;
	}

	@Override
	public EntityBuilder createCylinder(float scale, float height,
			float radius, PhysicsObjectOrientation orientation) {
		this.scale = scale;
		if (radius * 2 > height) {
			height = height / radius / 2 * scale;
			radius = scale / 2;
		} else {
			height = scale;
			radius = radius / height * scale;
		}
		if (orientation == PhysicsObjectOrientation.X) {
			this.height = radius * 2;
		} else if (orientation == PhysicsObjectOrientation.Y) {
			this.height = height;
		} else if (orientation == PhysicsObjectOrientation.Z) {
			this.height = radius * 2;
		}
		setTransform();
		physicsObject = factory.createCone(type, mass, height, radius, orientation, transform);
		return this;
	}

	@Override
	public EntityBuilder createSphere(float scale) {
		this.scale = scale;
		this.height = scale;
		setTransform();
		physicsObject = factory.createSphere(type, mass, scale / 2, transform);
		return this;
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
	public EntityBuilder setTarget(Entity target) {
		this.target = target;
		return this;
	}

	@Override
	public Entity registerResult() {
		Entity retEntity = null;
		
		switch (objectType) {
		case ANT:
			if (type.equals("forager")) {
				retEntity = new Forager(graphicsEntity, physicsObject);
			} else if (type.equals("worker")) {
				retEntity = new Worker(graphicsEntity, physicsObject);
			}
			// else if (type.equals("queen")) {
			// retEntity = new Queen(graphicsEntity, physicsObject);
			// }
			break;
		case ENEMY:
			if (type.equals("enemyAnt")) {
				retEntity = new EnemyAnt(graphicsEntity, physicsObject);
			} else if (type.equals("enemyGrasshopper")) {
				retEntity = new EnemyGrasshopper(graphicsEntity, physicsObject);
			}
			break;
		case ENVIRONMENT:
			retEntity = new EnvironmentObject(graphicsEntity, physicsObject);
			break;
		case FOOD:
			if (type.equals("apple")) {
				retEntity = new Apple(graphicsEntity, physicsObject);
			} else if (type.equals("deadAnt")) {
				retEntity = new DeadAnt(graphicsEntity, physicsObject);
			} else if (type.equals("deadGrasshopper")) {
				retEntity = new DeadGrasshopper(graphicsEntity, physicsObject);
			} else if (type.equals("squirrel")) {
				retEntity = new Squirrel(graphicsEntity, physicsObject);
			}
			break;
		case PHEROMONE:
			retEntity = new Pheromone(graphicsEntity, physicsObject);
			break;
		case MOVING:
			retEntity = new Moving(graphicsEntity, physicsObject);
			break;
		case LOCATOR:
			retEntity = new PositionLocator(graphicsEntity, physicsObject,
					target, maxAnts);
			break;
		case HIVE:
			retEntity = Hive.getInstance(hiveFoodstackSize, graphicsEntity,
					physicsObject);
		default:
			break;
		}
		PhysicsManager.getInstance().registerPhysicsObject(retEntity.physicsObject);
		
		// set initial world transform for transparent triangles
		if (graphicsEntity.getModel().usesTransparency() && physicsObject instanceof ReadOnlyPhysicsObject)
		{
			Entity.setTriangleTransforms((ReadOnlyPhysicsObject) physicsObject);
		}
		
		reset();
		return retEntity;
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

	@Override
	public EntityBuilder setHiveParameters(int foodStacks) {
		this.hiveFoodstackSize = foodStacks;
		return this;
	}

	@Override
	public EntityBuilder setLocatorMaxAnts(int maxAnts) {
		this.maxAnts = maxAnts;
		return this;
	}
}
