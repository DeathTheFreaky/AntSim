package at.antSim.eventSystem;

/**
 * Created by Clemens on 31.03.2015.
 */
public interface EventListener<E extends Event>{

    public void handle(E event);

    public Class<E> getEventType();

}
