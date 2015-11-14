package at.antSim.graphics.graphicsUtils;

import java.util.ArrayList;
import java.util.Collections;

import org.lwjgl.util.vector.Matrix4f;
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
public class TransparentsWorker implements Runnable {
	
	private TransparentTriangleComparator comparator = new TransparentTriangleComparator();
	
	@Override
	public void run() {
				
		// make copy and sort copy, so renderer still has access to old list and there is no concurrent modification
		ArrayList<Pair<Entity, TransparentTriangle>> transparentTriangles = new ArrayList<Pair<Entity, TransparentTriangle>>();
		
		Vector3f cameraPos = Entity.getLastCamerPosTransparents();
		
		for (Pair<Entity, TransparentTriangle> t : Entity.getTransparentTriangles())
		{
			TransparentTriangle copiedTriangle = new TransparentTriangle(t.getValue());
			transparentTriangles.add(new Pair<Entity, TransparentTriangle>(t.getKey(), copiedTriangle));
		}
		
		if (cameraPos != null)
		{
			for (Pair<Entity, TransparentTriangle> triangle : transparentTriangles)
			{
				triangle.getValue().calcCameraDist(cameraPos);
			}
		}
		
		for (Entity entity : Entity.consumeChangedTransparents())
		{
			ReadOnlyPhysicsObject po = (ReadOnlyPhysicsObject) entity.getPhysicsObject();
			
			Matrix4f transformationMatrix = Maths.createTransformationMatrix(Maths.vec3fToSlickUtil(po.getPosition()), 
					po.getRotationDegrees().x, po.getRotationDegrees().y, po.getRotationDegrees().z, 
					entity.getGraphicsEntity().getScale()); //transformation matrix to be applied in the shader program
						
			for (Pair<Entity, TransparentTriangle> triangle : transparentTriangles)
			{
				if (triangle.getKey() == entity)
				{
					triangle.getValue().updateTransform(transformationMatrix);
				}
			}
		}
		
		Collections.sort(transparentTriangles, comparator);
				
		Entity.setTransparentTriangles(transparentTriangles);
	}
}
