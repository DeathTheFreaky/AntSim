package renderEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;

/**OBJLoader 
 * 
 * @author Flo
 *
 */
public class OBJLoader {
	
	/**Takes in an .obj File, extracts all data from it (vertices, texture coordinates, normals, indices)
	 * and returns a {@link RawModel} built on this data.
	 * 
	 * @param fileName - the name of the .obj file
	 * @param loader - a {@link Loader} object for loading 3D models into memory
	 * @return - the {@link RawModel} created from the data in the .obj File
	 */
	public static RawModel loadObjModel(String fileName, Loader loader) {
		
		//open the .obj file
		FileReader fr = null;
		try {
			fr = new FileReader(new File("res/"+fileName+".obj"));
		} catch (FileNotFoundException e) {
			System.err.println("Could not load file!");
			e.printStackTrace();
		}
		
		//create a BufferedReader to read the files contents into a line String
		BufferedReader reader = new BufferedReader(fr);
		String line;
		
		//Lists holding all data for a 3DModel stored in a .obj File.
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		
		//Loader needs float and integer arrays in order to load data up into VAOs
		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] textureArray = null;
		int[] indicesArray = null;
		
		try {
			
			//read all vertices, texture coordinates and normals data from .obj file and store to our ArrayLists
			while(true) {
				
				line = reader.readLine();
				String[] currentLine = line.split(" "); //splits line into single data values separated by spaces
				
				if (line.startsWith("v ")) {
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), 
							Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
				}
				else if (line.startsWith("vt ")) {
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), 
							Float.parseFloat(currentLine[2]));
					textures.add(texture);
				}
				else if (line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), 
							Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					normals.add(normal);
				}
				else if (line.startsWith("f ")) { //arrived at next section of .obj file
					textureArray = new float[vertices.size() * 2];
					normalsArray = new float[vertices.size() * 3];
					break;
				}
			}
			
			//the next section in .obj file tells which vertices belong to a triangle
			while(line!=null) {
				if(!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				
				//put vertex data into correct order
				processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);
				
				line = reader.readLine(); //read next line
			}
			
			reader.close(); //close buffered reader when finished reading from file
			
		} catch(Exception e) {
			
			//invalid file format
			e.printStackTrace();
		}
		
		//convert vertex list into new float array and indices list into new int array in order to load them into a VAO
		verticesArray = new float[vertices.size() * 3];
		indicesArray = new int[indices.size()];
		
		int vertexPointer = 0;
		for (Vector3f vertex:vertices) {
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		
		for (int i = 0; i < indices.size(); i++) {
			indicesArray[i] = indices.get(i);
		}
		
		return loader.loadToVAO(verticesArray, textureArray, normalsArray, indicesArray); //loads data extracted from .obj file into a VAO returning a new RawModel
	}
	
	/**Puts vertex data into the correct position in the textureArray and normalsArray.<br>
	 * These values need to be ordered correctly because in the .obj file they are stored in random order.
	 * 
	 * @param vertexData
	 * @param indices
	 * @param textures
	 * @param normals
	 * @param textureArray
	 * @param normalsArray
	 */
	private static void processVertex(String[] vertexData, List<Integer> indices, 
			List<Vector2f> textures, List<Vector3f> normals, float[] textureArray,
			float[] normalsArray) {
		
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1; //index of vertex position in vertex positions list (-1 because .obj starts at 1 and array at 0)
		indices.add(currentVertexPointer); //store currentVertexPointer as index in indices
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
		textureArray[currentVertexPointer * 2] = currentTex.x; //put texture coords in same position as vertex index (multiplied by 2 since there are x and y values)
		textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y; //do "1 - " because OpenGL starts from top left of the texture whereas Blender starts from bottom left
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsArray[currentVertexPointer * 3] = currentNorm.x; //put normals in same position as vertex index (multiplied by 3 since there are x, y and z values)
		normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y; 
		normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
	}

}
