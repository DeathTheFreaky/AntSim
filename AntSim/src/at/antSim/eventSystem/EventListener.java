package at.antSim.eventSystem;

/**
 * Created on 31.03.2015.
 * @author Clemens
 */
public interface EventListener<E extends Event>{

    public void handle(E event);

    public Class<E> getEventType();

}
