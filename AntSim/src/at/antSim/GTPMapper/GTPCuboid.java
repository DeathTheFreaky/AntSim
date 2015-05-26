package at.antSim.GTPMapper;

import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.basics.PhysicsObject;

/**Holds data needed to create a {@link PhysicsObject} cuboid from the geometry data of a {@link GraphicsEntity}.
 * 
 * @author Flo
 *
 */
public class GTPCuboid extends GTPObject {
	
	float xLength;
	float yLength;
	float zLength;

	public GTPCuboid(GraphicsEntity graphicsEntity, float scale) {
		this.xLength = graphicsEntity.getModel().getRawModel().getxLength() * scale;
		this.yLength = graphicsEntity.getModel().getRawModel().getyLength() * scale;
		this.zLength = graphicsEntity.getModel().getRawModel().getzLength() * scale;
	}

	public float getxLength() {
		return xLength;
	}

	public float getyLength() {
		return yLength;
	}

	public float getzLength() {
		return zLength;
	}
}
