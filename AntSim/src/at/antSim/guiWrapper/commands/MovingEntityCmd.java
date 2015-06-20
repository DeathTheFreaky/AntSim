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

/**
 * @author Alexander
 *
 */
public abstract class MovingEntityCmd implements Command {
	
	EntityBuilder builder;
	Random random;
	String type; //used as key to obtain TexturedModel from the HashMap storing all texturedModels and to set the type of the physicsObject
	
	public MovingEntityCmd(EntityBuilder builder, Random random, String type) {
		this.builder = builder;
		this.random = random;
		this.type = type;
	}
	
	@Override
	public void execute() {
		if (MainApplication.getInstance().getMovingEntity().getEntity() == null) {
			createMovingEntity();
		} else if (!type.equals(MainApplication.getInstance().getMovingEntity().getEntity().getPhysicsObject().getType())) {
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

