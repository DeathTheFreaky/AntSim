package at.antSim.inputHandlers;

import at.antSim.Globals;
import at.antSim.eventSystem.EventManager;
import at.antSim.eventSystem.events.KeyPressedEvent;
import at.antSim.eventSystem.events.KeyReleasedEvent;
import org.lwjgl.input.Keyboard;

/**
 * Created on 24.04.2015.<br />
 * Converts in an interval of 1/60 second every LWJGL Keyboard Event into an KeyPressed- or KeyReleasedEvent.
 *
 * @author Clemens
 */
public class KeyboardHandler extends Thread {

	public KeyboardHandler() {
		Keyboard.enableRepeatEvents(true);
	}

	@Override
	public void run() {
		long iterationStartTime = 0;
		EventManager eventManager = EventManager.getInstance();

		while (!interrupted()) {
			iterationStartTime = System.nanoTime();

			while (Keyboard.next()) {
				if (Keyboard.getEventKeyState()) {
					eventManager.addEventToQueue(new KeyPressedEvent(Keyboard.getEventKey(), Keyboard.getEventCharacter()));
				} else {
					eventManager.addEventToQueue(new KeyReleasedEvent(Keyboard.getEventKey(), Keyboard.getEventCharacter()));
				}
			}

			if ((System.nanoTime() - iterationStartTime) < Globals.FPS_60_DURATION_NANONS) {
				long waitTime = Globals.FPS_60_DURATION_NANONS - (System.nanoTime() - iterationStartTime);
				try {
					sleep(waitTime / 100, (int) waitTime % 100);
				} catch (InterruptedException e) {
					//do nothing --> will jump out of loop
				}
			}
		}

	}
}
