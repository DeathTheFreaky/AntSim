package at.antSim.config;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import at.antSim.Globals;

/**Writes options to config file. Options are all static, public and non-final fields in Globals.
 * 
 * @author Flo
 *
 */
public class ConfigWriter {
	
	public void writeConfig() throws FileNotFoundException, IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		//create FileInputStream and open Config file				
		try (OutputStream fs = new FileOutputStream(Globals.CONFIG + "/config.txt"); BufferedWriter filewriter = new BufferedWriter(new OutputStreamWriter(fs));) {
						
			Field[] fields = Globals.class.getFields();
			
			for (Field field : fields) {
				if (!Modifier.isFinal(field.getModifiers()) && Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
					filewriter.write(writeOption(field));
				}
			}
			
			//this should not be needed anyways
			filewriter.close();
			fs.close();
		}
	}
	
	/**Prepares a string that can be written to an option file.
	 * 
	 * @param option - the field that shall be stored as an option
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private String writeOption(Field option) throws IllegalArgumentException, IllegalAccessException {
		String optionStr = option.getName().concat(" = ");
		if (String.class.isInstance(option)) {
			optionStr = optionStr.concat("\"").concat(option.get(null).toString()).concat("\"\n");
		} else {
			optionStr = optionStr.concat(option.get(null).toString()).concat("\n");
		}
		return optionStr;	
	}
}
