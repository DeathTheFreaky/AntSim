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

	// Gibts was besseres als static wenn mehrere Threads drauf zugreifen
	public static ArrayList<Feedable> fa = new ArrayList<Feedable>();
	public static ArrayList<Ant> ant = new ArrayList<Ant>();
	public static int foodStacks;
	public static Queen queen;
	
	PositionLocator positionLocator;
	
	public Hive(GraphicsEntity graphicsEntity, PhysicsObject physicsObject){
		this(20, graphicsEntity, physicsObject);
	}
	
	//Startbedingungen aendern
	public Hive(int food, GraphicsEntity graphicsEntity, PhysicsObject physicsObject){
		super(graphicsEntity, physicsObject, ObjectType.HIVE);
		
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
