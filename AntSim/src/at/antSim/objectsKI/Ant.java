  package at.antSim.objectsKI;

import at.antSim.eventSystem.EventListener;
import at.antSim.eventSystem.EventPriority;
import at.antSim.eventSystem.events.CollisionEvent;


/**
 * 
 * 
 * TODO:
 *  - http://stackoverflow.com/questions/2790144/avoiding-instanceof-in-java + Reflection
 *    Ueberschreiben verwenden => dadurch muss kein type check gemacht werden
 * 
 * 	  Visitor Pattern
 * 
 * 
 * Physics - Entity inkludieren.
 * 
 * 
 * 
 * @author Martin
 *
 */
public class Ant {
	// search 1, food 2, war 3, nothing 0
	private int odorStatus = 0;
	private int hp;
	private int attack;
	//saturate?
	private int hunger;
	
	
	//wahrscheinlich eigene Jobklasse => fuer im Bautätige
	// und Worker/Forager
	private String job;
	
	public void move(){
		//check Odor stacks
		/*
		 * if (oderStacks > 0){
		 * 		followTrail();
		 * }else{
		 * 		randomMove(x,y)
		 * }
		 */
		
	}
	
	public void attackEnemy(){
		
	}
	
	@EventListener (priority = EventPriority.NORMAL)
	public void decideEvent(CollisionEvent ce){
		
	}
	
	
	public void eat(){
		hunger = hunger+40;
		Hive.foodStacks = Hive.foodStacks-1;
	}

	public int getOdorStatus() {
		return odorStatus;
	}

	public void setOdorStatus(int odorStatus) {
		this.odorStatus = odorStatus;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getHunger() {
		return hunger;
	}

	public void setHunger(int hunger) {
		this.hunger = hunger;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}
	
}
