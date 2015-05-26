package at.antSim.GTPMapper;

import at.antSim.graphics.entities.GraphicsEntity;

/**Converts geometry data from a graphics object to the geometry data of a physics object.
 * 
 * @author Flo
 *
 */
public class GTPMapper {

	/**Returns the data needed to create a cuboid from a {@link GraphicsEntity}.
	 * 
	 * @param graphicsEntity
	 * @param scale - scale to set initial size of Entity (original extents are 2.0 on "longest" axis -> -1.0 to 1.0)
	 * @return - a {@link GTPCuboid}
	 */
	public static GTPCuboid getCuboid(GraphicsEntity graphicsEntity, float scale) {
		return new GTPCuboid(graphicsEntity, scale);
	}
	
	/**Returns the data needed to create a cone from a {@link GraphicsEntity}.
	 * 
	 * @param graphicsEntity
	 * @param scale - scale to set initial size of Entity (original extents are 2.0 on "longest" axis -> -1.0 to 1.0)
	 * @return - a {@link GTPCone}
	 */
	public static GTPCone getCone(GraphicsEntity graphicsEntity, float scale) {
		return new GTPCone(graphicsEntity, scale);
	}
	
	/**Returns the data needed to create a cylinder from a {@link GraphicsEntity}.
	 * 
	 * @param graphicsEntity
	 * @param scale - scale to set initial size of Entity (original extents are 2.0 on "longest" axis -> -1.0 to 1.0)
	 * @return - a {@link GTPCuboid}
	 */
	public static GTPCylinder getCylinder(GraphicsEntity graphicsEntity, float scale) {
		return new GTPCylinder(graphicsEntity, scale);
	}
	
	/**Returns the data needed to create a sphere from a {@link GraphicsEntity}.
	 * 
	 * @param graphicsEntity
	 * @param scale - scale to set initial size of Entity (original extents are 2.0 on "longest" axis -> -1.0 to 1.0)
	 * @return - a {@link GTPSphere}
	 */
	public static GTPSphere getSphere(GraphicsEntity graphicsEntity, float scale) {
		return new GTPSphere(graphicsEntity, scale);
	}
}
