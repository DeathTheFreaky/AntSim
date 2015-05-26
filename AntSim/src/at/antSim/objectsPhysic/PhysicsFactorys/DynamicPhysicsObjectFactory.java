package at.antSim.objectsPhysic.PhysicsFactorys;

import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObjectOrientation;
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

	protected DynamicPhysicsObjectFactory() {

	}

	/*
		@Override
		public StaticPhysicsObject createPane(float mass) {
			return createPane(mass, new Transform());
		}

		@Override
		public StaticPhysicsObject createPane(float mass, Transform position) {
			MotionState motionState = new DefaultMotionState(position);
			CollisionShape shape = new StaticPlaneShape();
			return null;
		}
	*/
	@Override
	public DynamicPhysicsObject createSphere(float mass, float radius) throws UnsupportedOperationException {
		return createSphere(mass, radius, new Transform());
	}

	@Override
	public DynamicPhysicsObject createSphere(float mass, float radius, Transform position) throws UnsupportedOperationException {
		return new DynamicPhysicsObject(createSphereRigid(mass, radius, position));
	}

	@Override
	public DynamicPhysicsObject createCuboid(float mass, float xLength, float yLength, float zLength) throws UnsupportedOperationException {
		return createCuboid(mass, xLength, yLength, zLength, new Transform());
	}

	@Override
	public DynamicPhysicsObject createCuboid(float mass, float xLength, float yLength, float zLength, Transform position) throws UnsupportedOperationException {
		return new DynamicPhysicsObject(createCuboidRigid(mass, xLength, yLength, zLength, position));
	}

	@Override
	public DynamicPhysicsObject createCylinder(float mass, float height, float radius, PhysicsObjectOrientation orientation) throws UnsupportedOperationException {
		return createCylinder(mass, height, radius, orientation, new Transform());
	}

	@Override
	public DynamicPhysicsObject createCylinder(float mass, float height, float radius, PhysicsObjectOrientation orientation, Transform position) throws UnsupportedOperationException {
		return new DynamicPhysicsObject(createCylinderRigid(mass, height, radius, orientation, position));
	}

	@Override
	public DynamicPhysicsObject createCone(float mass, float height, float radius, PhysicsObjectOrientation orientation) throws UnsupportedOperationException {
		return createCone(mass, height, radius, orientation, new Transform());
	}

	@Override
	public DynamicPhysicsObject createCone(float mass, float height, float radius, PhysicsObjectOrientation orientation, Transform position) throws UnsupportedOperationException {
		return new DynamicPhysicsObject(createConeRigid(mass, height, radius, orientation, position));
	}

	@Override
	public DynamicPhysicsObject createExactObject(float mass, IndexedMesh mesh) {
		return createExactObject(mass, mesh, new Transform());
	}

	@Override
	public DynamicPhysicsObject createExactObject(float mass, IndexedMesh mesh, Transform position) {
		throw new NotImplementedException();
	}

	public static DynamicPhysicsObjectFactory getInstance() {
		return instance;
	}
}
