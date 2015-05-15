package at.antSim.guiWrapper.states;

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
		
		GuiContainer testContainer = new GuiContainer("testContainer", null, null, standardContainerQuad, loader.loadGuiTexture("white"), 640, 360, 
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0.5f, new Vector3f(0,0,0), 0f);
		
		//nur einmal, außer für andere Fonts
		OpenGLTextDrawer textDrawer = new OpenGLTextDrawer(loader, loader.loadGuiTexture("font"));
		GuiText testText = new GuiText("testText", textDrawer.createTextQuad("Flo war da!\nWhohoooo"), testContainer, null, 42, HorReference.PARENT, HorPositions.LEFT, 0, VerReference.PARENT, VerPositions.TOP, 0,
				0f, new Vector3f(0f, 1f, 0f), 0.5f);
		
		RawModel testImageQuad = loader.loadToVAO(positions, textureCoords, 2);
		GuiImage testImage = new GuiImage("testImage", testContainer, null, testImageQuad, loader.loadGuiTexture("health"), 500, 280, HorReference.PARENT, HorPositions.CENTER, 0, VerReference.SIBLING, VerPositions.BELOW, 0,
				0f, new Vector3f(1f, 0f, 0f), 0.2f);
		EventManager.getInstance().registerEventListener(testContainer);
		
		state.addContainer(testContainer);
		
		GuiWrapper.getInstance().addState(state);	
	}
}
