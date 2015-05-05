package at.antSim.eventSystem;

/**
 * Created on 25.03.2015.
 * @author Clemens
 */
public abstract class AbstractEvent implements Event {

    private boolean killed = false;

    @Override
    public void consume() {
        killed = true;
    }

    @Override
    public boolean isConsumed() {
        return killed;
    }
}