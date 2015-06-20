package at.antSim.objectsPhysic.PhysicsFactorys;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.bulletphysics.collision.shapes.IndexedMesh;
import com.bulletphysics.linearmath.Transform;

import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.TerrainPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObjectOrientation;

public class TerrainPhysicsObjectFactory extends AbstractPhysicsObjectFactory<TerrainPhysicsObject> {
	
	private static TerrainPhysicsObjectFactory instance = new TerrainPhysicsObjectFactory();

	private TerrainPhysicsObjectFactory() {}

	@Override
	public TerrainPhysicsObject createSphere(String type, float mass, float radius) throws UnsupportedOperationException {
		throw new NotImplementedException();
	}

	@Override
	public TerrainPhysicsObject createSphere(String type, float mass, float radius, Transform position) throws UnsupportedOperationException {
		throw new NotImplementedException();
	}

	@Override
	public TerrainPhysicsObject createCuboid(String type, float mass, float xLength, float yLength, float zLength) throws UnsupportedOperationException {
		throw new NotImplementedException();
	}

	@Override
	public TerrainPhysicsObject createCuboid(String type, float mass, float xLength, float yLength, float zLength, Transform position) throws UnsupportedOperationException {
		throw new NotImplementedException();
	}

	@Override
	public TerrainPhysicsObject createCylinder(String type, float mass, float height, float radius, PhysicsObjectOrientation orientation) throws UnsupportedOperationException {
		throw new NotImplementedException();
	}

	@Override
	public TerrainPhysicsObject createCylinder(String type, float mass, float height, float radius, PhysicsObjectOrientation orientation, Transform position) throws UnsupportedOperationException {
		throw new NotImplementedException();
	}

	@Override
	public TerrainPhysicsObject createCone(String type, float mass, float height, float radius, PhysicsObjectOrientation orientation) throws UnsupportedOperationException {
		throw new NotImplementedException();
	}

	@Override
	public TerrainPhysicsObject createCone(String type, float mass, float height, float radius, PhysicsObjectOrientation orientation, Transform position) throws UnsupportedOperationException {
		throw new NotImplementedException();
	}

	@Override
	public TerrainPhysicsObject createExactObject(String type, float mass, IndexedMesh mesh) {
		return createExactObject(type, mass, mesh, new Transform());
	}

	@Override
	public TerrainPhysicsObject createExactObject(String type, float mass, IndexedMesh mesh, Transform position) {
		return new TerrainPhysicsObject(createExactRigid(0, mesh, position), type);
	}

	public static TerrainPhysicsObjectFactory getInstance() {
		return instance;
	}
}
