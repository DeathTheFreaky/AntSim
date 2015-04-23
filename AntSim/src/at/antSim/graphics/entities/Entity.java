package at.antSim.graphics.entities;

import org.lwjgl.util.vector.Vector3f;

import at.antSim.graphics.models.TexturedModel;

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
	
	private int textureIndex = 0; //indicates which texture in texture atlas the entity uses
	
	/**Creates a new {@link Entity}.
	 * 
	 * @param textureIndex - indicates which texture in texture atlas the entity uses
	 * @param model - 3-dimensional model of the entity with a texture
	 * @param position - the entity's x,y,z coordinates in world space
	 * @param rotX - the entity's X rotation in degrees
	 * @param rotY - the entity's Y rotation in degrees
	 * @param rotZ - the entity's Z rotation in degrees
	 * @param scale - the scale applied to the entity's model
	 */
	public Entity(TexturedModel model, int textureIndex, Vector3f position, float rotX, float rotY, float rotZ, 
			float scale) {
		this.textureIndex = textureIndex;
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	
	/**
	 * @return - the Column of this Entitie's textureIndex in the texture atlas
	 */
	public float getTextureXOffset() {
		int column = textureIndex%model.getTexture().getNumberOfRows();
		return (float)column/(float)model.getTexture().getNumberOfRows();
	}
	
	/**
	 * @return - the Row of this Entitie's textureIndex in the texture atlas
	 */
	public float getTextureYOffset() {
		int row = textureIndex/model.getTexture().getNumberOfRows();
		return (float)row/(float)model.getTexture().getNumberOfRows();
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
	
	public void setPosition(Vector3f position) {
		this.position = position;
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
