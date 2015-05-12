package at.antSim.graphics.models;

import at.antSim.graphics.textures.ModelTexture;

/**TexturedModel stores a model's {@link RawModel} and {@link ModelTexture}.
 * 
 * @author Flo
 * @see RawModel
 * @see ModelTexture
 */
public class TexturedModel {
	
	private RawModel rawModel;
	private ModelTexture texture;
	
	public TexturedModel(RawModel model, ModelTexture texture) {
		this.rawModel = model;
		this.texture = texture;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}

}
