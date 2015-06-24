package at.antSim.objectsKI;

import org.lwjgl.util.vector.Vector3f;

import com.bulletphysics.linearmath.Transform;

import at.antSim.Globals;
import at.antSim.MainApplication;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.PhysicsManager;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.TerrainPhysicsObject;
import at.antSim.objectsPhysic.PhysicsFactorys.GhostPhysicsObjectFactory;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;
import at.antSim.utils.Maths;

/**For food...
 * 
 * @author Flo
 *
 */
public abstract class Food extends Entity implements Foodressource {

	PositionLocator positionLocator;
	int foodStacks;
	
	public Food(GraphicsEntity graphicsEntity, PhysicsObject physicsObject) {
		super(graphicsEntity, physicsObject, ObjectType.FOOD);
				
		//set initial position and size of PositionLocator ghost physics object
		javax.vecmath.Vector3f vecMathPos = ((ReadOnlyPhysicsObject) physicsObject).getPosition();
		vecMathPos.y = vecMathPos.y - Globals.POSITION_LOCATOR_MARGIN;
		
		positionLocator = (PositionLocator) MainApplication.getInstance().getDefaultEntityBuilder().setFactory(GhostPhysicsObjectFactory.getInstance())
				.setPosition(new Vector3f(vecMathPos.x, vecMathPos.y, vecMathPos.z))
				.buildGraphicsEntity("positionLocator", 1, graphicsEntity.getScale() + Globals.POSITION_LOCATOR_MARGIN * 2) 
				.setTarget(this)
				.buildPhysicsObject()
				.registerResult();
	}

	@Override
	public void react(StaticPhysicsObject staticPhysicsObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void react(DynamicPhysicsObject dynamicPhysicsObject) {
		
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
	public int harvest() {
		foodStacks--;
		if (foodStacks <= 0) {
			positionLocator.cancelAnts();
			this.delete();
		}
		return 1;
	}

	@Override
	public int getFoodStacks() {
		return foodStacks;
	}

	@Override
	protected void deleteSpecific() {
		deleteableLocators.add(positionLocator);
	}
}
