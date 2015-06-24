package at.antSim.objectsKI;

public class Egg implements Feedable{
	private int growth = 0;
	
	public void feed(){
		growth++;
		if(growth > Hive.getInstance().getGrowthSpeed()){
			Hive.getInstance().addRemoveableFeed(this);
			Hive.getInstance().removeEgg(this);
			Larva l = new Larva();
			Hive.getInstance().addAddableFeed(l);
			Hive.getInstance().addLarva(l);
			
		}
	}

	@Override
	public String getFeedableType() {
		return "Egg";
	}
}
