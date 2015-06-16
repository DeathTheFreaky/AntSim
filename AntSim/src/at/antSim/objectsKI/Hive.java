package at.antSim.objectsKI;

import java.util.ArrayList;

import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.TerrainPhysicsObject;
import at.antSim.objectsPhysic.basics.PhysicsObject;

public class Hive extends Entity {

	// Gibts was besseres als static wenn mehrere Threads drauf zugreifen
	public static ArrayList<Feedable> fa = new ArrayList<Feedable>();
	public static ArrayList<Ant> ant = new ArrayList<Ant>();
	public static int foodStacks;
	public static Queen queen;
	
	public Hive(GraphicsEntity graphicsEntity, PhysicsObject physicsObject){
		super(graphicsEntity, physicsObject, ObjectType.HIVE);
		foodStacks = 20;
		//queen = new Queen();
		fa.add(queen);
	}
	
	//Startbedingungen aendern
	public Hive(int food, GraphicsEntity graphicsEntity, PhysicsObject physicsObject){
		super(graphicsEntity, physicsObject, ObjectType.HIVE);
		foodStacks = food;
		//queen = new Queen();
	}
	
	public void addEgg(Egg e){
		fa.add(e);
	}
	
	public void addLarva(Larva l){
		fa.add(l);
	}
	
	public void takeFood(){
		foodStacks--;
	}
	
	public void storeFood(){
		foodStacks++;
	}

	@Override
	public void react(StaticPhysicsObject staticPhysicsObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void react(DynamicPhysicsObject dynamicPhysicsObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void react(GhostPhysicsObject ghostPhysicsObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void react(TerrainPhysicsObject terrainPhysicsObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void deleteSpecific() {
		// TODO Auto-generated method stub
		
	}
}
