package at.antSim.eventSystem;

import java.util.*;

/**
 * Created by Clemens on 24.03.2015.
 */
public class EventManager {

    private static EventManager instance;

    private Queue<Event> eventQueue;
    private Map<Class<? extends Event>, EventListenerManagement> eventListenerMap;

    protected EventManager() {
        eventListenerMap = new HashMap<Class<? extends Event>, EventListenerManagement>();
        eventQueue = new LinkedList<Event>();
    }

    public void registerEventListener(EventListener listener) {
        registerEventListener(listener, EventPriority.NORMAL);
    }

    public void registerEventListener(EventListener listener, EventPriority eventPriority) {
        Class<? extends Event> eventType = listener.getEventType();
        if (eventListenerMap.containsKey(eventType)) {
			EventListenerManagement listenerManagement = eventListenerMap.get(eventType);
			listenerManagement.add(listener, eventPriority);
		} else {
            EventListenerManagement eventListenerManagement = new EventListenerManagement();
            eventListenerManagement.add(listener, eventPriority);
            eventListenerMap.put(listener.getEventType(), eventListenerManagement);
        }
    }

    public void addEventToQueue(Event event) {
        eventQueue.offer(event);
    }

    public void workThroughQueue() {
		for (Event event : eventQueue) {
			eventListenerMap.get(event.getType()).handle(event);
		}
	}

    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

	private class EventListenerManagement {
		Map<EventListener, EventPriority> allEventListeners;
		Map<EventPriority, List<EventListener>> eventListenerMap;

		EventListenerManagement() {
			allEventListeners = new HashMap<EventListener, EventPriority>();
			eventListenerMap = new HashMap<EventPriority, List<EventListener>>(3);
			for (EventPriority priority : EventPriority.values()) {
				eventListenerMap.put(priority, new LinkedList<EventListener>());
			}
		}

		boolean contains(EventListener listener) {
			return allEventListeners.containsKey(listener);
		}

		void add(EventListener listener, EventPriority priority) {
			if (!allEventListeners.containsKey(listener)) {
				eventListenerMap.get(priority).add(listener);
				allEventListeners.put(listener, priority);
			} else if (allEventListeners.get(listener) != priority) {
				eventListenerMap.get(allEventListeners.get(listener)).remove(listener);
				eventListenerMap.get(priority).add(listener);
				allEventListeners.put(listener, priority);
			}
		}

		void remove(EventListener listener) {
			if (allEventListeners.containsKey(listener)) {
				eventListenerMap.get(allEventListeners.get(listener)).remove(listener);
				allEventListeners.remove(listener);
			}
		}

		void handle(Event event) {
			List<EventListener> eventListeners;
			for (EventPriority eventPriority : EventPriority.values()) {
				eventListeners= eventListenerMap.get(eventPriority);
				for (EventListener eventListener : eventListeners) {
					eventListener.handle(event);
					if (event.isKilled())
						return;
				}
			}
		}

	}

}
