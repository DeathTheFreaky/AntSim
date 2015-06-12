package at.antSim.objectsKI;

import at.antSim.Globals;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.basics.PhysicsObject;

public class Apple extends Food implements Foodressource {
	
	public Apple(GraphicsEntity graphicsEntity, PhysicsObject physicsObject){
		super(graphicsEntity, physicsObject);
		foodStacks = Globals.foodResAppleSize;
	}
}
