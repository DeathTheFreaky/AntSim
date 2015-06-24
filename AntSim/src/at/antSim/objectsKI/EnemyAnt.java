package at.antSim.objectsKI;

import javax.vecmath.Quat4f;

import at.antSim.Globals;
import at.antSim.MainApplication;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.Movement.MovementManager;
import at.antSim.objectsPhysic.PhysicsFactorys.StaticPhysicsObjectFactory;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.utils.Maths;

public class EnemyAnt extends Enemy {

	public EnemyAnt(GraphicsEntity graphicsEntity, PhysicsObject physicsObject) {
		super(graphicsEntity, physicsObject);
		this.hp = Globals.enemyAntHp;
		this.attack = Globals.enemyAntAttack;
	}
	
	private void spawnDeadAnt() {
		org.lwjgl.util.vector.Vector3f pos = Maths.vec3fToSlickUtil(physicsObject.getPosition());
		pos.y = MainApplication.getInstance().getTerrain().getHeightOfTerrain(pos.x, pos.y);
		Quat4f rot = physicsObject.getRotationQuaternions();
		
		MainApplication.getInstance().getDefaultEntityBuilder().setFactory(StaticPhysicsObjectFactory.getInstance())
				.setPosition(pos) //position will be set later anyway in main loop according to mouse position
				.setRotation(rot)
				.buildGraphicsEntity("deadAnt", 1, 15)
				.setObjectType(ObjectType.FOOD)
				.buildPhysicsObject()
				.registerResult();
	}

	@Override
	protected void deleteSpecific() {
		MovementManager.getInstance().removeAllMovementEntries(physicsObject, true);
		if (deleteAllowed) {
			positionLocator.delete();
			spawnDeadAnt();
			dynamicEntities.remove(this);
			enemies.remove(this);
		} else {
			deleteableLocators.add(positionLocator);
		}
	}
}
