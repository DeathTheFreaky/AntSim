package at.antSim.KI;

public class Worker extends Ant implements Runnable {

	private int threshold;
	
	public Worker(){
		setHp(90);
		setAttack(10);
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
			Hive.fa.get(0).feed();
		}		
	}
	

}
