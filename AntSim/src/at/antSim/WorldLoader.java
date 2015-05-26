package at.antSim;

import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.graphics.entities.Light;
import at.antSim.graphics.graphicsUtils.ModelLoader;
import at.antSim.graphics.graphicsUtils.OpenGLLoader;
import at.antSim.graphics.terrains.Terrain;
import at.antSim.graphics.textures.TerrainTexture;
import at.antSim.graphics.textures.TerrainTexturePack;
import at.antSim.objectsKI.Entity;
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
				builder.setFactory(StaticPhysicsObjectFactory.getInstance())
					.setPosition(new Vector3f(x, y, z))
					.setRotation(0, random.nextFloat() * 360, 0)
					.buildGraphicsEntity(ModelLoader.texturedModels.get("fern"), random.nextInt(4), 20f)
					.buildPhysicsObject()
					.getResult();
			}
			if (i % 5 == 0) {
				float x = random.nextFloat() * Globals.WORLD_SIZE;
				float z = random.nextFloat() * -Globals.WORLD_SIZE;
				float y = terrain.getHeightOfTerrain(x, z);
				builder.setFactory(StaticPhysicsObjectFactory.getInstance())
					.setPosition(new Vector3f(x, y, z))
					.setRotation(0, random.nextFloat() * 360, 0)
					.buildGraphicsEntity(ModelLoader.texturedModels.get("grass"), 1, random.nextFloat() * 2f + 25f)
					.buildPhysicsObject()
					.getResult();
				x = random.nextFloat() * Globals.WORLD_SIZE;
				z = random.nextFloat() * -Globals.WORLD_SIZE;
				y = terrain.getHeightOfTerrain(x, z);
				builder.setFactory(StaticPhysicsObjectFactory.getInstance())
					.setPosition(new Vector3f(x, y, z))
					.setRotation(0, random.nextFloat() * 360, 0)
					.buildGraphicsEntity(ModelLoader.texturedModels.get("tree"), 1, random.nextFloat() * 5f + 20f)
					.buildPhysicsObject()
					.getResult();
			}
		} 
		
		//add some lamps
		Entity lamp1 = builder.setFactory(StaticPhysicsObjectFactory.getInstance())
			.setPosition(new Vector3f(185, -4.7f, -293))
			.setRotation(0, random.nextFloat() * 360, 0)
			.buildGraphicsEntity(ModelLoader.texturedModels.get("lamp"), 1, 20)
			.buildPhysicsObject()
			.getResult();
		Entity lamp2 = builder.setFactory(StaticPhysicsObjectFactory.getInstance())
				.setPosition(new Vector3f(370, 4.2f, -300))
				.setRotation(0, random.nextFloat() * 360, 0)
				.buildGraphicsEntity(ModelLoader.texturedModels.get("lamp"), 1, 20)
				.buildPhysicsObject()
				.getResult();
		
		//add cool stanford demo dragon for specular lighting demo
		Entity dragon = builder.setFactory(DynamicPhysicsObjectFactory.getInstance())
				.setPosition(new Vector3f(Globals.WORLD_SIZE/2, -7, -Globals.WORLD_SIZE/2))
				.setRotation(90, 45, 90)
				.buildGraphicsEntity(ModelLoader.texturedModels.get("dragon"), 1, 25)
				.buildPhysicsObject()
				.getResult();
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
