package at.antSim.graphics.terrains;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import at.antSim.EngineTester;
import at.antSim.Globals;
import at.antSim.graphics.graphicsUtils.Loader;
import at.antSim.graphics.graphicsUtils.Maths;
import at.antSim.graphics.models.RawModel;
import at.antSim.graphics.textures.ModelTexture;
import at.antSim.graphics.textures.TerrainTexture;
import at.antSim.graphics.textures.TerrainTexturePack;

/**Respresents a Terrain made up of a {@link RawModel}, a Blendmap and a {@link TerrainTexturePack}.
 * 
 * @author Flo
 *
 */
public class Terrain {
	
	//for 3d terrain
	private static final float MAX_HEIGHT = 40; //maximum height in positive and negative range of the terrain -> -40 to 40
	private static final float MAX_PIXEL_COLOR = 256 * 256 * 256; //3 color channels -> each channel has value between 0 and 256 -> 256*256*256 colors in total 
	
	// for flat terrain: private static final int VERTEX_COUNT = 128; //vertices along each side of the terrain
	
	private float x;
	private float z;
	private RawModel model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap; //yeah, so it's not really a TerrainTexture but it works
	
	private float[][] heights; //stores heights of all vertices of the terrain
	
	/**Creates a new {@link Terrain}.
	 * 
	 * @param gridX - terrain's x position as an int in a grid of terrains
	 * @param gridZ - terrain's z position as an int in a grid of terrains
	 * @param loader - a 3d model {@link Loader}
	 * @param texturePack - a {@link TerrainTexturePack} containing the different {@link TerrainTexture} to be drawn on the {@link Terrain}
	 * @param blendMap - a blendMap indicating how much of each terrain texture to be used on a specific vertex
	 * @param heightMap - name of the heightMap to use
	 */
	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * Globals.WORLD_SIZE;
		this.z = gridZ * Globals.WORLD_SIZE;
		this.model = generateTerrain(loader, heightMap);
	}
	
	/**Generates a {@link RawModel} for a {@link Terrain}.
	 * 
	 * @param loader - a 3D model {@link Loader}
	 * @param heightMap - name of the heightMap to use for creating 3d terrain
	 * @return - the generated terrain as a {@link RawModel}
	 */
	private RawModel generateTerrain(Loader loader, String heightMap){
		
		//load up the heightMap into a BufferedImage
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("res/" + heightMap + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int VERTEX_COUNT = image.getHeight(); //number of vertices along each side of terrain - each pixel in height map represents one vertex
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT * 1)];
		int vertexPointer = 0;
		for(int i = 0; i < VERTEX_COUNT; i++){ //i<128
			for(int j = 0 ; j < VERTEX_COUNT; j++){ //j<128
				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * Globals.WORLD_SIZE;
				float height = getHeight(j, i, image);
				heights[j][i] = height;
				vertices[vertexPointer  * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * Globals.WORLD_SIZE;
				Vector3f normal = calculateNormal(j, i, image);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz = 0; gz < VERTEX_COUNT - 1; gz++){
			for(int gx = 0 ; gx < VERTEX_COUNT - 1; gx++){
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
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
	
	/**Gets height of terrain for any given x,z coordinates.
	 * 
	 * @param worldX - x coordinate in world space
	 * @param worldZ - z coordinate in world space
	 * @return - the height of terrain for any given x,z coordinates
	 */
	public float getHeightOfTerrain(float worldX, float worldZ) {
		
		//convert world coordinates to positions relative to the terrain (for multiple terrains)
		float terrainX = worldX - this.x; 
		float terrainZ = worldZ - this.z;
		
		float gridSquareSize = Globals.WORLD_SIZE / ((float)heights.length - 1); //size of 1 grid square - if terrain has 4 vertices/points along 1 side of terrain it has 3 grid squares in between the points
		int gridX = (int) Math.floor(terrainX / gridSquareSize); 
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);	
				
		//return height of 0 if coordinates are outside the terrain -> not a valid grid square
		if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
			return 0;
		}
		
		float xCoord = (terrainX % gridSquareSize)/gridSquareSize; //distance of player to the right from top left corner of grid square -> result between 0 and 1
		float zCoord = (terrainZ % gridSquareSize)/gridSquareSize; //distance of player to the bottom from top left corner of grid square -> result between 0 and 1
		
		float answer; 
		if (xCoord <= (1-zCoord)) { //check which of the 2 triangles of a gridSquare the player is on
			answer = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), xCoord, zCoord); //return interpolated height within a triangle of vertices
		} else {
			answer = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), xCoord, zCoord);
		}
		
		return answer;
	}
	
	/**Caclute normal of vertex based on heights of neighboring vertices.
	 * 
	 * @param x - x coordinate of vertex 
	 * @param z - z coordinate of vertex 
	 * @param image - BufferedImage of the heightMap
	 * @return - a 3-dim Vector for the normal of a vertex
	 */
	private Vector3f calculateNormal (int x, int z, BufferedImage image) {
		float heightL = getHeight(x-1, z, image);
		float heightR = getHeight(x+1, z, image);
		float heightD = getHeight(x, z-1, image);
		float heightU = getHeight(x, z+1, image);
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise(); //make length of normal 1
		return normal;
	}
	
	/**Returns the actual height represented by one pixel in the height map.
	 * 
	 * @param x - x coordinate of height map
	 * @param y - y coordinate of height map
	 * @param image - a BufferedImage of the height map
	 * @return the actual height represented by one pixel in the height map.
	 */
	private float getHeight(int x, int y, BufferedImage image) {
		
		//check if coordinates are out of bounds for the heightmap
		if (x < 0 || x >= image.getHeight() || y < 0 || y >= image.getHeight()) {
			return 0;
		}	
		
		float height = image.getRGB(x, y); //returns value representing color which is between -MAX_PIXEL_COLOR and 0
		height += MAX_PIXEL_COLOR/2f; //change range to -MAX_PIXEL_COLOR/2f to MAX_PIXEL_COLOR/2f
		height /= MAX_PIXEL_COLOR/2f; //change range to -1 to 1
		height *= MAX_HEIGHT; //change range to -MAX_HEIGHT to MAX_HEIGHT
		
		return height;
	}

	public static float getSizeX() {
		return Globals.WORLD_SIZE;
	}

	/*public static int getVertexCount() {
		return VERTEX_COUNT;
	}*/

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
