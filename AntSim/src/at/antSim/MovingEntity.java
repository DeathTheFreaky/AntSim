package at.antSim;

import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import at.antSim.eventSystem.EventListener;
import at.antSim.eventSystem.EventManager;
import at.antSim.eventSystem.EventPriority;
import at.antSim.eventSystem.events.KeyReleasedEvent;
import at.antSim.eventSystem.events.MouseButtonPressedEvent;
import at.antSim.eventSystem.events.MouseButtonReleasedEvent;
import at.antSim.graphics.graphicsUtils.Maths;
import at.antSim.graphics.graphicsUtils.ModelLoader;
import at.antSim.graphics.models.TexturedModel;
import at.antSim.objectsKI.Ant;
import at.antSim.objectsKI.Enemy;
import at.antSim.objectsKI.Entity;
import at.antSim.objectsKI.EntityBuilder;
import at.antSim.objectsKI.EntityBuilderImpl;
import at.antSim.objectsKI.EnvironmentObject;
import at.antSim.objectsKI.Food;
import at.antSim.objectsKI.Pheronome;
import at.antSim.objectsPhysic.PhysicsFactorys.DynamicPhysicsObjectFactory;
import at.antSim.objectsPhysic.PhysicsFactorys.GhostPhysicsObjectFactory;
import at.antSim.objectsPhysic.PhysicsFactorys.PhysicsObjectFactory;
import at.antSim.objectsPhysic.PhysicsFactorys.StaticPhysicsObjectFactory;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;

/**An Entity moving on the terrain. Will not be rendered if Entity is null.
 * 
 * @author Flo
 *
 */
public class MovingEntity {
	
	private Entity entity;
	private boolean moving;
	
	EntityBuilder builder;
	Random random;
	
	public MovingEntity() {
		builder = new EntityBuilderImpl();
		random = new Random();
	}
	
	public void setEntity(Entity entity) {
		if (entity == null && this.entity != null) {
			this.entity.delete();
			EventManager.getInstance().unregisterEventListener(this);
		}
		this.entity = entity;
		if (entity != null) {
			EventManager.getInstance().registerEventListener(this);
		} 
	}
	
	//return Entity
	
	public Entity getEntity() {
		return entity;
	}
	
	/**Places an Entity, selected from the GUI, on the Terrain.
	 * 
	 * @param event
	 */
	@EventListener (priority = EventPriority.LOW) //low guarantees that gui controls will be handled first
	public void placeEntityOnTerrain(MouseButtonReleasedEvent event){
		if (event.getButton() == 0) { //place entity on terrain if left mouse button is pressed
			//do some collision detection here -> only place entity if it does not overlap with another entity
			if (1 == 1) { //not collides
				
				PhysicsObjectFactory placedEntityFactory = null;
				
				//detect if new entity is static, dynamic or ghost
				switch (entity.getGraphicsEntity().getModel().getObjectType()) {
				case ANT:
					placedEntityFactory = DynamicPhysicsObjectFactory.getInstance();
				case ENEMY:
					placedEntityFactory = DynamicPhysicsObjectFactory.getInstance();
				case ENVIRONMENT:
					placedEntityFactory = StaticPhysicsObjectFactory.getInstance();
				case FOOD:
					placedEntityFactory = StaticPhysicsObjectFactory.getInstance();
				case PHEROMONE:
					placedEntityFactory = GhostPhysicsObjectFactory.getInstance();
				}
				
				//store data of currently moving entity which has to be dynamic to enable collision detection
				Vector3f placedPosition = Maths.convertVector3f(((ReadOnlyPhysicsObject) entity.getPhysicsObject()).getPosition());
				placedPosition.y = placedPosition.y - entity.getGraphicsEntity().getModel().getRawModel().getyLength()/2 * entity.getGraphicsEntity().getScale();
				TexturedModel placedTextureModel = entity.getGraphicsEntity().getModel();
				int placedTextureIndex = entity.getGraphicsEntity().getTextureIndex();
				float placedScale = entity.getGraphicsEntity().getScale() * 2; //*2 because scale is divided by two when creating graphics entity to reflect exect measurement
				
				//delete moving entity and...
				entity.delete();
				entity = null;
				event.consume();
				EventManager.getInstance().unregisterEventListener(this);
				
				//place entity equaling moving entity, but which can be static, dynamic or ghost
				Entity placedEntity = builder.setFactory(placedEntityFactory)
						.setPosition(placedPosition) //position will be set later anyway in main loop according to mouse position
						.setRotation(0, random.nextFloat() * 360, 0)
						.buildGraphicsEntity(placedTextureModel, placedTextureIndex, placedScale)
						.buildPhysicsObject()
						.registerResult();
				
			} else {
				System.out.println("cannot be placed cause collides");
			}
		}
	}
	
	/**Cancels placing the moving entity by pressing escape.
	 * 
	 * @param event
	 */
	@EventListener (priority = EventPriority.HIGH) //high guarantees that gui controls will be handled afterwards
	public void cancel(KeyReleasedEvent event) {
		if (event.getKey() == Keyboard.KEY_ESCAPE) {
			entity.delete();
			entity = null;
			event.consume();
			EventManager.getInstance().unregisterEventListener(this);
		}
	}
}
