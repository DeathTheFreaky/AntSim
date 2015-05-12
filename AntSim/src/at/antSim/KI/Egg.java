package at.antSim.KI;

public class Egg implements Feedable{
	private int growth = 0;
	
	public void feed(){
		growth++;
		if(growth > 10){
			Hive.fa.remove(this);
			Hive.fa.add(new Larva());
		}
	}
	
}
