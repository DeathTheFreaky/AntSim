package at.antSim.guiWrapper.commands;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import at.antSim.Globals;
import at.antSim.MainApplication;
import at.antSim.graphics.terrains.Terrain;
import at.antSim.objectsKI.Entity;
import at.antSim.objectsKI.EntityBuilder;
import at.antSim.objectsKI.ObjectType;
import at.antSim.objectsPhysic.PhysicsFactorys.GhostPhysicsObjectFactory;

/**
 * @author Alexander
 *
 */
public class ClickFoodAppleCmd extends MovingEntityCmd {
	
	public ClickFoodAppleCmd(EntityBuilder builder, Random random) {
		super(builder, random, "apple");
	}

	@Override
	public void createMovingEntity() {
		Entity movingEntity = builder.setFactory(GhostPhysicsObjectFactory.getInstance())
			.setPosition(new Vector3f(Globals.WORLD_SIZE/2, -Terrain.MAX_HEIGHT * 2, -Globals.WORLD_SIZE/2)) //position will be set later anyway in main loop according to mouse position
			.setRotation(0, random.nextFloat() * 360, 0)
			.buildGraphicsEntity(type, 1, 25)
			.setType("movingEntity")
			.setObjectType(ObjectType.MOVING)
			.buildPhysicsObject()
			.registerResult();
		MainApplication.getInstance().getMovingEntity().setEntity(movingEntity);
	}
}
