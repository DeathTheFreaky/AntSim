package at.antSim;

import at.antSim.config.ConfigReader;
import at.antSim.config.ConfigWriter;
import at.antSim.graphics.graphicsUtils.DisplayManager;
import at.antSim.graphics.graphicsUtils.Loader;
import at.antSim.graphics.renderer.MasterRenderer;

import java.io.IOException;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.nulldevice.NullSoundDevice;
import de.lessvoid.nifty.renderer.lwjgl.input.LwjglInputSystem;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.spi.time.TimeProvider;

/**
 * Created on 24.03.2015.
 * @author Clemens, Flo
 */
public class AntSim {
	
	Loader loader;
	Nifty nifty;
	MasterRenderer masterRenderer;
	
    public AntSim(){}

    public static void main(String[] args) {
        AntSim game = new AntSim();
        game.launch();
    }

    public void launch() {
        preInit();
        init();
        postInit();
    }

    private void preInit() {
    	
    	
        ConfigReader myreader = new ConfigReader();
        
        /* ConfigWriter mywriter = new ConfigWriter();
         try {
			mywriter.writeConfig(); 
			myreader.readConfig();
		} catch (NoSuchFieldException | SecurityException
				| IllegalArgumentException | IllegalAccessException
				| IOException e) {
			e.printStackTrace();
		} */ 	
        
        myreader.readConfig();
    }

    private void init() {		
    	
    	//initialize display manager
    	DisplayManager.createDisplay();
		loader = new Loader();
		
		//initialize nifty gui
		final LwjglInputSystem inputSystem = new LwjglInputSystem();
		try {
			inputSystem.startup();
		} catch (Exception e) {
			e.printStackTrace();
		}
		final TimeProvider timeProvider = new TimeProvider() {
			@Override
			public long getMsTime() {
				return DisplayManager.getCurrentTime();
			}
		};
		final LwjglRenderDevice renderDevice = new LwjglRenderDevice();
		final NullSoundDevice soundDevice = new NullSoundDevice();
		
		nifty = new Nifty(
			renderDevice,
			soundDevice,
			inputSystem,
			timeProvider);
		
		//initialize render engine
		masterRenderer = new MasterRenderer(loader);
    }

    private void postInit() {
    	EngineTester.launch(loader, masterRenderer, nifty);
    }
    
//	ConfigWriter mywriter = new ConfigWriter();
//	try {
//		//mywriter.writeConfig();
//	} catch (NoSuchFieldException | SecurityException
//			| IllegalArgumentException | IllegalAccessException
//			| IOException e) {
//		e.printStackTrace();
//	}

}
