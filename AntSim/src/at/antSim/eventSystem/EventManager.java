package at.antSim.eventSystem;

import at.antSim.utils.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Created on 24.03.2015.<br />
 * The EventManager is passing all Events to the respective Listeners.<br />
 * It is a Singleton.
 *
 * @author Clemens
 */
public class EventManager {

	private static EventManager instance;

	private Queue<Event> eventQueue;
	private Map<Class<? extends Event>, EventListenerManagement> eventListenerMap;

	protected EventManager() {
		eventListenerMap = new HashMap<Class<? extends Event>, EventListenerManagement>();
		eventQueue = new LinkedList<Event>();
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
		System.out.println("added to queue: " + event);
		eventQueue.offer(event);
		if(eventQueue.size()>0){
			System.out.println(eventQueue.size() + ": " + eventQueue);
			printAddresses("Address", eventQueue);
		}
	}
	
	
	
	public static void printAddresses(String label, Object... objects) {
	    System.out.print(label + ": 0x");
	    long last = 0;
	    int offset = unsafe.arrayBaseOffset(objects.getClass());
	    int scale = unsafe.arrayIndexScale(objects.getClass());
	    switch (scale) {
	    case 4:
	        long factor = is64bit ? 8 : 1;
	        final long i1 = (unsafe.getInt(objects, offset) & 0xFFFFFFFFL) * factor;
	        System.out.print(Long.toHexString(i1));
	        last = i1;
	        for (int i = 1; i < objects.length; i++) {
	            final long i2 = (unsafe.getInt(objects, offset + i * 4) & 0xFFFFFFFFL) * factor;
	            if (i2 > last)
	                System.out.print(", +" + Long.toHexString(i2 - last));
	            else
	                System.out.print(", -" + Long.toHexString( last - i2));
	            last = i2;
	        }
	        break;
	    case 8:
	        throw new AssertionError("Not supported");
	    }
	    System.out.println();
	}
	
	
	
	
	
	

	/**
	 * Handles all stored Events.
	 */
	public void workThroughQueue() {
		if(eventQueue.size()>0)
			System.out.println(eventQueue.size());
		for (Event event = eventQueue.poll(); event != null; event = eventQueue.poll()) {
			System.out.println("event in queue: " + event);
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
		if (instance == null) {
			instance = new EventManager();
		}
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
		public boolean equals(Object obj) {
			if (obj == null)
				return false;

			if (this == obj)
				return true;

			if (this.getClass() != obj.getClass())
				return false;

			ListenerInformation other = (ListenerInformation)obj;

			return (this.listener.equals(other.listener) && this.method.equals(other.method));
		}
	}

}
