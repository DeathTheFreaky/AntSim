package at.antSim.utils;

import java.util.ArrayList;
import java.util.Collections;

import at.antSim.graphics.graphicsUtils.TransparentTriangle;
import at.antSim.objectsKI.Entity;

/**Sorts transparent triangles in different thread.
 * 
 * @author Flo
 *
 */
public class TransparentsSorterWorker implements Runnable {
	
	private TransparentTriangleComparator comparator = new TransparentTriangleComparator();
	
	public TransparentsSorterWorker()
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
		
		Collections.sort(transparentTriangles, comparator);
				
		Entity.setTransparentTriangles(transparentTriangles);
	}
}
