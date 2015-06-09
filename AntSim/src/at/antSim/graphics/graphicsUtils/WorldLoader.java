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
import at.antSim.objectsPhysic.PhysicsFactorys.DynamicPhysicsObjectFactory;
import at.antSim.objectsPhysic.PhysicsFactorys.StaticPhysicsObjectFactory;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.Random;

/**Provides methods to load the world's content.
 * 
 * @author Flo
 *
 */
public class WorldLoader {
	
	private static EntityBuilder builder = new EntityBuilderImpl();
	
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
				builder.setFactory(StaticPhysicsObjectFactory.getInstance())
					.setPosition(new Vector3f(x, y, z))
					.setRotation(0, random.nextFloat() * 360, 0)
					.buildGraphicsEntity("fern", random.nextInt(4), 20f)
					.buildPhysicsObject()
					.registerResult();
			}
			if (i % 5 == 0) {
				float x = random.nextFloat() * Globals.WORLD_SIZE;
				float z = random.nextFloat() * -Globals.WORLD_SIZE;
				float y = terrain.getHeightOfTerrain(x, z);
				builder.setFactory(StaticPhysicsObjectFactory.getInstance())
					.setPosition(new Vector3f(x, y, z))
					.setRotation(0, random.nextFloat() * 360, 0)
					.buildGraphicsEntity("grass", 1, random.nextFloat() * 2f + 25f)
					.buildPhysicsObject()
					.registerResult();
				x = random.nextFloat() * Globals.WORLD_SIZE;
				z = random.nextFloat() * -Globals.WORLD_SIZE;
				y = terrain.getHeightOfTerrain(x, z);
				builder.setFactory(StaticPhysicsObjectFactory.getInstance())
					.setPosition(new Vector3f(x, y, z))
					.setRotation(0, random.nextFloat() * 360, 0)
					.buildGraphicsEntity("tree", 1, random.nextFloat() * 5f + 20f)
					.buildPhysicsObject()
					.registerResult();
			}
		}
		
		//add some lamps
		Entity lamp1 = builder.setFactory(StaticPhysicsObjectFactory.getInstance())
			.setPosition(new Vector3f(185, -4.7f, -293))
			.setRotation(0, random.nextFloat() * 360, 0)
			.buildGraphicsEntity("lamp", 1, 20)
			.buildPhysicsObject()
			.registerResult();

		Entity lamp2 = builder.setFactory(StaticPhysicsObjectFactory.getInstance())
				.setPosition(new Vector3f(370, 4.2f, -300))
				.setRotation(0, random.nextFloat() * 360, 0)
				.buildGraphicsEntity("lamp", 1, 20)
				.buildPhysicsObject()
				.registerResult();
		
		Entity sphereTest = builder.setFactory(StaticPhysicsObjectFactory.getInstance())
				.setPosition(new Vector3f(Globals.WORLD_SIZE/2, terrain.getHeightOfTerrain(Globals.WORLD_SIZE/2, -Globals.WORLD_SIZE/2), -Globals.WORLD_SIZE/2))
				.setRotation(0, random.nextFloat() * 360, 0)
				.buildGraphicsEntity("greenCube", 1, 10)
				.buildPhysicsObject()
				.registerResult();
		
		//add cool stanford demo dragon for specular lighting demo
		Entity redCube = builder.setFactory(DynamicPhysicsObjectFactory.getInstance())
				.setPosition(new Vector3f(Globals.WORLD_SIZE/2, 100, -Globals.WORLD_SIZE/2))
				.setRotation(90, 45, 90)
				.buildGraphicsEntity("sphere", 1, 25)
				.buildPhysicsObject()
				.registerResult();
		
		Entity dragon = builder.setFactory(DynamicPhysicsObjectFactory.getInstance())
				.setPosition(new Vector3f(Globals.WORLD_SIZE/2, 50, -Globals.WORLD_SIZE/2))
				.setRotation(90, 45, 90)
				.buildGraphicsEntity("dragon", 1, 25)
				.buildPhysicsObject()
				.registerResult();
		
		//PhysicsManager.getInstance().observingPhysicsObject = (DynamicPhysicsObject) dragon.getPhysicsObject();

		Entity stomp = builder.setFactory(StaticPhysicsObjectFactory.getInstance())
				.setPosition(new Vector3f(370, 4.2f, -300))
				.setRotation(0, random.nextFloat() * 360, 0)
				.buildGraphicsEntity(ModelLoader.texturedModels.get("stomp"), 1, 20)
				.buildPhysicsObject()
				.registerResult();

		// new
//		Entity rock = builder.setFactory(StaticPhysicsObjectFactory.getInstance())
//				.setPosition(new Vector3f(300, 4.2f, -300))
//				.setRotation(0, random.nextFloat() * 360, 0)
//				.buildGraphicsEntity(ModelLoader.texturedModels.get("rock"), 1, 20)
//				.buildPhysicsObject()
//				.registerResult();

		Entity antBlack = builder.setFactory(DynamicPhysicsObjectFactory.getInstance())
				.setPosition(new Vector3f(360, 4.2f, -300))
				.setRotation(0, random.nextFloat() * 360, 0)
				.buildGraphicsEntity(ModelLoader.texturedModels.get("antBlack"), 1, 20)
				.buildPhysicsObject()
				.registerResult();

		Entity antRed = builder.setFactory(DynamicPhysicsObjectFactory.getInstance())
				.setPosition(new Vector3f(360, 4.2f, -300))
				.setRotation(0, random.nextFloat() * 360, 0)
				.buildGraphicsEntity(ModelLoader.texturedModels.get("antRed"), 1, 20)
				.buildPhysicsObject()
				.registerResult();

		Entity antDark = builder.setFactory(DynamicPhysicsObjectFactory.getInstance())
				.setPosition(new Vector3f(Globals.WORLD_SIZE/2, 50, -Globals.WORLD_SIZE/2))
				.setRotation(0, random.nextFloat() * 360, 0)
				.buildGraphicsEntity(ModelLoader.texturedModels.get("antDark"), 1, 100)
				.buildPhysicsObject()
				.registerResult();

		// end of new
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
