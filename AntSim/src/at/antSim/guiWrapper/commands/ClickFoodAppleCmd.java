package at.antSim.guiWrapper.commands;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import at.antSim.Globals;
import at.antSim.MainApplication;
import at.antSim.eventSystem.EventManager;
import at.antSim.graphics.graphicsUtils.ModelLoader;
import at.antSim.graphics.graphicsUtils.WorldLoader;
import at.antSim.graphics.models.TexturedModel;
import at.antSim.graphics.terrains.Terrain;
import at.antSim.objectsKI.Entity;
import at.antSim.objectsKI.EntityBuilder;
import at.antSim.objectsKI.EntityBuilderImpl;
import at.antSim.objectsKI.ObjectType;
import at.antSim.objectsPhysic.PhysicsFactorys.DynamicPhysicsObjectFactory;
import at.antSim.objectsPhysic.PhysicsFactorys.GhostPhysicsObjectFactory;
import at.antSim.objectsPhysic.PhysicsFactorys.StaticPhysicsObjectFactory;

/**
 * Created by Alexander on 19.05.2015.
 */
public class ClickFoodAppleCmd extends MovingEntityCmd {
	
	public ClickFoodAppleCmd(EntityBuilder builder, Random random) {
		super(builder, random, "cylinder");
	}

	@Override
	public void createMovingEntity() {
		Entity movingEntity = builder.setFactory(DynamicPhysicsObjectFactory.getInstance())
			.setPosition(new Vector3f(0,0,0)) //position will be set later anyway in main loop according to mouse position
			.setRotation(0, random.nextFloat() * 360, 0)
			.buildGraphicsEntity(type, 1, 10)
			.setType("movingEntity")
			.setObjectType(ObjectType.MOVING)
			.buildPhysicsObject()
			.registerResult();
		MainApplication.getInstance().getMovingEntity().setEntity(movingEntity);
	}
}
