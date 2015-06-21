package at.antSim.objectsKI;

public class Egg implements Feedable{
	private int growth = 0;
	
	public void feed(){
		growth++;
		if(growth > 10){
			Hive.getInstance().removeEgg(this);
			Hive.getInstance().addLarva(new Larva());
		}
	}
}
