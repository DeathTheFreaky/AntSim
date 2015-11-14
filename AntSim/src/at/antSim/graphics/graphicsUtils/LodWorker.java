package at.antSim.graphics.graphicsUtils;

import java.util.HashSet;

import org.lwjgl.util.vector.Vector3f;

import at.antSim.objectsKI.Entity;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;
import at.antSim.utils.Maths;
import at.antSim.utils.Pair;

/**Sorts transparent triangles in different thread.
 * 
 * @author Flo
 *
 */
public class LodWorker implements Runnable {
	
	@Override
	public void run() {
		
		HashSet<Entity> changedLodEntities = Entity.consumeChangedLodEntities();
		
		Vector3f cameraPos = Entity.getLastCamerPosLods();
				
		// if cameraPos changed, all cameraDistances must change too
		if (cameraPos != null)
		{
			changedLodEntities.addAll(Entity.getLodEntities());
		}
				
		for (Entity e : changedLodEntities)
		{	
			ReadOnlyPhysicsObject po = (ReadOnlyPhysicsObject) e.getPhysicsObject();
			Vector3f pos = Maths.vec3fToSlickUtil(po.getPosition());
			Vector3f toCamera = new Vector3f();
			Vector3f.sub(pos, cameraPos, toCamera);						
			e.getGraphicsEntity().getModel().setCameraDist(toCamera.length());
		}
	}
}