package at.antSim;

import at.antSim.config.ConfigReader;
import at.antSim.config.ConfigWriter;

import java.io.IOException;

/**
 * Created on 24.03.2015.
 * @author Clemens, Flo
 */
public class AntSim {
	
    public AntSim(){}

    public static void main(String[] args) {
        AntSim game = new AntSim();
        ConfigWriter mywriter = new ConfigWriter();
        ConfigReader myreader = new ConfigReader();
        try {
			mywriter.writeConfig();
			myreader.readConfig();
			System.out.println("width: " + Globals.displayWidth);
			System.out.println("height: " + Globals.displayHeight);
		} catch (NoSuchFieldException | SecurityException
				| IllegalArgumentException | IllegalAccessException
				| IOException e) {
			e.printStackTrace();
		}
        game.launch();
    }

    public void launch() {
        preInit();
        init();
        postInit();
    }

    private void preInit() {

    }

    private void init() {

    }

    private void postInit() {

    }

}
