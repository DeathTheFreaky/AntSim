package at.antSim.guiWrapper.commands;

import at.antSim.MainApplication;
import at.antSim.WorldLoader;

/**
 * Created by Alexander on 19.05.2015.
 */
public class ClickFoodAppleCmd implements Command {
	
	@Override
	public void execute() {
		System.out.println("ClickFoodAppleCmd: execute()");
		if (MainApplication.getInstance().getMovingEntity().getGraphicsEntity() == null) {
			//MainApplication.getInstance().getMovingEntity().setGraphicsEntity(WorldLoader.createEntity("dragon", "test"));
		} else {
			//MainApplication.getInstance().getMovingEntity().setGraphicsEntity(null);
		}
	}
}
