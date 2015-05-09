package at.antSim.guiWrapper;

import java.util.HashMap;
import java.util.Map;

/**Provides useful methods for building a gui and stores gui elements to be drawn.
 * 
 * @author Flo
 *
 */
public class GuiWrapper {
	
	Map<String, GuiState> states = new HashMap<>();
	
	GuiState currentState;
	
	public void addState(String name, GuiState state) {
		states.put(name, state);
	}
	
	public void setCurrentState(String name) {
		currentState = states.get(name);
	}
	
	public GuiState getCurrentState() {
		return currentState;
	}

	public GuiState getState(String name) {
		return states.get(name);
	}
	
}
