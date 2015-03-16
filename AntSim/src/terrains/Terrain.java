package terrains;

import models.RawModel;
import renderEngine.Loader;
import textures.ModelTexture;

/**Respresents a Terrain made up of a {@link RawModel} and a {@link ModelTexture}.
 * 
 * @author Flo
 *
 */
public class Terrain {

	private static final float SIZE = 800;
	private static final int VERTEX_COUNT = 128; //vertices along each side of the terrain
	
	private float x;
	private float z;
	private RawModel model;
	private ModelTexture texture;
	
	/**Creates a new {@link Terrain}.
	 * 
	 * @param gridX - terrain's x position as an int
	 * @param gridZ - terrain's z position as an int
	 * @param loader - a 3d model {@link Loader}
	 * @param texture - a {@link ModelTexture}
	 */
	public Terrain(int gridX, int gridZ, Loader loader, ModelTexture texture) {
		this.texture = texture;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
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
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE - 400;
				vertices[vertexPointer*3+1] = 0;
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE - 800;
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

	public static float getSize() {
		return SIZE;
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

	public ModelTexture getTexture() {
		return texture;
	}
}
