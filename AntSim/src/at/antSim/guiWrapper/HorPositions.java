package at.antSim.guiWrapper;

/**Indicates where an element shall be positioned horizontally.<br>
 * <br>
 * LEFT means this element's left border will be positioned alongside the left border of a referenced element.<br>
 * LEFT_OF means this element's right border will be positioned alongside the left border of a referenced element.<br>
 * RIGHT means this element's right border will be positioned alongside the right border of a referenced element.<br>
 * RIGHT_OF means this element's left border will be positioned alongside the right border of a referenced element.<br>
 * CENTER means that this element's horizontal center will be positioned at the horizontal center of a referenced element.
 * 
 * @author Flo
 *
 */
public enum HorPositions {
	LEFT, CENTER, RIGHT, LEFT_OF, RIGHT_OF
}
