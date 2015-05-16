package at.antSim.guiWrapper.states;

import org.lwjgl.util.vector.Vector3f;

import at.antSim.Globals;
import at.antSim.graphics.graphicsUtils.Loader;
import at.antSim.graphics.graphicsUtils.OpenGLTextDrawer;
import at.antSim.guiWrapper.GuiContainer;
import at.antSim.guiWrapper.GuiText;
import at.antSim.guiWrapper.GuiWrapper;
import at.antSim.guiWrapper.HorPositions;
import at.antSim.guiWrapper.HorReference;
import at.antSim.guiWrapper.VerPositions;
import at.antSim.guiWrapper.VerReference;

/**Loading State displayed when world is loading.
 * 
 * @author Flo
 *
 */
public class LoadingState extends AbstractGuiState {

	public LoadingState(Loader loader, String name) {
		super(loader, name);
	}

	@Override
	public void initializeState(String... args) {
		
		OpenGLTextDrawer textDrawer = new OpenGLTextDrawer(loader, wrapper.getGuiTexture("font"));
		GuiContainer loadingContainer = new GuiContainer("loadingContainer", null, null, standardQuad, wrapper.getGuiTexture("white"),
				Globals.displayWidth, Globals.displayHeight, HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0, new Vector3f(0,0,0), 1f);
		GuiText loadingText = new GuiText("loadingText", textDrawer.createTextQuad("Loading..."), loadingContainer, null, 35,
				HorReference.PARENT, HorPositions.CENTER, 0, VerReference.PARENT, VerPositions.MIDDLE, 0, 0, new Vector3f(1,1,1) , 1f);
	
		state.addContainer(loadingContainer);
		
		GuiWrapper.getInstance().addState(state);	
	}
}
