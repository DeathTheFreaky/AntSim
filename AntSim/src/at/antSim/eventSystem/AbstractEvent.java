package at.antSim.eventSystem;

/**
 * Created on 25.03.2015.
 * @author Clemens
 */
public abstract class AbstractEvent implements Event {

    private boolean consumed = false;

    @Override
    public void consume() {
        consumed = true;
    }

    @Override
    public boolean isConsumed() {
        return consumed;
    }
}