package at.antSim.graphics.textures;

import at.antSim.graphics.terrains.Terrain;

/**{@link TerrainTexturePack} holds the different textures which are drawn on the {@link Terrain} using a blendMap.<br>
 * <br>
 * If a pixel on the blendMap is black, the backgroundTexture is chosen.<br>
 * Else, red green and blue textures will be mixed depending on the "strength" of each specific color channel of the pixel.<br>
 * So, if a pixel has a color of (255, 128, 0), the resulting texture mixture will consist of 2/3 red texture, 1/3 green texture and 0 blue texture.
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
