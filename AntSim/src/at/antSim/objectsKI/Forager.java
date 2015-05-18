package at.antSim.objectsKI;

public class Forager extends Ant implements Runnable {
	private int threshold;
	private int foodtransport;
	
	public Forager(){
		setHp(100);
		setAttack(20);
		threshold = 2;
		setOdorStatus(1);
	}
	
	public void foundFood(){
		setOdorStatus(2);
		//extractfood from Enemy
		foodtransport = 1;
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
		}		
	}
}
