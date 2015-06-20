package at.antSim.graphics.models;

import at.antSim.GTPMapper.PrimitiveType;
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
	private RawModel rawModel;
	private ModelTexture texture;
	private PrimitiveType sphereType;
	private ObjectType objectType;
	private float mass;
	private boolean useTransparency;
	
	public TexturedModel(RawModel model, ModelTexture texture, PrimitiveType sphereType, ObjectType objectType, float mass, boolean useTransparency, String type) {
		this.type = type;
		this.rawModel = model;
		this.texture = texture;
		this.sphereType = sphereType;
		this.objectType = objectType;
		this.mass = mass;
		this.useTransparency = useTransparency;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
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
	
	public boolean usesTransparency() {
		return useTransparency;
	}
}
