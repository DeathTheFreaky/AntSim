/**
 * 
 */
package at.antSim.objectsKI;

import at.antSim.Globals;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObject;

/**
 * @author Martin
 *
 */
public class Queen extends Ant implements Feedable {
	
	private int eggLayingCost = 1;
	private int eggLayingSpeed = 1;
	
	public Queen(GraphicsEntity graphicsEntity,PhysicsObject physicsObject, Hive hive) {
		super(graphicsEntity, physicsObject, hive);
		hp = Globals.queenHp;
		attack = Globals.queenAttack;
	}

	public void layEgg(){
		Egg e = new Egg();
		Hive.fa.add(e);
	}
	
	public void feed(){
		setHunger(getHunger()+1);
		if(getHunger() > eggLayingSpeed){
			layEgg();
			setHunger(getHunger()-eggLayingCost);
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
