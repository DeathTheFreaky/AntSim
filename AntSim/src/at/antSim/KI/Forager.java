package at.antSim.KI;

public class Forager extends Ant implements Runnable {
	private int threshold;
	
	public Forager(){
		setHp(100);
		setAttack(20);
		threshold = 2;
		setOdorStatus(1);
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
