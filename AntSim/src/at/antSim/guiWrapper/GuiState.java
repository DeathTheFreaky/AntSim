package at.antSim.guiWrapper;

import java.util.LinkedList;
import java.util.List;

/**Elements are put above each other depending on the order of insertion. 
 * 
 * @author Flo
 *
 */
public class GuiState {
	
	List<GuiContainer> elements = new LinkedList<GuiContainer>();

	/**Adds a container for positioning and scaling a group of gui elements.
	 * 
	 */
	public void addContainer(GuiContainer container) {
		elements.add(container);
	};
	
	/**Returns a list of all elements in this gui.
	 * 
	 * @return - list of {@link GuiElement}s
	 */
	public List<GuiContainer> getElements() {
		return elements;
	}
	
}
