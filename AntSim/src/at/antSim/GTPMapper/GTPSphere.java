package at.antSim.GTPMapper;

import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsKI.EntityBuilder;
import at.antSim.objectsPhysic.basics.PhysicsObject;

/**Holds data needed to create a {@link PhysicsObject} sphere from the geometry data of a {@link GraphicsEntity}.
 * 
 * @author Clemens
 *
 */
public class GTPSphere extends GTPObject {
	
	private float radious;

	public GTPSphere(GraphicsEntity graphicsEntity, float scale) {
		this.radious = graphicsEntity.getModel().getRawModel().getFurthestPoint() * scale; 
//		System.out.println(graphicsEntity + " - furthest point: " + graphicsEntity.getModel().getRawModel().getFurthestPoint());
//		System.out.println(graphicsEntity + " - radious: " + radious);
	}

	public float getRadious() {
		return radious;
	}

	@Override
	public void createPrimitive(EntityBuilder builder) {
		builder.createSphere(this);
	}
}
