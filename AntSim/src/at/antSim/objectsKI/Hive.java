package at.antSim.objectsKI;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import com.bulletphysics.collision.dispatch.CollisionFlags;

import at.antSim.Globals;
import at.antSim.MainApplication;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.TerrainPhysicsObject;
import at.antSim.objectsPhysic.PhysicsFactorys.GhostPhysicsObjectFactory;
import at.antSim.objectsPhysic.PhysicsFactorys.StaticPhysicsObjectFactory;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;

public class Hive extends Entity {
	
	private static Hive INSTANCE = null;

	// Gibts was besseres als static wenn mehrere Threads drauf zugreifen
	private ArrayList<Feedable> fa = new ArrayList<Feedable>();
	private ArrayList<Ant> ants = new ArrayList<Ant>();
	private ArrayList<Egg> eggs = new ArrayList<Egg>();
	private ArrayList<Larva> larvae = new ArrayList<Larva>();
	private int foodStacks;
	private Queen queen;
	
	private int startFood;
	
	PositionLocator positionLocator;
	
	protected Hive(GraphicsEntity graphicsEntity, PhysicsObject physicsObject){
		this(20, graphicsEntity, physicsObject);
	}
	
	//Startbedingungen aendern
	protected Hive(int food, GraphicsEntity graphicsEntity, PhysicsObject physicsObject){
		super(graphicsEntity, physicsObject, ObjectType.HIVE);
		
		startFood = food;
		foodStacks = food;
		//queen = new Queen();
//		fa.add(queen);
		
		//set initial position and size of PositionLocator ghost physics object
		javax.vecmath.Vector3f vecMathPos = ((ReadOnlyPhysicsObject) physicsObject).getPosition();
		vecMathPos.y = vecMathPos.y - Globals.POSITION_LOCATOR_MARGIN;
		
		positionLocator = (PositionLocator) MainApplication.getInstance().getDefaultEntityBuilder().setFactory(GhostPhysicsObjectFactory.getInstance())
				.setPosition(new Vector3f(vecMathPos.x, vecMathPos.y, vecMathPos.z))
				.buildGraphicsEntity("positionLocator", 1, graphicsEntity.getScale() + Globals.POSITION_LOCATOR_MARGIN * 2) 
				.setTarget(this)
				.buildPhysicsObject()
				.registerResult();
		
//		positionLocator.physicsObject.getCollisionBody().setCollisionFlags(positionLocator.physicsObject.getCollisionBody().getCollisionFlags() | CollisionFlags.NO_CONTACT_RESPONSE);
	}
	
	public void addEgg(Egg e){
		fa.add(e);
		eggs.add(e);
	}
	
	public void addLarva(Larva l){
		fa.add(l);
		larvae.add(l);
	}
	
	public void addAnt(Ant a) {
		ants.add(a);
	}
	
	public void removeEgg(Egg e){
		fa.remove(e);
		eggs.remove(e);
	}
	
	public void removeLarva(Larva l){
		fa.remove(l);
		larvae.remove(l);
	}
	
	public void removeAnt(Ant a) {
		ants.remove(a);
	}
	
	public int getAntsNum() {
		return ants.size();
	}
	
	public int getLarvaeNum() {
		return larvae.size();
	}
	
	public int getEggsNum() {
		return eggs.size();
	}
	
	public int getFoodNum() {
		return foodStacks;
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
	
	public void reset() {
		foodStacks = startFood;
		ants.clear();
		eggs.clear();
		fa.clear();
		larvae.clear();
	}

	public ArrayList<Feedable> getFeedables() {
		return fa;
	}
	
	public static Hive getInstance(GraphicsEntity graphicsEntity, PhysicsObject physicsObject) {
		if (INSTANCE == null) {
			INSTANCE = new Hive(graphicsEntity, physicsObject);
		}
		return INSTANCE;
	}
	
	public static Hive getInstance(int food, GraphicsEntity graphicsEntity, PhysicsObject physicsObject) {
		if (INSTANCE == null) {
			INSTANCE = new Hive(food, graphicsEntity, physicsObject);
		}
		return INSTANCE;
	}
	
	public static Hive getInstance() {
		return INSTANCE;
	}
}
