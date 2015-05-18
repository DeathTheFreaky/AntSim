package at.antSim.objectsPhysic.PhysicsFactorys;

import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObjectOrientation;
import com.bulletphysics.collision.dispatch.GhostObject;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.linearmath.Transform;

/**
 * Created on 18.05.2015.
 *
 * @author Clemens
 */
public class GhostPhysicsObjectFactory extends AbstractPhysicsObjectFactory<GhostPhysicsObject> {

	@Override
	public GhostPhysicsObject createSphere(float mass, float radius) throws UnsupportedOperationException {
		return createSphere(mass, radius, new Transform());
	}

	@Override
	public GhostPhysicsObject createSphere(float mass, float radius, Transform position) throws UnsupportedOperationException {
		GhostObject ghostObject = new GhostObject();
		ghostObject.setWorldTransform(position);
		CollisionShape shape = createSphereShape(radius);
		ghostObject.setCollisionShape(shape);
		return new GhostPhysicsObject(ghostObject);
	}

	@Override
	public GhostPhysicsObject createCuboid(float mass, float xLength, float yLength, float zLength) throws UnsupportedOperationException {
		return createCuboid(mass, xLength, yLength, zLength, new Transform());
	}

	@Override
	public GhostPhysicsObject createCuboid(float mass, float xLength, float yLength, float zLength, Transform position) throws UnsupportedOperationException {
		GhostObject ghostObject = new GhostObject();
		ghostObject.setWorldTransform(position);
		CollisionShape shape = createCuboidShape(xLength, yLength, zLength);
		ghostObject.setCollisionShape(shape);
		return new GhostPhysicsObject(ghostObject);
	}

	@Override
	public GhostPhysicsObject createCylinder(float mass, float height, float radius, PhysicsObjectOrientation orientation) throws UnsupportedOperationException {
		return createCylinder(mass, height, radius, orientation, new Transform());
	}

	@Override
	public GhostPhysicsObject createCylinder(float mass, float height, float radius, PhysicsObjectOrientation orientation, Transform position) throws UnsupportedOperationException {
		GhostObject ghostObject = new GhostObject();
		ghostObject.setWorldTransform(position);
		CollisionShape shape = createCylinderShape(height, radius, orientation);
		ghostObject.setCollisionShape(shape);
		return new GhostPhysicsObject(ghostObject);
	}

	@Override
	public GhostPhysicsObject createCone(float mass, float height, float radius, PhysicsObjectOrientation orientation) throws UnsupportedOperationException {
		return createCone(mass, height, radius, orientation, new Transform());
	}

	@Override
	public GhostPhysicsObject createCone(float mass, float height, float radius, PhysicsObjectOrientation orientation, Transform position) throws UnsupportedOperationException {
		GhostObject ghostObject = new GhostObject();
		ghostObject.setWorldTransform(position);
		CollisionShape shape = createConeShape(height, radius, orientation);
		ghostObject.setCollisionShape(shape);
		return new GhostPhysicsObject(ghostObject);
	}
}
