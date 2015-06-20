package at.antSim.guiWrapper;

import java.util.LinkedList;
import java.util.List;

/**A GuiState represents a Gui's appearance at a certain state<br>
 * <br>
 * {@link GuiState} holds a list of {@link GuiElement} and each element can be positioned relatively to the previous element,
 * so the order of insertion is significant -> elements should be added in the order they should be rendered.<br>
 * If two Gui elements have the same position, the later added one will be drawn above the earlier added one.
 * 
 * @author Alexander
 *
 */
public class GuiState {
	
	List<GuiContainer> elements = new LinkedList<GuiContainer>();

	String name;
	GuiState prevState;

	public GuiState(String name) {
		this.name = name;
	}

	/**Adds a container for positioning and scaling a group of gui elements and sets the {@link GuiState} associated with the passed {@link GuiElement}.
	 * 
	 * @param container
	 */
	public void addContainer(GuiContainer container) {
		container.setGuiState(this);
		addState(container);
		elements.add(container);
	};
	
	/**Returns a list of all elements in this gui.
	 * 
	 * @return - list of {@link GuiElement}s
	 */
	public List<GuiContainer> getElements() {
		return elements;
	}

	public String getName() {
		return name;
	}
	
	/**Adds associated gui state for container and all its children.
	 * @param container
	 */
	private void addState(GuiContainer container) {
		container.addStateForAllChildren(this);
	}
	
	public void setPrevState(GuiState prevState) {
		this.prevState = prevState;
	}
	
	public GuiState getPrevState() {
		return prevState;
	}
}
