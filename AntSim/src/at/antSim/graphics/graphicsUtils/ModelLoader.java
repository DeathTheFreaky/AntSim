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
import java.util.LinkedList;

/**Loads models from obj Files and creates presets for higher abstraction TexturedModels, which can be used to build entities.
 * 
 * @author Clemens
 *
 */
public class ModelLoader {
	
	public static float massDummie = Globals.MASS_DUMMIE;
	
	public static HashMap<String, TexturedModel> texturedModels = new HashMap<>(); //holds all textured models used for entities
	private static LinkedList<ModelNamesAndTypes> modelPresets = new LinkedList<>(); //names of all obj files and associated textures to be loaded

	/**Loads all {@link TexturedModel}s needed for creating {@link GraphicsEntity}s into a HashMap.<br>
	 * Needs to be called before loadEntities.
	 * 
	 * @param loader
	 */
	public static void loadTexturedModels(OpenGLLoader loader) {
		
		//first String in array is name of obj file, second is name of texture, third is desired name of TexturedModel in Hashmap
		
		//environment
		modelPresets.add(new ModelNamesAndTypes("tree", "tree", "tree", PrimitiveType.CYLINDER, ObjectType.ENVIRONMENT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("grass", "grassTexture", "grass", PrimitiveType.SPHERE, ObjectType.ENVIRONMENT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("fern", "fern", "fern", PrimitiveType.SPHERE, ObjectType.ENVIRONMENT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("bush", "bushTexture", "bush", PrimitiveType.CYLINDER, ObjectType.ENVIRONMENT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("lamp", "lamp", "lamp", PrimitiveType.CYLINDER, ObjectType.ENVIRONMENT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("stomp", "stomp", "stomp", PrimitiveType.CYLINDER, ObjectType.ENVIRONMENT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("rock", "rockTexture", "rock", PrimitiveType.CUBOID, ObjectType.ENVIRONMENT, massDummie, false));
		
		//ghosts
		modelPresets.add(new ModelNamesAndTypes("sphere", "orange", "pheromone", PrimitiveType.SPHERE, ObjectType.PHEROMONE, massDummie, true));
		modelPresets.add(new ModelNamesAndTypes("sphere", "blue", "positionLocatorBlue", PrimitiveType.SPHERE, ObjectType.LOCATOR, massDummie, true));
		modelPresets.add(new ModelNamesAndTypes("sphere", "green", "positionLocatorGreen", PrimitiveType.SPHERE, ObjectType.LOCATOR, massDummie, true));
		modelPresets.add(new ModelNamesAndTypes("sphere", "red", "positionLocatorRed", PrimitiveType.SPHERE, ObjectType.LOCATOR, massDummie, true));
		
		//test
		modelPresets.add(new ModelNamesAndTypes("dragon", "dragon", "dragon", PrimitiveType.CUBOID, ObjectType.ENEMY, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("cube", "green", "greenCube", PrimitiveType.CUBOID, ObjectType.ENVIRONMENT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("cube", "red", "redCube", PrimitiveType.CUBOID, ObjectType.ENVIRONMENT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("cube", "blue", "blueCube", PrimitiveType.CUBOID, ObjectType.ENVIRONMENT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("sphere", "orange", "sphere", PrimitiveType.SPHERE, ObjectType.ENVIRONMENT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("cylinder", "orange", "cylinder", PrimitiveType.CYLINDER, ObjectType.ENVIRONMENT, massDummie, false));
		
		//food
		modelPresets.add(new ModelNamesAndTypes("apple", "appleTexture", "apple", PrimitiveType.SPHERE, ObjectType.FOOD, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("ant", "antDarkRedTexture", "deadAnt", PrimitiveType.CUBOID, ObjectType.FOOD, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("grasshopper", "grasshopperDeadTexture", "deadGrasshopper", PrimitiveType.CUBOID, ObjectType.FOOD, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("squirrel", "squirrelTexture", "squirrel", PrimitiveType.CUBOID, ObjectType.FOOD, massDummie, false));

		//ants
		modelPresets.add(new ModelNamesAndTypes("ant", "antDarkTexture", "worker", PrimitiveType.CUBOID, ObjectType.ANT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("ant", "antDarkTexture", "forager", PrimitiveType.CUBOID, ObjectType.ANT, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("ant", "antDarkTexture", "queen", PrimitiveType.CUBOID, ObjectType.ANT, massDummie, false));
		
		//enemies
		modelPresets.add(new ModelNamesAndTypes("ant", "antDarkRedTexture", "enemyAnt", PrimitiveType.CUBOID, ObjectType.ENEMY, massDummie, false));
		modelPresets.add(new ModelNamesAndTypes("grasshopper", "grasshopperTexture", "enemyGrasshopper", PrimitiveType.CUBOID, ObjectType.ENEMY, massDummie, false));
		
		//hive
		modelPresets.add(new ModelNamesAndTypes("anthill", "anthillTexture", "hive", PrimitiveType.SPHERE, ObjectType.HIVE, massDummie, false));
		
		for (ModelNamesAndTypes modelPreset : modelPresets) {
			ModelData modelData = OBJFileLoader.loadOBJ(modelPreset.objFileName);
			RawModel rawModel = loader.loadToVAO(modelData.getVertices(), modelData.getTextureCoords(), modelData.getNormals(), modelData.getIndices());
			rawModel.setFurthestPoint(modelData.getFurthestPoint());
			rawModel.setLenghts(modelData.getxLength(), modelData.getyLength(), modelData.getzLength());
			ModelTexture modelTexture = new ModelTexture(loader.loadTexture(modelPreset.textureFileName));
			texturedModels.put(modelPreset.key, new TexturedModel(rawModel, modelTexture, modelPreset.primitiveType, modelPreset.objectType, modelPreset.mass, modelPreset.useTransparency, modelPreset.key));
		}
		
		texturedModels.get("fern").getTexture().setNumberOfRows(2); //set number of rows inside texture atlas
		
		//set parameters for specular lighting for demo dragon
		texturedModels.get("dragon").getTexture().setShineDamper(10); //set shine damper for specular lighting
		texturedModels.get("dragon").getTexture().setReflectivity(1); //set reflectivity for specular lighting
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
		
		public ModelNamesAndTypes(String objFileName, String textureFileName, String key, PrimitiveType sphereType, ObjectType objectType, float mass, boolean useTransparency) {
			this.objFileName = objFileName;
			this.textureFileName = textureFileName;
			this.key = key;
			this.primitiveType = sphereType;
			this.objectType = objectType;
			this.mass = mass;
			this.useTransparency = useTransparency;
		}
	}
}
