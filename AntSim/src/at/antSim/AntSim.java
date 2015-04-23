package at.antSim;

import java.io.IOException;

import at.antSim.config.ConfigReader;
import at.antSim.config.ConfigWriter;
import at.antSim.eventSystem.EventManager;

/**
 * Created by Clemens on 24.03.2015.
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
