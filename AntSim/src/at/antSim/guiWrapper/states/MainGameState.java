package at.antSim.guiWrapper.states;

import at.antSim.MainApplication;
import at.antSim.eventSystem.EventManager;
import at.antSim.graphics.graphicsUtils.Loader;
import at.antSim.graphics.graphicsUtils.OpenGLTextDrawer;
import at.antSim.guiWrapper.*;
import at.antSim.guiWrapper.commands.*;
import org.lwjgl.util.vector.Vector3f;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

/**Gui for the main application.
 * 
 * @author Flo
 *
 */
public class MainGameState extends AbstractGuiState {

	HashMap<String, GuiContainer> statContainers;
	OpenGLTextDrawer textDrawer;
	int textSize;

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
	int playFilledTexId;
	int playUnfilledTexId;
	int pauseTexId;
	int speedFilledTexId;
	int speedEmptyTexId;
	
	//used for toggling containers when pressing them twice
	GuiContainer visibleControlContainer = null;
	boolean foodSubContainerDisplayed = false;
	
	public MainGameState(Loader loader, String name) {
		super(loader, name);
	}

	@Override
	public void initializeState(String... args) {
				
		playFilledTexId = wrapper.getGuiTexture("controls/play_filled_small").getTextureId(); 
		playUnfilledTexId = wrapper.getGuiTexture("controls/play_unfilled_small").getTextureId(); 
		pauseTexId = wrapper.getGuiTexture("controls/pause").getTextureId();
		speedFilledTexId = wrapper.getGuiTexture("controls/speed_filled").getTextureId();
		speedEmptyTexId = wrapper.getGuiTexture("controls/speed_empty").getTextureId();
		
		statContainers = new HashMap<>();
		GuiContainer statusContainer = new GuiContainer("statusContainer", null, null, standardQuad, wrapper.getGuiTexture("white"), 230, 80,
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
				statusRowContainer = new GuiContainer("statusRowContainer", statusContainer, null, standardQuad,  wrapper.getGuiTexture("white"), statusContainer.getWidth(), 20,
						HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.TOP, 0, 1f, new Vector3f(0, 0, 0), 0f);
			} else {
				statusRowContainer = new GuiContainer("statusRowContainer", statusContainer, null, standardQuad,  wrapper.getGuiTexture("white"), statusContainer.getWidth(), 20,
						HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 0, 1f, new Vector3f(0, 0, 0), 0f);
			}
			GuiContainer statusLabelContainer = new GuiContainer("statusLabelContainer", statusRowContainer, null, standardQuad,  wrapper.getGuiTexture("white"), 140,
					statusRowContainer.getHeight(), HorReference.PARENT, HorPositions.LEFT, 0, VerReference.SIBLING, VerPositions.TOP, 0, 1f, new Vector3f(0, 0, 1), 0f);
			GuiText statusLabelText = new GuiText("statusLabelText" + str, textDrawer.createTextQuad(str), statusLabelContainer, null, textSize,
					HorReference.PARENT, HorPositions.LEFT, 5, VerReference.PARENT, VerPositions.MIDDLE, 5);
			GuiContainer statusValueContainer = new GuiContainer("statusValueContainer", statusRowContainer, null, standardQuad,  wrapper.getGuiTexture("white"), 90,
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
		Command clickFoodAppleCmd = new ClickFoodAppleCmd();
		Command clickFoodSquirrelCmd = new ClickFoodSquirrelCmd();
		Command clickFoodAntCmd = new ClickFoodAntCmd();
		Command clickFoodGrasshopperCmd = new ClickFoodGrasshopperCmd();
		Command toggleEnemyCmd = new ToggleEnemyCmd(this);
		Command clickEnemyAntCmd = new ClickEnemyAntCmd();
		Command clickEnemyGrasshopperCmd = new ClickEnemyGrasshopperCmd();

		//Controlsbar
		GuiContainer controlsBar = new GuiContainer("controlsBar", null, null, standardQuad, wrapper.getGuiTexture("white"), 281, 35,
				HorReference.PARENT, HorPositions.LEFT, 0, VerReference.PARENT, VerPositions.BOTTOM, -1, 0f, new Vector3f(1.0f, 1.0f, 1.0f), 1f);

		//speed controls
		playPauseContainer = new GuiContainer("playPauseContainer", controlsBar, togglePlayCmd, standardQuad, wrapper.getGuiTexture("white"), 35, 35,
				HorReference.PARENT, HorPositions.LEFT, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0f, 0f, 0f), 0f);
		speedContainer = new GuiContainer("speedContainer", controlsBar, null, standardQuad, wrapper.getGuiTexture("test2"), 75, 35,
				HorReference.SIBLING, HorPositions.RIGHT_OF, 0, VerReference.PARENT, VerPositions.BOTTOM, 0, 0f, new Vector3f(1f, 0f, 0f), 0f);
		speed1 = new GuiContainer("speed1", speedContainer, toggleSpeed1Cmd, standardQuad, wrapper.getGuiTexture("white"), 17, 26,
				HorReference.PARENT, HorPositions.LEFT, 4, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0.2f, 0, 0), 0.5f);
		speed2 = new GuiContainer("speed2", speedContainer, toggleSpeed2Cmd, standardQuad, wrapper.getGuiTexture("white"), 17, 26,
				HorReference.SIBLING, HorPositions.RIGHT_OF, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0.2f, 0), 0.5f);
		speed3 = new GuiContainer("speed3", speedContainer, toggleSpeed3Cmd, standardQuad, wrapper.getGuiTexture("white"), 17, 26,
				HorReference.SIBLING, HorPositions.RIGHT_OF, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0.2f), 0.5f);
		speed4 = new GuiContainer("speed4", speedContainer, toggleSpeed4Cmd, standardQuad, wrapper.getGuiTexture("white"), 17, 26,
				HorReference.SIBLING, HorPositions.RIGHT_OF, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0.2f, 0, 0), 0.5f);
		
		//food containers
		foodButton = new GuiContainer("foodButton", controlsBar, toggleFoodCmd, standardQuad, wrapper.getGuiTexture("white"), 70, 35,
				HorReference.SIBLING, HorPositions.RIGHT_OF, 1, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(1, 0, 0), 1f);
		foodContainer = new GuiContainer("foodContainer", foodButton, null, standardQuad, wrapper.getGuiTexture("white"), 70, 105,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.ABOVE, 0, 0f, new Vector3f(0, 1, 0), 1f);
		GuiContainer foodApple = new GuiContainer("foodApple", foodContainer, clickFoodAppleCmd, standardQuad, wrapper.getGuiTexture("white"), 70, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BOTTOM, 0, 0f, new Vector3f(0.2f, 0, 0), 1f);
		GuiContainer foodSquirrel = new GuiContainer("foodSquirrel", foodContainer, clickFoodSquirrelCmd, standardQuad, wrapper.getGuiTexture("white"), 70, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.ABOVE, 0, 0f, new Vector3f(0.4f, 0, 0), 1f);
		GuiContainer foodInsect = new GuiContainer("foodInsect", foodContainer, toggleFoodSubCmd, standardQuad, wrapper.getGuiTexture("white"), 70, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.ABOVE, 0, 0f, new Vector3f(0.6f, 0, 0), 1f);
		
		foodInsectSubContainer = new GuiContainer("foodSubContainer", foodInsect, null, standardQuad, wrapper.getGuiTexture("white"), 140, 35,
				HorReference.PARENT, HorPositions.RIGHT_OF, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0.8f, 0, 0), 1f);
		GuiContainer foodAnt = new GuiContainer("foodAnt", foodInsectSubContainer, clickFoodAntCmd, standardQuad, wrapper.getGuiTexture("white"), 70, 35,
				HorReference.PARENT, HorPositions.LEFT, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0.2f, 0), 1f);
		GuiContainer foodGrasshopper = new GuiContainer("foodGrasshopper", foodInsectSubContainer, clickFoodGrasshopperCmd, standardQuad, wrapper.getGuiTexture("white"), 70, 35,
				HorReference.SIBLING, HorPositions.RIGHT_OF, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0.4f, 0), 1f);
		enemyButton = new GuiContainer("enemyButton", controlsBar, toggleEnemyCmd, standardQuad, wrapper.getGuiTexture("white"), 70, 35,
				HorReference.SIBLING, HorPositions.RIGHT_OF, 1, VerReference.PARENT, VerPositions.MIDDLE, 0, 0f, new Vector3f(0, 0, 0.2f), 1f);
		enemyContainer = new GuiContainer("enemyContainer", enemyButton, null, standardQuad, wrapper.getGuiTexture("white"), 70, 70,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.ABOVE, 0, 0f, new Vector3f(0, 0, 0.4f), 1f);
		GuiContainer enemyAnt = new GuiContainer("enemyAnt", enemyContainer, clickEnemyAntCmd, standardQuad, wrapper.getGuiTexture("white"), 70, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.BOTTOM, 0, 0f, new Vector3f(0, 0, 0.6f), 1f);
		GuiContainer enemyGrasshopper = new GuiContainer("enemyGrasshopper", enemyContainer, clickEnemyGrasshopperCmd, standardQuad, wrapper.getGuiTexture("white"), 70, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.ABOVE, 0, 0f, new Vector3f(0, 0, 0.8f), 1f);
		
		// default hidings
		foodContainer.setDisabled(true);
		enemyContainer.setDisabled(true);
		
		// Images old (working properly
		//GuiImage playPauseContainerImg = new GuiImage("playPauseContainer", playPauseContainer, null, standardQuad, wrapper.getGuiTexture("controls/playPause"), 35, 35,
				//HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
		playPause = new GuiImage("playPause", playPauseContainer, null, standardQuad, wrapper.getGuiTexture("controls/pause"), 32, 32,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
		speed1Img = new GuiImage("speed1Img", speed1, null, standardQuad, wrapper.getGuiTexture("controls/play_filled_small"), 15, 30,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
		speed2Img = new GuiImage("speed2Img", speed2, null, standardQuad, wrapper.getGuiTexture("controls/play_filled_small"), 15, 30,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
		speed3Img = new GuiImage("speed3Img", speed3, null, standardQuad, wrapper.getGuiTexture("controls/play_unfilled_small"), 15, 30,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
		speed4Img = new GuiImage("speed4Img", speed4, null, standardQuad, wrapper.getGuiTexture("controls/play_unfilled_small"), 15, 30,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);

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

		updateSpeedButtons(2);
		
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
		if (!MainApplication.getInstance().isPaused()) {
			playPause.setTextureId(playFilledTexId);
		} else {
			playPause.setTextureId(pauseTexId);
		}
	}

	public void setPauseButton() {
		playPause.setTextureId(pauseTexId);
	}

	public void updateSpeedButtons(int numberOfButtons) {
		speed1Img.setTextureId(playFilledTexId);
		if (numberOfButtons >= 2) {
			speed2Img.setTextureId(playFilledTexId);
		} else {
			speed2Img.setTextureId(playUnfilledTexId);
		}
		if (numberOfButtons >= 3) {
			speed3Img.setTextureId(playFilledTexId);
		} else {
			speed3Img.setTextureId(playUnfilledTexId);
		}
		if (numberOfButtons >= 4) {
			speed4Img.setTextureId(playFilledTexId);
		} else {
			speed4Img.setTextureId(playUnfilledTexId);
		}
	}

	public void showFoodContainer() {
		foodContainer.setDisabled(false);
	}

	public void hideFoodContainer() {
		foodContainer.setDisabled(true);
	}
	
	public void toggleFoodContainer() {
		toggleContainer(foodContainer);
	}

	public void showEnemyContainer() {
		enemyContainer.setDisabled(false);
	}

	public void hideEnemyContainer() {
		enemyContainer.setDisabled(true);
	}
	
	public void toggleEnemyContainer() {
		toggleContainer(enemyContainer);
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
	}
	
	public void hideFoodSubContainer() {
		foodContainer.setDisabled(false);
		foodInsectSubContainer.setDisabled(true);
		foodSubContainerDisplayed = false;
	}

	public void toggleFoodSubContainer() {
		if (foodSubContainerDisplayed) {
			hideFoodSubContainer();
		} else {
			showFoodSubContainer();
		}
	}
}
