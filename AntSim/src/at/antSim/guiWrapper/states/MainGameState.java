package at.antSim.guiWrapper.states;

import at.antSim.MainApplication;
import at.antSim.eventSystem.EventManager;
import at.antSim.graphics.graphicsUtils.OpenGLLoader;
import at.antSim.graphics.graphicsUtils.OpenGLTextDrawer;
import at.antSim.graphics.terrains.Terrain;
import at.antSim.guiWrapper.*;
import at.antSim.guiWrapper.commands.*;
import at.antSim.objectsKI.EntityBuilder;
import at.antSim.objectsKI.EntityBuilderImpl;

import org.lwjgl.util.vector.Vector3f;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

/**Gui for the main application.
 * 
 * @author Flo
 *
 */
public class MainGameState extends AbstractGuiState {

	HashMap<String, GuiContainer> statContainers;
	OpenGLTextDrawer textDrawer;
	int textSize;
	
	EntityBuilder builder;
	Terrain terrain;
	Random random;

	GuiContainer playPauseContainer;
	GuiContainer speedContainer;
	GuiContainer speed1;
	GuiContainer speed2;
	GuiContainer speed3;
	GuiContainer speed4;
	GuiContainer foodButton;
	GuiContainer foodContainer;
	GuiContainer enemyButton;
	GuiContainer enemyContainer;
	GuiContainer foodInsectSubContainer;
	GuiImage playPause;
	GuiImage speed1Img;
	GuiImage speed2Img;
	GuiImage speed3Img;
	GuiImage speed4Img;
	
	//texture ids for exchanging
	int pauseTexId;
	int playTexId;
	int speedFilledTexId;
	int speedEmptyTexId;
	int foodButtonInactiveTexId;
	int foodButtonActiveTexId;
	int foodContainerTexId;
	int foodContainerSubTexId;
	int enemyButtonInactiveTexId;
	int enemyButtonActiveTexId;
	
	//used for toggling containers when pressing them twice
	GuiContainer visibleControlContainer = null;
	boolean foodSubContainerDisplayed = false;
	
	public MainGameState(OpenGLLoader loader, String name) {
		super(loader, name);
		
		builder = new EntityBuilderImpl();
		random = new Random();
	}

	@Override
	public void initializeState(String... args) {

		pauseTexId = wrapper.getGuiTexture("controlsBar/pause").getTextureID();
		playTexId = wrapper.getGuiTexture("controlsBar/play").getTextureID();
		speedFilledTexId = wrapper.getGuiTexture("controlsBar/speed_filled").getTextureID();
		speedEmptyTexId = wrapper.getGuiTexture("controlsBar/speed_empty").getTextureID();
		foodButtonInactiveTexId = wrapper.getGuiTexture("controlsBar/foodButton").getTextureID();
		foodButtonActiveTexId = wrapper.getGuiTexture("controlsBar/foodButtonActive").getTextureID();
		foodContainerTexId = wrapper.getGuiTexture("controlsBar/foodContainer").getTextureID();
		foodContainerSubTexId = wrapper.getGuiTexture("controlsBar/foodContainerSub").getTextureID();
		enemyButtonInactiveTexId = wrapper.getGuiTexture("controlsBar/foodButton").getTextureID();
		enemyButtonActiveTexId = wrapper.getGuiTexture("controlsBar/foodButtonActive").getTextureID();
		
		statContainers = new HashMap<>();
		GuiContainer statusContainer = new GuiContainer("statusContainer", loader, null, null, wrapper.getGuiTexture("white"), 230, 80,
				HorReference.PARENT, HorPositions.LEFT, 0, VerReference.PARENT, VerPositions.TOP, 0, 0.5f, new Vector3f(0.9f, 0.9f, 0.9f), 1f);
		
		//nur einmal, außer für andere Fonts
		textDrawer = new OpenGLTextDrawer(loader, wrapper.getGuiTexture("font"));
		
		List<String> statuses = new LinkedList<>();
		
		statuses.add("Population");
		statuses.add("Food");
		statuses.add("Eggs");
		statuses.add("Larvae");
		
		textSize = 12;
		
		int idx = 0;
		for (String str : statuses) {
			
			GuiContainer statusRowContainer;
			
			if (idx == 0) {
				statusRowContainer = new GuiContainer("statusRowContainer", loader, statusContainer, null,  wrapper.getGuiTexture("white"), statusContainer.getWidth(), 20,
						HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.TOP, 0, 1f, new Vector3f(0, 0, 0), 0f);
			} else {
				statusRowContainer = new GuiContainer("statusRowContainer", loader, statusContainer, null,  wrapper.getGuiTexture("white"), statusContainer.getWidth(), 20,
						HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 0, 1f, new Vector3f(0, 0, 0), 0f);
			}
			GuiContainer statusLabelContainer = new GuiContainer("statusLabelContainer", loader, statusRowContainer, null,  wrapper.getGuiTexture("white"), 140,
					statusRowContainer.getHeight(), HorReference.PARENT, HorPositions.LEFT, 0, VerReference.SIBLING, VerPositions.TOP, 0, 1f, new Vector3f(0, 0, 1), 0f);
			GuiText statusLabelText = new GuiText("statusLabelText" + str, textDrawer.createTextQuad(str), statusLabelContainer, null, textSize,
					HorReference.PARENT, HorPositions.LEFT, 5, VerReference.PARENT, VerPositions.MIDDLE, 5);
			GuiContainer statusValueContainer = new GuiContainer("statusValueContainer", loader, statusRowContainer, null,  wrapper.getGuiTexture("white"), 90,
					statusRowContainer.getHeight(), HorReference.PARENT, HorPositions.RIGHT, 0, VerReference.SIBLING, VerPositions.TOP, 0, 1f, new Vector3f(0, 1, 0), 0f);
			GuiText statusValue = new GuiText("statusValue" + str, textDrawer.createTextQuad("0"), statusValueContainer, null, textSize,
					HorReference.PARENT, HorPositions.RIGHT, 5, VerReference.PARENT, VerPositions.MIDDLE, 0);
			
			statContainers.put(str, statusValueContainer);
			idx++;
		}

		// Control Bar
		// Commands
		Command togglePlayCmd = new TogglePlayCmd(this);
		Command toggleSpeed1Cmd = new ToggleSpeedCmd(0.5f, this, 1);
		Command toggleSpeed2Cmd = new ToggleSpeedCmd(1f, this, 2);
		Command toggleSpeed3Cmd = new ToggleSpeedCmd(2f, this, 3);
		Command toggleSpeed4Cmd = new ToggleSpeedCmd(4f, this, 4);
		Command toggleFoodCmd = new ToggleFoodCmd(this);
		Command toggleFoodSubCmd = new ToggleFoodSubCmd(this);
		Command clickFoodAppleCmd = new ClickFoodAppleCmd(builder, random);
		Command clickFoodSquirrelCmd = new ClickFoodSquirrelCmd(builder, random);
		Command clickFoodAntCmd = new ClickFoodAntCmd(builder, random);
		Command clickFoodGrasshopperCmd = new ClickFoodGrasshopperCmd(builder, random);
		Command toggleEnemyCmd = new ToggleEnemyCmd(this);
		Command clickEnemyAntCmd = new ClickEnemyAntCmd(builder, random);
		Command clickEnemyGrasshopperCmd = new ClickEnemyGrasshopperCmd(builder, random);

		// Controls bar
		GuiContainer controlsBar = new GuiContainer("controlsBar", loader, null, null, null, 251, 35,
				HorReference.PARENT, HorPositions.LEFT, 0, VerReference.PARENT, VerPositions.BOTTOM, -1, 0f, new Vector3f(1.0f, 1.0f, 1.0f), 0.5f);

		//speed controls
		playPauseContainer = new GuiContainer("playPauseContainer", loader, controlsBar, togglePlayCmd, wrapper.getGuiTexture("controlsBar/playPause"), 35, 35,
				HorReference.PARENT, HorPositions.LEFT, 0, VerReference.PARENT, VerPositions.BOTTOM, 0, 0f, new Vector3f(0f, 0f, 0f), 0f);
		speedContainer = new GuiContainer("speedContainer", loader, controlsBar, null, wrapper.getGuiTexture("controlsBar/speedContainer"), 75, 35,
				HorReference.SIBLING, HorPositions.RIGHT_OF, -1, VerReference.PARENT, VerPositions.BOTTOM, 0, 0f, new Vector3f(1f, 0f, 0f), 0f);
		speed1 = new GuiContainer("speed1", loader, speedContainer, toggleSpeed1Cmd, null, 17, 26,
				HorReference.PARENT, HorPositions.LEFT, 4, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0.2f, 0, 0), 0f);
		speed2 = new GuiContainer("speed2", loader, speedContainer, toggleSpeed2Cmd, null, 17, 26,
				HorReference.SIBLING, HorPositions.RIGHT_OF, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0.2f, 0), 0f);
		speed3 = new GuiContainer("speed3", loader, speedContainer, toggleSpeed3Cmd, null, 17, 26,
				HorReference.SIBLING, HorPositions.RIGHT_OF, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0.2f), 0f);
		speed4 = new GuiContainer("speed4", loader, speedContainer, toggleSpeed4Cmd, null, 17, 26,
				HorReference.SIBLING, HorPositions.RIGHT_OF, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0.2f, 0, 0), 0f);
		
		//food containers
		foodButton = new GuiContainer("foodButton", loader, controlsBar, toggleFoodCmd, wrapper.getGuiTexture("controlsBar/foodButton"), 70, 35,
				HorReference.SIBLING, HorPositions.RIGHT_OF, -1, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(1, 0, 0), 0f);
		GuiImage foodTextImg = new GuiImage("foodTextImg", loader, foodButton, null, wrapper.getGuiTexture("controlsBar/foodText"), 70, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0), 0f);
		foodContainer = new GuiContainer("foodContainer", loader, foodButton, null, wrapper.getGuiTexture("controlsBar/foodContainer"), 70, 105,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.ABOVE, -1, 0f, new Vector3f(0, 1, 0), 0f);
		GuiContainer foodApple = new GuiContainer("foodApple", loader, foodContainer, clickFoodAppleCmd, null, 70, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BOTTOM, 0, 0f, new Vector3f(0.2f, 0, 0), 0f);
		GuiImage appleImg = new GuiImage("appleImg", loader, foodApple, null, wrapper.getGuiTexture("controlsBar/appleButton"), 70, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0), 0f);
		GuiContainer foodSquirrel = new GuiContainer("foodSquirrel", loader, foodContainer, clickFoodSquirrelCmd, null, 70, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.ABOVE, 0, 0f, new Vector3f(0.4f, 0, 0), 0f);
		GuiImage squirrelImg = new GuiImage("squirrelImg", loader, foodSquirrel, null, wrapper.getGuiTexture("controlsBar/squirrelButton"), 70, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0), 0f);
		GuiContainer foodInsect = new GuiContainer("foodInsect", loader, foodContainer, toggleFoodSubCmd, null, 70, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.ABOVE, 0, 0f, new Vector3f(0.6f, 0, 0), 0f);
		GuiImage insectTextImg = new GuiImage("insectTextImg", loader, foodInsect, null, wrapper.getGuiTexture("controlsBar/insectText"), 70, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0), 0f);
		
		foodInsectSubContainer = new GuiContainer("foodSubContainer", loader, foodInsect, null, wrapper.getGuiTexture("controlsBar/foodSub"), 140, 35,
				HorReference.PARENT, HorPositions.RIGHT_OF, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0.8f, 0, 0), 0f);
		GuiContainer foodAnt = new GuiContainer("foodAnt", loader, foodInsectSubContainer, clickFoodAntCmd, null, 70, 35,
				HorReference.PARENT, HorPositions.LEFT, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0.2f, 0), 0f);
		GuiImage antImg = new GuiImage("antImg", loader, foodAnt, null, wrapper.getGuiTexture("controlsBar/antButton"), 70, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0), 0f);
		GuiContainer foodGrasshopper = new GuiContainer("foodGrasshopper", loader, foodInsectSubContainer, clickFoodGrasshopperCmd, null, 70, 35,
				HorReference.SIBLING, HorPositions.RIGHT_OF, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0.4f, 0), 0f);
		GuiImage grasshopperImg = new GuiImage("grasshopperImg", loader, foodGrasshopper, null, wrapper.getGuiTexture("controlsBar/grasshopperButton"), 70, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0), 0f);
		enemyButton = new GuiContainer("enemyButton", loader, controlsBar, toggleEnemyCmd, wrapper.getGuiTexture("controlsBar/foodButton"), 70, 35,
				HorReference.SIBLING, HorPositions.RIGHT_OF, -1, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0.2f), 0f);
		GuiImage enemyTextImg = new GuiImage("enemyTextImg", loader, enemyButton, null, wrapper.getGuiTexture("controlsBar/enemyText"), 70, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0), 0f);
		enemyContainer = new GuiContainer("enemyContainer", loader, enemyButton, null, wrapper.getGuiTexture("controlsBar/enemyContainer"), 70, 70,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.ABOVE, 0, 0f, new Vector3f(0, 0, 0.4f), 0f);
		GuiContainer enemyAnt = new GuiContainer("enemyAnt", loader, enemyContainer, clickEnemyAntCmd, null, 70, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.BOTTOM, 0, 0f, new Vector3f(0, 0, 0.6f), 0f);
		GuiImage enemyAntImg = new GuiImage("enemyAntImg", loader, enemyAnt, null, wrapper.getGuiTexture("controlsBar/antButton"), 70, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0), 0f);
		GuiContainer enemyGrasshopper = new GuiContainer("enemyGrasshopper", loader, enemyContainer, clickEnemyGrasshopperCmd, null, 70, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.ABOVE, 0, 0f, new Vector3f(0, 0, 0.8f), 0f);
		GuiImage enemyGrasshopperImg = new GuiImage("enemyGrasshopperImg", loader, enemyGrasshopper, null, wrapper.getGuiTexture("controlsBar/grasshopperButton"), 70, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0), 0f);
		
		// default hidings
		foodContainer.setDisabled(true);
		enemyContainer.setDisabled(true);
		
		// Images old (working properly
		//GuiImage playPauseContainerImg = new GuiImage("playPauseContainer", playPauseContainer, null, standardQuad, wrapper.getGuiTexture("controls/playPause"), 35, 35,
				//HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
		playPause = new GuiImage("playPause", loader, playPauseContainer, null, wrapper.getGuiTexture("controlsBar/pause"), 30, 30,
				HorReference.PARENT, HorPositions.LEFT, 1, VerReference.PARENT, VerPositions.BOTTOM, 2);
		speed1Img = new GuiImage("speed1Img", loader, speed1, null, wrapper.getGuiTexture("controlsBar/speed_filled"), 15, 26,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.TOP, 1);
		speed2Img = new GuiImage("speed2Img", loader, speed2, null, wrapper.getGuiTexture("controlsBar/speed_filled"), 15, 26,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.TOP, 1);
		speed3Img = new GuiImage("speed3Img", loader, speed3, null, wrapper.getGuiTexture("controlsBar/speed_empty"), 15, 26,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.TOP, 1);
		speed4Img = new GuiImage("speed4Img", loader, speed4, null, wrapper.getGuiTexture("controlsBar/speed_empty"), 15, 26,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.TOP, 1);

		// images new
//		playPause = new GuiImage("playPause", playPauseContainer, null, standardQuad, wrapper.getGuiTexture("controls/pause"), 32, 32,
//				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
//		speed1Img = new GuiImage("speed1Img", speed1, null, standardQuad, wrapper.getGuiTexture("controls/speed_filled"), 15, 26,
//				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
//		speed2Img = new GuiImage("speed2Img", speed2, null, standardQuad, wrapper.getGuiTexture("controls/speed_filled"), 15, 26,
//				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
//		speed3Img = new GuiImage("speed3Img", speed3, null, standardQuad, wrapper.getGuiTexture("controls/speed_empty"), 15, 26,
//				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
//		speed4Img = new GuiImage("speed4Img", speed4, null, standardQuad, wrapper.getGuiTexture("controls/speed_empty"), 15, 26,
//				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);

		//updateSpeedButtons(2);
		
//		playPauseContainer.addChild(playPausePause);
//		speed1Img = new GuiImage("speed1Img", speed1, null, standardQuad, wrapper.getGuiTexture("controls/play_filled_small"), 26, 32,
//				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
//		speed1.addChild(speed1Img);
//		speed2.addChild(speed2Filled);
//		speed3.addChild(speed3Unfilled);
//		speed4.addChild(speed4Unfilled);
		
		// Registering listeners
		EventManager.getInstance().registerEventListener(playPauseContainer);
		EventManager.getInstance().registerEventListener(speed1);
		EventManager.getInstance().registerEventListener(speed2);
		EventManager.getInstance().registerEventListener(speed3);
		EventManager.getInstance().registerEventListener(speed4);
		EventManager.getInstance().registerEventListener(foodButton);
		EventManager.getInstance().registerEventListener(enemyButton);
		EventManager.getInstance().registerEventListener(foodApple);
		EventManager.getInstance().registerEventListener(foodSquirrel);
		EventManager.getInstance().registerEventListener(foodInsect);
		EventManager.getInstance().registerEventListener(foodAnt);
		EventManager.getInstance().registerEventListener(foodGrasshopper);
		EventManager.getInstance().registerEventListener(enemyAnt);
		EventManager.getInstance().registerEventListener(enemyGrasshopper);
		
		state.addContainer(statusContainer);
		state.addContainer(controlsBar);
		
		GuiWrapper.getInstance().addState(state);	
	}

	@Override
	public void resetState() {
		hideFoodContainer();
		hideEnemyContainer();
	}
	
	public void updateStatus() {
		for (Entry<String, Integer> entry : MainApplication.getInstance().getStats().entrySet()) {
			statContainers.get(entry.getKey()).removeChildren();
			GuiText statusValue = new GuiText("statusValue" + entry.getKey(), textDrawer.createTextQuad(String.valueOf(entry.getValue()), true), statContainers.get(entry.getKey()), null, textSize,
					HorReference.PARENT, HorPositions.RIGHT, 5, VerReference.PARENT, VerPositions.MIDDLE, 0);
		}
	}

	public void updatePlayButton() {
		visibleControlContainer = null;
		if (!MainApplication.getInstance().isPaused()) {
			setPlayButton();
		} else {
			setPauseButton();
		}
	}

	public void setPauseButton() {
		playPause.setTextureId(pauseTexId);
	}

	public void setPlayButton() {
		playPause.setTextureId(playTexId);
	}

	public void updateSpeedButtons(int numberOfButtons) {
		visibleControlContainer = null;
		speed1Img.setTextureId(speedFilledTexId);
		if (numberOfButtons >= 2) {
			speed2Img.setTextureId(speedFilledTexId);
		} else {
			speed2Img.setTextureId(speedEmptyTexId);
		}
		if (numberOfButtons >= 3) {
			speed3Img.setTextureId(speedFilledTexId);
		} else {
			speed3Img.setTextureId(speedEmptyTexId);
		}
		if (numberOfButtons >= 4) {
			speed4Img.setTextureId(speedFilledTexId);
		} else {
			speed4Img.setTextureId(speedEmptyTexId);
		}
	}

	public void showFoodContainer() {
		foodContainer.setDisabled(false);
		foodButton.setTextureId(foodButtonActiveTexId);
	}

	public void hideFoodContainer() {
		foodContainer.setDisabled(true);
		foodButton.setTextureId(foodButtonInactiveTexId);
	}
	
	public void toggleFoodContainer() {
		toggleContainer(foodContainer);
		if (visibleControlContainer == foodContainer) {
			foodButton.setTextureId(foodButtonActiveTexId);
		} else {
			foodButton.setTextureId(foodButtonInactiveTexId);
		}
	}

	public void showEnemyContainer() {
		enemyContainer.setDisabled(false);
		enemyButton.setTextureId(enemyButtonActiveTexId);
	}

	public void hideEnemyContainer() {
		enemyContainer.setDisabled(true);
		enemyButton.setTextureId(enemyButtonInactiveTexId);
	}
	
	public void toggleEnemyContainer() {
		toggleContainer(enemyContainer);
		if (visibleControlContainer == enemyContainer) {
			enemyButton.setTextureId(enemyButtonActiveTexId);
		} else {
			enemyButton.setTextureId(enemyButtonInactiveTexId);
		}
	}
	
	public void toggleContainer(GuiContainer container) {
		if (visibleControlContainer == container) {
			container.setDisabled(true);
			visibleControlContainer = null;
		} else if (visibleControlContainer == null){
			container.setDisabled(false);
			visibleControlContainer = container;
			if (container == foodContainer) {
				hideFoodSubContainer();
			}
		} else {
			visibleControlContainer.setDisabled(true);
			container.setDisabled(false);
			visibleControlContainer = container;
			if (container == foodContainer) {
				hideFoodSubContainer();
			}
		}
	}
	
	public void showFoodSubContainer() {
		foodContainer.setDisabled(false);
		foodInsectSubContainer.setDisabled(false);
		foodSubContainerDisplayed = true;
		foodContainer.setTextureId(foodContainerSubTexId);
		foodButton.setTextureId(foodButtonActiveTexId);
	}
	
	public void hideFoodSubContainer() {
		foodContainer.setDisabled(false);
		foodInsectSubContainer.setDisabled(true);
		foodSubContainerDisplayed = false;
		foodContainer.setTextureId(foodContainerTexId);
		foodButton.setTextureId(foodButtonActiveTexId);
	}

	public void toggleFoodSubContainer() {
		if (foodSubContainerDisplayed) {
			hideFoodSubContainer();
		} else {
			showFoodSubContainer();
		}
	}
	
	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	};
}
