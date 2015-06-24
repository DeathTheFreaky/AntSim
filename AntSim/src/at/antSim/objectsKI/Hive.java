package at.antSim.objectsKI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import com.bulletphysics.collision.dispatch.CollisionFlags;

import at.antSim.Globals;
import at.antSim.MainApplication;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.PhysicsManager;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.TerrainPhysicsObject;
import at.antSim.objectsPhysic.Movement.MovementManager;
import at.antSim.objectsPhysic.Movement.MovementModeType;
import at.antSim.objectsPhysic.PhysicsFactorys.DynamicPhysicsObjectFactory;
import at.antSim.objectsPhysic.PhysicsFactorys.GhostPhysicsObjectFactory;
import at.antSim.objectsPhysic.PhysicsFactorys.StaticPhysicsObjectFactory;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;

public class Hive extends Entity {

	private static Hive INSTANCE = null;

	// Gibts was besseres als static wenn mehrere Threads drauf zugreifen
	private ArrayList<Feedable> fa = new ArrayList<Feedable>();
	private ArrayList<Feedable> removeFeed = new ArrayList<Feedable>();
	private ArrayList<Feedable> addFeed = new ArrayList<Feedable>();
	private ArrayList<Ant> ants = new ArrayList<Ant>();
	private ArrayList<Ant> deleteAnts = new ArrayList<Ant>();

	private ArrayList<Egg> eggs = new ArrayList<Egg>();
	private ArrayList<Larva> larvae = new ArrayList<Larva>();
	
	private List<Entity> deleteablePheromones = new LinkedList<>();

	private int foodStacks;
	private Queen queen;

	private int startFood;

	PositionLocator positionLocator;

	protected Hive(GraphicsEntity graphicsEntity, PhysicsObject physicsObject) {
		this(Globals.hiveFoodStacks, graphicsEntity, physicsObject);
	}

	// Startbedingungen aendern
	protected Hive(int food, GraphicsEntity graphicsEntity,
			PhysicsObject physicsObject) {
		super(graphicsEntity, physicsObject, ObjectType.HIVE);

		startFood = food;
		foodStacks = food;
		queen = new Queen();
		fa.add(queen);

		buildPositionLocator();
		
		Entity.hive = this;
	}

	private void buildPositionLocator() {
		
		// set initial position and size of PositionLocator ghost physics object
				javax.vecmath.Vector3f vecMathPos = ((ReadOnlyPhysicsObject) physicsObject)
						.getPosition();
				vecMathPos.y = vecMathPos.y - Globals.POSITION_LOCATOR_MARGIN;
		
		positionLocator = (PositionLocator) MainApplication
				.getInstance()
				.getDefaultEntityBuilder()
				.setFactory(GhostPhysicsObjectFactory.getInstance())
				.setLocatorMaxAnts(5)
				.setPosition(
						new Vector3f(vecMathPos.x, vecMathPos.y, vecMathPos.z))
				.buildGraphicsEntity(
						"positionLocatorBlue", 1, graphicsEntity.getScale() + Globals.POSITION_LOCATOR_MARGIN * 2)
				.setTarget(this).buildPhysicsObject().registerResult();
	}

	public void foodChain() {
		for (Ant a : ants) {
			a.setHp(a.getHp() - 1);
		}
		ants.removeAll(deleteAnts);
		deleteAnts.clear();
	}
	public void addEgg(Egg e) {
		eggs.add(e);
	}
	public void addLarva(Larva l) {
		larvae.add(l);
	}

	public void removeEgg(Egg e) {
		eggs.remove(e);
	}
	public void removeLarva(Larva l) {
		larvae.remove(l);
	}
	
	public void addFeedable(Feedable feed) {
		fa.add(feed);
	}

	public void removeFeedable(Feedable feed) {
		fa.remove(feed);
	}

	public void addRemoveableFeed(Feedable feed) {
		removeFeed.add(feed);
	}

	public void addAddableFeed(Feedable feed) {
		addFeed.add(feed);
	}

	public void addAnt(Ant a) {
		ants.add(a);
	}

	public void addDeleteAnt(Ant a) {
		deleteAnts.add(a);
	}

	public void removePheromone(Entity e) {
		deleteablePheromones.add(e);
	}

	public void removeAnt(Ant a) {
		deleteAnts.add(a);
	}

	public int getAntsNum() {
		return ants.size();
	}

	public int getLarvaeNum() {
		return larvae.size();
	}

	public int getEggsNum() {
		return eggs.size();
	}

	public int getFoodNum() {
		return foodStacks;
	}

	public int takeFood(int amount) {
		int takenAmount = Math.min(foodStacks - amount, amount);
		foodStacks -= takenAmount;
		return takenAmount;
	}

	public void storeFood(int amount) {
		foodStacks += amount;
	}

	@Override
	public void react(StaticPhysicsObject staticPhysicsObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void react(DynamicPhysicsObject dynamicPhysicsObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void react(GhostPhysicsObject ghostPhysicsObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void react(TerrainPhysicsObject terrainPhysicsObject) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void deleteSpecific() {
		positionLocator.reset();
	}

	public void reset() {
		foodStacks = startFood;
		ants.clear();
		deleteAnts.clear();
		fa.clear();
		eggs.clear();
		larvae.clear();
		fa.add(queen);
		removeFeed.clear();
		Entity.resetHive();
		buildPositionLocator();
	}

	public ArrayList<Feedable> getFeedables() {
		return fa;
	}

	public static Hive getInstance(GraphicsEntity graphicsEntity,
			PhysicsObject physicsObject) {
		if (INSTANCE == null) {
			INSTANCE = new Hive(graphicsEntity, physicsObject);
		}
		return INSTANCE;
	}

	public static Hive getInstance(int food, GraphicsEntity graphicsEntity,
			PhysicsObject physicsObject) {
		if (INSTANCE == null) {
			INSTANCE = new Hive(food, graphicsEntity, physicsObject);
		}
		return INSTANCE;
	}

	public static Hive getInstance() {
		return INSTANCE;
	}

	public void layPheromones() {
		for (Ant a : ants) {
			if (a.getOdorStatus() == 2 && a.currentPheromone == null) {
				Pheromone p = (Pheromone) a.layPheromones();
				if (MovementManager.getInstance().getTopMovementMode(a.physicsObject).getType() == MovementModeType.TARGET) {
					javax.vecmath.Vector3f dir = MovementManager.getInstance().getTopMovementMode(a.physicsObject).getDirection();
					if (dir != null) {
						dir.x = dir.x * -1;
						dir.z = dir.z * -1;
						dir.y = 0;
						p.setDirection(dir);
						Entity.pheromones.add(p);
					} else {
						System.err.println("Direciton of pheromone to be added was null ");
					}
				}
			}
		}

		// remove pheromones over time
		for (Entity e : Entity.pheromones) {
			Pheromone p = (Pheromone) e;
			p.setLifetime(p.getLifetime() - 1);
		}

		// Destroy pheromones
		for (Entity e : deleteablePheromones) {
			Pheromone p = (Pheromone) e;
			p.delete();
		}
	}

	public void feedAll() {
		for (Feedable feed : fa) {
			if(foodStacks > 0){
				feed.feed();
				foodStacks--;
			}		
		}
		fa.removeAll(removeFeed);
		fa.addAll(addFeed);
		removeFeed.clear();
		addFeed.clear();

	}
	
	/**Used when restarting game in singleton Hive.
	 * 
	 * @param ge
	 */
	public void setGraphicsEntity(GraphicsEntity ge) {
		graphicsEntity = ge;
	}
	
	/**Used when restarting game in singleton Hive.
	 * 
	 * @param po
	 */
	public void setPhysicsObject(PhysicsObject po) {
		physicsObject = po;
	}
	
	public void newAnt(){
		Entity ant = MainApplication.getInstance().getDefaultEntityBuilder().setFactory(DynamicPhysicsObjectFactory.getInstance())
//				.setPosition(new Vector3f(Globals.WORLD_SIZE/2, MainApplication.getInstance().getTerrain().getHeightOfTerrain(Globals.WORLD_SIZE/2, -Globals.WORLD_SIZE/2 - 150) +35, -Globals.WORLD_SIZE/2 - 150)) //position will be set later anyway in main loop according to mouse position
				.setPosition(new Vector3f(Globals.WORLD_SIZE/2, 40, -Globals.WORLD_SIZE/2 - 150)) //position will be set later anyway in main loop according to mouse position
				.setRotation(0, 0, 0)
				.buildGraphicsEntity("forager", 1, 15)
				.setObjectType(ObjectType.ANT)
				.buildPhysicsObject()
				.registerResult();
	}

}
