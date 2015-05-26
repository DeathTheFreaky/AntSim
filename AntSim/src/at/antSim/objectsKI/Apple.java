package at.antSim.objectsKI;

public class Apple implements Foodressource{
	private int foodStacks;
	
	public Apple(){
		foodStacks = 100;
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
