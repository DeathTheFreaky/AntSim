package at.antSim.graphics.textures;

import at.antSim.graphics.terrains.Terrain;

/**Represents a TerrainTexture that can be used to texture {@link Terrain}s.
 * 
 * @author Flo
 *
 */
public class TerrainTexture {
	
	private int textureID;
	
	/**Creates a new {@link TerrainTexture}.
	 * 
	 * @param textureID - id of the raw texture to use for this {@link TerrainTexture}
	 */
	public TerrainTexture(int textureID) {
		this.textureID = textureID;
	}
	
	public int getTextureID() {
		return textureID;
	}
}
