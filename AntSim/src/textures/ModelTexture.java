package textures;

/**Represents a texture that can be used to texture RawModels. 
 * 
 * @author Flo
 * @see RawModel
 *
 */
public class ModelTexture {

	private int textureID;
	
	private float shineDamper = 1; //how strong specular lighting appears when camera is not directly facing the reflected light
	private float reflectivity = 0; //reflectivity used for specular lighting
	
	public ModelTexture(int id){
		this.textureID = id;
	}
	
	/**
	 * @return - a texture's ID
	 */
	public int getID(){
		return this.textureID;
	}

	/**
	 * @return - shineDamper =  how strong specular lighting appears when camera is not directly facing the reflected light
	 */
	public float getShineDamper() {
		return shineDamper;
	}

	/**
	 * @param shineDamper - how strong specular lighting appears when camera is not directly facing the reflected light
	 */
	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	/**
	 * @return - reflectivity used for specular lighting
	 */
	public float getReflectivity() {
		return reflectivity;
	}

	/**
	 * @param reflectivity - how strongly light is reflected for specular lighting
	 */
	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	
	
}
