package textures;

import terrains.Terrain;

/**{@link TerrainTexturePack} holds the different textures which are drawn on the {@link Terrain} using a blendMap.
 * 
 * @author Flo
 *
 */
public class TerrainTexturePack {
	
	private TerrainTexture backgroundTexture;
	private TerrainTexture rTexture;
	private TerrainTexture gTexture;
	private TerrainTexture bTexture;
	
	/**Creates a {@link TerrainTexturePack} for storing different textures drawn to a terrain using a blendMap.
	 * 
	 * @param backgroundTexture - the actual backgroundTexture
	 * @param rTexture - texture corresponding red on the blendMap
	 * @param gTexture - texture corresponding green on the blendMap
	 * @param bTexture - texture corresponding blue on the blendMap
	 */
	public TerrainTexturePack(TerrainTexture backgroundTexture,
			TerrainTexture rTexture, TerrainTexture gTexture,
			TerrainTexture bTexture) {
		
		this.backgroundTexture = backgroundTexture;
		this.rTexture = rTexture;
		this.gTexture = gTexture;
		this.bTexture = bTexture;
	}

	public TerrainTexture getBackgroundTexture() {
		return backgroundTexture;
	}

	public TerrainTexture getrTexture() {
		return rTexture;
	}

	public TerrainTexture getgTexture() {
		return gTexture;
	}

	public TerrainTexture getbTexture() {
		return bTexture;
	}
}
