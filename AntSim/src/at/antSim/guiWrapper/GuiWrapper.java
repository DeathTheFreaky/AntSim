package at.antSim.guiWrapper;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.opengl.Texture;

import at.antSim.graphics.graphicsUtils.OpenGLLoader;

/**A gui wrapper wraps the current state of a gui and stores all GuiTextures to be reused.<br>
 * <br>
 * It manages all states known to the GUI and can set and get the currently activated state of the GUI.
 * 
 * @author Alexander, Flo
 *
 */
public class GuiWrapper {
	
	private static GuiWrapper INSTANCE = null;
	
	Map<String, GuiState> states = new HashMap<>();
	Map<String, Texture> guiTextures = new HashMap<>();
	
	GuiState currentState;
	OpenGLLoader loader;
	
	static {
		INSTANCE = new GuiWrapper();
	}
	
	private GuiWrapper() {};
	
	/**Adds a new State to the GUI.<br>
	 * <br>
	 * In case a state with the same name was already present, the old state will be replaced by the new state.
	 *
	 * @param state - the GUI state to be added
	 */
	public void addState(GuiState state) {
		states.put(state.getName(), state);
	}
	
	/**Sets the currently activated {@link GuiState}.<br>
	 * If no state was found with the passed name, sets currently activated state to null - no GUI will be rendered.
	 * 
	 * @param name - name of the GUI State to be set active
	 */
	public synchronized void setCurrentState(String name) {
		currentState = states.get(name);
	}
	
	/**
	 * @return - the currently active {@link GuiState}
	 */
	public GuiState getCurrentState() {
		return currentState;
	}

	/**Gets a new State from the GUI.<br>
	
	 * @param name - name of the GUI state to be returned
	 * @return - a {@link GuiState}
	 */
	public GuiState getState(String name) {
		return states.get(name);
	}
	
	/**Retrieves a gui Texture.
	 * 
	 * @param filename
	 * @return
	 */
	public Texture getGuiTexture(String filename) {
		
		Texture tex = guiTextures.get(filename);
		
		if (tex == null) {
			tex = loader.loadGuiTexture(filename);
			guiTextures.put(filename, tex);
		}
		
		return tex;
	}
	
	/**Sets a loader to be used for loading GuiTextures. Needs to be called before any GuiTexture can be loaded.
	 * @param loader
	 */
	public void setLoader(OpenGLLoader loader) {
		this.loader = loader;
	}
		
	public static GuiWrapper getInstance() {
		return INSTANCE;
	}
}
