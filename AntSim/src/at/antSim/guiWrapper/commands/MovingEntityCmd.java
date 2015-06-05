package at.antSim.guiWrapper.commands;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import at.antSim.Globals;
import at.antSim.MainApplication;
import at.antSim.graphics.graphicsUtils.ModelLoader;
import at.antSim.graphics.models.TexturedModel;
import at.antSim.graphics.terrains.Terrain;
import at.antSim.objectsKI.Entity;
import at.antSim.objectsKI.EntityBuilder;
import at.antSim.objectsPhysic.PhysicsFactorys.DynamicPhysicsObjectFactory;

public abstract class MovingEntityCmd implements Command {
	
	EntityBuilder builder;
	Random random;
	TexturedModel texturedModel; //used as key to check if currently selected model equals newly picked model or not
	
	public MovingEntityCmd(EntityBuilder builder, Random random, TexturedModel texturedModel) {
		this.builder = builder;
		this.random = random;
		this.texturedModel = texturedModel;
	}
	
	@Override
	public void execute() {
		if (MainApplication.getInstance().getMovingEntity().getEntity() == null) {
			createMovingEntity();
		} else if (texturedModel != MainApplication.getInstance().getMovingEntity().getEntity().getGraphicsEntity().getModel()) {
			MainApplication.getInstance().getMovingEntity().setEntity(null);
			createMovingEntity();
		} else {
			MainApplication.getInstance().getMovingEntity().setEntity(null);
		}
	}
	
	/**Moving Entity to be placed on the terrain.
	 * 
	 */
	public abstract void createMovingEntity();
}

