package at.antSim.config;

import at.antSim.Globals;
import at.antSim.exceptions.ConfigParseException;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;


/**Parses a config file and stores its entries in {@link Globals}.
 * 
 * @author Flo
 *
 */
public class ConfigReader {
	
	public static void readConfig() {
		
		try {
			parseConfig();
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
	
	private static void parseConfig() throws FileNotFoundException, IOException, ConfigParseException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
				
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
					var.set(null, ObjectConverter.convert(value, var.getType()));
										
					//log what has been set to which values
					
				}
			}
			
			filereader.close();
			fs.close();
			
			Globals.displayHeight = Globals.displaySaveHeight;
			Globals.displayWidth = Globals.displaySaveWidth;
			
		} catch (FileNotFoundException e) {
			
			File configDir = new File(Globals.CONFIG);

			// if the directory does not exist, create it
			if (!configDir.exists()) {
			    boolean result = false;

			    try{
			        configDir.mkdir();
			        result = true;
			    } 
			    catch(SecurityException se){
			        System.exit(-1);
			    }        
			}
			
			//write default values if config file does not exist
			PrintWriter writer = new PrintWriter(Globals.CONFIG + "/config.txt", "UTF-8");
			writer.close();
						
			//set default values
			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			
			Globals.displayWidth = gd.getDisplayMode().getWidth();
			Globals.displayHeight = gd.getDisplayMode().getHeight();
			Globals.displaySaveWidth = gd.getDisplayMode().getWidth();
			Globals.displaySaveHeight = gd.getDisplayMode().getHeight();
			Globals.fullscreen = true;
			Globals.fontRows = 16;
			Globals.fontCols = 16;
			
			Globals.moveForwardKey = Keyboard.KEY_W;
			Globals.moveBackwardKey = Keyboard.KEY_S;
			Globals.moveLeftKey = Keyboard.KEY_A;
			Globals.moveRightKey = Keyboard.KEY_D;
			Globals.moveUpKey = Keyboard.KEY_SPACE;
			Globals.moveDownKey = Keyboard.KEY_LSHIFT;
			Globals.tiltDownKey = Keyboard.KEY_UP;
			Globals.tiltUpKey = Keyboard.KEY_DOWN;
			Globals.turnLeftKey = Keyboard.KEY_LEFT;
			Globals.turnRightKey = Keyboard.KEY_RIGHT;
			Globals.zoomInKey = Keyboard.KEY_Q;
			Globals.zoomOutKey = Keyboard.KEY_E;
			Globals.restoreCameraPosition = Keyboard.KEY_R;
			Globals.showGhostSpheres = Keyboard.KEY_G;
			
			Globals.invertHorizontalAxis = -1;
			Globals.invertVerticalAxis = -1;
			
			Globals.maxPheromoneLifetime = 70;
			
			Globals.foodResAppleSize = 150;
			Globals.foodResAntSize = 30;
			Globals.foodResGrasshopperSize = 50;
			Globals.foodResSquirrelSize = 300;
			
			Globals.enemyAntHp = 75;
			Globals.enemyAntAttack = 0.2f;
			Globals.enemyGrasshopperHp = 100;
			Globals.enemyGrasshopperAttack = 0.3f;
			
			Globals.antAttack = 0.15f;
			Globals.antHp = 50;
			Globals.queenAttack = 1f;
			Globals.queenHp = 1000;
			
			Globals.maxFoodCarry = 10;
			
			Globals.hiveFoodStacks = 100;
			
			ConfigWriter.writeConfig();
		}
	}
}
