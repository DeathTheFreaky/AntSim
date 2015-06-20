package at.antSim.graphics.entities;

import org.lwjgl.util.vector.Vector3f;

/**Represents a light source with a position and a color (the light intensity).
 * 
 * @author Flo
 *
 */
public class Light {
	
	private Vector3f position;
	private Vector3f color;
	/*attenuation -> pointed lighting - light gets weaker if distance increases
	* the below causes no attenuation (param 2 and 3 are 0) -> sunlight = infinte
	* attenuation factor = (att1) + (att2*d) + (att3*d*d); d = distance from light - see: http://www.ozone3d.net/tutorials/glsl_lighting_phong_p4.php*/
	private Vector3f attenuation = new Vector3f(1, 0, 0);
	
	/**Constructs a light source.
	 * 
	 * @param position - the light's position in the world
	 * @param color - the intensity of the light
	 */
	public Light(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
	}
	
	/**Constructs a light source.
	 * 
	 * @param position - the light's position in the world
	 * @param color - the intensity of the light
	 * @param attenuation - the attenuation to be used for pointed light sources
	 */
	public Light(Vector3f position, Vector3f color, Vector3f attenuation) {
		this.position = position;
		this.color = color;
		this.attenuation = attenuation;
	}
	
	public Vector3f getAttenuation() {
		return attenuation;
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
