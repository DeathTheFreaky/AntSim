package at.antSim.graphics.graphicsUtils;

/**Used by {@link OBJFileLoader} to return geometrical data.
 * 
 * @author Flo
 *
 */
public class OBJLoaderGeometryData {
	
	float furthestPoint;
	float xLength;
	float yLength;
	float zLength;
	
	public OBJLoaderGeometryData(float furthestPoint, float xLength, float yLength, float zLength) {
		this.furthestPoint = furthestPoint;
		this.xLength = xLength;
		this.yLength = yLength;
		this.zLength = zLength;
	}

	public float getFurthestPoint() {
		return furthestPoint;
	}

	public float getxLength() {
		return xLength;
	}

	public float getyLength() {
		return yLength;
	}

	public float getzLength() {
		return zLength;
	}
}
