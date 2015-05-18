package at.antSim.guiWrapper.states;

import java.util.LinkedList;
import java.util.List;

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
	
	public MainGameState(Loader loader, String name) {
		super(loader, name);
	}

	@Override
	public void initializeState(String... args) {
		
		GuiContainer statusContainer = new GuiContainer("statusContainer", null, null, standardQuad, wrapper.getGuiTexture("white"), 200, 240, 
				HorReference.PARENT, HorPositions.LEFT, 0, VerReference.PARENT, VerPositions.TOP, 0, 0.5f, new Vector3f(0,0,0), 0f);
		
		//nur einmal, außer für andere Fonts
		OpenGLTextDrawer textDrawer = new OpenGLTextDrawer(loader, wrapper.getGuiTexture("font"));
		
		List<String> statuses = new LinkedList<>();
		
		statuses.add("Population");
		statuses.add("Food");
		statuses.add("Eggs");
		statuses.add("Larva");
		
		int textSize = 12;
		
		int idx = 0;
		for (String str : statuses) {
			
			GuiContainer statusRowContainer;
			
			if (idx == 0) {
				statusRowContainer = new GuiContainer("statusRowContainer", statusContainer, null, standardQuad,  wrapper.getGuiTexture("white"), statusContainer.getWidth(), 35,
						HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.TOP, 0, 0f, new Vector3f(0, 0, 0), 0.5f);
			} else {
				statusRowContainer = new GuiContainer("statusRowContainer", statusContainer, null, standardQuad,  wrapper.getGuiTexture("white"), statusContainer.getWidth(), 35,
						HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 0, 0f, new Vector3f(0, 0, 0), 0.5f);
			}
			GuiContainer statusLabelContainer = new GuiContainer("statusLabelContainer", statusRowContainer, null, standardQuad,  wrapper.getGuiTexture("white"), statusRowContainer.getWidth()/2, 
					statusRowContainer.getHeight(), HorReference.PARENT, HorPositions.LEFT, 0, VerReference.SIBLING, VerPositions.TOP, 0, 0f, new Vector3f(0, 0, 1), 0.5f);
			GuiText statusLabelText = new GuiText("statusLabelText" + str, textDrawer.createTextQuad(str), statusLabelContainer, null, textSize,
					HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 5);
			GuiContainer statusValueContainer = new GuiContainer("statusValueContainer", statusRowContainer, null, standardQuad,  wrapper.getGuiTexture("white"), statusRowContainer.getWidth()/2, 
					statusRowContainer.getHeight(), HorReference.PARENT, HorPositions.RIGHT, 0, VerReference.SIBLING, VerPositions.TOP, 0, 0f, new Vector3f(0, 1, 0), 0.5f);
			GuiText statusValue = new GuiText("statusValue" + str, textDrawer.createTextQuad("0"), statusValueContainer, null, textSize,
					HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0);
			
			idx++;
		}
		
		
//		RawModel testImageQuad = loader.loadToVAO(positions, textureCoords, 2);
//		GuiImage testImage = new GuiImage("testImage", testContainer, null, testImageQuad, wrapper.getGuiTexture("health"), 500, 280, HorReference.PARENT, HorPositions.CENTER, 0, 
//				VerReference.SIBLING, VerPositions.BELOW, 0, 0f, new Vector3f(1f, 0f, 0f), 0.2f);
//		EventManager.getInstance().registerEventListener(testContainer);
		
		state.addContainer(statusContainer);
		
		GuiWrapper.getInstance().addState(state);	
	}

	@Override
	public void resetState() {
		
	}
	
	public void updateStatus() {
		
	}
}
