package at.antSim.objectsPhysic.PhysicsFactorys;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import at.antSim.Globals;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObjectOrientation;

import com.bulletphysics.collision.shapes.IndexedMesh;
import com.bulletphysics.linearmath.Transform;

/**
 * Created on 12.05.2015.
 *
 * @author Clemens
 */
public class StaticPhysicsObjectFactory extends AbstractPhysicsObjectFactory<StaticPhysicsObject> {

	private static StaticPhysicsObjectFactory instance = new StaticPhysicsObjectFactory();

	protected StaticPhysicsObjectFactory() {}

	@Override
	public StaticPhysicsObject createSphere(String type, float mass, float radius) throws UnsupportedOperationException {
		return createSphere(type, mass, radius, new Transform());
	}

	@Override
	public StaticPhysicsObject createSphere(String type, float mass, float radius, Transform position) throws UnsupportedOperationException {
		return new StaticPhysicsObject(createSphereRigid(0, radius, position), type);
	}

	@Override
	public StaticPhysicsObject createCuboid(String type, float mass, float xLength, float yLength, float zLength) throws UnsupportedOperationException {
		return createCuboid(type, mass, xLength, yLength, zLength, new Transform());
	}

	@Override
	public StaticPhysicsObject createCuboid(String type, float mass, float xLength, float yLength, float zLength, Transform position) throws UnsupportedOperationException {
		return new StaticPhysicsObject(createCuboidRigid(0, xLength, yLength, zLength, position), type);
	}

	@Override
	public StaticPhysicsObject createCylinder(String type, float mass, float height, float radius, PhysicsObjectOrientation orientation) throws UnsupportedOperationException {
		return createCylinder(type, mass, height, radius, orientation, new Transform());
	}

	@Override
	public StaticPhysicsObject createCylinder(String type, float mass, float height, float radius, PhysicsObjectOrientation orientation, Transform position) throws UnsupportedOperationException {
		return new StaticPhysicsObject(createCylinderRigid(0, height, radius, orientation, position), type);
	}

	@Override
	public StaticPhysicsObject createCone(String type, float mass, float height, float radius, PhysicsObjectOrientation orientation) throws UnsupportedOperationException {
		return createCone(type, mass, height, radius, orientation, new Transform());
	}

	@Override
	public StaticPhysicsObject createCone(String type, float mass, float height, float radius, PhysicsObjectOrientation orientation, Transform position) throws UnsupportedOperationException {
		return new StaticPhysicsObject(createConeRigid(0, height, radius, orientation, position), type);
	}

	@Override
	public StaticPhysicsObject createExactObject(String type, float mass, IndexedMesh mesh) {
		return createExactObject(type, mass, mesh, new Transform());
	}

	@Override
	public StaticPhysicsObject createExactObject(String type, float mass, IndexedMesh mesh, Transform position) {
		throw new NotImplementedException();
	}

	public static StaticPhysicsObjectFactory getInstance() {
		return instance;
	}
}
