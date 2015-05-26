package at.antSim.eventSystem.events;

/**
 * Created on 12.05.2015.
 *
 * @author Clemens
 */
public class MouseScrollEvent extends AbstractMouseEvent {

	private final int DWheel;

	public MouseScrollEvent(int DWheel, int posX, int posY) {
		super(posX, posY);
		this.DWheel = DWheel;
	}

	public int getDWheel() {
		return DWheel;
	}
}
