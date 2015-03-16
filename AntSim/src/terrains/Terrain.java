package terrains;

import engineTester.MainApplication;
import models.RawModel;
import renderEngine.Loader;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

/**Respresents a Terrain made up of a {@link RawModel} and a {@link ModelTexture}.
 * 
 * @author Flo
 *
 */
public class Terrain {

	private final static float sizeX = MainApplication.getWorldSizeX();
	private final static float sizeZ = MainApplication.getWorldSizeZ();
	private static final int VERTEX_COUNT = 128; //vertices along each side of the terrain
	
	private float x;
	private float z;
	private RawModel model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap; //yeah, so it's not really a TerrainTexture but it works
	
	/**Creates a new {@link Terrain}.
	 * 
	 * @param gridX - terrain's x position as an int
	 * @param gridZ - terrain's z position as an int
	 * @param loader - a 3d model {@link Loader}
	 * @param texturePack - a {@link TerrainTexturePack} containing the different {@link TerrainTexture} to be drawn on the {@link Terrain}
	 * @param blendMap - a blendMap indicating how much of each terrain texture to be used on a specific vertex
	 */
	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * sizeX;
		this.z = gridZ * sizeZ;
		this.model = generateTerrain(loader);
	}
	
	/**Generates a {@link RawModel} for a {@link Terrain}.
	 * 
	 * @param loader - a 3D model {@link Loader}
	 * @return - the generated terrain as a {@link RawModel}
	 */
	private RawModel generateTerrain(Loader loader){
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT*1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){ //i<128
			for(int j=0;j<VERTEX_COUNT;j++){ //j<128
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * sizeX - sizeX/2;
				vertices[vertexPointer*3+1] = 0;
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * sizeZ;
				normals[vertexPointer*3] = 0;
				normals[vertexPointer*3+1] = 1;
				normals[vertexPointer*3+2] = 0;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

	public static float getSizeX() {
		return sizeX;
	}
	
	public static float getSizeZ() {
		return sizeZ;
	}

	public static int getVertexCount() {
		return VERTEX_COUNT;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawModel getModel() {
		return model;
	}

	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	public void setTexturePack(TerrainTexturePack texturePack) {
		this.texturePack = texturePack;
	}

	public TerrainTexture getBlendMap() {
		return blendMap;
	}

	public void setBlendMap(TerrainTexture blendMap) {
		this.blendMap = blendMap;
	}
}
