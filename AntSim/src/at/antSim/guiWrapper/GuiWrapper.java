package at.antSim.guiWrapper;

import java.util.HashMap;
import java.util.Map;

/**A gui wrapper wraps the current state of a gui.<br>
 * <br>
 * It manages all states known to the GUI and can set and get the currently activated state of the GUI.
 * 
 * @author Flo
 *
 */
public class GuiWrapper {
	
	private static GuiWrapper INSTANCE = null;
	
	Map<String, GuiState> states = new HashMap<>();
	
	GuiState currentState;
	
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
	
	public static GuiWrapper getInstance() {
		return INSTANCE;
	}
	
}
