package at.antSim.objectsPhysic;

import at.antSim.objectsPhysic.basics.PositionablePhysicsObject;
import at.antSim.utils.Maths;

import com.bulletphysics.linearmath.QuaternionUtil;
import com.bulletphysics.linearmath.Transform;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

/**
 * Created on 12.05.2015.
 *
 * @author Clemens
 */
public abstract class PositionablePhysicsObjectImpl extends ReadOnlyPhysicsObjectImpl implements PositionablePhysicsObject {

	@Override
	public void setPosition(float posX, float posZ, float posY) {
		setPosition(new Vector3f(posX, posY, posZ));
	}

	@Override
	public void setPosition(Vector3f position) {
		Transform bodyTransform = new Transform();
		getCollisionBody().getWorldTransform(bodyTransform);
		Quat4f bodyRotation = new Quat4f();
		bodyTransform.getRotation(bodyRotation);
		getCollisionBody().setWorldTransform(new Transform(new Matrix4f(bodyRotation, position, 1)));
	}

	@Override
	public void setRotation(float yaw, float pitch, float roll) {
//		Quat4f rotation = new Quat4f();
//		QuaternionUtil.setEuler(rotation, yaw, pitch, roll);
//		Transform bodyTransform = new Transform();
//		getCollisionBody().getWorldTransform(bodyTransform);
//		bodyTransform.setRotation(rotation);
//		getCollisionBody().setWorldTransform(bodyTransform);
		Vector3f position = getPosition();
		Transform bodyTransform = new Transform(Maths.createTransformationMatrix(Maths.vec3fToSlickUtil(position), pitch, yaw, roll));
		getCollisionBody().setWorldTransform(bodyTransform);
	}
}