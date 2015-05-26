package at.antSim.objectsKI;

import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.graphics.models.TexturedModel;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObject;

import java.util.*;

/**
 * Created on 18.05.2015.
 *
 * @author Clemens
 */
public abstract class Entity {

	static final Map<PhysicsObject, ObjectType> physicsObjectTypeMap = new HashMap<PhysicsObject, ObjectType>();
	static final Map<TexturedModel, List<Entity>> renderingMap = new HashMap<TexturedModel, List<Entity>>();
	
	final GraphicsEntity graphicsEntity;
	final PhysicsObject physicsObject;
	
	public Entity (GraphicsEntity graphicsEntity, PhysicsObject physicsObject, ObjectType type) {
		this.graphicsEntity = graphicsEntity;
		this.physicsObject = physicsObject;
		
		//add Entity to physics and rendering hashmaps
		physicsObjectTypeMap.put(physicsObject, type);
		addRenderingEntity();
	}

	public abstract void react(StaticPhysicsObject staticPhysicsObject);

	public abstract void react(DynamicPhysicsObject dynamicPhysicsObject);

	public abstract void react(GhostPhysicsObject ghostPhysicsObject);

	
	/**Adds an Entity to the renderingMap.
	 * 
	 */
	private void addRenderingEntity() {
		TexturedModel entityModel = graphicsEntity.getModel();
		List<Entity> batch = renderingMap.get(entityModel);
		if(batch != null) {
			batch.add(this);
		}
		else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(this);
			renderingMap.put(entityModel, newBatch);
		}
	}
	
	/**Deletes this Entity, removing it from the physicsObjectTypeMap and the renderingMap.
	 * 
	 */
	public void delete() {
		physicsObjectTypeMap.remove(this);
		renderingMap.get(graphicsEntity.getModel()).remove(this);
	}
	
	/**
	 * @return - an unmodifiable version of renderingMap for preventing changes on renderingMap while allowing it to be retrieved for rendering
	 */
	public static Map<TexturedModel, List<Entity>> getUnmodifiableRenderingMap() {
		return Collections.unmodifiableMap(renderingMap);
	}
	
	public GraphicsEntity getGraphicsEntity() {
		return graphicsEntity;
	}
	
	public PhysicsObject getPhysicsObject() {
		return physicsObject;
	}
}
