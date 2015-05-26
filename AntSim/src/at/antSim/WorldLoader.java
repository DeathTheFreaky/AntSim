package at.antSim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import com.bulletphysics.linearmath.Transform;

import at.antSim.GTPMapper.GTPCone;
import at.antSim.GTPMapper.GTPCuboid;
import at.antSim.GTPMapper.GTPCylinder;
import at.antSim.GTPMapper.GTPMapper;
import at.antSim.GTPMapper.GTPSphere;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.graphics.entities.Light;
import at.antSim.graphics.graphicsUtils.Loader;
import at.antSim.graphics.graphicsUtils.Maths;
import at.antSim.graphics.graphicsUtils.OBJFileLoader;
import at.antSim.graphics.models.ModelData;
import at.antSim.graphics.models.RawModel;
import at.antSim.graphics.models.TexturedModel;
import at.antSim.graphics.terrains.Terrain;
import at.antSim.graphics.textures.ModelTexture;
import at.antSim.graphics.textures.TerrainTexture;
import at.antSim.graphics.textures.TerrainTexturePack;
import at.antSim.objectsKI.Entity;
import at.antSim.objectsKI.EnvironmentObject;
import at.antSim.objectsKI.ObjectType;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.PhysicsFactorys.DynamicPhysicsObjectFactory;
import at.antSim.objectsPhysic.PhysicsFactorys.StaticPhysicsObjectFactory;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;

/**Provides methods to load the world's content.
 * 
 * @author Flo
 *
 */
public class WorldLoader {

	private static StaticPhysicsObjectFactory staticFactory = StaticPhysicsObjectFactory.getInstance();
	private static DynamicPhysicsObjectFactory dynamicFactory = DynamicPhysicsObjectFactory.getInstance();
	private static float massDummie = 0.1f;
	
	public static HashMap<String, TexturedModel> texturedModels = new HashMap<>(); //holds all textured models used for entities
	private static LinkedList<String[]> modelTextureNames = new LinkedList<>(); //names of all obj files and associated textures to be loaded
	
	/**Loads all {@link TexturedModel}s needed for creating {@link GraphicsEntity}s into a HashMap.<br>
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
			rawModel.setFurthestPoint(modelData.getFurthestPoint());
			rawModel.setLenghts(modelData.getxLength(), modelData.getyLength(), modelData.getzLength());
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
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("terrain/grassy2"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("terrain/grassFlowers"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("terrain/pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("terrain/mud"));
		
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
	 * @return - a list of the World's {@link GraphicsEntity}s
	 */
	public static void loadEntities(Loader loader, Terrain terrain) {
				
		//create a random flora
		Random random = new Random(676452);
		for (int i = 0; i < 1200; i++) {
			if (i % 20 == 0) {
				float x = random.nextFloat() * Globals.WORLD_SIZE;
				float z = random.nextFloat() * -Globals.WORLD_SIZE;
				float y = terrain.getHeightOfTerrain(x, z);
				GraphicsEntity fernGraphicsEntity = new GraphicsEntity(texturedModels.get("fern"), random.nextInt(4), 20f);
				GTPCylinder fernCylinder = GTPMapper.getCylinder(fernGraphicsEntity, fernGraphicsEntity.getScale());
				PhysicsObject fernPhysicsObject = staticFactory.createCylinder(massDummie, fernCylinder.getHeight(), fernCylinder.getRadius(), fernCylinder.getOrientation(), 
						new Transform(Maths.createTransformationMatrix(new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0)));
				new EnvironmentObject(fernGraphicsEntity, fernPhysicsObject);
			}
			if (i % 5 == 0) {
				float x = random.nextFloat() * Globals.WORLD_SIZE;
				float z = random.nextFloat() * -Globals.WORLD_SIZE;
				float y = terrain.getHeightOfTerrain(x, z);
				GraphicsEntity grassGraphicsEntity = new GraphicsEntity(texturedModels.get("grass"), 1, random.nextFloat() * 2f + 25f);
				GTPCuboid grassCuboid = GTPMapper.getCuboid(grassGraphicsEntity, grassGraphicsEntity.getScale());
				PhysicsObject grassPhysicsObject = staticFactory.createCuboid(massDummie, grassCuboid.getxLength(), grassCuboid.getyLength(), grassCuboid.getzLength(), 
						new Transform(Maths.createTransformationMatrix(new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0)));
				new EnvironmentObject(grassGraphicsEntity, grassPhysicsObject);
				x = random.nextFloat() * Globals.WORLD_SIZE;
				z = random.nextFloat() * -Globals.WORLD_SIZE;
				y = terrain.getHeightOfTerrain(x, z);
				GraphicsEntity treeGraphicsEntity = new GraphicsEntity(texturedModels.get("tree"), 1, random.nextFloat() * 5f + 20f);
				GTPCylinder treeCylinder = GTPMapper.getCylinder(treeGraphicsEntity, treeGraphicsEntity.getScale());
				PhysicsObject treePhysicsObject = staticFactory.createCylinder(massDummie, treeCylinder.getHeight(), treeCylinder.getRadius(), treeCylinder.getOrientation(), 
						new Transform(Maths.createTransformationMatrix(new Vector3f(x, y, z), 0, 0, 0)));
				new EnvironmentObject(treeGraphicsEntity, treePhysicsObject);
			}
		} 
		
		//add some lamps
		GraphicsEntity lamp1GraphicsEntity = new GraphicsEntity(texturedModels.get("lamp"), 1, 20);
		GraphicsEntity lamp2GraphicsEntity = new GraphicsEntity(texturedModels.get("lamp"), 1, 20);
		GTPCylinder lamp1Cylinder = GTPMapper.getCylinder(lamp1GraphicsEntity, lamp1GraphicsEntity.getScale());
		GTPCylinder lamp2Cylinder = GTPMapper.getCylinder(lamp2GraphicsEntity, lamp2GraphicsEntity.getScale());
		PhysicsObject lamp1PhysicsObject = staticFactory.createCylinder(massDummie, lamp1Cylinder.getHeight(), lamp1Cylinder.getRadius(), lamp1Cylinder.getOrientation(), 
				new Transform(Maths.createTransformationMatrix(new Vector3f(185, -4.7f, -293), 0, 0, 0)));
		PhysicsObject lamp2PhysicsObject = staticFactory.createCylinder(massDummie, lamp2Cylinder.getHeight(), lamp2Cylinder.getRadius(), lamp2Cylinder.getOrientation(), 
				new Transform(Maths.createTransformationMatrix(new Vector3f(370, 4.2f, -300), 0, 0, 0)));
		new EnvironmentObject(lamp1GraphicsEntity, lamp1PhysicsObject);
		new EnvironmentObject(lamp2GraphicsEntity, lamp2PhysicsObject);
		
		//add cool stanford demo dragon for specular lighting demo
		GraphicsEntity dragonGraphicsEntity = new GraphicsEntity(texturedModels.get("dragon"), 1, 25);
		GTPSphere dragonGtpSphere = GTPMapper.getSphere(dragonGraphicsEntity, dragonGraphicsEntity.getScale());
//		PhysicsObject dragonPhysicsObject = staticFactory.createSphere(massDummie, dragonGtpSphere.getRadious(), 
//				new Transform(Maths.createTransformationMatrix(new Vector3f(Globals.WORLD_SIZE/2, -7, -Globals.WORLD_SIZE/2), 90, 45, 90)));
		DynamicPhysicsObject dragonPhysicsObject = dynamicFactory.createSphere(massDummie, dragonGtpSphere.getRadious(), 
				new Transform(Maths.createTransformationMatrix(new Vector3f(Globals.WORLD_SIZE/2, -7, -Globals.WORLD_SIZE/2), 90, 45, 90)));
		dragonPhysicsObject.setAngularVelocity(new javax.vecmath.Vector3f(0, 5, 0)); //not working
		dragonPhysicsObject.getRigidBody().activate(); //does also not work
		new EnvironmentObject(dragonGraphicsEntity, dragonPhysicsObject);
		
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
