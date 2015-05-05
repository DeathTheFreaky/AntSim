package at.antSim.eventSystem;

/**
 * Created on 25.03.2015.
 * @author Clemens
 */
public interface Event {

    void consume();
    boolean isConsumed();

}
