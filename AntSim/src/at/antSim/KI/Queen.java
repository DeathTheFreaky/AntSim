/**
 * 
 */
package at.antSim.KI;

/**
 * @author Martin
 *
 */
public class Queen extends Ant implements Feedable {
	
	private int eggLayingCost = 1;
	private int eggLayingSpeed = 1;
	
	public Queen(){
		setHp(300);
		setAttack(20);
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

}
