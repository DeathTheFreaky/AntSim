package at.antSim.objectsKI;

import at.antSim.Globals;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.basics.PhysicsObject;

public class Squirrel extends Food{
	
	public Squirrel(GraphicsEntity graphicsEntity, PhysicsObject physicsObject){
		super(graphicsEntity, physicsObject);
		foodStacks = Globals.foodResSquirrelSize;
	}
}
