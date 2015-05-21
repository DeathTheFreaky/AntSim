package at.antSim;

import at.antSim.graphics.entities.GraphicsEntity;

/**An Entity moving on the terrain. Will not be rendered if Entity is null.
 * 
 * @author Flo
 *
 */
public class MovingEntity {
	
	private GraphicsEntity graphicModel;
	private boolean moving;
	
	public void setGraphicsEntity(GraphicsEntity graphicModel) {
		this.graphicModel = graphicModel;
	}
	
	//return Entity
	
	public GraphicsEntity getGraphicsEntity() {
		return graphicModel;
	}
	
	public void placeEntityOnTerrain() {
		setGraphicsEntity(null);
	}
}
