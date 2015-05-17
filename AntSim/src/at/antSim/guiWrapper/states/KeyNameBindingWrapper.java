package at.antSim.guiWrapper.states;

import java.lang.reflect.Field;

import org.lwjgl.input.Keyboard;

import at.antSim.Globals;
import at.antSim.guiWrapper.GuiText;

/**Storage class for key binding changes in options menu.
 * 
 * @author Flo
 *
 */
public class KeyNameBindingWrapper {

	String label;
	String fieldname;
	GuiText textElem;
	Field field;
	
	public KeyNameBindingWrapper(String label, String fieldname) {
		this.label = label;
		this.fieldname = fieldname;
		try {
			field = Globals.class.getField(fieldname);
		} catch (NoSuchFieldException|SecurityException e) {
			e.printStackTrace();
		} 
	}
	
	public String getLabel() {
		return label;
	}
	
	public String getBindingName() {
		String bindingName = null;
		try {
			bindingName = Keyboard.getKeyName(field.getInt(null));
		} catch (IllegalArgumentException|IllegalAccessException e) {
			e.printStackTrace();
		} 
		return bindingName;
	}
	
	public void setGuiText(GuiText textElem) {
		this.textElem = textElem;
	}
	
	public GuiText getTextElem() {
		return textElem;
	}
	
	public Field getField() {
		return field;
	}
	
	public String getFieldName() {
		return fieldname;
	}
}
