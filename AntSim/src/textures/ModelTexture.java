package textures;

/**Represents a texture that can be used to texture RawModels. 
 * 
 * @author Flo
 * @see RawModel
 *
 */
public class ModelTexture {

	private int textureID;
	
	public ModelTexture(int id){
		this.textureID = id;
	}
	
	/**
	 * @return - a texture's ID
	 */
	public int getID(){
		return this.textureID;
	}
}
