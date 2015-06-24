package at.antSim.objectsKI;

public class Larva implements Feedable{

	private int growth = 0;
	
	public void feed(){
		growth++;
		if(growth > Hive.getInstance().getGrowthSpeed()){
			Hive.getInstance().addRemoveableFeed(this);
			Hive.getInstance().removeLarva(this);
			Hive.getInstance().newAnt();
		}
	}

	@Override
	public String getFeedableType() {
		return "Larva";
	}
	
}
