package at.antSim.GTPMapper;

import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsKI.EntityBuilder;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObjectOrientation;

/**Holds data needed to create a {@link PhysicsObject} cylinder from the geometry data of a {@link GraphicsEntity}.
 * 
 * @author Flo
 *
 */
public class GTPCylinder extends GTPObject {
	
	float xLength;
	float yLength;
	float zLength;
	
	float height;
	float radius;
	PhysicsObjectOrientation orientation;
	
	public GTPCylinder(GraphicsEntity graphicsEntity, float scale) {
		
		this.xLength = graphicsEntity.getModel().getRawModel().getxLength() * scale;
		this.yLength = graphicsEntity.getModel().getRawModel().getyLength() * scale;
		this.zLength = graphicsEntity.getModel().getRawModel().getzLength() * scale;
		
//		System.out.println("xLength: " + xLength);
//		System.out.println("yLength: " + yLength);
//		System.out.println("zLength: " + zLength);
		
		//define orientation by max extents per axis
		if (xLength > yLength && xLength > zLength) {
//			System.out.println("xOrientation");
			height = xLength;
			if (yLength > zLength) radius = yLength;
			else radius = zLength;
			orientation = PhysicsObjectOrientation.X;
		} else if (yLength > xLength && yLength > zLength) {
//			System.out.println("yOrientation");
			height = yLength;
			if (xLength > zLength) radius = xLength;
			else radius = zLength;
			orientation = PhysicsObjectOrientation.Y;
		} else if (zLength > xLength && zLength > yLength) {
//			System.out.println("zOrientation");
			height = zLength;
			if (yLength > xLength) radius = yLength;
			else radius = xLength;
			orientation = PhysicsObjectOrientation.Z;
		}
	}

	public float getHeight() {
		return height;
	}

	public float getRadius() {
		return radius;
	}

	public PhysicsObjectOrientation getOrientation() {
		return orientation;
	}

	@Override
	public void createPrimitive(EntityBuilder builder) {
		builder.createCylinder(this);
	}
}
