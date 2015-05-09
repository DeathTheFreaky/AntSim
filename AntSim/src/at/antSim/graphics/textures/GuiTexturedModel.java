package at.antSim.graphics.textures;

import org.lwjgl.util.vector.Vector2f;

import at.antSim.eventSystem.EventListener;
import at.antSim.eventSystem.EventPriority;
import at.antSim.eventSystem.events.MouseButtonPressedEvent;
import at.antSim.eventSystem.events.MouseButtonReleasedEvent;
import at.antSim.graphics.models.RawModel;

/**GuiTexture holds all types of textures which are rendered to the screen as part of the Gui.
 * 
 * @author Flo
 *
 */
public class GuiTexturedModel {

	private RawModel model; //holds geometry data
	private int textureId; //id or "name" of this texture returned by org.newdawn.slick.opengl.Texture.getTextureID(), 0 for gui containers
	private Vector2f position; //need to store center position of the gui quad since gui Texture has no underlying ModelData storing positional data
	private Vector2f scale;
	private String id;
	
	/**Creates a new {@link GuiTexturedModel}.
	 * 
	 * @param texture - id of the texture to use for this gui element
	 * @param position - center of the gui quad in the openGl coordinate system
	 * @param scale - size of the gui quad in relation to the size of the screen
	 * @param model - {@link RawModel} holding the geometry data for this gui element
	 */
	public GuiTexturedModel(int texture, Vector2f position, Vector2f scale, RawModel model, String id) {
		this.textureId = texture;
		this.position = position;
		this.scale = scale;
		this.model = model;
		this.id = id;
	}
	
	public int getTexture() {
		return textureId;
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public Vector2f getScale() {
		return scale;
	}
	
	public RawModel getRawModel() {
		return model;
	}
	
	public String getId() {
		return id;
	}
	
	@EventListener(priority = EventPriority.HIGH)
	public void onMousePress(MouseButtonPressedEvent event){
		
	}
	
	@EventListener
	public void onMouseReleased(MouseButtonReleasedEvent event){
		
	}
}
