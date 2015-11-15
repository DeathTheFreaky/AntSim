package at.antSim.objectsKI;

import at.antSim.Globals;
import at.antSim.MainApplication;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.graphics.graphicsUtils.LodWorker;
import at.antSim.graphics.graphicsUtils.TransparentTriangle;
import at.antSim.graphics.graphicsUtils.TransparentTriangleComparator;
import at.antSim.graphics.graphicsUtils.TransparentsWorker;
import at.antSim.graphics.models.TexturedModel;
import at.antSim.graphics.terrains.Terrain;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.PhysicsManager;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.TerrainPhysicsObject;
import at.antSim.objectsPhysic.Movement.BasicMovement;
import at.antSim.objectsPhysic.Movement.MovementManager;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.PositionablePhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;
import at.antSim.utils.Maths;
import at.antSim.utils.Pair;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.vecmath.Vector3f;

import org.lwjgl.util.vector.Matrix4f;

/**
 * Created on 18.05.2015.
 *
 * @author Flo, Clemens
 */
/**
 * @author Flo
 *
 */
public abstract class Entity {

	static final Map<PhysicsObject, ObjectType> physicsObjectTypeMap = new HashMap<PhysicsObject, ObjectType>();
	static final Map<TexturedModel, List<Entity>> renderingMap = new HashMap<TexturedModel, List<Entity>>();
	static final HashSet<Entity> lodEntities = new HashSet<>();
	static final Set<Entity> changedLodEntities = Collections.synchronizedSet(new HashSet<Entity>());
	static ArrayList<Pair<Entity, TransparentTriangle>> transparentTriangles = new ArrayList<Pair<Entity, TransparentTriangle>>();
	static final Set<Entity> changedTransparentEntities = Collections.synchronizedSet(new HashSet<Entity>());
	static final List<Entry<Entity, Integer>> entityVertices = new ArrayList<Entry<Entity, Integer>>();
	static final Map<PhysicsObject, Entity> parentingEntities = new HashMap<PhysicsObject, Entity>(); //allows us to get eg. the Food Entity in a react() method when an ant hit the Food Entitie's physicsObject
	static final List<Entity> entities = new LinkedList<>(); //used to delete all entities
	static final List<Entity> dynamicEntities = new LinkedList<>();
	static final List<Ant> ants = new LinkedList<Ant>();
	static final List<Entity> pheromones = new LinkedList<>();
	static final List<PositionLocator> deleteableLocators = new LinkedList<PositionLocator>();
	static final Vector3f lastCameraPos = new Vector3f();
	static boolean cameraPosChangedTransparents = false;
	static boolean cameraPosChangedLods = false;
	static Hive hive;
	
	static TransparentsWorker triangleSorter = new TransparentsWorker();
	static LodWorker lodWorker = new LodWorker();
	
	static boolean deleteAllowed = true; //set to false when quitting a game so that events do not delete entities that are already being deleted

	GraphicsEntity graphicsEntity;
	PhysicsObject physicsObject;
	final ObjectType objectType;

	public Entity(GraphicsEntity graphicsEntity, PhysicsObject physicsObject, ObjectType type) {
		this.graphicsEntity = graphicsEntity;
		this.physicsObject = physicsObject;
		this.objectType = type;
		
		entities.add(this);
		parentingEntities.put(physicsObject, this);
		
		//add Entity to physics and rendering hashmaps
		physicsObjectTypeMap.put(physicsObject, type);
		if (graphicsEntity != null) {
			
			addRenderingEntity();
			
			if (graphicsEntity.getModel().usesLod())
			{
				System.out.println(graphicsEntity.getModel().getType() + " uses lod");
				lodEntities.add(this);
			}
			
			if (graphicsEntity.getModel().usesTransparency()) {
				
				// do not insert elements at correct position but sort whole list at the end -> should be faster for big amounts of data				
				for (TransparentTriangle triangle : graphicsEntity.getModel().getRawModel().getTransparentVertices())
				{
					//!!! IMPORTANT: MAKE COPIES AND NOT REFERENCES OF RAW MODEL PRESET'S TRIANGLES
					TransparentTriangle copiedTriangle = new TransparentTriangle(triangle);
					transparentTriangles.add(new Pair<Entity, TransparentTriangle>(this, copiedTriangle));
				}
								
				sortAllTransparentTriangles();
			}
		}
	}

	public abstract void react(StaticPhysicsObject staticPhysicsObject);

	public abstract void react(DynamicPhysicsObject dynamicPhysicsObject);

	public abstract void react(GhostPhysicsObject ghostPhysicsObject);
	
	public abstract void react(TerrainPhysicsObject terrainPhysicsObject);


	/**
	 * Adds an Entity to the renderingMap.
	 */
	void addRenderingEntity() {
		TexturedModel entityModel = graphicsEntity.getModel();
		List<Entity> batch = renderingMap.get(entityModel);
		if (batch != null) {
			batch.add(this);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(this);
			renderingMap.put(entityModel, newBatch);
		}
	}

	/**
	 * Deletes this Entity, removing it from the physicsObjectTypeMap and the renderingMap.
	 * 
	 * @param removeFromEntities - set to false if iterating through Entity.entities list to avoid concurrentModification exception
	 */
	public void delete() {
		deleteSpecific();
		PhysicsManager.getInstance().unregisterPhysicsObject(physicsObject);
		if (deleteAllowed) {
			entities.remove(this);
			
			// remove from transparent triangles
			for (Iterator<Pair<Entity, TransparentTriangle>> iterator = transparentTriangles.iterator(); iterator.hasNext();) {
				
				Pair<Entity, TransparentTriangle> triangle = iterator.next();
			    
			    if (triangle.getKey() == this)
			    {
			    	iterator.remove();
			    }
			}
			
			parentingEntities.remove(physicsObject);
			physicsObjectTypeMap.remove(this);
			if (graphicsEntity != null) { //null for Pheromones
				lodEntities.remove(this);
				changedLodEntities.remove(this);
				changedTransparentEntities.remove(this);
				if (renderingMap.containsKey(graphicsEntity.getModel())) {
					renderingMap.get(graphicsEntity.getModel()).remove(this);
				}
			}
		}
	}
	
	/**Allows to execute deletion routines specific to a subclass of Entity.
	 * 
	 */
	protected  abstract void deleteSpecific();

	/**
	 * @return - an unmodifiable version of renderingMap for preventing changes on renderingMap while allowing it to be retrieved for rendering
	 */
	public static Map<TexturedModel, List<Entity>> getUnmodifiableRenderingMap() {
		return Collections.unmodifiableMap(renderingMap);
	}
	
	public static Entity getParentEntity(PhysicsObject physicsObject) {
		return parentingEntities.get(physicsObject);
	}

	public GraphicsEntity getGraphicsEntity() {
		return graphicsEntity;
	}

	public PhysicsObject getPhysicsObject() {
		return physicsObject;
	}
	
	public ObjectType getObjectType() {
		return objectType;
	}
	
	public static void setDeleteAllowed(boolean allowed) {
		deleteAllowed = allowed;
	}
	
	/**Deletes all Entities from physicsObjects and GraphicsEntities maps when game session is quit (return to main menu).
	 * 
	 */
	public static void deleteAllEntities() {
				
		setDeleteAllowed(false);
		for (Entity entity : entities) {
			entity.delete();
		}
		for (PositionLocator loc : deleteableLocators) {
			loc.delete();
		}
		physicsObjectTypeMap.clear();
		renderingMap.clear();
		parentingEntities.clear();
		entities.clear();
		dynamicEntities.clear();
		ants.clear();
		pheromones.clear();
		deleteableLocators.clear();
		lodEntities.clear();
		changedLodEntities.clear();
		changedTransparentEntities.clear();
		transparentTriangles.clear();
		setDeleteAllowed(true);
	}
	
	public static void testDynamicsHaveMovementModes() {
		for (Entity e : dynamicEntities) {
			if (PhysicsManager.getInstance().getPhysicsObject(e.physicsObject.getCollisionBody()) == null) {
				PhysicsManager.getInstance().unregisterPhysicsObject(e.physicsObject);
				PhysicsManager.getInstance().registerPhysicsObject(e.physicsObject);
//				System.out.println("entity " + e + " has been reset");
			}
			else if (MovementManager.getInstance().getTopMovementMode(e.physicsObject) == null) {
//				System.out.println("dynamic entity lost basic movement");
				Vector3f dir = new Vector3f(-1f + 2*(float) Math.random(), 0, -1f + 2*(float) Math.random());
				MovementManager.getInstance().addMovementEntry((DynamicPhysicsObject) e.physicsObject, new BasicMovement((DynamicPhysicsObject) e.physicsObject, dir, Globals.ANT_SPEED));
			} else {
				if (e.objectType == ObjectType.ANT) {
					Ant a = (Ant) e;
					Vector3f currentPos = ((ReadOnlyPhysicsObject) e.getPhysicsObject()).getPosition();
					Vector3f diffPosition = new Vector3f(currentPos.x - a.previousResetPosition.x, 0, currentPos.z - a.previousResetPosition.z);
					a.previousResetPosition = currentPos;
					if (diffPosition.length() < 1f) {
						a.previousResetCtr++;
						if (a.previousResetCtr >= a.previousResetThreshold) {
							a.previousResetCtr = 0;
							Vector3f dir = new Vector3f(-1f + 2*(float) Math.random(), 0, -1f + 2*(float) Math.random());
							MovementManager.getInstance().removeAllMovementEntries(a.physicsObject, false);
							((BasicMovement) MovementManager.getInstance().getBaseMovementMode(a.physicsObject)).setDirection(dir);
							try {
								PhysicsManager.getInstance().unregisterPhysicsObject(a.physicsObject);
								PhysicsManager.getInstance().registerPhysicsObject(a.physicsObject);
//								System.out.println("ant " + a  + " has been reset");
							} catch (Exception ex) {
								System.out.println(e + " failed to be readded to physicsWorld");
							}
						}
					}
				} else if (e.objectType == ObjectType.ENEMY) {
					Enemy en = (Enemy) e;
					Vector3f currentPos = ((ReadOnlyPhysicsObject) e.getPhysicsObject()).getPosition();
					Vector3f diffPosition = new Vector3f(currentPos.x - en.previousResetPosition.x, 0, currentPos.z - en.previousResetPosition.z);
					en.previousResetPosition = currentPos;
					if (diffPosition.length() < 1f) {
						en.previousResetCtr++;
						if (en.previousResetCtr >= en.previousResetThreshold) {
							en.previousResetCtr = 0;
							Vector3f dir = new Vector3f(-1f + 2*(float) Math.random(), 0, -1f + 2*(float) Math.random());
							MovementManager.getInstance().removeAllMovementEntries(en.physicsObject, false);
							((BasicMovement) MovementManager.getInstance().getBaseMovementMode(en.physicsObject)).setDirection(dir);
							try {
								PhysicsManager.getInstance().unregisterPhysicsObject(en.physicsObject);
								PhysicsManager.getInstance().registerPhysicsObject(en.physicsObject);
//								System.out.println("enemy " + e + " has been reset");
							} catch (Exception ex) {
								System.out.println(e + " failed to be readded to physicsWorld");
							}
						}
					}
				}
			}
		}
	}
	
	public static void resetHive() {
		//reset hive 
		entities.add(hive);
		hive.addRenderingEntity();
		physicsObjectTypeMap.put(hive.physicsObject, hive.objectType);
		parentingEntities.put(hive.physicsObject, hive);
	}
	
	/**Strangely, sometimes dynamic objects seem to fall below the world. 
	 * This method resets them a little above terrain height, preserving their original forces.
	 * 
	 */
	public static void resetUndergroundEntities() {
		for (Entity entity : dynamicEntities) {
			DynamicPhysicsObject phyObj = (DynamicPhysicsObject) entity.getPhysicsObject();
			float terrainHeight = MainApplication.getInstance().getTerrain().getHeightOfTerrain(phyObj.getPosition().x, phyObj.getPosition().z);
			float modelHeight = entity.getGraphicsEntity().getModel().getRawModel().getyLength() / 2 * entity.getGraphicsEntity().getScale();
			if ((phyObj.getPosition().y - modelHeight) < terrainHeight) {
				float desiredHeight = terrainHeight + modelHeight  + 1f;
				Vector3f linVelocity = new javax.vecmath.Vector3f();
				Vector3f angVelocity = new javax.vecmath.Vector3f();
				phyObj.getCollisionBody().getLinearVelocity(linVelocity);
				phyObj.getCollisionBody().getAngularVelocity(angVelocity);
				PhysicsManager.getInstance().unregisterPhysicsObject(phyObj);
				phyObj.getCollisionBody().clearForces();
				phyObj.setPosition(new Vector3f(phyObj.getPosition().x, desiredHeight, phyObj.getPosition().z));
				PhysicsManager.getInstance().registerPhysicsObject(phyObj);
				phyObj.getCollisionBody().setLinearVelocity(linVelocity);
				phyObj.getCollisionBody().setAngularVelocity(angVelocity);
			}
		}
	}

	// sorts all vertices by the distance between their barrycentre and the camera, from the back to the front
	// positions, normals and texture coords as well as all other data stays the same as in original entities,
	// 
	
	/**Sorts all vertices by the distance between their barrycentre and the camera, from the back to the front.
	 * Positions, normals and texture coords can stay as they are since they are referenced by vertex indices.
	 * However, the array of vertex indices needs to be sorted from back to front and in the process an Entity
	 * might be "split" into multiple pieces if it intersects with another Entity.
	 * 
	 * Finally, these parts of the original Entities will be rendered in the correct sorted order from back to front.
	 * 
	 */
	private static void sortAllTransparentTriangles() 
	{
		if (!Globals.sortingDisabled && MainApplication.getWorldLoaded())
		{
			MainApplication.getTransparentExecutor().execute(triangleSorter);
			//Collections.sort(transparentTriangles, triangleComp);	
		}
	}
	
	public static List<Pair<Entity, TransparentTriangle>> getTransparentTriangles()
	{
		return transparentTriangles;
	}
	
	/**Returns all lod Entities of which the position has changed since the last update and resets the hashSet afterwards.
	 * @return
	 */
	public static HashSet<Entity> consumeChangedLodEntities()
	{
		HashSet<Entity> ret = new HashSet<>();
		
		for (Entity e : changedLodEntities)
		{
			ret.add(e);
		}
		
		changedLodEntities.clear();
		return ret;
	}
	
	public static HashSet<Entity> getLodEntities()
	{
		return lodEntities;
	}
	
	/**Returns all transparent Entities of which the position has changed since the last update and resets the hashSet afterwards.
	 * @return
	 */
	public static HashSet<Entity> consumeChangedTransparents()
	{
		// make sure caller (TransparentsWorker) receives copy of current hashSet state
		HashSet<Entity> ret = new HashSet<>();
		
		for (Entity e : changedTransparentEntities)
		{
			ret.add(e);
		}
		
		changedTransparentEntities.clear();
		return ret;
	}
	
	public static void setTransparentTriangles(ArrayList<Pair<Entity, TransparentTriangle>> triangles)
	{
		transparentTriangles = triangles;
	}
	
	/**1. Updates transparent triangle sorting if camera position has changed.
	 * 2. Updates lod Entities' distance to the camera.
	 * 
	 * @param vector3f
	 */
	public static void updateCameraPos(org.lwjgl.util.vector.Vector3f cameraPos)
	{		
		lastCameraPos.set(cameraPos.x, cameraPos.y, cameraPos.z);	
		cameraPosChangedTransparents = true;
		cameraPosChangedLods = true;
		sortAllTransparentTriangles();
		updateLods();
	}
	
	/**Always returns last camera position, no matter wether the camera position changed or not.
	 * @return
	 */
	public static org.lwjgl.util.vector.Vector3f getLastCameraPos()
	{
		return new org.lwjgl.util.vector.Vector3f(lastCameraPos.x, lastCameraPos.y, lastCameraPos.z);
	}
	
	/**
	 * @return - new lastCameraPos Vector3f or null if camera position did not change
	 */
	public static org.lwjgl.util.vector.Vector3f getLastCamerPosTransparents()
	{
		if (cameraPosChangedTransparents)
		{
			cameraPosChangedTransparents = false;
			return new org.lwjgl.util.vector.Vector3f(lastCameraPos.x, lastCameraPos.y, lastCameraPos.z);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * @return - new lastCameraPos Vector3f or null if camera position did not change
	 */
	public static org.lwjgl.util.vector.Vector3f getLastCamerPosLods()
	{
		if (cameraPosChangedLods)
		{
			cameraPosChangedLods = false;
			return new org.lwjgl.util.vector.Vector3f(lastCameraPos.x, lastCameraPos.y, lastCameraPos.z);
		}
		else
		{
			return null;
		}
	}
	
	public static void update()
	{
		if (changedTransparentEntities.size() > 0)
		{
			sortAllTransparentTriangles();
		}
		if (changedLodEntities.size() > 0)
		{
			updateLods();
		}
	}
	
	/**Update camera distance of all lod Entities' of which the position has changed since the last update.
	 * 
	 */
	private static void updateLods()
	{
		if (MainApplication.getWorldLoaded())
		{
			MainApplication.getLodExecutor().execute(lodWorker);
		}
	}
	
	/**1. Updates a transparent entitie's triangles' barrycentre if the triangle's parenting Entity's world transform has changed.
	 * 2. Updates lod Entities' distance to the camera.
	 * @param po
	 */
	public static void setTransforms(ReadOnlyPhysicsObject po)
	{
		Entity entity = parentingEntities.get(po);
		
		// entity will be null for GhostPhysicsObjects (collision detection triggers)
		if (entity != null && entity.getGraphicsEntity() != null)
		{
			if (entity.getGraphicsEntity().getModel().usesTransparency())
			{
				changedTransparentEntities.add(entity);
			}
			
			if (entity.getGraphicsEntity().getModel().usesLod())
			{
				if (lodEntities.contains(entity))
				{
					changedLodEntities.add(entity);
				}
			}
		}
	}
}
