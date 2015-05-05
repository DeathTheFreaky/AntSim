package at.antSim.inputHandlers;

import at.antSim.eventSystem.Event;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created on 04.05.2015.<br />
 * Is managing all mouse and keyboard inputs.<br />
 * Is a Singleton.
 *
 * @author Clemens
 */
public class InputManager {

	static final InputManager instance = new InputManager();

	/**
	 * @return returns the only instance of InputManager
	 */
	public static InputManager getInstance() {
		return instance;
	}

	final BlockingQueue<Event> mouseInput = new LinkedBlockingQueue<Event>();
	final BlockingQueue<Event> keyboardInput = new LinkedBlockingQueue<Event>();

	final MouseHandler mouseHandler = new MouseHandler(mouseInput);
	final KeyboardHandler keyboardHandler = new KeyboardHandler(keyboardInput);

	protected InputManager() {
	}

	/**
	 * Starts the InputManager. From now on all Mouse and Keyboard interaction will be registered.
	 */
	public void start() {
		mouseHandler.start();
		keyboardHandler.start();
	}

	/**
	 * Stops the InputManager. From now on no Mouse or Keyboard interactions will be registered. All interactions that have taken place before and where not gotten are now destroyed.
	 */
	public void stop() {
		mouseHandler.interrupt();
		keyboardHandler.interrupt();
	}

	/**
	 * @return Returns an Event if and only if either one mouse interaction or one keyboard interaction have been taken place.<br />
	 * If a mouse and a keyboard interaction have been taken place the mouse interaction will be returned first.<br />
	 * If no interaction have been taken place null is returned.
	 */
	public Event getNextEvent() {
		return mouseInput.peek() != null ? mouseInput.poll() : keyboardInput.poll();
	}

	/**
	 * @return Returns an Event if and only if a mouse interaction have been taken place.<br />
	 * If no interaction have been taken place null is returned.
	 */
	public Event getNextMouseEvent() {
		return mouseInput.poll();
	}

	/**
	 * @return Returns and Event if and only if a keyboard interaction have been taken place.<br />
	 * If no interaction have been taken place null is returned.
	 */
	public Event getNextKeyboardEvent() {
		return keyboardInput.poll();
	}

}
