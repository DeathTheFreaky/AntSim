package at.antSim.objectsKI;

import at.antSim.Globals;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.basics.PhysicsObject;

public class EnemyGrasshopper extends Enemy {

	public EnemyGrasshopper(GraphicsEntity graphicsEntity, PhysicsObject physicsObject) {
		super(graphicsEntity, physicsObject);
		this.hp = Globals.enemyGrasshopperHp;
		this.attack = Globals.enemyGrasshopperAttack;
	}
}
