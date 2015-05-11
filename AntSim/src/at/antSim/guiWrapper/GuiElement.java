package at.antSim.guiWrapper;

import org.lwjgl.util.Point;
import org.lwjgl.util.vector.Vector2f;

import at.antSim.Globals;
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
	private Point topLeft;
	private Point middle;
	private int width;
	private int height;
	
	private HorPositions horPos;
	private int horOffset;
	private VerPositions verPos;
	private int verOffset;
	private int textureWidth;
	private int textureHeight;

	public GuiElement(HorPositions horPos, int horOffset, VerPositions verPos, int verOffset, int textureWidth, int textureHeight, int desiredWidth, int desiredHeight, RawModel model, int textureId, String id, GuiContainer parent) {
		
		this.width = desiredWidth;
		this.height = desiredHeight;
		this.model = model;
		this.textureId = textureId;
		this.id = id;
		this.parent = parent;
		this.horPos = horPos;
		this.horOffset = horOffset;
		this.verPos = verPos;
		this.verOffset = verOffset;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		
		this.scale = new Vector2f(((float) textureWidth/Globals.displayWidth) * ((float) desiredWidth/textureWidth), ((float) textureHeight/Globals.displayHeight) * ((float) desiredHeight/textureHeight));
		//this.scale = new Vector2f((float) desiredWidth/textureWidth*desiredWidth/Globals.displayWidth, (float) desiredHeight/textureHeight*desiredHeight/Globals.displayHeight/9*16);
		
		System.out.println("desired width: " + desiredWidth + ", texture width: " + textureWidth);
		System.out.println("desired height: " + desiredHeight + ", texture height: " + textureHeight);
		System.out.println("scale x: " + scale.x);
		System.out.println("scale y: " + scale.y);
				
		calculatePos();
		
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
	
	public Point getTopLeft() {
		return topLeft;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public void calculatePos() {
		
		int top = 0;
		int left = 0;
		
		int parentWidth;
		int parentHeight;
		Point parentTopLeft;
		
		if (parent != null) {
			parentWidth = parent.getWidth();
			parentHeight = parent.getHeight();
			parentTopLeft = parent.getTopLeft();
		} else {
			parentWidth = Globals.displayWidth;
			parentHeight = Globals.displayHeight;
			parentTopLeft = new Point(0,0);
		}
		
		switch (horPos) {
			case LEFT:
				left = (int) (parentTopLeft.getX() + horOffset); 
				break;
			case CENTER:
				left = (int) (parentTopLeft.getX() + parentWidth/2 - width/2);
				break;
			case RIGHT:
				left = (int) (parentTopLeft.getX() + parentWidth - horOffset - width);
				break;
		}
		
		switch (verPos) {
			case TOP:
				top = (int) (parentTopLeft.getY() + verOffset); 
				break;
			case MIDDLE:
				top = (int) (parentTopLeft.getY() + parentHeight/2 - height/2);
				break;
			case BOTTOM:
				top = (int) (parentTopLeft.getY() + parentHeight - verOffset - height);
				break;
		}
				
		this.topLeft = new Point(left, top);
		this.middle = new Point(left + width/2, top + height/2);
		this.position = new Vector2f((float) middle.getX()/Globals.displayWidth * 2 - 1, ((float) middle.getY()/Globals.displayHeight * 2 - 1) * -1f);
		
		System.out.println("topLeft: " + topLeft.getX() + ", " + topLeft.getY());
		System.out.println("middle: " + middle.getX() + ", " + middle.getY());
		System.out.println("position: " + position.x + ", " + position.y);
	}

	@EventListener(priority = EventPriority.HIGH)
	public void onMousePress(MouseButtonPressedEvent event){
		
	}
	
	@EventListener
	public void onMouseReleased(MouseButtonReleasedEvent event){
		
	}
}
