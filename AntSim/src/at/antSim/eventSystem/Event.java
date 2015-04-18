package at.antSim.eventSystem;

import java.lang.reflect.Type;

/**
 * Created by Clemens on 25.03.2015.
 */
public interface Event {

    void kill();
    boolean isKilled();
    Type getType();
	Object getSource();

}
