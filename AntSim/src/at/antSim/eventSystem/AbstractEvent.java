package at.antSim.eventSystem;

/**
 * Created by Clemens on 25.03.2015.
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