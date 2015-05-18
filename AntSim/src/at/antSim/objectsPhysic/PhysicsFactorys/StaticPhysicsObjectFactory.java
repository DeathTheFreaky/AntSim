package at.antSim.objectsPhysic.PhysicsFactorys;

import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObjectOrientation;
import com.bulletphysics.linearmath.Transform;

/**
 * Created on 12.05.2015.
 *
 * @author Clemens
 */
public class StaticPhysicsObjectFactory extends AbstractPhysicsObjectFactory<StaticPhysicsObject> {

	private static StaticPhysicsObjectFactory instance = new StaticPhysicsObjectFactory();

	protected StaticPhysicsObjectFactory() {

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
	public StaticPhysicsObject createSphere(float mass, float radius) throws UnsupportedOperationException {
		return createSphere(mass, radius, new Transform());
	}

	@Override
	public StaticPhysicsObject createSphere(float mass, float radius, Transform position) throws UnsupportedOperationException {
		return new StaticPhysicsObject(createSphereRigid(mass, radius, position));
	}

	@Override
	public StaticPhysicsObject createCuboid(float mass, float xLength, float yLength, float zLength) throws UnsupportedOperationException {
		return createCuboid(mass, xLength, yLength, zLength, new Transform());
	}

	@Override
	public StaticPhysicsObject createCuboid(float mass, float xLength, float yLength, float zLength, Transform position) throws UnsupportedOperationException {
		return new StaticPhysicsObject(createCuboidRigid(mass, xLength, yLength, zLength, position));
	}

	@Override
	public StaticPhysicsObject createCylinder(float mass, float height, float radius, PhysicsObjectOrientation orientation) throws UnsupportedOperationException {
		return createCylinder(mass, height, radius, orientation, new Transform());
	}

	@Override
	public StaticPhysicsObject createCylinder(float mass, float height, float radius, PhysicsObjectOrientation orientation, Transform position) throws UnsupportedOperationException {
		return new StaticPhysicsObject(createCylinderRigid(mass, height, radius, orientation, position));
	}

	@Override
	public StaticPhysicsObject createCone(float mass, float height, float radius, PhysicsObjectOrientation orientation) throws UnsupportedOperationException {
		return createCone(mass, height, radius, orientation, new Transform());
	}

	@Override
	public StaticPhysicsObject createCone(float mass, float height, float radius, PhysicsObjectOrientation orientation, Transform position) throws UnsupportedOperationException {
		return new StaticPhysicsObject(createConeRigid(mass, height, radius, orientation, position));
	}

	public static StaticPhysicsObjectFactory getInstance() {
		return instance;
	}
}
