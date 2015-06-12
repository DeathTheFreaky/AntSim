package at.antSim.objectsKI;

import at.antSim.Globals;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.basics.PhysicsObject;

public class DeadGrasshopper extends Food {
	
	public DeadGrasshopper(GraphicsEntity graphicsEntity, PhysicsObject physicsObject){
		super(graphicsEntity, physicsObject);
		foodStacks = Globals.foodResGrasshopperSize;
	}
}
