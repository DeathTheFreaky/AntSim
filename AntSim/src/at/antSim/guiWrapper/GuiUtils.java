package at.antSim.guiWrapper;

public class GuiUtils {

	/**Used to obtain a texture string for a current invert axis checkbox state.
	 * 
	 * @param checked - -1 if inverted, 1 if not inverted
	 * @return - string for current checkbox state
	 */
	public static String getInvertCheckboxTexStr(int checked) {
		if (checked == -1) {
			return "controlsBar/checked";
		} else {
			return "controlsBar/unchecked";
		}
	}

	public static String getCheckboxTexStr(boolean checked) {
		if (checked) {
			return "controlsBar/checked";
		} else {
			return "controlsBar/unchecked";
		}
	}
}
