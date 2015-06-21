package at.antSim.objectsKI;

import at.antSim.Globals;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObject;

public class Worker extends Ant implements Runnable {

	private int threshold;
	
	public Worker(GraphicsEntity graphicsEntity,PhysicsObject physicsObject) {
		super(graphicsEntity, physicsObject);
		hp = Globals.antHp;
		attack = Globals.antAttack;
		threshold = 2;
	}

	@Override
	public void run() {
		while(getHp()>0){
			// Wenn er unter dem threshold ist muss die Ameise essen
			// Falls kein Futter vorhanden ist verliert sie HP
			// bei <1 HP stirbt die Ameise aka die while im Thread läuft nicht mehr
			if(getHunger() < threshold){
				try{
					this.eat();
				}catch(Exception e){
					setHp(getHp()-10);
				}	
			}
			
			//Always get the first one which is not served from Hive.fa list
			Hive.getInstance().getFeedables().get(0).feed();
		}		
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
