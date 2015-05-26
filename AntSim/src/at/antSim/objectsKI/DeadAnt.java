package at.antSim.objectsKI;

public class DeadAnt implements Foodressource{
	private int foodStacks;
	
	public DeadAnt(){
		foodStacks = 5;
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
