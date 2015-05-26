package at.antSim.objectsPhysic.PhysicsFactorys;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;

import at.antSim.GTPMapper.GTPCone;
import at.antSim.GTPMapper.GTPCuboid;
import at.antSim.GTPMapper.GTPCylinder;
import at.antSim.GTPMapper.GTPObject;
import at.antSim.GTPMapper.GTPSphere;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObject;
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

	protected StaticPhysicsObjectFactory() {

	}

	@Override
	public StaticPhysicsObject createSphere(float mass, float radius) throws UnsupportedOperationException {
		return createSphere(mass, radius, new Transform());
	}

	@Override
	public StaticPhysicsObject createSphere(float mass, float radius, Transform position) throws UnsupportedOperationException {
		return new StaticPhysicsObject(createSphereRigid(0, radius, position));
	}

	@Override
	public StaticPhysicsObject createCuboid(float mass, float xLength, float yLength, float zLength) throws UnsupportedOperationException {
		return createCuboid(mass, xLength, yLength, zLength, new Transform());
	}

	@Override
	public StaticPhysicsObject createCuboid(float mass, float xLength, float yLength, float zLength, Transform position) throws UnsupportedOperationException {
		return new StaticPhysicsObject(createCuboidRigid(0, xLength, yLength, zLength, position));
	}

	@Override
	public StaticPhysicsObject createCylinder(float mass, float height, float radius, PhysicsObjectOrientation orientation) throws UnsupportedOperationException {
		return createCylinder(mass, height, radius, orientation, new Transform());
	}

	@Override
	public StaticPhysicsObject createCylinder(float mass, float height, float radius, PhysicsObjectOrientation orientation, Transform position) throws UnsupportedOperationException {
		return new StaticPhysicsObject(createCylinderRigid(0, height, radius, orientation, position));
	}

	@Override
	public StaticPhysicsObject createCone(float mass, float height, float radius, PhysicsObjectOrientation orientation) throws UnsupportedOperationException {
		return createCone(mass, height, radius, orientation, new Transform());
	}

	@Override
	public StaticPhysicsObject createCone(float mass, float height, float radius, PhysicsObjectOrientation orientation, Transform position) throws UnsupportedOperationException {
		return new StaticPhysicsObject(createConeRigid(0, height, radius, orientation, position));
	}

	@Override
	public StaticPhysicsObject createExactObject(float mass, IndexedMesh mesh) {
		return createExactObject(mass, mesh, new Transform());
	}

	@Override
	public StaticPhysicsObject createExactObject(float mass, IndexedMesh mesh, Transform position) {
		return new StaticPhysicsObject(createExactRigid(mass, mesh, position));
	}

	public static StaticPhysicsObjectFactory getInstance() {
		return instance;
	}
}
