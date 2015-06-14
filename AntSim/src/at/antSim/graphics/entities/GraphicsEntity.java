package at.antSim.graphics.entities;

import at.antSim.Globals;
import at.antSim.graphics.models.TexturedModel;
import at.antSim.objectsKI.Entity;
import at.antSim.objectsPhysic.basics.PhysicsObject;

/**An GraphicsEntity represents a graphical representation of a real-world object.<br>
 * It consists of a {@link TexturedModel}, a PhysicsObject and the scale of its model.<br>
 * Position and Rotation of the parenting {@link Entity} are stored in its {@link PhysicsObject}.<br>
 * Additional behavior may be defined by extending this {@link GraphicsEntity} class.
 * 
 * @author Flo
 * @see TexturedModel
 */
public class GraphicsEntity {
	
	private TexturedModel model;
	private float scale;
	private boolean useTransparency = false;
	
	private int textureIndex = 0; //indicates which texture in a texture atlas the entity uses
	
	/**Creates a new {@link GraphicsEntity}.
	 * 
	 * @param textureIndex - indicates which texture in texture atlas the entity uses
	 * @param model - 3-dimensional model of the entity with a texture
	 * @param scale - the scale applied to the entity's model
	 */
	public GraphicsEntity(TexturedModel model, int textureIndex, float scale) {
		this.textureIndex = textureIndex;
		this.model = model;
		this.scale = scale;
		this.useTransparency = model.usesTransparency();
	}
	
	/**
	 * @return - the u-texture coordinate of this Entitie's textureIndex in the texture atlas
	 */
	public float getTextureXOffset() {
		int column = textureIndex % model.getTexture().getNumberOfRows(); //get column index of texture in texture atlas
		return (float)column/(float)model.getTexture().getNumberOfRows(); //calculate u-texture coordinate offset in texture atlas
	}
	
	/**
	 * @return - the v-texture coordinate of this Entitie's textureIndex in the texture atlas
	 */
	public float getTextureYOffset() {
		int row = textureIndex/model.getTexture().getNumberOfRows(); //get row index of texture in texture atlas
		return (float)row/(float)model.getTexture().getNumberOfRows(); //calculate v-texture coordinate offset in texture atlas
	}
	
//	/**Moves the Entity in the world.
//	 * 
//	 * @param dx - how far to move the Entity on the x-Axis
//	 * @param dy - how far to move the Entity on the y-Axis
//	 * @param dz - how far to move the Entity on the z-Axis
//	 */
//	public void increasePosition(float dx, float dy, float dz) {
//		this.position.x += dx;
//		this.position.y += dy;
//		this.position.z += dz;
//	}
//	
//	/**Rotates the Entity in the world.
//	 * 
//	 * @param dx - how far to rotate the Entity around the x-Axis
//	 * @param dy - how far to rotate the Entity around the y-Axis
//	 * @param dz - how far to rotate the Entity around the z-Axis
//	 */
//	public void increaseRotation(float dx, float dy, float dz) {
//		this.rotX += dx;
//		this.rotY += dy;
//		this.rotZ += dz;
//	}

	/**
	 * @return - the TexturedModel used for this Entity
	 */
	public TexturedModel getModel() {
		return model;
	}

	public float getScale() {
		return scale;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public void setTextureIndex(int textureIndex) {
		this.textureIndex = textureIndex;
	}
	
	public int getTextureIndex() {
		return textureIndex;
	}
	
	public void useTransparency(boolean useTransparency) {
		this.useTransparency = useTransparency;
	}

	public float getTransparency() {
		if (useTransparency) {
			return Globals.currentGhostTransparency;
		}
		else {
			return 0;
		}
	}
}
