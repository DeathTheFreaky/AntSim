package at.antSim.guiWrapper.commands;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import at.antSim.MainApplication;
import at.antSim.graphics.graphicsUtils.ModelLoader;
import at.antSim.graphics.graphicsUtils.WorldLoader;
import at.antSim.objectsKI.Entity;
import at.antSim.objectsKI.EntityBuilder;
import at.antSim.objectsKI.ObjectType;
import at.antSim.objectsPhysic.PhysicsFactorys.DynamicPhysicsObjectFactory;
import at.antSim.objectsPhysic.PhysicsFactorys.GhostPhysicsObjectFactory;

/**
 * @author Alexander
 *
 */
public class ClickEnemyAntCmd extends MovingEntityCmd {
	
	public ClickEnemyAntCmd(EntityBuilder builder, Random random) {
		super(builder, random, "forager");
	}

	@Override
	public void createMovingEntity() {
		Entity movingEntity = builder.setFactory(GhostPhysicsObjectFactory.getInstance())
			.setPosition(new Vector3f(0,0,0)) //position will be set later anyway in main loop according to mouse position
			.setRotation(0, random.nextFloat() * 360, 0)
			.buildGraphicsEntity(type, 1, 20)
			.setType("movingEntity")
			.setObjectType(ObjectType.MOVING)
			.buildPhysicsObject()
			.registerResult();
		MainApplication.getInstance().getMovingEntity().setEntity(movingEntity);
	}
}
