package at.antSim.graphics.terrains;

import at.antSim.Globals;
import at.antSim.graphics.graphicsUtils.Maths;
import at.antSim.graphics.graphicsUtils.OpenGLLoader;
import at.antSim.graphics.models.RawModel;
import at.antSim.graphics.textures.TerrainTexture;
import at.antSim.graphics.textures.TerrainTexturePack;
import at.antSim.objectsPhysic.PhysicsFactorys.StaticPhysicsObjectFactory;
import at.antSim.objectsPhysic.PhysicsManager;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import com.bulletphysics.collision.shapes.IndexedMesh;
import com.bulletphysics.linearmath.Transform;
import org.lwjgl.util.vector.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**Respresents a Terrain made up of a {@link RawModel}, a Blendmap and a {@link TerrainTexturePack}.<br>
 * The terrain's heights will be created according to the values stored in an imagefile grayscale heightmap.<br>
 * <br>
 * A tutorial on how to use imagefile heightmaps for terrain creation can be found at:
 * http://www.videotutorialsrock.com/opengl_tutorial/terrain/video.php.
 * 
 * @author Flo
 *
 */
public class Terrain {
	
	//for 3d terrain - used with heightmap
	public static final float MAX_HEIGHT = 40; //maximum height in positive and negative range of the terrain -> -40 to 40
	private static final float MAX_PIXEL_COLOR = 256 * 256 * 256; //3 color channels -> each channel has value between 0 and 256 -> 256*256*256 colors in total 
		
	private float x; //position of this terrain in the worldspace -> there can be muliple terrains, eg. if terrains have size 800 and then terrain 1 starts at 0, terrain 2 at 800...
	private float z;
	private RawModel model;
	private TerrainTexturePack texturePack; //use different textures and a blendmap to texture the terrain 3d model
	private TerrainTexture blendMap; //yeah, so it's not really a TerrainTexture but it works
	
	private float[][] heights; //stores heights of all vertices of the terrain
	
	/**Creates a new {@link Terrain}.
	 * 
	 * @param gridX - terrain's x position as an int in a grid of terrains
	 * @param gridZ - terrain's z position as an int in a grid of terrains
	 * @param loader - a 3d model {@link OpenGLLoader}
	 * @param texturePack - a {@link TerrainTexturePack} containing the different {@link TerrainTexture} to be drawn on the {@link Terrain}
	 * @param blendMap - a blendMap indicating how much of each terrain texture to be used on a specific vertex
	 * @param heightMap - name of the heightMap file to use - must be stored in /res folder and must by a square (height = width in pixels)
	 */
	public Terrain(int gridX, int gridZ, OpenGLLoader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * Globals.WORLD_SIZE;
		this.z = gridZ * Globals.WORLD_SIZE;
		this.model = generateTerrain(loader, heightMap);
	}
	
	/**Generates a {@link RawModel} for a {@link Terrain}.<br>
	 * <br>
	 * 
	 * @param loader - a 3D model {@link OpenGLLoader}
	 * @param heightMapStr - name of the heightMap to use for creating 3d terrain
	 * @return - the generated terrain as a {@link RawModel}
	 */
	private RawModel generateTerrain(OpenGLLoader loader, String heightMapStr){
		
		//generates terrain from (0,0,0) to (Worldsize, 0 , Worldsize)
				
		//load up the heightMap into a BufferedImage
		BufferedImage heightMap = null;
		try {
			heightMap = ImageIO.read(new File(Globals.RESOURCES + heightMapStr + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/* We need to determine how many vertexes are represented by our image-file heightmap. 
		 * To do so, we define that one vertex shall be described by one pixel in the image-file.
		 * Since our Terrain is a square, we only need to get either the height or the width of our heightmap in pixels
		 * and set the result as our vertex count.
		 */
		int VERTEX_COUNT = heightMap.getHeight(); 
		heights = new float[VERTEX_COUNT][VERTEX_COUNT]; //create a 2dim array to hold our height values
		
		int count = VERTEX_COUNT * VERTEX_COUNT; //determine the total number of vertices
		float[] vertices = new float[count * 3]; //the terrain's vertices as 3dim vectors (x,y,z positions)
		float[] normals = new float[count * 3]; //the terrain's normals as 3dim vectors (x,y,z positions)
		float[] textureCoords = new float[count * 2]; //the terrain's texture Coordinates as 2dim vectors (u,v positions)
	
		/*
		 * vertexPointer is the index of current the vertex in one-dimensional float array of vertexes' positions, normals and texture Coordinates. 
		 * Eg: 	vertex index:	0			1
		 * 		position coord:	x0, y0, z0, x1, y1, z1
		 * 		normal coord:	x0, y0, z0, x1, y1, z1
		 * 		texture coord:	u0, v0		u1, v1
		 */
		int vertexPointer = 0; 
		
		//fill in all vertices (positions), normals and texture Coordinates; use vertex position y-values calculated from 2dim heightmap
		for(int i = 0; i < VERTEX_COUNT; i++) { //height map's "rows" -> represent z value = height of pixel in height map image
			for(int j = 0 ; j < VERTEX_COUNT; j++) { //height map's "columns" -> represent x value = width of pixel in  height map image
				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * Globals.WORLD_SIZE; //calculate the scaled vertex's x coordinate; maximum is VERTEX_COUNT -1 because index j starts at 0!
				float height = getHeight(j, i, heightMap);
				heights[j][i] = height;
				vertices[vertexPointer  * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * Globals.WORLD_SIZE; //calculate the scaled vertex's z coordinate; maximum is VERTEX_COUNT -1 because index i starts at 0!
				Vector3f normal = calculateNormal(j, i, heightMap);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1); //texture coordinate u corresponds to pixel at x-coordinate of height map image
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1); //texture coordinate v corresponds to pixel at y-coordinate of height map image
				vertexPointer++;
			}
		}
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int pointer = 0;
		for(int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			for(int gx = 0 ; gx < VERTEX_COUNT - 1; gx++) {
				/*
				 * Stores the vertex indices for the two triangles of a grid square in a one dimensional array. 
				 * So each triangle will occupy 3 consecutive places in the 1dim array, and a square takes up 6 consecutive places in the 1dim array.
				 * This way, instead of storing three x,y,z coords for each vertex multiple times (when vertexes share the same coordinates),
				 * we only store an index to these coordinates (and also its normal and texture coords), resulting in much smaller storage use and data transfer to the gpu.
				 * 
				 * The order in which we store our triangles does not really matter, as long as all three vertex indices of the triangle are stored consecutively and the data
				 * the indices are pointing to is stored in the correct order. (Which is the case anyways if we create our 3d model dynamically, however for OBJFileLoader
				 * we would first have to order our position coords, texture coords and normals accordingly to the vertex indices.
				 * 
				 * For more information about how vertex positional coordinates, texture coordinates, normals and indices are stored, see the comments in OBJFileLoader.
				 */
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				/* store two triangles for each grid square in counter-clockwise order -> in context of OpenGL counter-clockwise order means the front face of a triangle */
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		
		//create indexedMesh and load to physics world for collision detection
		// http://www.java-gaming.org/index.php?topic=22894.0
		IndexedMesh myMesh = new IndexedMesh();
		myMesh.numTriangles = indices.length / 3; //number of triangles -> each vertex in a triangle has a unique index, each triangle is composed of 3 vertices
		myMesh.numVertices = vertices.length / 3; //number of vertices
		myMesh.triangleIndexBase = ByteBuffer.allocateDirect(indices.length*4).order(ByteOrder.nativeOrder());
		myMesh.triangleIndexBase.asIntBuffer().put(indices);
		myMesh.triangleIndexStride = 3 * 4;
		myMesh.numVertices = vertices.length / 3;
		myMesh.vertexBase = ByteBuffer.allocateDirect(vertices.length*4).order(ByteOrder.nativeOrder());
		myMesh.vertexBase.asFloatBuffer().put(vertices);
		myMesh.vertexStride = 3 * 4;

		StaticPhysicsObject obj = StaticPhysicsObjectFactory.getInstance().createExactObject("terrain", 0, myMesh,
				new Transform(Maths.createTransformationMatrix(new Vector3f(0, 0, -Globals.WORLD_SIZE), 0, 0, 0)));
		PhysicsManager.getInstance().registerPhysicsObject(obj);
		
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	
	/**Gets height of terrain for any given x,z coordinates.
	 * 
	 * @param worldX - x coordinate in world space
	 * @param worldZ - z coordinate in world space
	 * @return - the height of terrain for any given x,z coordinates
	 */
	public float getHeightOfTerrain(float worldX, float worldZ) {
		
		/* The term "player" in these comments is only used for simpler visualization. In reality, we are talking about the point the cursor is currently placed at. */
		
		//convert world coordinates to positions relative to the terrain (for multiple terrains - object space)
		float terrainX = worldX - this.x; 
		float terrainZ = worldZ - this.z;
		
		float gridSquareSize = Globals.WORLD_SIZE / ((float)heights.length - 1); //size of 1 grid square - if terrain has 4 vertices/points along 1 side of terrain it has 3 grid squares in between the points
		int gridX = (int) Math.floor(terrainX / gridSquareSize); //array's indizes can only be accessed with ints, hence the gridX position is only an approximation of the original float worldX coordinate
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);	
				
		//return height of 0 if coordinates are outside the terrain -> not a valid grid square inside this terrain
		if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
			return 0;
		}
		
		/* distance to the right from left border of the grid square where player is standing on -> result scaled between 0 (left border of grid square) and 1 (right border of grid square) */
		float xCoord = (terrainX % gridSquareSize)/gridSquareSize; 
		/* distance to the bottom from top border of the grid square where player is standing on -> result scaled between 0 (top border of grid square) and 1 (bottom border of grid square) */
		float zCoord = (terrainZ % gridSquareSize)/gridSquareSize; 
		
		float calcHeight; 
		/*
		 * Each grid square consists of two triangles.
		 * The square has the four corner points (X,Z) in the upper left, (X+1, Z) in the upper right, (X, Z+1) in the lower left and (X+1, Z+1) in the lower right corner.
		 * (Notice that the z-Axis starts with 0 at the top border!!!!).
		 * The first triangle has the corner points (X, Z), (X+1, Z), (X, Z+1) and the second triangle (X+1, Z), (X+1, Z+1), (X, Z+1).
		 * 
		 * To determine on which of the two triangles the player is currently standing, we use a simple mathematical observation:
		 * Since we are using perfect squares, the both diagonals split the square into same proportions.
		 * So a player will be standing on the left triangle if its x-coord is smaller than its z-coord. 
		 * If x-coord and z-coord equal, the player is standing on the edge between the left and the right triangle.
		 * If the x-coord is bigger than the z-coord, the player is standing on the right triangle.
		 * 
		 * Since z is measured from the top border, as opposed to the bottom border as we are used to, we have to substract the z-coord from 1 to get our desired results.
		 */
		if (xCoord <= (1-zCoord)) { 
			//return interpolated height within a triangle of vertices
			calcHeight = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), xCoord, zCoord); 
		} else {
			//return interpolated height within a triangle of vertices
			calcHeight = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), xCoord, zCoord);
		}
		
		return calcHeight;
	}
	
	/**Calculate the normal of a vertex as an average of the heights of its neighboring vertices.<br>
	 * The normal will make the terrain respond "realistically" to light sources.
	 * 
	 * @param x - x coordinate of vertex 
	 * @param z - z coordinate of vertex 
	 * @param heightMap - BufferedImage of the heightMap
	 * @return - a 3-dim Vector for the normal of a vertex
	 */
	private Vector3f calculateNormal (int x, int z, BufferedImage heightMap) {
		float heightL = getHeight(x-1, z, heightMap);
		float heightR = getHeight(x+1, z, heightMap);
		float heightD = getHeight(x, z-1, heightMap);
		float heightU = getHeight(x, z+1, heightMap);
		/*
		 * Calculate the normal of this vertex based on the heights of the surrounding vertexes.
		 * This might not be the most precise mathematical method, but it looks good since the normals don't just "stick upwards", 
		 * but take into consideration the "flow of surrounding terrain".
		 * 
		 * Eg.: if the left vertex's height equals the right vertex's height, the x component of the normal will be zero; 
		 * 		if the left vertex's height is smaller then the right vertex's height, the normal tilts to the left -> its x component becomes negative
		 * The higher the height difference between the left and the right neighboring vertexes, the bigger the tilt to the lower of the two.
		 * 
		 * The same rules apply for the z component, based and the up- and down neighbouring vertexes' heights.
		 * 
		 * The value of the y component is basically an empirical value that controls the magnitude of a normal vector's tilt.
		 * Setting it to two looks pretty good when testing in-game.
		 */
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise(); //make length of normal 1 because the normal is a direction, not a point, and a directions are only useful if the refer to the same standard unit, which is 1 in our case
		return normal;
	}
	
	/**Returns the actual height represented by one pixel in the height map.
	 * 
	 * @param x - x coordinate of height map
	 * @param y - y coordinate of height map
	 * @param heightMap - a BufferedImage of the height map
	 * @return the actual height represented by one pixel in the height map.
	 */
	private float getHeight(int x, int y, BufferedImage heightMap) {
		
		//check if coordinates are out of bounds for the heightmap
		if (x < 0 || x >= heightMap.getHeight() || y < 0 || y >= heightMap.getHeight()) {
			return 0;
		}	
		
		/* The color white represents the highest possible, the color black the lowest possible terrain,
		 * and grey is somewhere in between. */
		float height = heightMap.getRGB(x, y); //Returns an integer pixel in the default RGB color model (TYPE_INT_ARGB) which stretches from -MAX_PIXEL_COLOR to 0
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
