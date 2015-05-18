package at.antSim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import at.antSim.graphics.entities.Entity;
import at.antSim.graphics.entities.Light;
import at.antSim.graphics.graphicsUtils.Loader;
import at.antSim.graphics.graphicsUtils.OBJFileLoader;
import at.antSim.graphics.models.ModelData;
import at.antSim.graphics.models.RawModel;
import at.antSim.graphics.models.TexturedModel;
import at.antSim.graphics.terrains.Terrain;
import at.antSim.graphics.textures.ModelTexture;
import at.antSim.graphics.textures.TerrainTexture;
import at.antSim.graphics.textures.TerrainTexturePack;

/**Provides methods to load the world's content.
 * 
 * @author Flo
 *
 */
public class WorldLoader {

	public static HashMap<String, Entity> specificEntities = new HashMap<>();
	public static HashMap<String, TexturedModel> texturedModels = new HashMap<>(); //holds all textured models used for entities
	private static LinkedList<String[]> modelTextureNames = new LinkedList<>(); //names of all obj files and associated textures to be loaded
	
	/**Loads all {@link TexturedModel}s needed for creating {@link Entity}s into a HashMap.<br>
	 * Needs to be called before loadEntities.
	 * 
	 * @param loader
	 */
	public static void loadTexturedModels(Loader loader) {
		
		//first String in array is name of obj file, second is name of texture, third is desired name of TexturedModel in Hashmap
		modelTextureNames.add(new String[] {"tree" , "tree", "tree"});
		modelTextureNames.add(new String[] {"grass" , "grass", "grass"});
		modelTextureNames.add(new String[] {"fern" , "fern", "fern"});
		modelTextureNames.add(new String[] {"lamp" , "lamp", "lamp"});
		modelTextureNames.add(new String[] {"dragon", "dragon", "dragon"});
		
		for (String[] params : modelTextureNames) {
			ModelData modelData = OBJFileLoader.loadOBJ(params[0]);
			RawModel rawModel = loader.loadToVAO(modelData.getVertices(), modelData.getTextureCoords(), modelData.getNormals(), modelData.getIndices());
			ModelTexture modelTexture = new ModelTexture(loader.loadTexture(params[1]));
			texturedModels.put(params[2], new TexturedModel(rawModel, modelTexture));
		}
		
		
		texturedModels.get("fern").getTexture().setNumberOfRows(2); //set number of rows inside texture atlas
		
		//set parameters for specular lighting for demo dragon
		texturedModels.get("dragon").getTexture().setShineDamper(10); //set shine damper for specular lighting
		texturedModels.get("dragon").getTexture().setReflectivity(1); //set reflectivity for specular lighting
		
		//set transparency and fake lighting for grass and fern (to avoid weird shadow look)
		texturedModels.get("grass").getTexture().setHasTransparency(true);
		texturedModels.get("grass").getTexture().setUseFakeLighting(true);
		texturedModels.get("fern").getTexture().setHasTransparency(true);
		texturedModels.get("fern").getTexture().setUseFakeLighting(true);
		
	}
	
	/**Loads the world's terrain.
	 * 
	 * @param loader
	 * @return - the world's {@link Terrain}
	 */
	public static Terrain loadTerrain(Loader loader) {
	
		//load the different terrain textures
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		//store different terrain textures in a TerrainTexturePack
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		
		//create a blend map texture
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		return new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");
	}

	/**Loads the world's initial contents.
	 * 
	 * @param loader
	 * @param terrain
	 * @return - a list of the World's {@link Entity}s
	 */
	public static ArrayList<Entity> loadEntities(Loader loader, Terrain terrain) {
		
		ArrayList<Entity> entities = new ArrayList<Entity>();
		
		//create a random flora
		Random random = new Random(676452);
		for (int i = 0; i < 1200; i++) {
			if (i % 20 == 0) {
				float x = random.nextFloat() * Globals.WORLD_SIZE;
				float z = random.nextFloat() * -Globals.WORLD_SIZE;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(texturedModels.get("fern"), random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 
						0, 0.9f));
			}
			if (i % 5 == 0) {
				float x = random.nextFloat() * Globals.WORLD_SIZE;
				float z = random.nextFloat() * -Globals.WORLD_SIZE;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(texturedModels.get("grass"), 1, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 
						0, random.nextFloat() * 0.1f + 0.6f));
				x = random.nextFloat() * Globals.WORLD_SIZE;
				z = random.nextFloat() * -Globals.WORLD_SIZE;
				y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(texturedModels.get("tree"), 1, new Vector3f(x, y, z), 0, 0,
						0, random.nextFloat() * 1 + 4));
			}
		} 
		
		//add some lamps
		entities.add(new Entity(texturedModels.get("lamp"), 1, new Vector3f(185, -4.7f, -293), 0, 0, 0, 1));
		entities.add(new Entity(texturedModels.get("lamp"), 1, new Vector3f(370, 4.2f, -300), 0, 0, 0, 1));
		
		//add cool stanford demo dragon for specular lighting demo
		Entity dragon = new Entity(texturedModels.get("dragon"), 1, new Vector3f(0, 0, 0), 0, 0, 0, 1);
		entities.add(dragon);
		specificEntities.put("dragon", dragon);
		
		return entities;
	}
	
	/**Loads the world's light sources.
	 * 
	 * @return - a list of the world's light sources
	 */
	public static ArrayList<Light> loadLights() {
		
		ArrayList<Light> lights = new ArrayList<Light>();
		
		lights.add(new Light(new Vector3f(0, 1000, -7000), new Vector3f(0.4f, 0.4f, 0.4f))); //sun
		lights.add(new Light(new Vector3f(185, 10, -293), new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f))); //lamp
		lights.add(new Light(new Vector3f(370, 17, -300), new Vector3f(0, 2, 2), new Vector3f(1, 0.01f, 0.002f))); //lamp
		
		return lights;
	}
	
	
}
