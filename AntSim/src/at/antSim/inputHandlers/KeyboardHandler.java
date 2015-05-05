package at.antSim.inputHandlers;

import at.antSim.Globals;
import at.antSim.eventSystem.Event;
import at.antSim.eventSystem.events.KeyPressedEvent;
import at.antSim.eventSystem.events.KeyReleasedEvent;
import com.sun.istack.internal.NotNull;
import org.lwjgl.input.Keyboard;

import java.util.concurrent.BlockingQueue;

/**
 * Created on 24.04.2015.<br />
 * Converts in an interval of 1/60 second every LWJGL Keyboard Event into an KeyPressed- or KeyReleasedEvent.
 *
 * @author Clemens
 */
public class KeyboardHandler extends Thread {

	final BlockingQueue<Event> inputQueue;

	public KeyboardHandler(@NotNull BlockingQueue<Event> inputQueue) {
		this.inputQueue = inputQueue;
		Keyboard.enableRepeatEvents(true);
	}

	@Override
	public void run() {
		long iterationStartTime = 0;

		while (!interrupted()) {
			iterationStartTime = System.nanoTime();

			while (Keyboard.next()) {
				if (Keyboard.getEventKeyState()) {
					inputQueue.offer(new KeyPressedEvent(Keyboard.getEventKey(), Keyboard.getEventCharacter()));
				} else {
					inputQueue.offer(new KeyReleasedEvent(Keyboard.getEventKey(), Keyboard.getEventCharacter()));
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

		inputQueue.clear();
	}
}
