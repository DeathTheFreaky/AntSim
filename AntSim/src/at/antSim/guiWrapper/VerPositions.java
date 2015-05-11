package at.antSim.guiWrapper;

/**Indicates where an element shall be positioned vertically.<br>
 * <br>
 * TOP means this element's top border will be positioned alongside the top border of a referenced element.<br>
 * ABOVE means this element's bottom border will be positioned alongside the top border of a referenced element.<br>
 * BOTTOM means this element's bottom border will be positioned alongside the bottom border of a referenced element.<br>
 * BELOW means this element's top border will be positioned alongside the bottom border of a referenced element.<br>
 * MIDDLE means that this element's vertical center will be positioned at the vertical center of a referenced element.
 * 
 * @author Flo
 *
 */
public enum VerPositions {
	TOP, MIDDLE, BOTTOM, ABOVE, BELOW
}
