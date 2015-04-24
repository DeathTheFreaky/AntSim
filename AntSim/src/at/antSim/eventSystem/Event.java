package at.antSim.eventSystem;

/**
 * Created by Clemens on 25.03.2015.
 */
public interface Event {

    void kill();
    boolean isKilled();
    Class<? extends Event> getType();

}
