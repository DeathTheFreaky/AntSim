package at.antSim.eventSystem;

/**
 * Created on 25.03.2015.
 * @author Clemens
 */
public abstract class AbstractEvent implements Event {

    private boolean killed = false;

    @Override
    public void kill() {
        killed = true;
    }

    @Override
    public boolean isKilled() {
        return killed;
    }
}