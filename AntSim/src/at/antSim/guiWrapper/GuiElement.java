package at.antSim.guiWrapper;

import org.lwjgl.util.vector.Vector2f;

import at.antSim.eventSystem.EventListener;
import at.antSim.eventSystem.EventPriority;
import at.antSim.eventSystem.events.MouseButtonPressedEvent;
import at.antSim.eventSystem.events.MouseButtonReleasedEvent;
import at.antSim.graphics.models.RawModel;

public abstract class GuiElement {

	private RawModel model; //holds geometry data
	private Vector2f position; //need to store center position of the gui quad since gui Texture has no underlying ModelData storing positional data
	private Vector2f scale;
	private String id;
	private int textureId;
	private GuiContainer parent;
	
	//positional values in pixel
	private Vector2f topLeft;
	private Vector2f width;
	private Vector2f height;
	
	/**Creates a new {@link GuiElement}.
	 * 
	 * 
	 * @param position - center of the gui quad in the openGl coordinate system
	 * @param scale - size of the gui quad in relation to the size of the screen
	 * @param model - {@link RawModel} holding the geometry data for this gui element
	 * @param textureId - id of the texture to use for this gui element
	 * @param id - unique identifier of this gui element stored as a string
	 * @param parent - parent of this gui element
	 */
	public GuiElement(Vector2f position, Vector2f scale, RawModel model, int textureId, String id, GuiContainer parent) {
		this.position = position;
		this.scale = scale;
		this.model = model;
		this.textureId = textureId;
		this.id = id;
		this.parent = parent;
		
		if (parent != null) {
			parent.getChildren().add(this);
		}
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
	
	/**
	 * @return - a number < 0 if no texture shall be drawn for this gui element, or >= 0 if a texture shall be drawn
	 */
	public int getTextureId() {
		return textureId;
	}
	
	public Vector2f getTopLeft() {
		return topLeft;
	}

	public Vector2f getWidth() {
		return width;
	}

	public Vector2f getHeight() {
		return height;
	}

	@EventListener(priority = EventPriority.HIGH)
	public void onMousePress(MouseButtonPressedEvent event){
		
	}
	
	@EventListener
	public void onMouseReleased(MouseButtonReleasedEvent event){
		
	}
}
