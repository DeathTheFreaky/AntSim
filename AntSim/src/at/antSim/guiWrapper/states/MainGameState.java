package at.antSim.guiWrapper.states;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import at.antSim.MainApplication;
import org.lwjgl.util.vector.Vector3f;

import at.antSim.eventSystem.EventManager;
import at.antSim.graphics.graphicsUtils.Loader;
import at.antSim.graphics.graphicsUtils.OpenGLTextDrawer;
import at.antSim.graphics.models.RawModel;
import at.antSim.guiWrapper.GuiContainer;
import at.antSim.guiWrapper.GuiImage;
import at.antSim.guiWrapper.GuiText;
import at.antSim.guiWrapper.GuiWrapper;
import at.antSim.guiWrapper.HorPositions;
import at.antSim.guiWrapper.HorReference;
import at.antSim.guiWrapper.VerPositions;
import at.antSim.guiWrapper.VerReference;

/**Gui for the main application.
 * 
 * @author Flo
 *
 */
public class MainGameState extends AbstractGuiState {

	HashMap<String, GuiContainer> statContainers;
	OpenGLTextDrawer textDrawer;
	int textSize;
	
	public MainGameState(Loader loader, String name) {
		super(loader, name);
	}

	@Override
	public void initializeState(String... args) {

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
		
		GuiContainer controlsBar = new GuiContainer("controlsBar", null, null, standardQuad, wrapper.getGuiTexture("white"), 250, 35,
				HorReference.PARENT, HorPositions.LEFT, 0, VerReference.PARENT, VerPositions.BOTTOM, -1, 0f, new Vector3f(0, 0, 0), 0f);


//		RawModel testImageQuad = loader.loadToVAO(positions, textureCoords, 2);
//		GuiImage testImage = new GuiImage("testImage", testContainer, null, testImageQuad, wrapper.getGuiTexture("health"), 500, 280, HorReference.PARENT, HorPositions.CENTER, 0, 
//				VerReference.SIBLING, VerPositions.BELOW, 0, 0f, new Vector3f(1f, 0f, 0f), 0.2f);
//		EventManager.getInstance().registerEventListener(testContainer);
		
		state.addContainer(statusContainer);
		state.addContainer(controlsBar);
		
		GuiWrapper.getInstance().addState(state);	
	}

	@Override
	public void resetState() {
		
	}
	
	public void updateStatus() {
		for (Entry<String, Integer> entry : MainApplication.getInstance().getStats().entrySet()) {
			statContainers.get(entry.getKey()).removeChildren();
			GuiText statusValue = new GuiText("statusValue" + entry.getKey(), textDrawer.createTextQuad(String.valueOf(entry.getValue())), statContainers.get(entry.getKey()), null, textSize,
					HorReference.PARENT, HorPositions.RIGHT, 5, VerReference.PARENT, VerPositions.MIDDLE, 0);
		}
	}
}
