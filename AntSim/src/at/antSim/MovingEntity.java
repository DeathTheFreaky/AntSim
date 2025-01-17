package at.antSim;

import java.util.Random;

import javax.vecmath.Quat4f;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import com.sun.javafx.geom.Vec3f;

import at.antSim.eventSystem.EventListener;
import at.antSim.eventSystem.EventManager;
import at.antSim.eventSystem.EventPriority;
import at.antSim.eventSystem.events.CollisionEvent;
import at.antSim.eventSystem.events.KeyReleasedEvent;
import at.antSim.eventSystem.events.MouseButtonReleasedEvent;
import at.antSim.graphics.graphicsUtils.WorldLoader;
import at.antSim.graphics.models.TexturedModel;
import at.antSim.graphics.terrains.Terrain;
import at.antSim.objectsKI.Entity;
import at.antSim.objectsKI.EntityBuilder;
import at.antSim.objectsKI.EntityBuilderImpl;
import at.antSim.objectsKI.Hive;
import at.antSim.objectsKI.ObjectType;
import at.antSim.objectsPhysic.PhysicsFactorys.DynamicPhysicsObjectFactory;
import at.antSim.objectsPhysic.PhysicsFactorys.GhostPhysicsObjectFactory;
import at.antSim.objectsPhysic.PhysicsFactorys.PhysicsObjectFactory;
import at.antSim.objectsPhysic.PhysicsFactorys.StaticPhysicsObjectFactory;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;
import at.antSim.utils.Maths;

/**An Entity moving on the terrain. Will not be rendered if Entity is null.
 * 
 * @author Flo
 *
 */
public class MovingEntity {
	
	private Entity entity;
	private boolean colliding = false;
	
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
			entity.getPhysicsObject().setCollisionFilterGroup(Globals.COL_MOVING);
			short tempFilterMask = 0;
			tempFilterMask = (short) (tempFilterMask | Globals.COL_KINEMATIC | Globals.COL_STATIC); //indicate there would be a collision, but still need to overwrite return value with false in CollisionFilterCallback
			entity.getPhysicsObject().setCollisionFilterMask(tempFilterMask);
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
			if (!colliding) { //not collides
				
				PhysicsObjectFactory placedEntityFactory = null;
				float dropHeight = 0;
				ObjectType objectType = entity.getGraphicsEntity().getModel().getObjectType();
				Hive hive = null;
				
				//detect if new entity is static, dynamic or ghost
				switch (objectType) {
				case ANT:
					placedEntityFactory = DynamicPhysicsObjectFactory.getInstance();
					dropHeight = Terrain.MAX_HEIGHT/4;
					hive = WorldLoader.hive;
					break;
				case ENEMY:
					placedEntityFactory = DynamicPhysicsObjectFactory.getInstance();
					dropHeight = Terrain.MAX_HEIGHT/4;
					break;
				case ENVIRONMENT:
					placedEntityFactory = StaticPhysicsObjectFactory.getInstance();
					break;
				case FOOD:
					placedEntityFactory = StaticPhysicsObjectFactory.getInstance();
					break;
				case PHEROMONE:
					placedEntityFactory = GhostPhysicsObjectFactory.getInstance();
					break;
				case LOCATOR:
					placedEntityFactory = GhostPhysicsObjectFactory.getInstance();
					break;
				default:
					break;
				}
								
//				placedEntityFactory = NoResponsePhysicsObjectFactory.getInstance();
				
				//store data of currently moving entity which has to be dynamic to enable collision detection
				Vector3f placedPosition = Maths.vec3fToSlickUtil(((ReadOnlyPhysicsObject) entity.getPhysicsObject()).getPosition());
				placedPosition.y = placedPosition.y - entity.getGraphicsEntity().getModel().getRawModel().getyLength()/2 * entity.getGraphicsEntity().getScale() + dropHeight;
				String type = entity.getPhysicsObject().getType();
				ReadOnlyPhysicsObject rpo = (ReadOnlyPhysicsObject) entity.getPhysicsObject();
				Quat4f placedQuats = rpo.getRotationQuaternions();
				TexturedModel placedTextureModel = entity.getGraphicsEntity().getModel();
				int placedTextureIndex = entity.getGraphicsEntity().getTextureIndex();
				float placedScale = entity.getGraphicsEntity().getScale() * 2;
				
				//delete moving entity and...
				entity.delete();
				entity = null;
				event.consume();
				EventManager.getInstance().unregisterEventListener(this);
						
				//place entity equaling moving entity, but which can be static, dynamic or ghost
				Entity placedEntity = builder.setFactory(placedEntityFactory)
					.setPosition(placedPosition) //position will be set later anyway in main loop according to mouse position
					.setRotation(placedQuats)
					.buildGraphicsEntity(placedTextureModel.getType(), placedTextureIndex, placedScale)
					.buildPhysicsObject()
					.registerResult();
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
	
	@EventListener(priority = EventPriority.HIGH)
	public void collideEvent(CollisionEvent ce) {
		
		if((ce.getPhyObj1().equals(entity.getPhysicsObject()) && !ce.getPhyObj2().getType().equals("terrain"))
				|| (ce.getPhyObj2().equals(entity.getPhysicsObject()) && !ce.getPhyObj1().getType().equals("terrain"))){
	
			ReadOnlyPhysicsObject ob1 = (ReadOnlyPhysicsObject) ce.getPhyObj1();
			ReadOnlyPhysicsObject ob2 = (ReadOnlyPhysicsObject) ce.getPhyObj2();
			
//			System.out.println("Flags: " + ce.getPhyObj2().getCollisionBody().getCollisionFlags());
//			
//			System.out.println("collision occured between " + ce.getPhyObj1().getType() + "(" + ce.getPhyObj1() + ") and " + ce.getPhyObj2().getType() + "(" + ce.getPhyObj2() + ")");
			
			ce.consume();
			this.colliding = true;
		} 
	}
	
	/**Used to reset colliding to false for each collision cycle before checking for collision events.
	 * 
	 * @param colliding
	 */
	public void setColliding(boolean colliding) {
		this.colliding = colliding;
	}
	
	/**Indicates whether the MovingEntity is colliding with another object.
	 * @return - true if MovingEntity is colliding with another object
	 */
	public boolean isColliding() {
		return colliding;
	}
}
