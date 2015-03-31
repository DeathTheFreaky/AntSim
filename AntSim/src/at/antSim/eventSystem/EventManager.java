package at.antSim.eventSystem;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by Clemens on 24.03.2015.
 */
public class EventManager {

    private static EventManager instance;

    private Queue<Event> eventQueue;
    private Map<Type, Set<EventListener>> eventListenerMap;

    protected EventManager() {
        eventListenerMap = new HashMap<Type, Set<EventListener>>();
        eventQueue = new LinkedList<Event>();
    }

    public void registerEventListener(EventListener listener) {
        registerEventListener(listener, EventPriority.NORMAL);
    }

    public void registerEventListener(EventListener listener, EventPriority eventPriority) {
        Type eventType = listener.getEventType();
        if (eventListenerMap.containsKey(eventType)) {
            Set<EventListener> eventListeners = eventListenerMap.get(eventType);
            if (!eventListeners.contains(listener)) {
                eventListeners.add(listener);
            }
        } else {
            Set<EventListener> eventListeners = new HashSet<EventListener>();
            eventListeners.add(listener);
            eventListenerMap.put(listener.getEventType(), eventListeners);
        }
    }

    public void addEventToQueue(Event event) {
        eventQueue.offer(event);
    }

    public void workThroughQueue(){

    }

    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

}
