package at.antSim.graphics.graphicsUtils;

import at.antSim.Globals;
import at.antSim.eventSystem.EventManager;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.graphics.entities.Light;
import at.antSim.graphics.terrains.Terrain;
import at.antSim.graphics.textures.TerrainTexture;
import at.antSim.graphics.textures.TerrainTexturePack;
import at.antSim.objectsKI.Entity;
import at.antSim.objectsKI.EntityBuilder;
import at.antSim.objectsKI.EntityBuilderImpl;
import at.antSim.objectsKI.Hive;
import at.antSim.objectsKI.ObjectType;
import at.antSim.objectsKI.PositionLocator;
import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.PhysicsManager;
import at.antSim.objectsPhysic.PhysicsFactorys.DynamicPhysicsObjectFactory;
import at.antSim.objectsPhysic.PhysicsFactorys.GhostPhysicsObjectFactory;
import at.antSim.objectsPhysic.PhysicsFactorys.StaticPhysicsObjectFactory;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;

import org.lwjgl.util.vector.Vector3f;

import com.bulletphysics.linearmath.Transform;

import java.util.ArrayList;
import java.util.Random;

/**Provides methods to load the world's content.
 * 
 * @author Clemens, Flo
 *
 */
public class WorldLoader {
	
	//ATTENTION: bullet coordinate system is right-handed!!! -> X values increase to the left-hand side! world ranges from WorldSize on the left to 0 on the right
	
	private static EntityBuilder builder = new EntityBuilderImpl();
	public static Hive hive;
	
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
//		for (int i = 0; i < 1200; i++) {
//			if (i % 20 == 0) {
//				float x = random.nextFloat() * Globals.WORLD_SIZE;
//				float z = random.nextFloat() * -Globals.WORLD_SIZE;
//				float y = terrain.getHeightOfTerrain(x, z);
//				builder.setFactory(StaticPhysicsObjectFactory.getInstance())
//					.setPosition(new Vector3f(x, y, z))
//					.setRotation(0, random.nextFloat() * 360, 0)
//					.buildGraphicsEntity("fern", random.nextInt(4), 20f)
//					.buildPhysicsObject()
//					.registerResult();
//			}
//			if (i % 5 == 0) {
//				float x = random.nextFloat() * Globals.WORLD_SIZE;
//				float z = random.nextFloat() * -Globals.WORLD_SIZE;
//				float y = terrain.getHeightOfTerrain(x, z);
//				builder.setFactory(StaticPhysicsObjectFactory.getInstance())
//					.setPosition(new Vector3f(x, y, z))
//					.setRotation(0, random.nextFloat() * 360, 0)
//					.buildGraphicsEntity("grass", 1, random.nextFloat() * 2f + 25f)
//					.buildPhysicsObject()
//					.registerResult();
//				x = random.nextFloat() * Globals.WORLD_SIZE;
//				z = random.nextFloat() * -Globals.WORLD_SIZE;
//				y = terrain.getHeightOfTerrain(x, z);
//				builder.setFactory(StaticPhysicsObjectFactory.getInstance())
//					.setPosition(new Vector3f(x, y, z))
//					.setRotation(0, random.nextFloat() * 360, 0)
//					.buildGraphicsEntity("tree", 1, random.nextFloat() * 5f + 20f)
//					.buildPhysicsObject()
//					.registerResult();
//			}
//		}
//		
//		//add some lamps
//		Entity lamp1 = builder.setFactory(StaticPhysicsObjectFactory.getInstance())
//			.setPosition(new Vector3f(185, -4.7f, -293))
//			.setRotation(0, random.nextFloat() * 360, 0)
//			.buildGraphicsEntity("lamp", 1, 20)
//			.buildPhysicsObject()
//			.registerResult();
//		
//		Entity redSphere = builder.setFactory(DynamicPhysicsObjectFactory.getInstance())
//		.setPosition(new Vector3f(Globals.WORLD_SIZE/2, 100, -Globals.WORLD_SIZE/2))
//		.setRotation(90, 45, 90)
//		.buildGraphicsEntity("sphere", 1, 25)
//		.buildPhysicsObject()
//		.registerResult();
//		EventManager.getInstance().registerEventListener(redSphere);
//
//
//		Entity lamp2 = builder.setFactory(StaticPhysicsObjectFactory.getInstance())
//				.setPosition(new Vector3f(370, 4.2f, -300))
//				.setRotation(0, random.nextFloat() * 360, 0)
//				.buildGraphicsEntity("lamp", 1, 20)
//				.buildPhysicsObject()
//				.registerResult();
//		
//		Entity sphereTest = builder.setFactory(StaticPhysicsObjectFactory.getInstance())
//				.setPosition(new Vector3f(Globals.WORLD_SIZE/2, terrain.getHeightOfTerrain(Globals.WORLD_SIZE/2, -Globals.WORLD_SIZE/2), -Globals.WORLD_SIZE/2))
//				.setRotation(0, random.nextFloat() * 360, 0)
//				.buildGraphicsEntity("greenCube", 1, 10)
//				.buildPhysicsObject()
//				.registerResult();
//		
//		//add cool stanford demo dragon for specular lighting demo
//		Entity redCube = builder.setFactory(DynamicPhysicsObjectFactory.getInstance())
//				.setPosition(new Vector3f(Globals.WORLD_SIZE/2, 100, -Globals.WORLD_SIZE/2))
//				.setRotation(0, 0, 0)
//				.buildGraphicsEntity("sphere", 1, 25)
//				.buildPhysicsObject()
//				.registerResult();
//	
//		Entity dragon = builder.setFactory(DynamicPhysicsObjectFactory.getInstance())
//				.setPosition(new Vector3f(Globals.WORLD_SIZE/2, 50, -Globals.WORLD_SIZE/2))
//				.setRotation(0, 0, 0)
//				.buildGraphicsEntity("enemyGrasshopper", 1, 25)
//				.buildPhysicsObject()
//				.registerResult();
		
		//				//PhysicsManager.getInstance().observingPhysicsObject = (DynamicPhysicsObject) dragon.getPhysicsObject();
//
//				Entity stomp = builder.setFactory(StaticPhysicsObjectFactory.getInstance())
//						.setPosition(new Vector3f(370, 4.2f, -300))
//						.setRotation(0, random.nextFloat() * 360, 0)
//						.buildGraphicsEntity("stomp", 1, 20)
//						.buildPhysicsObject()
//						.registerResult();
//
//				// new
//				Entity rock = builder.setFactory(StaticPhysicsObjectFactory.getInstance())
//						.setPosition(new Vector3f(300, 4.2f, -300))
//						.setRotation(0, random.nextFloat() * 360, 0)
//						.buildGraphicsEntity("rock", 1, 20)
//						.buildPhysicsObject()
//						.registerResult();
//
//				Entity squirrel = builder.setFactory(StaticPhysicsObjectFactory.getInstance())
//						.setPosition(new Vector3f(350, 4.2f, -350))
//						.setRotation(0, random.nextFloat() * 360, 0)
//						.buildGraphicsEntity("squirrel", 1, 20)
//						.buildPhysicsObject()
//						.registerResult();

//				Entity antBlack = builder.setFactory(DynamicPhysicsObjectFactory.getInstance())
//						.setPosition(new Vector3f(Globals.WORLD_SIZE/2 + 10, 10, -Globals.WORLD_SIZE/2))
//						.setRotation(0, random.nextFloat() * 360, 0)
//						.buildGraphicsEntity("antBlack", 1, 20)
//						.buildPhysicsObject()
//						.registerResult();
		//
//				Entity antRed = builder.setFactory(DynamicPhysicsObjectFactory.getInstance())
//						.setPosition(new Vector3f(Globals.WORLD_SIZE/2 - 10, 10, -Globals.WORLD_SIZE/2))
//						.setRotation(0, random.nextFloat() * 360, 0)
//						.buildGraphicsEntity("antRed", 1, 20)
//						.buildPhysicsObject()
//						.registerResult();
		//
//				Entity antDark = builder.setFactory(DynamicPhysicsObjectFactory.getInstance())
//						.setPosition(new Vector3f(Globals.WORLD_SIZE/2, 10, -Globals.WORLD_SIZE/2))
//						.setRotation(0, random.nextFloat() * 360, 0)
//						.buildGraphicsEntity("antDark", 1, 10)
//						.buildPhysicsObject()
//						.registerResult();

				// end of new
		
		Entity pheromone = builder.setFactory(GhostPhysicsObjectFactory.getInstance())
				.setPosition(new Vector3f(Globals.WORLD_SIZE/2 + 200, terrain.getHeightOfTerrain(Globals.WORLD_SIZE/2 + 200, -Globals.WORLD_SIZE/2) - Globals.PHERONOME_SIZE/2, -Globals.WORLD_SIZE/2))
				.buildGraphicsEntity("pheromone", 1, Globals.PHERONOME_SIZE) //enable for debugging just to visualize the pheromones
				.buildPhysicsObject()
				.registerResult();
		
		Entity hiveEntity = builder.setFactory(StaticPhysicsObjectFactory.getInstance())
				.setPosition(new Vector3f(Globals.WORLD_SIZE/2, terrain.getHeightOfTerrain(Globals.WORLD_SIZE/2, -Globals.WORLD_SIZE/2) - Globals.PHERONOME_SIZE/2, -Globals.WORLD_SIZE/2))
				.setHiveParameters(20)
				.buildGraphicsEntity("hive", 1, 50) //enable for debugging just to visualize the pheromones
				.buildPhysicsObject()
				.registerResult();
		
		hive = (Hive) hiveEntity;
		
		loadBorders(terrain);
	}
	
	/**Loads borders at the edge of the world to trigger events telling ants/enemies they are leaving the world.
	 * 
	 */
	private static void loadBorders(Terrain terrain) {
		
		short tempFilterMask = 0;
		tempFilterMask = (short) (tempFilterMask | Globals.COL_KINEMATIC);
			
		Transform positionNorth = new Transform();
		positionNorth.set(Maths.createTransformationMatrix(new Vector3f(Globals.WORLD_SIZE/2, terrain.getHeightOfTerrain(Globals.WORLD_SIZE/2, 0), 0), 0, 0, 0));
		GhostPhysicsObject northBorder = (GhostPhysicsObject) GhostPhysicsObjectFactory.getInstance().createCuboid("border", Globals.MASS_DUMMIE, Globals.WORLD_SIZE, Globals.WORLD_SIZE, 5f, positionNorth);
		northBorder.setCollisionFilterGroup(Globals.COL_BORDER);
		northBorder.setCollisionFilterMask(tempFilterMask);
		PhysicsManager.getInstance().registerPhysicsObject(northBorder);
		
		Transform positionSouth = new Transform();
		positionSouth.set(Maths.createTransformationMatrix(new Vector3f(Globals.WORLD_SIZE/2, terrain.getHeightOfTerrain(Globals.WORLD_SIZE/2, -Globals.WORLD_SIZE), -Globals.WORLD_SIZE), 0, 0, 0));
		GhostPhysicsObject southBorder = (GhostPhysicsObject) GhostPhysicsObjectFactory.getInstance().createCuboid("border", Globals.MASS_DUMMIE, Globals.WORLD_SIZE, Globals.WORLD_SIZE, 5f, positionSouth);
		southBorder.setCollisionFilterGroup(Globals.COL_BORDER);
		southBorder.setCollisionFilterMask(tempFilterMask);
		PhysicsManager.getInstance().registerPhysicsObject(southBorder);
		
		Transform positionEast = new Transform();
		positionEast.set(Maths.createTransformationMatrix(new Vector3f(Globals.WORLD_SIZE, terrain.getHeightOfTerrain(Globals.WORLD_SIZE, -Globals.WORLD_SIZE/2), -Globals.WORLD_SIZE/2), 0, 0, 0));
		GhostPhysicsObject eastBorder = (GhostPhysicsObject) GhostPhysicsObjectFactory.getInstance().createCuboid("border", Globals.MASS_DUMMIE, 5f, Globals.WORLD_SIZE, Globals.WORLD_SIZE, positionEast);
		eastBorder.setCollisionFilterGroup(Globals.COL_BORDER);
		eastBorder.setCollisionFilterMask(tempFilterMask);
		PhysicsManager.getInstance().registerPhysicsObject(eastBorder);
		
		Transform positionWest = new Transform();
		positionWest.set(Maths.createTransformationMatrix(new Vector3f(0, terrain.getHeightOfTerrain(0, -Globals.WORLD_SIZE/2), -Globals.WORLD_SIZE/2), 0, 0, 0));
		GhostPhysicsObject westBorder = (GhostPhysicsObject) GhostPhysicsObjectFactory.getInstance().createCuboid("border", Globals.MASS_DUMMIE, 5f, Globals.WORLD_SIZE, Globals.WORLD_SIZE, positionWest);
		westBorder.setCollisionFilterGroup(Globals.COL_BORDER);
		westBorder.setCollisionFilterMask(tempFilterMask);
		PhysicsManager.getInstance().registerPhysicsObject(westBorder);

	}
	
	/**Loads the world's light sources.
	 * 
	 * @return - a list of the world's light sources
	 */
	public static ArrayList<Light> loadLights() {
		
		ArrayList<Light> lights = new ArrayList<Light>();
		
		lights.add(new Light(new Vector3f(0, 1000, -7000), new Vector3f(0.4f, 0.4f, 0.4f))); //sun
		lights.add(new Light(new Vector3f(185, 10, -293), new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f))); //lamp
		
		return lights;
	}
}
