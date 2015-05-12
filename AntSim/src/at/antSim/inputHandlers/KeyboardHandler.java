package at.antSim.inputHandlers;

import at.antSim.Globals;
import at.antSim.eventSystem.EventManager;
import at.antSim.eventSystem.events.KeyPressedEvent;
import at.antSim.eventSystem.events.KeyReleasedEvent;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

/**
 * Created on 24.04.2015.<br />
 * Converts in an interval of 1/60 second every LWJGL Keyboard Event into an KeyPressed- or KeyReleasedEvent.
 *
 * @author Clemens
 */
public class KeyboardHandler extends Thread {

	public KeyboardHandler() throws KeyboardHandlerException {
		if (!Keyboard.isCreated())
			try {
				Keyboard.create();
			} catch (LWJGLException e) {
				throw new KeyboardHandlerException(e);
			}
		Keyboard.enableRepeatEvents(true);
	}

	@Override
	public void run() {
		long iterationStartTime = 0;
		EventManager eventManager = EventManager.getInstance();

		while (!interrupted()) {
			iterationStartTime = System.nanoTime();

			while (Keyboard.next()) {
				System.out.println("keyboard reading next");
				if (Keyboard.getEventKeyState()) {
					eventManager.addEventToQueue(new KeyPressedEvent(Keyboard.getEventKey(), Keyboard.getEventCharacter()));
				} else {
					eventManager.addEventToQueue(new KeyReleasedEvent(Keyboard.getEventKey(), Keyboard.getEventCharacter()));
				}
			}

			if ((System.nanoTime() - iterationStartTime) < Globals.FPS_DURATION_NANONS) {
				long waitTime = Globals.FPS_DURATION_NANONS - (System.nanoTime() - iterationStartTime);
				try {
					sleep(waitTime / Globals.MILIS_TO_NANOS_RATIO, (int) waitTime % Globals.MILIS_TO_NANOS_RATIO);
				} catch (InterruptedException e) {
					//do nothing --> will jump out of loop
					interrupt();
				}
			}
		}

	}
}
