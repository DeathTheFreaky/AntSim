package at.antSim.eventSystem;

import at.antSim.utils.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Created on 24.03.2015.<br />
 * The EventManager is passing all Events to the respective Listeners.<br />
 * It is a Singleton.
 *
 * @author Clemens
 */
public class EventManager {

	private static EventManager instance = new EventManager();

	private Queue<Event> eventQueue;
	private Map<Class<? extends Event>, EventListenerManagement> eventListenerMap;

	protected EventManager() {
		eventListenerMap = new HashMap<Class<? extends Event>, EventListenerManagement>();
		eventQueue = new ConcurrentLinkedQueue<Event>();
	}

	/**
	 * Registers an EventListener with the {@link EventPriority} passed by the {@link EventListener}.<br />
	 *
	 * @param listener The instance of a class that implements a method annotated with {@link EventListener}.
	 */
	public void registerEventListener(Object listener) {

		Method[] methods = listener.getClass().getMethods();

		for (int i = 0; i < methods.length; i++) {
			EventListener eventListener = methods[i].getAnnotation(EventListener.class);
			if (eventListener != null) {
				Class[] methodParams = methods[i].getParameterTypes();
				EventPriority eventPriority = eventListener.priority();

				if (methodParams.length < 1)
					continue;

				List<Class<?>> paramInterfaces = ClassUtils.getAllInterfaces(methodParams[0]);
				boolean found = false;
				for (Class<?> interf : paramInterfaces) {
					if (interf == Event.class) {
						found = true;
						break;
					}
				}

				if (!found)
					continue;

				Class<? extends Event> eventType = methodParams[0];
				if (eventListenerMap.containsKey(eventType)) {
					EventListenerManagement listenerManagement = eventListenerMap.get(eventType);
					listenerManagement.add(new ListenerInformation(listener, methods[i]), eventPriority);
				} else {
					EventListenerManagement eventListenerManagement = new EventListenerManagement();
					eventListenerManagement.add(new ListenerInformation(listener, methods[i]), eventPriority);
					eventListenerMap.put(eventType, eventListenerManagement);
				}
			}
		}
	}
	
	/**
	 * Removes an EventListener with the {@link EventPriority} passed by the {@link EventListener}.<br />
	 *
	 * @param listener The instance of a class that implements a method annotated with {@link EventListener}.
	 */
	public void unregisterEventListener(Object listener) {

		Method[] methods = listener.getClass().getMethods();

		for (int i = 0; i < methods.length; i++) {
			EventListener eventListener = methods[i].getAnnotation(EventListener.class);
			if (eventListener != null) {
				Class[] methodParams = methods[i].getParameterTypes();

				if (methodParams.length < 1)
					continue;

				Class<? extends Event> eventType = methodParams[0];
				if (eventListenerMap.containsKey(eventType)) {
					EventListenerManagement listenerManagement = eventListenerMap.get(eventType);
					listenerManagement.remove(new ListenerInformation(listener, methods[i]));
				}
			}
		}
	}

	/**
	 * Handles an passed Event immediately. Is not thread safe.
	 *
	 * @param event Event to handle.
	 */
	public void handleEvent(Event event) {
		eventListenerMap.get(event.getClass()).handle(event);
	}

	/**
	 * Stores the passed Event to be handled as {@link #workThroughQueue() workThroughQueue} is called. Is thread safe.
	 *
	 * @param event Event to handle.
	 */
	public synchronized void addEventToQueue(Event event) {
		eventQueue.offer(event);
	}

	/**
	 * Handles all stored Events.
	 */
	public void workThroughQueue() {
		for (Event event = eventQueue.poll(); event != null; event = eventQueue.poll()) {
			EventListenerManagement management = eventListenerMap.get(event.getClass());
			if (management != null) {
				management.handle(event);
			}
		}
	}

	/**
	 * @return Returns instance of EventManager.
	 */
	public static EventManager getInstance() {
		return instance;
	}

	private class EventListenerManagement {
		Map<ListenerInformation, EventPriority> allEventListeners;
		Map<EventPriority, List<ListenerInformation>> eventListenerMap;

		EventListenerManagement() {
			allEventListeners = new HashMap<ListenerInformation, EventPriority>();
			eventListenerMap = new HashMap<EventPriority, List<ListenerInformation>>(3);
			for (EventPriority priority : EventPriority.values()) {
				eventListenerMap.put(priority, new LinkedList<ListenerInformation>());
			}
		}

		boolean contains(ListenerInformation listenerInformation) {
			return allEventListeners.containsKey(listenerInformation);
		}

		void add(ListenerInformation listenerInformation, EventPriority priority) {
			if (!allEventListeners.containsKey(listenerInformation)) {
				eventListenerMap.get(priority).add(listenerInformation);
				allEventListeners.put(listenerInformation, priority);
			} else if (allEventListeners.get(listenerInformation) != priority) {
				eventListenerMap.get(allEventListeners.get(listenerInformation)).remove(listenerInformation);
				eventListenerMap.get(priority).add(listenerInformation);
				allEventListeners.put(listenerInformation, priority);
			}
		}

		void remove(ListenerInformation listenerInformation) {
			if (allEventListeners.containsKey(listenerInformation)) {
				eventListenerMap.get(allEventListeners.get(listenerInformation)).remove(listenerInformation);
				allEventListeners.remove(listenerInformation);
			}
		}

		void handle(Event event) {
			List<ListenerInformation> listenerInformations;
			for (EventPriority eventPriority : EventPriority.values()) {
				listenerInformations = eventListenerMap.get(eventPriority);
				for (ListenerInformation listenerInformation : listenerInformations) {
					try {
						listenerInformation.method.invoke(listenerInformation.listener, event);
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
					if (event.isConsumed())
						return;
				}
			}
		}
	}

	private class ListenerInformation {
		final Object listener;
		final Method method;

		private ListenerInformation(Object listener, Method method) {
			this.listener = listener;
			this.method = method;
		}

		@Override
		public int hashCode() {
			int result = listener != null ? listener.hashCode() : 0;
			result = 31 * result + (method != null ? method.hashCode() : 0);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null && obj instanceof ListenerInformation)
				return false;

			if (this == obj)
				return true;

			ListenerInformation other = (ListenerInformation)obj;

			return (this.listener.equals(other.listener) && this.method.equals(other.method));
		}
	}

	public void reset() {
		eventQueue.clear();
	}

}
