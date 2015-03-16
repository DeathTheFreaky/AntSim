package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;

/**An Entity represents an instance of a TexturedModel.<br>
 * In addition to the TexturedModel, an Entity also contains the position, rotation and scale of this model.
 * 
 * @author Flo
 * @see TexturedModel
 */
public class Entity {
	
	private TexturedModel model;
	private Vector3f position;
	private float rotX, rotY, rotZ;
	private float scale;
	
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, 
			float scale) {
		super();
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	
	/**Moves the Entity in the world.
	 * 
	 * @param dx - how far to move the Entity on the x-Axis
	 * @param dy - how far to move the Entity on the y-Axis
	 * @param dz - how far to move the Entity on the z-Axis
	 */
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	
	/**Rotates the Entity in the world.
	 * 
	 * @param dx - how far to rotate the Entity around the x-Axis
	 * @param dy - how far to rotate the Entity around the y-Axis
	 * @param dz - how far to rotate the Entity around the z-Axis
	 */
	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}

	/**
	 * @return - the TexturedModel used for this Entity
	 */
	public TexturedModel getModel() {
		return model;
	}

	/**
	 * @return - the Entitie's position
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * @return - the Entitie's X rotation
	 */
	public float getRotX() {
		return rotX;
	}

	/**
	 * @return - the Entitie's Y rotation
	 */
	public float getRotY() {
		return rotY;
	}

	/**
	 * @return - the Entitie's Z rotation
	 */
	public float getRotZ() {
		return rotZ;
	}

	/**
	 * @return - the Entitie's scale
	 */
	public float getScale() {
		return scale;
	}
}