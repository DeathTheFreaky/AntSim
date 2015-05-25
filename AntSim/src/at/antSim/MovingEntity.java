package at.antSim;

import org.lwjgl.input.Keyboard;

import at.antSim.eventSystem.EventListener;
import at.antSim.eventSystem.EventManager;
import at.antSim.eventSystem.EventPriority;
import at.antSim.eventSystem.events.KeyReleasedEvent;
import at.antSim.eventSystem.events.MouseButtonPressedEvent;
import at.antSim.objectsKI.Entity;

/**An Entity moving on the terrain. Will not be rendered if Entity is null.
 * 
 * @author Flo
 *
 */
public class MovingEntity {
	
	private Entity entity;
	private boolean moving;
	
	public void setEntity(Entity entity) {
		this.entity = entity;
		if (entity != null) {
			EventManager.getInstance().registerEventListener(this);
		}
	}
	
	//return Entity
	
	public Entity getEntity() {
		return entity;
	}
	
	@EventListener (priority = EventPriority.LOW) //low guarantees that gui controls will be handled first
	public void placeEntityOnTerrain(MouseButtonPressedEvent event){
		if (event.getButton() == 0) { //place entity on terrain if left mouse button is pressed
			//do some collision detection here -> only place entity if it does not overlap with another entity
			entity = null;
			event.consume();
			EventManager.getInstance().unregisterEventListener(this);
			System.out.println("placing entity on terrain");
		}
	}
	
	@EventListener (priority = EventPriority.HIGH) //high guarantees that gui controls will be handled after
	public void cancel(KeyReleasedEvent event) {
		if (event.getKey() == Keyboard.KEY_ESCAPE) {
			entity = null;
			event.consume();
			EventManager.getInstance().unregisterEventListener(this);
		}
	}
}
