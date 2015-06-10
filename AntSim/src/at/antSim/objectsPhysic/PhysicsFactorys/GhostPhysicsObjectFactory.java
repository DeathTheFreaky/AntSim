package at.antSim.objectsPhysic.PhysicsFactorys;

import at.antSim.Globals;
import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObjectOrientation;

import com.bulletphysics.collision.dispatch.GhostObject;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.IndexedMesh;
import com.bulletphysics.linearmath.Transform;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created on 18.05.2015.
 *
 * @author Clemens
 */
public class GhostPhysicsObjectFactory extends AbstractPhysicsObjectFactory<GhostPhysicsObject> {
	
	private static GhostPhysicsObjectFactory INSTANCE = null;
	
	private GhostPhysicsObjectFactory() {};

	@Override
	public GhostPhysicsObject createSphere(String type, float mass, float radius) throws UnsupportedOperationException {
		return createSphere(type, mass, radius, new Transform());
	}

	@Override
	public GhostPhysicsObject createSphere(String type, float mass, float radius, Transform position) throws UnsupportedOperationException {
		GhostObject ghostObject = new GhostObject();
		ghostObject.setWorldTransform(position);
		CollisionShape shape = createSphereShape(radius);
		ghostObject.setCollisionShape(shape);
		return new GhostPhysicsObject(ghostObject, type);
	}

	@Override
	public GhostPhysicsObject createCuboid(String type, float mass, float xLength, float yLength, float zLength) throws UnsupportedOperationException {
		return createCuboid(type, mass, xLength, yLength, zLength, new Transform());
	}

	@Override
	public GhostPhysicsObject createCuboid(String type, float mass, float xLength, float yLength, float zLength, Transform position) throws UnsupportedOperationException {
		GhostObject ghostObject = new GhostObject();
		ghostObject.setWorldTransform(position);
		CollisionShape shape = createCuboidShape(xLength, yLength, zLength);
		ghostObject.setCollisionShape(shape);
		return new GhostPhysicsObject(ghostObject, type);
	}

	@Override
	public GhostPhysicsObject createCylinder(String type, float mass, float height, float radius, PhysicsObjectOrientation orientation) throws UnsupportedOperationException {
		return createCylinder(type, mass, height, radius, orientation, new Transform());
	}

	@Override
	public GhostPhysicsObject createCylinder(String type, float mass, float height, float radius, PhysicsObjectOrientation orientation, Transform position) throws UnsupportedOperationException {
		GhostObject ghostObject = new GhostObject();
		ghostObject.setWorldTransform(position);
		CollisionShape shape = createCylinderShape(height, radius, orientation);
		ghostObject.setCollisionShape(shape);
		return new GhostPhysicsObject(ghostObject, type);
	}

	@Override
	public GhostPhysicsObject createCone(String type, float mass, float height, float radius, PhysicsObjectOrientation orientation) throws UnsupportedOperationException {
		return createCone(type, mass, height, radius, orientation, new Transform());
	}

	@Override
	public GhostPhysicsObject createCone(String type, float mass, float height, float radius, PhysicsObjectOrientation orientation, Transform position) throws UnsupportedOperationException {
		GhostObject ghostObject = new GhostObject();
		ghostObject.setWorldTransform(position);
		CollisionShape shape = createConeShape(height, radius, orientation);
		ghostObject.setCollisionShape(shape);
		return new GhostPhysicsObject(ghostObject, type);
	}

	@Override
	public GhostPhysicsObject createExactObject(String type, float mass, IndexedMesh mesh) {
		return createExactObject(type, mass, mesh, new Transform());
	}

	@Override
	public GhostPhysicsObject createExactObject(String type, float mass, IndexedMesh mesh, Transform position) {
		throw new NotImplementedException();
	}

	public static PhysicsObjectFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new GhostPhysicsObjectFactory();
		}
		return INSTANCE;
	}
}
