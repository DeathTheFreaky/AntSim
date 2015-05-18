package at.antSim.objectsKI;

public class Larva implements Feedable{

	private int growth = 0;
	
	public void feed(){
		growth++;
		if(growth > 10){
			Hive.fa.remove(this);
			decideTask();
		}
	}
	
	// wahrscheinlich kein return sondern gleich thread starten
	public void decideTask(){
		double rndFactor = Math.random();
		if(rndFactor > 0.25){
			Hive.ant.add(new Forager());
		}else{
			Hive.ant.add(new Worker());
		}
	}
}
