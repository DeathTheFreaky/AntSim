package at.antSim.KI;

public class Locust implements Foodressource{
	private int foodStacks;
	
	public Locust(){
		foodStacks = 20;
	}

	@Override
	public int harvest() {
		if(foodStacks > 0){
			foodStacks--;
			return 1;
		}
		else
			return 0;
	}

	@Override
	public int getFoodStacks() {
		return foodStacks;
	}

}
