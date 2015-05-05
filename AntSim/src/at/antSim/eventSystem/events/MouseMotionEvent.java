package at.antSim.eventSystem.events;

/**
 * Created on 24.04.2015.
 *
 * @author Clemens
 */
public class MouseMotionEvent extends AbstractMouseEvent {

	//Delta of cursor position
	final int DX;
	final int DY;

	public MouseMotionEvent(int DX, int DY, int posX, int posY) {
		super(posX, posY);
		this.DX = DX;
		this.DY = DY;
	}

	public int getDX() {
		return DX;
	}

	public int getDY() {
		return DY;
	}
}
