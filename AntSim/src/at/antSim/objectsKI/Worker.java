package at.antSim.objectsKI;

import at.antSim.Globals;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObject;

public class Worker extends Ant {

	private int threshold;
	
	public Worker(GraphicsEntity graphicsEntity,PhysicsObject physicsObject) {
		super(graphicsEntity, physicsObject);
		hp = Globals.antHp;
		attack = Globals.antAttack;
		threshold = 2;
	}

	@Override
	public void reactSpecific(StaticPhysicsObject staticPhysicsObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactSpecific(DynamicPhysicsObject dynamicPhysicsObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactSpecific(GhostPhysicsObject ghostPhysicsObject) {
		// TODO Auto-generated method stub
		
	}
	

}
