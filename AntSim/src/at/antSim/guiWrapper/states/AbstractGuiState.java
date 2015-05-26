package at.antSim.guiWrapper.states;

import at.antSim.graphics.graphicsUtils.OpenGLLoader;
import at.antSim.graphics.models.RawModel;
import at.antSim.guiWrapper.GuiElement;
import at.antSim.guiWrapper.GuiState;
import at.antSim.guiWrapper.GuiWrapper;

/**An abstract class for encapsulating manipulations of a specific {@link GuiState}.
 * @author Flo
 *
 */
public abstract class AbstractGuiState {
	
	GuiWrapper wrapper;
	
	OpenGLLoader loader;
	GuiState state;
	String name;

	public AbstractGuiState(OpenGLLoader loader, String name) {
		this.loader = loader;
		this.name = name;
		
		state = new GuiState(name);
		
		wrapper = GuiWrapper.getInstance();
	}
	
	/**Initially fills the {@link GuiState} with {@link GuiElement}s.
	 * 
	 * @param args - eg. names of target states for command operations
	 */
	public abstract void initializeState(String... args);
	
	/**Used to hide error messages etc...
	 * 
	 */
	public abstract void resetState();
	
	/**Retrieves this {@link GuiState} from the GuiWrapper for manipulation.
	 * @return - the {@link GuiState}
	 */
	public GuiState getState() {
		return GuiWrapper.getInstance().getState(name);
	};
	
	/**Returns name of {@link GuiState}.
	 * @return - name of {@link GuiState}
	 */
	public String getName() {
		return name;
	};
}
