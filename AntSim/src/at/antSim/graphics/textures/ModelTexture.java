package at.antSim.graphics.textures;

/**Represents a texture that can be used to texture RawModels. 
 * 
 * @author Clemens
 * @see RawModel
 *
 */
public class ModelTexture {

	private int textureID; //id or "name" of this texture returned by org.newdawn.slick.opengl.Texture.getTextureID()
	
	private float shineDamper = 1; //how strong specular lighting appears when camera is not directly facing the reflected light
	private float reflectivity = 0; //used for specular lighting when camera is directly facing the reflected light - set to values > 0 for "shiny" objects like china
	/* Transparent textures need backface culling to be turned off, because since we can look through the mesh, 
	 * we could see that it has no "back" face -> backface would be black.
	 * 
	 * More information about face culling: https://www.youtube.com/watch?v=DZXbLk9_NJw and http://www.opengl-tutorial.org/intermediate-tutorials/tutorial-10-transparency/
	 */
	private boolean hasTransparency = false; 
	private boolean useFakeLighting = false; //transparent textures like grass need fake lighting (normals pointing directly upwards) to avoid weird shadow look
	
	private int numberOfRows = 1; //number of rows in the textureAtlas (since any ModelTexture could be a textureAtlas)
	
	/**Creates a new {@link ModelTexture}.
	 * 
	 * @param id - the id of the texture to be used for this ModelTexture.
	 */
	public ModelTexture(int id){
		this.textureID = id;
	}
	
	/**
	 * @return - the number of rows of sub-textures stored inside this texture which will be > 1 for a texture atlas
	 */
	public int getNumberOfRows() {
		return numberOfRows;
	}

	/**
	 * @param numberOfRows - the number of rows of textures stored inside this texture which will be > 1 for a texture atlas
	 */
	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
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
	 * @param reflectivity - how strongly light is reflected for specular lighting when camera is aiming directly at the reflected light
	 */
	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	/**
	 * @return - true if this texture has transparency
	 */
	public boolean isHasTransparency() {
		return hasTransparency;
	}

	/**
	 * @param hasTransparency - indicates whether a texture has transparency<br> 
	 * (back face culling will be disabled for transparent textures)
	 */
	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	/**
	 * @return - true if useFakeLighting is enabled (all normals pointing upwards for transparent textures like grass to avoid weird look)
	 */
	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	/**
	 * @param useFakeLighting - if set to true, all normals point upwards for transparent textures like grass to avoid weird look
	 */
	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}
}
