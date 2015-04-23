package at.antSim.graphics.graphicUtilsNew;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import at.antSim.AntSim;
import at.antSim.Globals;
import at.antSim.graphics.models.ModelData;

/**OBJFileLoader parses .obj files and stores the parsed values into {@link ModelData} objects.<br>
 * This OBJFileLoader can also handle texture themes.
 * 
 * THIS CLASS HAS BEEN COPIED FROM "3D GAME DEVELOPMENT SERIES": https://www.dropbox.com/sh/x1fyet1otejxk3z/AAAVdsje7VSnwrS93NT43K3ta/OBJFileLoader.java?dl=0<br>
 * TO ENSURE COMPREHENSION, EXTENSIVE COMMENTS HAVE BEEN ADDED.
 * 
 * @author Flo
 *
 */
public class OBJFileLoader {
	
	/**Takes in an .obj File, extracts all data from it (vertices, texture coordinates, normals, indices)
	 * and returns a {@link RawModel} built on this data.
	 * 
	 * @param fileName - the name of the .obj file
	 * @param loader - a {@link Loader} object for loading 3D models into memory
	 * @return - the {@link RawModel} created from the data in the .obj File
	 */
	public static ModelData loadOBJ(String objFileName) {
		
		//initialize filereader, bufferedreader and arraylists holding the data
        FileReader isr = null;
        File objFile = new File(Globals.RESOURCES + objFileName + ".obj");
        try {
            isr = new FileReader(objFile);
        } catch (FileNotFoundException e) {
            System.err.println("File not found in " + Globals.RESOURCES + "; don't use any extention");
        }
        BufferedReader reader = new BufferedReader(isr);
        String line;
        List<Vertex> vertices = new ArrayList<Vertex>(); //will contain: x1, y1, z1, x2, y2, z2, x3, y3, z3...
        List<Vector2f> textures = new ArrayList<Vector2f>(); //will contain: u1, v1, u2, v2, u3, v3...
        List<Vector3f> normals = new ArrayList<Vector3f>(); //will contain: x1, y1, z1, x2, y2, z2, x3, y3, z3...
        List<Integer> indices = new ArrayList<Integer>(); //will contain: vertex1index, texture1index, normal1index, vertex2index, texture2index, normal2index...
       
        try {
            while (true) {
                line = reader.readLine();
                
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
             * Hence, for each vertex of of model the information about the indices of its positional coords, texture coords and normals
             * (which line in each "paragraph" of data these are stored in) needs to be stored as well. 
             * 
             * Each of the lines starting with "f" represents a triangle.
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
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading the file");
        }
        removeUnusedVertices(vertices);
        float[] verticesArray = new float[vertices.size() * 3];
        float[] texturesArray = new float[vertices.size() * 2];
        float[] normalsArray = new float[vertices.size() * 3];
        float furthest = convertDataToArrays(vertices, textures, normals, verticesArray,
                texturesArray, normalsArray);
        int[] indicesArray = convertIndicesListToArray(indices);
        ModelData data = new ModelData(verticesArray, texturesArray, normalsArray, indicesArray,
                furthest);
        return data;
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
	private static void processVertex(String[] vertex, List<Vertex> vertices, List<Integer> indices) {
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
	
	/**Writes .obj parsed vertex, texture and normal data that is stored inside lists into floatArrays.
	 * 
	 * @param vertices - a list of vertexes parsed from an .obj File
	 * @param textures - a list of textures parsed from an .obj File
	 * @param normals - a list of normals parsed from an .obj File
	 * @param verticesArray - the float array which the data from the list of vertexes shall be written to
	 * @param texturesArray - the float array which the data from the list of textures shall be written to
	 * @param normalsArray - the float array which the data from the list of normals shall be written to
	 * @return - the furthestPoint of all vertexes
	 */
	private static float convertDataToArrays(List<Vertex> vertices, List<Vector2f> textures,
            List<Vector3f> normals, float[] verticesArray, float[] texturesArray,
            float[] normalsArray) {
        float furthestPoint = 0;
        for (int i = 0; i < vertices.size(); i++) {
            Vertex currentVertex = vertices.get(i);
            if (currentVertex.getLength() > furthestPoint) {
                furthestPoint = currentVertex.getLength();
            }
            Vector3f position = currentVertex.getPosition();
            Vector2f textureCoord = textures.get(currentVertex.getTextureIndex());
            Vector3f normalVector = normals.get(currentVertex.getNormalIndex());
            verticesArray[i * 3] = position.x;
            verticesArray[i * 3 + 1] = position.y;
            verticesArray[i * 3 + 2] = position.z;
            texturesArray[i * 2] = textureCoord.x;
            texturesArray[i * 2 + 1] = 1 - textureCoord.y;
            normalsArray[i * 3] = normalVector.x;
            normalsArray[i * 3 + 1] = normalVector.y;
            normalsArray[i * 3 + 2] = normalVector.z;
        }
        return furthestPoint;
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
