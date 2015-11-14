package at.antSim.utils;

import java.util.ArrayList;
import java.util.Collections;

import org.lwjgl.util.vector.Matrix4f;

import at.antSim.graphics.graphicsUtils.TransparentTriangle;
import at.antSim.objectsKI.Entity;
import at.antSim.objectsPhysic.basics.ReadOnlyPhysicsObject;

/**Sorts transparent triangles in different thread.
 * 
 * @author Flo
 *
 */
public class TransparentsWorker implements Runnable {
	
	private TransparentTriangleComparator comparator = new TransparentTriangleComparator();
	
	public TransparentsWorker()
	{
		
	}
	
	@Override
	public void run() {
		
		// make copy and sort copy, so renderer still has access to old list and there is no concurrent modification
		ArrayList<Pair<Entity, TransparentTriangle>> transparentTriangles = new ArrayList<Pair<Entity, TransparentTriangle>>();
		
		for (Pair<Entity, TransparentTriangle> t : Entity.getTransparentTriangles())
		{
			TransparentTriangle copiedTriangle = new TransparentTriangle(t.getValue());
			transparentTriangles.add(new Pair<Entity, TransparentTriangle>(t.getKey(), copiedTriangle));
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
