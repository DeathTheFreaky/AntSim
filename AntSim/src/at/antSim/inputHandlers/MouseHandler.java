package at.antSim.inputHandlers;

import at.antSim.Globals;
import at.antSim.eventSystem.EventManager;
import at.antSim.eventSystem.events.MouseButtonPressedEvent;
import at.antSim.eventSystem.events.MouseButtonReleasedEvent;
import at.antSim.eventSystem.events.MouseMotionEvent;
import at.antSim.eventSystem.events.MouseScrollEvent;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;

/**
 * Created on 24.04.2015.<br />
 * Converts in an interval of 1/60 second every LWJGL Mouse Event into an MouseMotion-, MouseButtonPressed- or MouseButtonReleasedEvent
 *
 * @author Clemens
 */
public class MouseHandler extends Thread {

	public MouseHandler() throws MouseHandlerException {
		if (!Mouse.isCreated())
			try {
				Mouse.create();
			} catch (LWJGLException e) {
				throw new MouseHandlerException(e);
			}
		Mouse.setClipMouseCoordinatesToWindow(true);
	}

	@Override
	public void run() {
		long iterationStartTime = 0;

		EventManager eventManager = EventManager.getInstance();
		
		while (!isInterrupted()) {
			iterationStartTime = System.nanoTime();
			while (Mouse.next()) {
				if (Mouse.getEventDX() != 0 || Mouse.getEventDY() != 0) {
					eventManager.addEventToQueue(new MouseMotionEvent(Mouse.getEventDX(), Mouse.getEventDY(), Mouse.getEventX(), Mouse.getEventY()));
				} else if (Mouse.getEventButton() != -1 && Mouse.getEventButtonState()) {
					eventManager.addEventToQueue(new MouseButtonPressedEvent(Mouse.getEventButton(), Mouse.getEventX(), Mouse.getEventY()));
				} else if (Mouse.getEventButton() != -1){
					eventManager.addEventToQueue(new MouseButtonReleasedEvent(Mouse.getEventButton(), Mouse.getEventX(), Mouse.getEventY()));
				}else if (Mouse.getEventDWheel() != 0) {
					eventManager.addEventToQueue(new MouseScrollEvent(Mouse.getEventDWheel(), Mouse.getEventX(), Mouse.getEventY()));
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
