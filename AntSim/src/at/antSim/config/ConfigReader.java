package at.antSim.config;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

import at.antSim.Globals;
import at.antSim.exceptions.ConfigParseException;
import javafx.stage.Stage;

/**Parses a config file and stores its entries in {@link Globals}.
 * 
 * @author Flo
 *
 */
public class ConfigReader {
	
	public void readConfig() {
		
		try {
			parseConfig();
		} catch (FileNotFoundException e) {
			//Platform.runLater(new AlertMessage(AlertType.ERROR, "Config Load Error", "Config File could not be found!", e.getMessage()));
			e.printStackTrace();	
			System.exit(1);
			
		} catch (IOException e) {
			//Platform.runLater(new AlertMessage(AlertType.ERROR, "Config Load Error", "Config File could not be read!", e.getMessage()));
			e.printStackTrace();
			System.exit(1);
			
		} catch (ConfigParseException e) {
			//Platform.runLater(new AlertMessage(AlertType.ERROR, "Config Load Error", "Config File could not be parsed!", e.getMessage()));
			e.printStackTrace();
			System.exit(1);
			
		} catch (NoSuchFieldException|SecurityException|IllegalArgumentException|IllegalAccessException e) {
			//Platform.runLater(new AlertMessage(AlertType.ERROR, "Config Load Error", "Config File has undefined options!", e.getMessage()));
			e.printStackTrace();
			System.exit(1);
			
		} 
	}
	
	private void parseConfig() throws FileNotFoundException, IOException, ConfigParseException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
				
		//create FileInputStream and open Config file				
		try (InputStream fs = new FileInputStream(Globals.CONFIG + "/config.txt"); BufferedReader filereader = new BufferedReader(new InputStreamReader(fs));) {
			
			String line;
			
			//read lines from config file
			while ((line = filereader.readLine()) != null) {
								
				//skip comments and empty lines
				if (line.startsWith("//") || line.length() == 0) {
					continue;
					
				}
				
				//create concrete Prototypes using the currentPrototype class and store them in itemPrototypes HashMap
				else {					
					
					String trimmedLine = line.trim().replace(" ", "").replace("\"", "");
					String[] lineParts = trimmedLine.split("=", 2);
					
					if (lineParts.length == 0 || lineParts.length > 2) {
						filereader.close();
						throw new ConfigParseException("Config File could not be parsed!"); //warning: filreader is closed in finally no matter what
					}
					
					String option = lineParts[0];
					String value = lineParts[1];
																				
					Field var = Globals.class.getField(option);					
					var.set(this, ObjectConverter.convert(value, var.getType()));
					
					//log what has been set to which values
					
				}
				
			}
			
			filereader.close();
			fs.close();
		} 
	}

}
