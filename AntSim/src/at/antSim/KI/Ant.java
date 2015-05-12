package at.antSim.KI;

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
		
	}
	
	public void attackEnemy(){
		
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
