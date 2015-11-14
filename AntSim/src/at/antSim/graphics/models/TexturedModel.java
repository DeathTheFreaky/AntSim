package at.antSim.graphics.models;

import at.antSim.GTPMapper.PrimitiveType;
import at.antSim.graphics.graphicsUtils.ModelLoader;
import at.antSim.graphics.textures.ModelTexture;
import at.antSim.objectsKI.ObjectType;

/**TexturedModel stores a model's {@link RawModel}, {@link ModelTexture}, {@link PrimitiveType} and {@link ObjectType}.
 * 
 * @author Clemens
 * @see RawModel
 * @see ModelTexture
 */
public class TexturedModel {
	
	private String type;
	private PrimitiveType sphereType;
	private ObjectType objectType;
	private float mass;
	private boolean useTransparency;
	private boolean usesLod = false;
	private float cameraDist = 0;
	
	public TexturedModel(PrimitiveType sphereType, ObjectType objectType, float mass, boolean useTransparency, String type) {
		this.type = type;
		this.sphereType = sphereType;
		this.objectType = objectType;
		this.mass = mass;
		this.useTransparency = useTransparency;
	}

	public RawModel getRawModel() {
		if (usesLod)
		{
			return ModelLoader.getRawModel(type, cameraDist);
		}
		else
		{
			return ModelLoader.getRawModel(type);
		}
	}

	public ModelTexture getTexture() {
		if (usesLod)
		{
			return ModelLoader.getModelTexture(type, cameraDist);
		}
		else
		{
			return ModelLoader.getModelTexture(type);
		}
	}
	
	public PrimitiveType getPrimitiveType() {
		return sphereType;
	}
	
	public ObjectType getObjectType()  {
		return objectType;
	}
	
	public float getMass() {
		return mass;
	}
	
	public String getType() {
		return type;
	}
	
	public void setUsesLod(boolean usesLod)
	{
		this.usesLod = usesLod;
	}
	
	public boolean usesLod()
	{
		return usesLod;
	}
	
	public boolean usesTransparency() {
		return useTransparency;
	}
	
	public void setCameraDist(float cameraDist)
	{
		this.cameraDist = cameraDist;
	}
	
	public float getCameraDist()
	{
		return cameraDist;
	}
}
