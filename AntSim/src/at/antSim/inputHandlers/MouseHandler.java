package at.antSim.inputHandlers;

import at.antSim.eventSystem.Event;
import at.antSim.eventSystem.events.MouseButtonPressedEvent;
import at.antSim.eventSystem.events.MouseButtonReleasedEvent;
import at.antSim.eventSystem.events.MouseMotionEvent;
import com.sun.istack.internal.NotNull;
import org.lwjgl.input.Mouse;

import java.util.concurrent.BlockingQueue;

/**
 * Created by Clemens on 24.04.2015.
 */
public class MouseHandler extends Thread{

	private BlockingQueue<Event> inputQueue;

	public MouseHandler(@NotNull BlockingQueue<Event> inputQueue) {
		this.inputQueue = inputQueue;
	}

	@Override
	public void run() {
		long iterationStartTime = 0;

		int lastPosX = 0;
		int lastPosY = 0;

		int currentPosX = 0;
		int currentPosY = 0;

		boolean lastLeftButtonDown = false;
		boolean lastRightButtonDown = false;
		boolean lastMiddleButtonDown = false;

		boolean currentLeftButtonDown = false;
		boolean currentRightButtonDown = false;
		boolean currentMiddleButtonDown = false;

		while (!isInterrupted()) {
			iterationStartTime = System.nanoTime();

			currentPosX = Mouse.getX();
			currentPosY = Mouse.getY();

			currentLeftButtonDown = Mouse.isButtonDown(0);
			currentRightButtonDown = Mouse.isButtonDown(1);
			currentMiddleButtonDown = Mouse.isButtonDown(2);

			if (lastLeftButtonDown == false && lastLeftButtonDown != currentLeftButtonDown) {
				inputQueue.offer(new MouseButtonPressedEvent(MouseButtons.LEFT));
			}

			if (lastRightButtonDown == false && lastRightButtonDown != currentRightButtonDown) {
				inputQueue.offer(new MouseButtonPressedEvent(MouseButtons.RIGHT));
			}

			if (lastMiddleButtonDown == false && lastMiddleButtonDown != currentMiddleButtonDown) {
				inputQueue.offer(new MouseButtonPressedEvent(MouseButtons.MIDDLE));
			}

			if(lastPosX != currentPosX || lastPosY != currentPosY) {
				inputQueue.offer(new MouseMotionEvent(lastPosX, lastPosY, currentPosX, currentPosY));
			}

			if (lastLeftButtonDown == true && lastLeftButtonDown != currentLeftButtonDown) {
				inputQueue.offer(new MouseButtonReleasedEvent(MouseButtons.LEFT));
			}

			if (lastRightButtonDown == true && lastRightButtonDown != currentRightButtonDown) {
				inputQueue.offer(new MouseButtonReleasedEvent(MouseButtons.RIGHT));
			}

			if (lastMiddleButtonDown == true && lastMiddleButtonDown != currentMiddleButtonDown) {
				inputQueue.offer(new MouseButtonReleasedEvent(MouseButtons.MIDDLE));
			}

			lastPosX = currentPosX;
			lastPosY = currentPosY;

			lastLeftButtonDown = currentLeftButtonDown;
			lastRightButtonDown = currentRightButtonDown;
			lastMiddleButtonDown = currentMiddleButtonDown;

			if ((System.nanoTime() - iterationStartTime) < (1 / 60) * 100 * 100 * 100) {
				long waitTime = (1/60)*100*100*100 - (System.nanoTime() - iterationStartTime);
				try {
					sleep(waitTime/100, (int)waitTime%100);
				} catch (InterruptedException e) {
					//do nothing --> will jump out of loop
				}
			}
		}

	}
}
