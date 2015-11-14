package at.antSim.graphics.graphicsUtils;

import java.util.Comparator;

import at.antSim.objectsKI.Entity;
import at.antSim.utils.Pair;

public class TransparentTriangleComparator implements Comparator<Pair<Entity, TransparentTriangle>> {

	// sort from back to front
	@Override
	public int compare(Pair<Entity, TransparentTriangle> o1, Pair<Entity, TransparentTriangle> o2) {
		
		if (o1.getValue().getCameraDist() > o2.getValue().getCameraDist())
		{
			return -1;
		}
		else if (o1.getValue().getCameraDist() == o2.getValue().getCameraDist())
		{
			return 0;
		}
		else
		{
			return 1;
		}
	}
}
