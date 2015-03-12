package entities;

import org.lwjgl.util.vector.Vector3f;

/**Represents a light source with a position and a color (the light intensity).
 * 
 * @author Flo
 *
 */
public class Light {
	
	private Vector3f position;
	private Vector3f color;
	
	/**Constructs a light source.
	 * 
	 * @param position - the light's position in the world
	 * @param color - the intensity of the light
	 */
	public Light(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}
}
