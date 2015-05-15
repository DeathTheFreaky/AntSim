package at.antSim.guiWrapper.states;

import at.antSim.graphics.graphicsUtils.Loader;
import at.antSim.graphics.models.RawModel;
import at.antSim.guiWrapper.GuiElement;
import at.antSim.guiWrapper.GuiState;
import at.antSim.guiWrapper.GuiWrapper;

/**An abstract class for encapsulating manipulations of a specific {@link GuiState}.
 * @author Flo
 *
 */
public abstract class AbstractGuiState {
	
	Loader loader;
	GuiState state;
	String name;
	
	float[] positions = { -1, 1, -1, -1, 1, 1, 1, 1, -1, -1, 1, -1 }; //gui quad positions for images
	float[] textureCoords = {0, 0, 0, 1, 1, 0, 1, 0, 0, 1, 1, 1}; //gui texture coords for images
	RawModel standardContainerQuad;

	public AbstractGuiState(Loader loader, String name) {
		this.loader = loader;
		this.name = name;
		
		state = new GuiState(name);
		standardContainerQuad = loader.loadToVAO(positions, textureCoords, 2);
	}
	
	/**Initially fills the {@link GuiState} with {@link GuiElement}s.
	 * 
	 * @param args - eg. names of target states for command operations
	 */
	public abstract void initializeState(String... args);
	
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
