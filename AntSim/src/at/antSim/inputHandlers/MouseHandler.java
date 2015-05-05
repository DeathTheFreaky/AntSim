package at.antSim.inputHandlers;

import at.antSim.Globals;
import at.antSim.eventSystem.Event;
import at.antSim.eventSystem.events.MouseButtonPressedEvent;
import at.antSim.eventSystem.events.MouseButtonReleasedEvent;
import at.antSim.eventSystem.events.MouseMotionEvent;
import com.sun.istack.internal.NotNull;
import org.lwjgl.input.Mouse;

import java.util.concurrent.BlockingQueue;

/**
 * Created on 24.04.2015.<br />
 * Converts in an interval of 1/60 second every LWJGL Mouse Event into an MouseMotion-, MouseButtonPressed- or MouseButtonReleasedEvent
 *
 * @author Clemens
 */
public class MouseHandler extends Thread {

	final BlockingQueue<Event> inputQueue;

	public MouseHandler(@NotNull BlockingQueue<Event> inputQueue) {
		this.inputQueue = inputQueue;
		Mouse.setClipMouseCoordinatesToWindow(true);
	}

	@Override
	public void run() {
		long iterationStartTime = 0;

		while (!isInterrupted()) {
			iterationStartTime = System.nanoTime();

			while (Mouse.next()) {
				if (Mouse.getEventButton() == -1) {
					inputQueue.offer(new MouseMotionEvent(Mouse.getEventDX(), Mouse.getEventDY(), Mouse.getEventX(), Mouse.getEventY()));
				} else if (Mouse.getEventButtonState()) {
					inputQueue.offer(new MouseButtonPressedEvent(Mouse.getEventButton(), Mouse.getEventX(), Mouse.getEventY()));
				} else {
					inputQueue.offer(new MouseButtonReleasedEvent(Mouse.getEventButton(), Mouse.getEventX(), Mouse.getEventY()));
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
