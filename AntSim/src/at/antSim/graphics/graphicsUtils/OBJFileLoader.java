package at.antSim.graphics.graphicsUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import at.antSim.Globals;
import at.antSim.graphics.models.ModelData;

/**OBJFileLoader parses .obj files and stores the parsed values into {@link ModelData} objects.<br>
 * This OBJFileLoader can also handle texture themes.
 * 
 * THIS CLASS HAS BEEN COPIED AND LATER MODIFIED FROM "3D GAME DEVELOPMENT SERIES": https://www.dropbox.com/sh/x1fyet1otejxk3z/AAAVdsje7VSnwrS93NT43K3ta/OBJFileLoader.java?dl=0<br>
 * TO ENSURE COMPREHENSION, EXTENSIVE COMMENTS HAVE BEEN ADDED.
 * 
 * @author Clemens
 *
 */
public class OBJFileLoader {
	
	static boolean debug = false;
		
	/**Takes in an .obj File, extracts all data from it (vertices, texture coordinates, normals, indices)
	 * and returns a {@link RawModel} built on this data.
	 * 
	 * @param fileName - the name of the .obj file
	 * @param loader - a {@link OpenGLLoader} object for loading 3D models into memory
	 * @return - the {@link RawModel} created from the data in the .obj File
	 */
	public static ModelData loadOBJ(String objFileName) {
		
//		if (objFileName.equals("sphere")) {
//			debug = true;
//		} else {
//			debug = false;
//		}
//		
		//initialize filereader, bufferedreader and arraylists holding the data
        FileReader isr = null;
        File objFile = new File(Globals.MODELS + objFileName + ".obj");
        try {
            isr = new FileReader(objFile);
        } catch (FileNotFoundException e) {
            System.err.println("File " + objFile + " not found in " + Globals.RESOURCES + ".");
        }
        BufferedReader reader = new BufferedReader(isr);
        String line;
        List<Vertex> vertices = new ArrayList<Vertex>(); //will contain: x1, y1, z1, x2, y2, z2, x3, y3, z3...
        List<Vector2f> textures = new ArrayList<Vector2f>(); //will contain: u1, v1, u2, v2, u3, v3...
        List<Vector3f> normals = new ArrayList<Vector3f>(); //will contain: x1, y1, z1, x2, y2, z2, x3, y3, z3...
        List<Integer> indices = new ArrayList<Integer>(); //will contain: vertex1index, texture1index, normal1index, vertex2index, texture2index, normal2index...
       
        try {
        	int linectr = 0;
            while (true) {
                line = reader.readLine();
                
                if (debug) System.out.println("linectr: " + linectr);
                
                linectr++;
                
                //parse a model's vertices' x,y,z coordinates as floats, create a Vertex Object with these coordinates, add this Vertex Object to list of vertices
                if (line.startsWith("v ")) {
                    String[] currentLine = line.split(" ");
                    Vector3f vertex = new Vector3f((float) Float.valueOf(currentLine[1]),
                            (float) Float.valueOf(currentLine[2]),
                            (float) Float.valueOf(currentLine[3]));
                    Vertex newVertex = new Vertex(vertices.size(), vertex);
                    vertices.add(newVertex);
 
                } 
                
                //parse a model's u,v texture coordinates as floats, create a 2-dim Vector with these coordinates, add this 2-dim Vector to list of textures
                else if (line.startsWith("vt ")) {
                    String[] currentLine = line.split(" ");
                    Vector2f texture = new Vector2f((float) Float.valueOf(currentLine[1]),
                            (float) Float.valueOf(currentLine[2]));
                    textures.add(texture);
                } 
                
                //parse a model's x,y,z normal coordinates as floats, create a 3-dim Vector with these coordinates, add this 3-dim Vector to list of vectors
                else if (line.startsWith("vn ")) {
                    String[] currentLine = line.split(" ");
                    Vector3f normal = new Vector3f((float) Float.valueOf(currentLine[1]),
                            (float) Float.valueOf(currentLine[2]),
                            (float) Float.valueOf(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) {
                    break;
                }
            }
            
            /* In .obj Files, a vertices positional coords, texture coords and normals are not stored with the same indices.
             * Hence, for each vertex of a model the information about the indices of its positional coords, texture coords and normals
             * (which line in each "paragraph" of data these are stored in) needs to be stored as well. 
             * 
             * Each of the lines starting with "f" represents a triangle.
             * It contains a triangle's three vertices, and each of the vertices contains data in the following format: positions/texture coordinates/normals.
             * The indixes stored in positions/texture coordinates/normals define the absolute position of the order in which the specific data has been defined.
             * So if a texture coordinate has an index of 2, the index is referring to the 2nd entry of texture coordinates.
             * 
             * Altogether, this way of storing helps to use up less space by avoiding to store duplicate coordinate values...
             * 
             * For example:
             * 
             * A raw data line of "f 3/1/1 4/2/2 1/3/3", representing one triangle, means that:
             * 
             * 	Vertex1's positional coordinates are stored at index 3 (third line) inside the positional coordinates paragraph
             *  Vertex1's texture coordinates are stored at index 1 (first line) inside the texture coordinates paragraph
             *  Vertex1's normal coordinates are stored at index 1 (first line) inside the normal coordinates paragraph
             *  
             *  Vertex2's positional coordinates are stored at index 4 inside the positional coordinates paragraph
             *  Vertex2's texture coordinates are stored at index 2 inside the texture coordinates paragraph
             *  Vertex2's normal coordinates are stored at index 2 inside the normal coordinates paragraph
             *  
             *  Vertex3's positional coordinates are stored at index 1 inside the positional coordinates paragraph
             *  Vertex3's texture coordinates are stored at index 3 inside the texture coordinates paragraph
             *  Vertex3's normal coordinates are stored at index 3 inside the normal coordinates paragraph
             *  
             *  For more information on the obj file format see: http://www.scratchapixel.com/old/lessons/3d-advanced-lessons/obj-file-format/obj-file-format/
             * */
            
            while (line != null && line.startsWith("f ")) {
                String[] currentLine = line.split(" "); 
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");
                processVertex(vertex1, vertices, indices);
                processVertex(vertex2, vertices, indices);
                processVertex(vertex3, vertices, indices);
                line = reader.readLine();
                linectr++;
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading the file");
        }
        removeUnusedVertices(vertices);
        float[] verticesArray = new float[vertices.size() * 3];
        float[] texturesArray = new float[vertices.size() * 2];
        float[] normalsArray = new float[vertices.size() * 3];
        OBJLoaderGeometryData geometryData = convertDataToArrays(vertices, textures, normals, verticesArray,
                texturesArray, normalsArray);
        int[] indicesArray = convertIndicesListToArray(indices);
        ModelData data = new ModelData(verticesArray, texturesArray, normalsArray, indicesArray, geometryData);    
        
        return data;
    }
	
	/**Sets the indices for a {@link Vertex} objects vertex positional coordinates, texture coordinates and normals indices.<br>
	 * These can later be used to retrieve the respective data at the specified indices from lists and store it in according to the order of vertex indices.<br>
	 * The actual process of storing the data at the right positions is handled in the convertDataToArrays() function.<br>
	 * 
	 * @param vertexData
	 * @param indices
	 * @param textures
	 * @param normals
	 * @param textureArray
	 * @param normalsArray
	 */
	private static void processVertex(String[] vertex, List<Vertex> vertices, List<Integer> indices) {
		/* in obj indices start with 1, we store our indices starting with 0, so we substract 1 from all indices */
        int index = Integer.parseInt(vertex[0]) - 1; 
        Vertex currentVertex = vertices.get(index);
        int textureIndex = Integer.parseInt(vertex[1]) - 1;
        int normalIndex = Integer.parseInt(vertex[2]) - 1;
        if (!currentVertex.isSet()) {
            currentVertex.setTextureIndex(textureIndex);
            currentVertex.setNormalIndex(normalIndex);
            indices.add(index);
        } else {
            dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex, indices,
                    vertices);
        }
    }
	
	/**Converts a list of indices to an integer array keeping the original order of elements.
	 * 
	 * @param indices - a List of indices stored as integers
	 * @return - an integer array of indices
	 */
	private static int[] convertIndicesListToArray(List<Integer> indices) {
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indicesArray.length; i++) {
            indicesArray[i] = indices.get(i);
        }
        return indicesArray;
    }
	
	/**Puts vertex positional coordinates, texture coordinates and normals in float arrays, ensuring that the order of these matches the order of associated vertices.<br>
	 * These values need to be ordered correctly because in the .obj file they are stored in random order.<br>
	 * <br>
	 * Eg: An obj File defines the following indices for a vertex: 5/4/3<br>
	 * The first number represents the vertex positional index, the second number the texture coordinate index and the third number the normal index of a vertex.<br>
	 * The function now has to retrieve the respective positional coordinates, texture coordinates and normals at the absolute positions specified in the obj File
	 * and store them in the same order as the order of vertex indices.<br>
	 * <br>
	 * So if our current vertex has an a vertex index of 3, the vertex's positional coordinates will be stored inside an array of floats in consecutive order,
	 * starting with an offset of 6 (because vertex 1 and 2 took up 3 places each -> x,y,z).<br>
	 * <br>
	 * After finishing this operation, the vertex positional coordinate float array will contain:<br>
	 * vertex1.x, vertex1.y, vertex1.z, vertex2.x, vertex2.y, vertex2.z, vertex3.x, vertex3.y, vertex3.z<br>
	 * <br>
	 * The model will be normalized to allow for precise sizing with scale transform.
	 * 
	 * @param vertices - a list of vertexes parsed from an .obj File
	 * @param textures - a list of textures parsed from an .obj File
	 * @param normals - a list of normals parsed from an .obj File
	 * @param verticesArray - the float array which the data from the list of vertexes shall be written to
	 * @param texturesArray - the float array which the data from the list of textures shall be written to
	 * @param normalsArray - the float array which the data from the list of normals shall be written to
	 * @return - {@link OBJLoaderGeometryData}
	 */
	private static OBJLoaderGeometryData convertDataToArrays(List<Vertex> vertices, List<Vector2f> textures,
            List<Vector3f> normals, float[] verticesArray, float[] texturesArray, float[] normalsArray) {
		
		float normalizeMultiplier = findNormalizeModelDivider(vertices, normals);
		
        float furthestPoint = 0;
        float xLength;
        float yLength;
        float zLength;
        
        //find max extents for each axis to calculate lengths in arbitrary origins from obj model -> it is possible that y values stretch from 0.3 to 2.3 instead of -1 to +1
        float maxNegX = 0;
        float maxPosX = 0;
        float maxNegY = 0;
        float maxPosY = 0;
        float maxNegZ = 0;
        float maxPosZ = 0;
        
        for (int i = 0; i < vertices.size(); i++) {
        	
            Vertex currentVertex = vertices.get(i);
            
            Vector3f position = (Vector3f) currentVertex.getPosition();
            Vector2f textureCoord = (Vector2f) textures.get(currentVertex.getTextureIndex());
            Vector3f normalVector = (Vector3f) normals.get(currentVertex.getNormalIndex());
            
            float x = position.x * normalizeMultiplier, y = position.y * normalizeMultiplier, z = position.z * normalizeMultiplier;
            
            if (x > maxPosX) maxPosX = x;
            else if (x < maxNegX) maxNegX = x;
            if (y > maxPosY) maxPosY = y;
            else if (y < maxNegY) maxNegY = y;
            if (z > maxPosZ) maxPosZ = z;
            else if (z < maxNegZ) maxNegZ = z;
            
        }
        
        xLength = maxPosX - maxNegX;
        yLength = maxPosY - maxNegY;
        zLength = maxPosZ - maxNegZ;
        
        //set origin to 0,0
        for (int i = 0; i < vertices.size(); i++) {
        	
            Vertex currentVertex = vertices.get(i);
            
            Vector3f position = (Vector3f) currentVertex.getPosition();
            Vector2f textureCoord = (Vector2f) textures.get(currentVertex.getTextureIndex());
            Vector3f normalVector = (Vector3f) normals.get(currentVertex.getNormalIndex());
                                    
            float x = position.x * normalizeMultiplier - (maxPosX - xLength/2), y = position.y * normalizeMultiplier  - (maxPosY - yLength/2), z = position.z * normalizeMultiplier  - (maxPosZ - zLength/2);
            
            Vector3f furthestPointCalculator = new Vector3f(x, y, z);
            if (furthestPointCalculator.length() > furthestPoint) {
                furthestPoint = furthestPointCalculator.length();
            }
            
            verticesArray[i * 3] = x;
            verticesArray[i * 3 + 1] = y;
            verticesArray[i * 3 + 2] = z;
            texturesArray[i * 2] = textureCoord.x;
            texturesArray[i * 2 + 1] = 1 - textureCoord.y;
            normalsArray[i * 3] = normalVector.x;
            normalsArray[i * 3 + 1] = normalVector.y;
            normalsArray[i * 3 + 2] = normalVector.z;
        }
        
        if (debug) {
        	System.out.println("maxPosX: " + maxPosX);
        	System.out.println("minPosX: " + maxNegX);
        	System.out.println("maxPosY: " + maxPosY);
        	System.out.println("minPosY: " + maxNegY);
        	System.out.println("maxPosZ: " + maxPosZ);
        	System.out.println("minPosZ: " + maxNegZ);
        	System.out.println("xLength: " + xLength);
        	System.out.println("yLength: " + yLength);
        	System.out.println("zLength: " + zLength);
        	System.out.println("furthestPoint: " + furthestPoint);
        }
        
        return new OBJLoaderGeometryData(furthestPoint, xLength, yLength, zLength);
    }
	
	/**Finds multiplier to normalize a model used for dividing all its positional data with the length of the axis with the biggest extent.
	 * 
	 * @param vertices
	 * @param normals
	 */
	private static float findNormalizeModelDivider(List<Vertex> vertices, List<Vector3f> normals) {
		
		float xLength;
        float yLength;
        float zLength;
        
        //find max extents for each axis to calculate lengths
        float maxNegX = 0;
        float maxPosX = 0;
        float maxNegY = 0;
        float maxPosY = 0;
        float maxNegZ = 0;
        float maxPosZ = 0;
        
        for (int i = 0; i < vertices.size(); i++) {
        	
            Vertex currentVertex = vertices.get(i);
            
            Vector3f position = currentVertex.getPosition();
            
            if (position.x > maxPosX) maxPosX = position.x;
            else if (position.x < maxNegX) maxNegX = position.x;
            if (position.y > maxPosY) maxPosY = position.y;
            else if (position.y < maxNegY) maxNegY = position.y;
            if (position.z > maxPosZ) maxPosZ = position.z;
            else if (position.z < maxNegZ) maxNegZ = position.z;
        }
        
        xLength = maxPosX - maxNegX;
        yLength = maxPosY - maxNegY;
        zLength = maxPosZ - maxNegZ;
        
        return 1/java.lang.Math.max(xLength, java.lang.Math.max(yLength, zLength)) * 2;
	}

	/**Deals with vertices that have already been indexed (by their positional data in vertices array).<br>
	 * 
	 * If the two vertices match in texture index and normal index, simply adds the vertices index to the list of indices.<br>
	 * Else loop through the chain of duplicate vertices, referencing each other via the duplicateVertx variable in {@link Vertex}.<br>
	 * Once the end of this chain is reached, create the new {@link Vertex} object, setting its duplicateVertex variable to the {@link Vertex}
	 * object which has previously been the last element in the chain of vertices with same positional data.
	 * 
	 * @param previousVertex - a Vertex which has already been indexed by its positional data in a list of vertices
	 * @param newTextureIndex - texture index to be stored in the list of textures for a vertex not matching in texture and normals with the vertices being compared to
	 * @param newNormalIndex - normal index to be stored in the list of normals for a vertex not matching in texture and normals with the vertices being compared to
	 * @param indices - a list of indices of a repeating series of vertices, texture coords, normals
	 * @param vertices - a list of {@link Vertex} objects
	 */
	private static void dealWithAlreadyProcessedVertex(Vertex previousVertex, int newTextureIndex,
            int newNormalIndex, List<Integer> indices, List<Vertex> vertices) {
        if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
            indices.add(previousVertex.getIndex());
        } else {
            Vertex anotherVertex = previousVertex.getDuplicateVertex();
            if (anotherVertex != null) {
                dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex,
                        indices, vertices);
            } else {
                Vertex duplicateVertex = new Vertex(vertices.size(), previousVertex.getPosition());
                duplicateVertex.setTextureIndex(newTextureIndex);
                duplicateVertex.setNormalIndex(newNormalIndex);
                previousVertex.setDuplicateVertex(duplicateVertex);
                vertices.add(duplicateVertex);
                indices.add(duplicateVertex.getIndex());
            }
 
        }
    }
     
    /**Removes all vertices whose texture index and normal index have not been set.
     * 
     * @param vertices - a List containing 
     */
    private static void removeUnusedVertices(List<Vertex> vertices){
        for(Vertex vertex:vertices){
            if(!vertex.isSet()){
                vertex.setTextureIndex(0);
                vertex.setNormalIndex(0);
            }
        }
    }
}
