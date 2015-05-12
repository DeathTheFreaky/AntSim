package at.antSim.objectsPhysic.PhysicsFactorys;

import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObjectOrientation;
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
public interface PhysicsObjectFactory<E extends PhysicsObject> {

	/**
	 * Creates an PhysicsObject representing a Sphere with mass and radius.
	 *
	 * @param mass   Mass of the Object
	 * @param radius Radius of the Sphere
	 * @return
	 */
	E createSphere(float mass, float radius);

	/**
	 * Creates an PhysicsObject representing a Sphere with mass and radius.
	 *
	 * @param mass     Mass of the Object
	 * @param radius   Radius of the Sphere
	 * @param position Position of the Sphere
	 * @return
	 */
	E createSphere(float mass, float radius, Transform position);

	/**
	 * Creates an PhysicsObject representing a Cuboid with mass and x y z dimensions.
	 *
	 * @param mass    Mass of the Object
	 * @param xLength Length in X-dimension
	 * @param yLength Length in Y-dimension
	 * @param zLength Length in Z-dimension
	 */
	E createCuboid(float mass, float xLength, float yLength, float zLength);

	/**
	 * Creates an PhysicsObject representing a Cuboid with mass and x y z dimensions.
	 *
	 * @param mass     Mass of the Object
	 * @param xLength  Length in X-dimension
	 * @param yLength  Length in Y-dimension
	 * @param zLength  Length in Z-dimension
	 * @param position Position of the Cuboid
	 * @return
	 */
	E createCuboid(float mass, float xLength, float yLength, float zLength, Transform position);

	/**
	 * Creates an PhysicsObject representing a Cylinder with mass, height, radius and oriented on the axis.
	 *
	 * @param mass        Mass of the Object
	 * @param height      Height of the Cylinder in the specified orientation
	 * @param radius      Radius of the Cylinder
	 * @param orientation Orientation of the Cylinder defined by {@link PhysicsObjectOrientation}
	 */
	E createCylinder(float mass, float height, float radius, PhysicsObjectOrientation orientation);


	/**
	 * Creates an PhysicsObject representing a Cylinder with mass, height, radius and oriented on the axis.
	 *
	 * @param mass        Mass of the Object
	 * @param height      Height of the Cylinder in the specified orientation
	 * @param radius      Radius of the Cylinder
	 * @param orientation Orientation of the Cylinder defined by {@link PhysicsObjectOrientation}
	 * @param position    Position of the Cylinder
	 * @return
	 */
	E createCylinder(float mass, float height, float radius, PhysicsObjectOrientation orientation, Transform position);

	/**
	 * Creates an PhysicsObject representing a Cone with mass, height, radius and oriented on the axis.
	 *
	 * @param mass        Mass of the Object
	 * @param height      Height of the Cone in the specified orientation
	 * @param radius      Radius of the Cone
	 * @param orientation Orientation of the Cone defined by {@link PhysicsObjectOrientation}
	 */
	E createCone(float mass, float height, float radius, PhysicsObjectOrientation orientation);


	/**
	 * Creates an PhysicsObject representing a Cone with mass, height, radius and oriented on the axis.
	 *
	 * @param mass        Mass of the Object
	 * @param height      Height of the Cone in the specified orientation
	 * @param radius      Radius of the Cone
	 * @param orientation Orientation of the Cone defined by {@link PhysicsObjectOrientation}
	 * @param position    Position of the Cone
	 * @return
	 */
	E createCone(float mass, float height, float radius, PhysicsObjectOrientation orientation, Transform position);

	/**
	 * Converts the passed parameters to a {@link Transform}-Object which can be used to create PhysicsObjects at a set start location.
	 *
	 * @param posX
	 * @param posY
	 * @param posZ
	 * @param yaw
	 * @param pitch
	 * @param roll
	 * @return
	 */
	public static Transform convertToTransform(float posX, float posY, float posZ, float yaw, float pitch, float roll) {
		Quat4f rotation = new Quat4f();
		QuaternionUtil.setEuler(rotation, yaw, pitch, roll);
		return convertToTransform(new Vector3f(posX, posY, posZ), rotation);
	}

	/**
	 * Converts the passed parameters to a {@link Transform}-Object which can be used to create PhysicsObjects at a set start location.
	 *
	 * @param position
	 * @param rotation
	 * @return
	 */
	public static Transform convertToTransform(Vector3f position, Quat4f rotation) {
		return new Transform(new Matrix4f(rotation, position, 1));
	}
}
