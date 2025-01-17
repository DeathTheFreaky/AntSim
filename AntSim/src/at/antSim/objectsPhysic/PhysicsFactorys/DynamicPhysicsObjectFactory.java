package at.antSim.objectsPhysic.PhysicsFactorys;

import at.antSim.Globals;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObjectOrientation;

import com.bulletphysics.collision.broadphase.CollisionFilterGroups;
import com.bulletphysics.collision.shapes.IndexedMesh;
import com.bulletphysics.linearmath.Transform;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created on 12.05.2015.
 *
 * @author Clemens
 */
public class DynamicPhysicsObjectFactory extends AbstractPhysicsObjectFactory<DynamicPhysicsObject> {

	private static DynamicPhysicsObjectFactory instance = new DynamicPhysicsObjectFactory();

	protected DynamicPhysicsObjectFactory() {}

	@Override
	public DynamicPhysicsObject createSphere(String type, float mass, float radius) throws UnsupportedOperationException {
		return createSphere(type, mass, radius, new Transform());
	}

	@Override
	public DynamicPhysicsObject createSphere(String type, float mass, float radius, Transform position) throws UnsupportedOperationException {
		return new DynamicPhysicsObject(createSphereRigid(mass, radius, position), type);
	}

	@Override
	public DynamicPhysicsObject createCuboid(String type, float mass, float xLength, float yLength, float zLength) throws UnsupportedOperationException {
		return createCuboid(type, mass, xLength, yLength, zLength, new Transform());
	}

	@Override
	public DynamicPhysicsObject createCuboid(String type, float mass, float xLength, float yLength, float zLength, Transform position) throws UnsupportedOperationException {
		return new DynamicPhysicsObject(createCuboidRigid(mass, xLength, yLength, zLength, position), type);
	}

	@Override
	public DynamicPhysicsObject createCylinder(String type, float mass, float height, float radius, PhysicsObjectOrientation orientation) throws UnsupportedOperationException {
		return createCylinder(type, mass, height, radius, orientation, new Transform());
	}

	@Override
	public DynamicPhysicsObject createCylinder(String type, float mass, float height, float radius, PhysicsObjectOrientation orientation, Transform position) throws UnsupportedOperationException {
		return new DynamicPhysicsObject(createCylinderRigid(mass, height, radius, orientation, position), type);
	}

	@Override
	public DynamicPhysicsObject createCone(String type, float mass, float height, float radius, PhysicsObjectOrientation orientation) throws UnsupportedOperationException {
		return createCone(type, mass, height, radius, orientation, new Transform());
	}

	@Override
	public DynamicPhysicsObject createCone(String type, float mass, float height, float radius, PhysicsObjectOrientation orientation, Transform position) throws UnsupportedOperationException {
		return new DynamicPhysicsObject(createConeRigid(mass, height, radius, orientation, position), type);
	}

	@Override
	public DynamicPhysicsObject createExactObject(String type, float mass, IndexedMesh mesh) {
		return createExactObject(type, mass, mesh, new Transform());
	}

	@SuppressWarnings("restriction")
	@Override
	public DynamicPhysicsObject createExactObject(String type, float mass, IndexedMesh mesh, Transform position) {
		throw new NotImplementedException();
	}

	public static DynamicPhysicsObjectFactory getInstance() {
		return instance;
	}
}