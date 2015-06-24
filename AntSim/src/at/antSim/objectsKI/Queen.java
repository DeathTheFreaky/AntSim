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
public class Queen  implements Feedable {
	private float hp;
	private int hunger;
	private int eggLayingCost = 1;
	private int eggLayingSpeed = 1;
	
	public Queen() {
		hp = Globals.queenHp;
	}

	public void layEgg(){
		Egg e = new Egg();
		Hive.getInstance().addAddableFeed(e);
		Hive.getInstance().addEgg(e);
	}
	
	public void feed(){
		hunger++;
		if(hunger > Hive.getInstance().getGrowthSpeed()){
			layEgg();
			hunger = hunger-eggLayingCost;
		}
	}

	public float getHp() {
		return hp;
	}

	public void setHp(float hp) {
		this.hp = hp;
	}

	public int getHunger() {
		return hunger;
	}

	public void setHunger(int hunger) {
		this.hunger = hunger;
	}

	public int getEggLayingCost() {
		return eggLayingCost;
	}

	public void setEggLayingCost(int eggLayingCost) {
		this.eggLayingCost = eggLayingCost;
	}

	public int getEggLayingSpeed() {
		return eggLayingSpeed;
	}

	public void setEggLayingSpeed(int eggLayingSpeed) {
		this.eggLayingSpeed = eggLayingSpeed;
	}

	@Override
	public String getFeedableType() {
		return "Queen";
	}
}
