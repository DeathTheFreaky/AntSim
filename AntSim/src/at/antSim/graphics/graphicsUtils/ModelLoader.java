package at.antSim.graphics.graphicsUtils;

import at.antSim.Globals;
import at.antSim.GTPMapper.PrimitiveType;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.graphics.models.ModelData;
import at.antSim.graphics.models.RawModel;
import at.antSim.graphics.models.TexturedModel;
import at.antSim.graphics.textures.ModelTexture;
import at.antSim.objectsKI.Entity;
import at.antSim.objectsKI.ObjectType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

/**Loads models from obj Files and creates presets for higher abstraction TexturedModels, which can be used to build entities.
 * 
 * @author Clemens
 *
 */
public class ModelLoader {
	
	public static float massDummie = Globals.MASS_DUMMIE;
	
	public static HashMap<String, TexturedModel> texturedModels = new HashMap<>(); //holds all textured models used for entities
	private static HashMap<String, HashMap<Integer, RawModel>> rawModels = new HashMap<>(); //holds all textured models used for entities
	private static HashMap<String, HashMap<Integer, ModelTexture>> modelTextures = new HashMap<>(); //holds all textured models used for entities
	private static LinkedList<ModelNamesAndTypes> modelPresets = new LinkedList<>(); //names of all obj files and associated textures to be loaded

	/**Loads all {@link TexturedModel}s needed for creating {@link GraphicsEntity}s into a HashMap.<br>
	 * Needs to be called before loadEntities.
	 * 
	 * @param loader
	 */
	public static void loadTexturedModels(OpenGLLoader loader) {
		
		//first String in array is name of obj file, second is name of texture, third is desired name of TexturedModel in Hashmap
		
		//environment
		modelPresets.add(new ModelNamesAndTypes("grass", "grassTexture", "grass", PrimitiveType.SPHERE, ObjectType.ENVIRONMENT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("fern", "fern", "fern", PrimitiveType.SPHERE, ObjectType.ENVIRONMENT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("bush", "bushTexture", "bush", PrimitiveType.CYLINDER, ObjectType.ENVIRONMENT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("lamp", "lamp", "lamp", PrimitiveType.CYLINDER, ObjectType.ENVIRONMENT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("stomp", "stomp", "stomp", PrimitiveType.CYLINDER, ObjectType.ENVIRONMENT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("rock", "rockTexture", "rock", PrimitiveType.CUBOID, ObjectType.ENVIRONMENT, massDummie, false));
		
		//ghosts
		modelPresets.add(new ModelNamesAndTypes("sphere1", "orange", "pheromone", PrimitiveType.SPHERE, ObjectType.PHEROMONE, massDummie, true));
		modelPresets.add(new ModelNamesAndTypes("sphere1", "blue", "positionLocatorBlue", PrimitiveType.SPHERE, ObjectType.LOCATOR, massDummie, true));
		modelPresets.add(new ModelNamesAndTypes("sphere1", "green", "positionLocatorGreen", PrimitiveType.SPHERE, ObjectType.LOCATOR, massDummie, true));
		modelPresets.add(new ModelNamesAndTypes("sphere1", "red", "positionLocatorRed", PrimitiveType.SPHERE, ObjectType.LOCATOR, massDummie, true));
		
		//test
		modelPresets.add(new ModelNamesAndTypes("cube", "green", "greenCube", PrimitiveType.CUBOID, ObjectType.ENVIRONMENT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("cube", "red", "redCube", PrimitiveType.CUBOID, ObjectType.ENVIRONMENT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("cube", "blue", "blueCube", PrimitiveType.CUBOID, ObjectType.ENVIRONMENT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("sphere", "orange", "sphere", PrimitiveType.SPHERE, ObjectType.ENVIRONMENT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("cylinder", "orange", "cylinder", PrimitiveType.CYLINDER, ObjectType.ENVIRONMENT, massDummie, false));
		
		//food
		modelPresets.add(new ModelNamesAndTypes("apple", "appleTexture", "apple", PrimitiveType.SPHERE, ObjectType.FOOD, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("ant", "antDarkRedTexture", "deadAnt", PrimitiveType.CUBOID, ObjectType.FOOD, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("grasshopper", "grasshopperDeadTexture", "deadGrasshopper", PrimitiveType.CUBOID, ObjectType.FOOD, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("squirrel", "squirrelTexture", "squirrel", PrimitiveType.CYLINDER, ObjectType.FOOD, massDummie, false));

		//ants
		modelPresets.add(new ModelNamesAndTypes("ant", "antDarkTexture", "worker", PrimitiveType.CUBOID, ObjectType.ANT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("ant", "antDarkTexture", "forager", PrimitiveType.CUBOID, ObjectType.ANT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("ant", "antDarkTexture", "queen", PrimitiveType.CUBOID, ObjectType.ANT, massDummie, false));
		
		//enemies
		modelPresets.add(new ModelNamesAndTypes("ant", "antDarkRedTexture", "enemyAnt", PrimitiveType.CUBOID, ObjectType.ENEMY, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("grasshopper", "grasshopperTexture", "enemyGrasshopper", PrimitiveType.CUBOID, ObjectType.ENEMY, massDummie, false));
		
		//hive
		modelPresets.add(new ModelNamesAndTypes("anthill", "anthillTexture", "hive", PrimitiveType.SPHERE, ObjectType.HIVE, massDummie, false));
		
		//lod test objects
		modelPresets.add(new ModelNamesAndTypes("dragon_hq", "dragon", "dragon", 0, PrimitiveType.CUBOID, ObjectType.ENVIRONMENT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("dragon_mq", "dragon", "dragon", 1, PrimitiveType.CUBOID, ObjectType.ENVIRONMENT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("dragon_lq", "dragon", "dragon", 2, PrimitiveType.CUBOID, ObjectType.ENVIRONMENT, massDummie, false));
		//modelPresets.add(new ModelNamesAndTypes("tree_new", "tree", "tree", 0, PrimitiveType.CUBOID, ObjectType.ENVIRONMENT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("tree_hq", "orange", "tree", 0, PrimitiveType.CUBOID, ObjectType.ENVIRONMENT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("tree_mq", "orange", "tree", 1, PrimitiveType.CUBOID, ObjectType.ENVIRONMENT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("tree_lq", "orange", "tree", 2, PrimitiveType.CUBOID, ObjectType.ENVIRONMENT, massDummie, false));
		
		for (ModelNamesAndTypes modelPreset : modelPresets) {
			ModelData modelData = OBJFileLoader.loadOBJ(modelPreset.objFileName);
			RawModel rawModel = loader.loadToVAO(modelData.getVertices(), modelData.getTextureCoords(), modelData.getNormals(), modelData.getIndices());
			rawModel.setFurthestPoint(modelData.getFurthestPoint());
			rawModel.setLenghts(modelData.getxLength(), modelData.getyLength(), modelData.getzLength());
			ModelTexture modelTexture = new ModelTexture(loader.loadTexture(modelPreset.textureFileName));
			
			if (modelPreset.useTransparency)
			{
				rawModel.loadTransparentVertices(modelData);
			}
			
			if (!texturedModels.containsKey(modelPreset.key))
			{
				texturedModels.put(modelPreset.key, new TexturedModel(modelPreset.primitiveType, modelPreset.objectType, modelPreset.mass, modelPreset.useTransparency, modelPreset.key));
			}
			
			addRawModelAndModelTexture(modelPreset.key, modelPreset.lodLevel, rawModel, modelTexture);
		}
		
		texturedModels.get("fern").getTexture().setNumberOfRows(2); //set number of rows inside texture atlas
		
		//set parameters for specular lighting for demo dragon
		modelTextures.get("dragon").get(0).setShineDamper(10);
		modelTextures.get("dragon").get(0).setReflectivity(1);
		modelTextures.get("dragon").get(1).setShineDamper(10);
		modelTextures.get("dragon").get(1).setReflectivity(1);
		modelTextures.get("dragon").get(2).setShineDamper(10);
		modelTextures.get("dragon").get(2).setReflectivity(1);
		texturedModels.get("forager").getTexture().setShineDamper(10); //set shine damper for specular lighting
		texturedModels.get("forager").getTexture().setReflectivity(1); //set reflectivity for specular lighting
		texturedModels.get("enemyAnt").getTexture().setShineDamper(10); //set shine damper for specular lighting
		texturedModels.get("enemyAnt").getTexture().setReflectivity(1); //set reflectivity for specular lighting
		texturedModels.get("enemyGrasshopper").getTexture().setShineDamper(10); //set shine damper for specular lighting
		texturedModels.get("enemyGrasshopper").getTexture().setReflectivity(1); //set reflectivity for specular lighting
		
		//set transparency and fake lighting for grass and fern (to avoid weird shadow look)
		texturedModels.get("grass").getTexture().setHasTransparency(true);
		texturedModels.get("grass").getTexture().setUseFakeLighting(true);
		texturedModels.get("fern").getTexture().setHasTransparency(true);
		texturedModels.get("fern").getTexture().setUseFakeLighting(true);
	}
	
	private static void addRawModelAndModelTexture(String key, int lodLevel, RawModel rawModel, ModelTexture modelTexture)
	{
		if (!rawModels.containsKey(key))
		{
			HashMap<Integer, RawModel> lodMap = new HashMap<>();
			
			rawModels.put(key, lodMap);
		}
		
		if (!modelTextures.containsKey(key))
		{
			HashMap<Integer, ModelTexture> lodMap = new HashMap<>();
			
			modelTextures.put(key, lodMap);
		}
		
		rawModels.get(key).put(lodLevel, rawModel);
		modelTextures.get(key).put(lodLevel, modelTexture);
		
		if (lodLevel > 0)
		{
			if (texturedModels.containsKey(key) && !texturedModels.get(key).usesLod())
			{
				texturedModels.get(key).setUsesLod(true);
			}
		}
	}
	
	/**If no distance is passed, always return high poly mesh.
	 * 
	 * @param key
	 * @return
	 */
	public static RawModel getRawModel(String key)
	{
		return rawModels.get(key).get(0);
	}
	
	/**Return appropriate lod Raw Model, determined by distance to the camera.
	 * 
	 * @param key
	 * @param cameraDist
	 * @return
	 */
	public static RawModel getRawModel(String key, float cameraDist)
	{
		if (cameraDist < Globals.mqDist)
		{
			return rawModels.get(key).get(0);
		}
		else if (cameraDist < Globals.lqDist)
		{
			return rawModels.get(key).get(1);
		}
		else
		{
			return rawModels.get(key).get(2);
		}
	}
	
	/**If no distance is passed, always return high poly mesh.
	 * 
	 * @param key
	 * @return
	 */
	public static ModelTexture getModelTexture(String key)
	{
		return modelTextures.get(key).get(0);
	}
	
	/**Return appropriate lod Raw Model, determined by distance to the camera.
	 * 
	 * @param key
	 * @param cameraDist
	 * @return
	 */
	public static ModelTexture getModelTexture(String key, float cameraDist)
	{
		if (cameraDist < Globals.mqDist)
		{
			return modelTextures.get(key).get(0);
		}
		else if (cameraDist < Globals.lqDist)
		{
			return modelTextures.get(key).get(1);
		}
		else
		{
			return modelTextures.get(key).get(2);
		}
	}
	
	/**Stores "presets" for an {@link Entity}'s model.
	 * 
	 * @author Flo
	 *
	 */
	static class ModelNamesAndTypes {
		
		String objFileName;
		String textureFileName;
		String key;
		PrimitiveType primitiveType;
		ObjectType objectType;
		float mass;
		boolean useTransparency;
		int lodLevel;
		
		public ModelNamesAndTypes(String objFileName, String textureFileName, String key, int lodLevel, PrimitiveType sphereType, ObjectType objectType, float mass, boolean useTransparency) {
			this.objFileName = objFileName;
			this.textureFileName = textureFileName;
			this.key = key;
			this.lodLevel = lodLevel;
			this.primitiveType = sphereType;
			this.objectType = objectType;
			this.mass = mass;
			this.useTransparency = useTransparency;
		}
		
		public ModelNamesAndTypes(String objFileName, String textureFileName, String key, PrimitiveType sphereType, ObjectType objectType, float mass, boolean useTransparency) {
			this.objFileName = objFileName;
			this.textureFileName = textureFileName;
			this.key = key;
			this.lodLevel = 0;
			this.primitiveType = sphereType;
			this.objectType = objectType;
			this.mass = mass;
			this.useTransparency = useTransparency;
		}
	}
}
