package at.antSim;

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
	}
	
	//return Entity
	
	public Entity getEntity() {
		return entity;
	}
	
	public void placeEntityOnTerrain() {
		setEntity(null);
	}
}
