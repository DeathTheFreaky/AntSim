package at.antSim.utils;

import java.util.Comparator;

import at.antSim.graphics.graphicsUtils.TransparentTriangle;
import at.antSim.objectsKI.Entity;

public class TransparentTriangleComparator implements Comparator<Pair<Entity, TransparentTriangle>> {

	@Override
	public int compare(Pair<Entity, TransparentTriangle> o1, Pair<Entity, TransparentTriangle> o2) {
		
		if (o1.getValue().getCameraSquaredDist() > o2.getValue().getCameraSquaredDist())
		{
			return 1;
		}
		else if (o1.getValue().getCameraSquaredDist() == o2.getValue().getCameraSquaredDist())
		{
			return 0;
		}
		else
		{
			return -1;
		}
	}
}
