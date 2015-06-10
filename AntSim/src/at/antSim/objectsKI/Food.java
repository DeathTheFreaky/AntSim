package at.antSim.objectsKI;

import org.lwjgl.util.vector.Vector3f;

import com.bulletphysics.linearmath.Transform;

import at.antSim.Globals;
import at.antSim.MainApplication;
import at.antSim.graphics.entities.GraphicsEntity;
import at.antSim.graphics.graphicsUtils.Maths;
import at.antSim.objectsPhysic.DynamicPhysicsObject;
import at.antSim.objectsPhysic.GhostPhysicsObject;
import at.antSim.objectsPhysic.PhysicsManager;
import at.antSim.objectsPhysic.StaticPhysicsObject;
import at.antSim.objectsPhysic.TerrainPhysicsObject;
import at.antSim.objectsPhysic.PhysicsFactorys.GhostPhysicsObjectFactory;
import at.antSim.objectsPhysic.basics.PhysicsObject;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;

/**For food...
 * 
 * @author Flo
 *
 */
public class Food extends Entity {

	PositionLocator positionLocator;
	
	public Food(GraphicsEntity graphicsEntity, PhysicsObject physicsObject) {
		super(graphicsEntity, physicsObject, ObjectType.FOOD);
		
		//set initial position and size of PositionLocator ghost physics object
		javax.vecmath.Vector3f vecMathPos = ((ReadOnlyPhysicsObject) physicsObject).getPosition();
		Transform position = new Transform();
		position.set(Maths.createTransformationMatrix(new Vector3f(vecMathPos.x, vecMathPos.y, vecMathPos.z), 0, 0, 0));
		position.setRotation(((ReadOnlyPhysicsObject) physicsObject).getRotationQuaternions());
	
		GhostPhysicsObject phyObj = (GhostPhysicsObject) GhostPhysicsObjectFactory.getInstance().createSphere("positionLocator", 1, 
				graphicsEntity.getScale()/2 + Globals.POSITION_LOCATOR_MARGIN, position);
		positionLocator = new PositionLocator(phyObj, this);		
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
	public void delete() {
		PhysicsManager.getInstance().unregisterPhysicsObject(physicsObject);
		PhysicsManager.getInstance().unregisterPhysicsObject(positionLocator.getPhysicsObject());
		physicsObjectTypeMap.remove(this);
		renderingMap.get(graphicsEntity.getModel()).remove(this);
	}
}
