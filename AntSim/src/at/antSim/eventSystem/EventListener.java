package at.antSim.eventSystem;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 05.05.2015.<br />
 * This Annotation is used to flag a method as listener method for an Event.
 *
 * @author Clemens
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventListener {
}
