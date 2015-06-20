package at.antSim.objectsKI;

import at.antSim.Globals;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.basics.PhysicsObject;

public class EnemyAnt extends Enemy {

	public EnemyAnt(GraphicsEntity graphicsEntity, PhysicsObject physicsObject) {
		super(graphicsEntity, physicsObject);
		this.hp = Globals.enemyAntHp;
		this.attack = Globals.enemyAntAttack;
	}
}
