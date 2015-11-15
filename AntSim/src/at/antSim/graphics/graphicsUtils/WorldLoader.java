package at.antSim.graphics.graphicsUtils;

import at.antSim.Globals;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.graphics.entities.Light;
import at.antSim.graphics.terrains.Terrain;
import at.antSim.graphics.textures.TerrainTexture;
import at.antSim.graphics.textures.TerrainTexturePack;
import at.antSim.objectsKI.Entity;
import at.antSim.objectsKI.EntityBuilder;
import at.antSim.objectsKI.EntityBuilderImpl;
import at.antSim.objectsKI.Hive;
import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.PhysicsFactorys.DynamicPhysicsObjectFactory;
import at.antSim.objectsPhysic.PhysicsFactorys.GhostPhysicsObjectFactory;
import at.antSim.objectsPhysic.PhysicsFactorys.StaticPhysicsObjectFactory;
import at.antSim.objectsPhysic.PhysicsManager;
import at.antSim.utils.Maths;

import com.bulletphysics.linearmath.Transform;

import org.lwjgl.util.vector.Vector3f;

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
	
	private static float margin = 20;
	
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
			if (i % 30 == 0) {
				float x = margin + random.nextFloat() * (Globals.WORLD_SIZE - 2 * margin);
				float z = -margin + random.nextFloat() * (-Globals.WORLD_SIZE + 2 * margin);
				float y = terrain.getHeightOfTerrain(x, z);
				if (!insideStartingArea(x, z)) {
					Entity grass = builder.setFactory(StaticPhysicsObjectFactory.getInstance())
							.setPosition(new Vector3f(x, y - 6, z))
							.setRotation(0, random.nextFloat() * 360, 0)
							.buildGraphicsEntity("grass", 1, 30)
							.buildPhysicsObject()
							.registerResult();
				}
			}
			if (i % 180 == 0) {
				float x = margin + random.nextFloat() * (Globals.WORLD_SIZE - 2 * margin);
				float z = -margin + random.nextFloat() * (-Globals.WORLD_SIZE + 2 * margin);
				float y = terrain.getHeightOfTerrain(x, z);
				if (!insideStartingArea(x, z)) {
					Entity fern = builder.setFactory(StaticPhysicsObjectFactory.getInstance())
							.setPosition(new Vector3f(x, y - 5, z))
							.setRotation(0, random.nextFloat() * 360, 0)
							.buildGraphicsEntity("fern", random.nextInt(4), 70)
							.buildPhysicsObject()
							.registerResult();
				}
			}
			if (i % 380 == 0) {
				float x = margin + random.nextFloat() * (Globals.WORLD_SIZE - 2 * margin);
				float z = -margin + random.nextFloat() * (-Globals.WORLD_SIZE + 2 * margin);
				float y = terrain.getHeightOfTerrain(x, z);
				if (!insideStartingArea(x, z)) {
					Entity bush = builder.setFactory(StaticPhysicsObjectFactory.getInstance())
							.setPosition(new Vector3f(x, y - 3, z))
							.setRotation(0, random.nextFloat() * 360, 0)
							.buildGraphicsEntity("bush", 1, 70)
							.buildPhysicsObject()
							.registerResult();
				}
			}
			if (i % 250 == 0) {
				float x = margin + random.nextFloat() * (Globals.WORLD_SIZE - 2 * margin);
				float z = -margin + random.nextFloat() * (-Globals.WORLD_SIZE + 2 * margin);
				float y = terrain.getHeightOfTerrain(x, z);
				if (!insideStartingArea(x, z)) {
					Entity rock = builder.setFactory(StaticPhysicsObjectFactory.getInstance())
							.setPosition(new Vector3f(x, y - 10, z))
							.setRotation(0, random.nextFloat() * 360, 0)
							.buildGraphicsEntity("rock", 1, 40)
							.buildPhysicsObject()
							.registerResult();
				}
			}
			if (i % 400 == 0) {
				float x = margin + random.nextFloat() * (Globals.WORLD_SIZE - 2 * margin);
				float z = -margin + random.nextFloat() * (-Globals.WORLD_SIZE + 2 * margin);
				float y = terrain.getHeightOfTerrain(x, z);
				if (!insideStartingArea(x, z)) {
					Entity stomp = builder.setFactory(StaticPhysicsObjectFactory.getInstance())
							.setPosition(new Vector3f(x, y - 12, z))
							.setRotation(0, random.nextFloat() * 360, 0)
							.buildGraphicsEntity("stomp", 1, 70)
							.buildPhysicsObject()
							.registerResult();
				}
			}
		}

		Entity hiveEntity = builder.setFactory(StaticPhysicsObjectFactory.getInstance())
				.setPosition(new Vector3f(Globals.WORLD_SIZE/2, terrain.getHeightOfTerrain(Globals.WORLD_SIZE/2, -Globals.WORLD_SIZE/2 - 150) -5, -Globals.WORLD_SIZE/2 - 150))
				.setHiveParameters(Globals.hiveFoodStacks)
				.buildGraphicsEntity("hive", 1, 80) //enable for debugging just to visualize the pheromones
				.buildPhysicsObject()
				.registerResult();
		
		if (!insideStartingArea(Globals.WORLD_SIZE/2, -Globals.WORLD_SIZE/2 - 150)) {
			
		} else {
			
		}

		hive = (Hive) hiveEntity;		
				
		Entity pointedLightLamp = builder.setFactory(StaticPhysicsObjectFactory.getInstance())
				.setPosition(new Vector3f(185, terrain.getHeightOfTerrain(185, -293) - 3, -293))
				.buildGraphicsEntity("lamp", 1, 30)
				.buildPhysicsObject()
				.registerResult();
		
		Entity lodDragon = builder.setFactory(StaticPhysicsObjectFactory.getInstance())
				.setPosition(new Vector3f(100, terrain.getHeightOfTerrain(100, -780), -780))
				.buildGraphicsEntity("dragon", 1, 50)
				.buildPhysicsObject()
				.registerResult();
		
		Entity lodTree = builder.setFactory(StaticPhysicsObjectFactory.getInstance())
				.setPosition(new Vector3f(150, terrain.getHeightOfTerrain(150, -780), -780))
				.buildGraphicsEntity("tree", 1, 100)
				.buildPhysicsObject()
				.registerResult();

		loadBorders(terrain);
	}
	
	private static boolean insideStartingArea(float x, float z) {
		float left = Globals.WORLD_SIZE/2 - 75;
		float right = Globals.WORLD_SIZE/2 + 75;
		float top = -Globals.WORLD_SIZE/2 - 75;
		float bottom = -Globals.WORLD_SIZE/2 - 225;
		if (x > left && x < right && z > bottom && z < top) {
			return true;
		}
		return false;
	}

	/**Loads borders at the edge of the world to trigger events telling ants/enemies they are leaving the world.
	 * 
	 */
	private static void loadBorders(Terrain terrain) {
		
		short tempFilterMask = 0;
		tempFilterMask = (short) (tempFilterMask | Globals.COL_KINEMATIC);
			
		Transform positionNorth = new Transform();
		positionNorth.set(Maths.createTransformationMatrix(new Vector3f(Globals.WORLD_SIZE/2, terrain.getHeightOfTerrain(Globals.WORLD_SIZE/2, 0), 0), 0, 0, 0));
		StaticPhysicsObject northBorder = (StaticPhysicsObject) StaticPhysicsObjectFactory.getInstance().createCuboid("border", Globals.MASS_DUMMIE, Globals.WORLD_SIZE, Globals.WORLD_SIZE, 5f, positionNorth);
		northBorder.setCollisionFilterGroup(Globals.COL_BORDER);
		northBorder.setCollisionFilterMask(tempFilterMask);
		PhysicsManager.getInstance().registerPhysicsObject(northBorder);
		
		Transform positionSouth = new Transform();
		positionSouth.set(Maths.createTransformationMatrix(new Vector3f(Globals.WORLD_SIZE/2, terrain.getHeightOfTerrain(Globals.WORLD_SIZE/2, -Globals.WORLD_SIZE), -Globals.WORLD_SIZE), 0, 0, 0));
		StaticPhysicsObject southBorder = (StaticPhysicsObject) StaticPhysicsObjectFactory.getInstance().createCuboid("border", Globals.MASS_DUMMIE, Globals.WORLD_SIZE, Globals.WORLD_SIZE, 5f, positionSouth);
		southBorder.setCollisionFilterGroup(Globals.COL_BORDER);
		southBorder.setCollisionFilterMask(tempFilterMask);
		PhysicsManager.getInstance().registerPhysicsObject(southBorder);
		
		Transform positionEast = new Transform();
		positionEast.set(Maths.createTransformationMatrix(new Vector3f(Globals.WORLD_SIZE, terrain.getHeightOfTerrain(Globals.WORLD_SIZE, -Globals.WORLD_SIZE/2), -Globals.WORLD_SIZE/2), 0, 0, 0));
		StaticPhysicsObject eastBorder = (StaticPhysicsObject) StaticPhysicsObjectFactory.getInstance().createCuboid("border", Globals.MASS_DUMMIE, 5f, Globals.WORLD_SIZE, Globals.WORLD_SIZE, positionEast);
		eastBorder.setCollisionFilterGroup(Globals.COL_BORDER);
		eastBorder.setCollisionFilterMask(tempFilterMask);
		PhysicsManager.getInstance().registerPhysicsObject(eastBorder);
		
		Transform positionWest = new Transform();
		positionWest.set(Maths.createTransformationMatrix(new Vector3f(0, terrain.getHeightOfTerrain(0, -Globals.WORLD_SIZE/2), -Globals.WORLD_SIZE/2), 0, 0, 0));
		StaticPhysicsObject westBorder = (StaticPhysicsObject) StaticPhysicsObjectFactory.getInstance().createCuboid("border", Globals.MASS_DUMMIE, 5f, Globals.WORLD_SIZE, Globals.WORLD_SIZE, positionWest);
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
		
		lights.add(new Light(new Vector3f(0, 1000, -7000), new Vector3f(0.7f, 0.7f, 0.7f))); //sun
		lights.add(new Light(new Vector3f(0, -400, 7000), new Vector3f(0.4f, 0.4f, 0.4f))); //moon
		
		//pointed light sources with attenuation -> 1 lamp, lit from 4 sources to make lamp seem glowing
		lights.add(new Light(new Vector3f(185, 18, -288), new Vector3f(2, 0, 0), new Vector3f(0.5f, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(185, 18, -298), new Vector3f(2, 0, 0), new Vector3f(0.5f, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(180, 18, -293), new Vector3f(2, 0, 0), new Vector3f(0.5f, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(190, 18, -293), new Vector3f(2, 0, 0), new Vector3f(0.5f, 0.01f, 0.002f)));
		
		return lights;
	}
}
