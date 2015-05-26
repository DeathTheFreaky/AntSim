package at.antSim;

import at.antSim.GTPMapper.GTPCuboid;
import at.antSim.GTPMapper.GTPCylinder;
import at.antSim.GTPMapper.GTPMapper;
import at.antSim.GTPMapper.GTPObject;
import at.antSim.GTPMapper.GTPSphere;
import at.antSim.GTPMapper.PrimitiveType;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.graphics.entities.Light;
import at.antSim.graphics.graphicsUtils.ModelLoader;
import at.antSim.graphics.graphicsUtils.OpenGLLoader;
import at.antSim.graphics.graphicsUtils.Maths;
import at.antSim.graphics.graphicsUtils.OBJFileLoader;
import at.antSim.graphics.models.ModelData;
import at.antSim.graphics.models.RawModel;
import at.antSim.graphics.models.TexturedModel;
import at.antSim.graphics.terrains.Terrain;
import at.antSim.graphics.textures.ModelTexture;
import at.antSim.graphics.textures.TerrainTexture;
import at.antSim.graphics.textures.TerrainTexturePack;
import at.antSim.objectsKI.Ant;
import at.antSim.objectsKI.Enemy;
import at.antSim.objectsKI.Entity;
import at.antSim.objectsKI.EnvironmentObject;
import at.antSim.objectsKI.Food;
import at.antSim.objectsKI.ObjectType;
import at.antSim.objectsKI.Pheronome;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.PhysicsFactorys.AbstractPhysicsObjectFactory;
import at.antSim.objectsPhysic.PhysicsFactorys.DynamicPhysicsObjectFactory;
import at.antSim.objectsPhysic.PhysicsFactorys.PhysicsFactoryType;
import at.antSim.objectsPhysic.PhysicsFactorys.PhysicsObjectFactory;
import at.antSim.objectsPhysic.PhysicsFactorys.StaticPhysicsObjectFactory;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import com.bulletphysics.linearmath.Transform;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

/**Provides methods to load the world's content.
 * 
 * @author Flo
 *
 */
public class WorldLoader {
	
	private static StaticPhysicsObjectFactory staticFactory = StaticPhysicsObjectFactory.getInstance();
	private static DynamicPhysicsObjectFactory dynamicFactory = DynamicPhysicsObjectFactory.getInstance();
	
	/**Loads the world's terrain.
	 * 
	 * @param loader
	 * @return - the world's {@link Terrain}
	 */
	public static Terrain loadTerrain(OpenGLLoader loader) {
	
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
	public static void loadWorldEntities(OpenGLLoader loader, Terrain terrain) {
				
		//create a random flora
		Random random = new Random(676452);
		for (int i = 0; i < 1200; i++) {
			if (i % 20 == 0) {
				float x = random.nextFloat() * Globals.WORLD_SIZE;
				float z = random.nextFloat() * -Globals.WORLD_SIZE;
				float y = terrain.getHeightOfTerrain(x, z);
				createEntity("fern", PhysicsFactoryType.STATIC, random.nextInt(4), 20f, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0);
			}
			if (i % 5 == 0) {
				float x = random.nextFloat() * Globals.WORLD_SIZE;
				float z = random.nextFloat() * -Globals.WORLD_SIZE;
				float y = terrain.getHeightOfTerrain(x, z);
				createEntity("grass", PhysicsFactoryType.STATIC, 1, random.nextFloat() * 2f + 25f, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0);
				x = random.nextFloat() * Globals.WORLD_SIZE;
				z = random.nextFloat() * -Globals.WORLD_SIZE;
				y = terrain.getHeightOfTerrain(x, z);
				createEntity("tree", PhysicsFactoryType.STATIC, 1, random.nextFloat() * 5f + 20f, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0);
			}
		} 
		
		//add some lamps
		Entity lamp1 = createEntity("lamp", PhysicsFactoryType.STATIC, 1, 20, new Vector3f(185, -4.7f, -293), 0, 0, 0);
		Entity lamp2 = createEntity("lamp", PhysicsFactoryType.STATIC, 1, 20, new Vector3f(370, 4.2f, -300), 0, 0, 0);
		
		//add cool stanford demo dragon for specular lighting demo
		Entity dragon = createEntity("dragon", PhysicsFactoryType.DYNAMIC, 1, 25, new Vector3f(Globals.WORLD_SIZE/2, -7, -Globals.WORLD_SIZE/2), 90, 45, 90);
	}
	
	/**Creates an {@link Entity} which is not initially part of the world.<br>
	 * Linear and angular velocity for dynamic {@link Entity}s can be set afterwards for the returned {@link Entity}.
	 * 
	 * @param modelId - a unique String identifier for the model to be used which holds the texture and geometry data for the created {@link Entity}
	 * @param factoryType - dynamic or static
	 * @param objectType - the {@link Entity}'s {@link ObjectType}
	 * @param primitiveType - the {@link Entity}'s {@link SphereType}
	 * @param textureIndex - in case there are multiple textures in a texture atlas, specify which one to use
	 * @param scale - scale determining the size of the Entity in [units]
	 * @param position - initial position of the new created Entity
	 * @param rx - initial rotation around the x - axis
	 * @param ry - initial rotation around the y - axis
	 * @param rz - initial rotation around the z - axis
	 * 
	 * @return - the created {@link Entity}
	 */
	public static Entity createEntity(String modelId, PhysicsFactoryType factoryType, int textureIndex, float scale, Vector3f position, float rx, float ry, float rz) {
		
		return createEntity(modelId, factoryType, textureIndex, scale, -1, position, rx, ry, rz);
	}
	
	/**Creates an {@link Entity} which is not initially part of the world.<br>
	 * Linear and angular velocity for dynamic {@link Entity}s can be set afterwards for the returned {@link Entity}.
	 * 
	 * @param modelId - a unique String identifier for the model to be used which holds the texture and geometry data for the created {@link Entity}
	 * @param factoryType - dynamic or static
	 * @param objectType - the {@link Entity}'s {@link ObjectType}
	 * @param primitiveType - the {@link Entity}'s {@link SphereType}
	 * @param textureIndex - in case there are multiple textures in a texture atlas, specify which one to use
	 * @param scale - scale determining the size of the Entity in [units]
	 * @param mass - mass of the object in [units]
	 * @param position - initial position of the new created Entity
	 * @param rx - initial rotation around the x - axis
	 * @param ry - initial rotation around the y - axis
	 * @param rz - initial rotation around the z - axis
	 * 
	 * @return - the created {@link Entity}
	 */
	public static Entity createEntity(String modelId, PhysicsFactoryType factoryType, int textureIndex, float scale, float mass, Vector3f position, float rx, float ry, float rz) {
		
		Entity entity;
		PhysicsObject physicsObject = null;
		GraphicsEntity graphicsEntity = new GraphicsEntity(ModelLoader.texturedModels.get(modelId), textureIndex, scale);
		GTPObject gtpObject = GTPMapper.getObject(graphicsEntity, scale, graphicsEntity.getModel().getPrimitiveType());
		
		PhysicsObjectFactory factory;
		
		if (mass < 0) {
			mass = graphicsEntity.getModel().getMass();
		} else if (mass == 0) { //do all objects need mass? if no, delete this
			mass = ModelLoader.massDummie;
		}
		
		switch(factoryType) {
		case STATIC:
			factory = staticFactory;
			physicsObject = staticFactory.createPrimitive(gtpObject, mass, new Transform(Maths.createTransformationMatrix(position, rx, ry, rz)));
			break;
		case DYNAMIC:
			physicsObject = dynamicFactory.createPrimitive(gtpObject, mass, new Transform(Maths.createTransformationMatrix(position, rx, ry, rz)));
			break;
		}
		
		switch (graphicsEntity.getModel().getObjectType()) {
		case ANT:
			return new Ant(graphicsEntity, physicsObject);
		case ENEMY:
			return new Enemy(graphicsEntity, physicsObject);
		case ENVIRONMENT:
			return new EnvironmentObject(graphicsEntity, physicsObject);
		case FOOD:
			return new Food(graphicsEntity, physicsObject);
		case PHEROMONE:
			return new Pheronome(graphicsEntity, physicsObject);
		}
				
		return null;
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
