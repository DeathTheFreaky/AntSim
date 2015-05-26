package at.antSim.objectsKI;

import javax.vecmath.Vector3f;

import at.antSim.Globals;
import at.antSim.eventSystem.EventListener;
import at.antSim.eventSystem.EventPriority;
import at.antSim.eventSystem.events.CollisionEvent;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObject;

/**
 * 
 * 
 * TODO:
 *  - http://stackoverflow.com/questions/2790144/avoiding-instanceof-in-java + Reflection
 *    Ueberschreiben verwenden => dadurch muss kein type check gemacht werden
 * 
 * 	  Visitor Pattern
 * 
 * 
 * Physics - Entity inkludieren.
 * 
 * 
 * 
 * @author Martin
 *
 */
public class Ant extends Entity{
	// search 1, food 2, war 3, nothing 0
	private int odorStatus = 0;
	private int hp;
	private int attack;
	//saturate?
	private int hunger;
	private DynamicPhysicsObject physicsObject = null;
	
	//wahrscheinlich eigene Jobklasse => fuer im Bautätige
	// und Worker/Forager
	private String job;
	
	public Ant(GraphicsEntity graphicsEntity,
			PhysicsObject physicsObject) {
		super(graphicsEntity, physicsObject, ObjectType.ANT);
		this.physicsObject = (DynamicPhysicsObject)physicsObject;
	}

	@Override
	public void react(StaticPhysicsObject staticPhysicsObject) {
		System.out.println(" wow wow chill it is a tree");
		
	}

	@Override
	public void react(DynamicPhysicsObject dynamicPhysicsObject) {
			
	}
	@Override
	public void react(GhostPhysicsObject ghostPhysicsObject) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void move(){
		//check Odor stacks
		/*
		 * if (oderStacks > 0){
		 * 		followTrail();
		 * }else{
		 * 		randomMove(x,y)
		 * }
		 */
		
	}
	
	public void attackEnemy(){
		
	}
	
	@EventListener (priority = EventPriority.NORMAL)
	public void decideEvent(CollisionEvent ce){
		
	}
	
	
	public void eat(){
		hunger = hunger+40;
		Hive.foodStacks = Hive.foodStacks-1;
	}

	public int getOdorStatus() {
		return odorStatus;
	}

	public void setOdorStatus(int odorStatus) {
		this.odorStatus = odorStatus;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getHunger() {
		return hunger;
	}

	public void setHunger(int hunger) {
		this.hunger = hunger;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}
}

