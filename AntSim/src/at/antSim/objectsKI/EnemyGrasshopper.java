package at.antSim.objectsKI;

import javax.vecmath.Quat4f;

import at.antSim.Globals;
import at.antSim.MainApplication;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.PhysicsFactorys.StaticPhysicsObjectFactory;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.utils.Maths;

public class EnemyGrasshopper extends Enemy {

	public EnemyGrasshopper(GraphicsEntity graphicsEntity, PhysicsObject physicsObject) {
		super(graphicsEntity, physicsObject);
		this.hp = Globals.enemyGrasshopperHp;
		this.attack = Globals.enemyGrasshopperAttack;
	}
	
	@Override
	protected void deleteSpecific() {
		spawnDeadGrasshopper();
		positionLocator.delete(true);
		dynamicEntities.remove(this);
		enemies.remove(this);
	}
	
	private void spawnDeadGrasshopper() {
		org.lwjgl.util.vector.Vector3f pos = Maths.vec3fToSlickUtil(physicsObject.getPosition());
		Quat4f rot = physicsObject.getRotationQuaternions();
		
		MainApplication.getInstance().getDefaultEntityBuilder().setFactory(StaticPhysicsObjectFactory.getInstance())
				.setPosition(pos) //position will be set later anyway in main loop according to mouse position
				.setRotation(rot)
				.buildGraphicsEntity("deadGrasshopper", 1, 20)
				.setObjectType(ObjectType.FOOD)
				.buildPhysicsObject()
				.registerResult();
	}
}
