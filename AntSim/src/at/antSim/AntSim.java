package at.antSim;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

import at.antSim.config.ConfigReader;
import at.antSim.graphics.graphicsUtils.DisplayManager;
import at.antSim.graphics.graphicsUtils.Loader;
import at.antSim.graphics.renderer.MasterRenderer;
import at.antSim.inputHandlers.KeyboardHandler;
import at.antSim.inputHandlers.KeyboardHandlerException;
import at.antSim.inputHandlers.MouseHandler;
import at.antSim.inputHandlers.MouseHandlerException;

/**
 * Created on 24.03.2015.
 * @author Clemens, Flo
 */
public class AntSim {
	
	private static AntSim INSTANCE = null;
	
	static {
		INSTANCE = new AntSim();
	}
	
	Loader loader;
	MasterRenderer masterRenderer;
	static List<Thread> threads = new LinkedList<>();
	
    private AntSim(){}

    public static void main(String[] args) {
        INSTANCE.launch();
    }

    public void launch() {
        preInit();
        init();
        postInit();
    }

    private void preInit() {
    	
//    	Runtime.getRuntime().addShutdownHook(new Thread() {
//			public void run() {
//				System.out.println("entered shutdown hook at " + LocalTime.now());
//				System.out.println("finished shutdown hook at " + LocalTime.now());
//			}
//		});
    	
    	//read config
        ConfigReader myreader = new ConfigReader();
        myreader.readConfig();
    }

    private void init() {		
    	
    	//initialize display manager
    	DisplayManager.createDisplay();
		loader = new Loader();
		
		//initialize render engine
		masterRenderer = new MasterRenderer(loader);
		
		//initialize inputs
		Thread mouseHandler;
		Thread keyboardHandler;
		
		try {
			mouseHandler = new MouseHandler();
			keyboardHandler = new KeyboardHandler();
			threads.add(mouseHandler);
			threads.add(keyboardHandler);
			mouseHandler.start();
			keyboardHandler.start();
		} catch (MouseHandlerException | KeyboardHandlerException e) {
			//to be logged
			System.err.println("Unable to load input handler: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
    }

    private void postInit() {
    	
    	MainApplication mainApp = MainApplication.getInstance();
    	
    	//launch main application
    	mainApp.launch(loader, masterRenderer);
    }
    
    /**Close all threads.
     * 
     */
    public void onClose() {
    	for (Thread t : threads) {
			t.interrupt();
		}
    	for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }
    
    public static AntSim getInstance() {
    	return INSTANCE;
    }
    
//	ConfigWriter mywriter = new ConfigWriter();
//	try {
//		//mywriter.writeConfig();
//	} catch (NoSuchFieldException | SecurityException
//			| IllegalArgumentException | IllegalAccessException
//			| IOException e) {
//		e.printStackTrace();
//	}
    
    public static List<Thread> getThreads() {
    	return threads;
    }
}
