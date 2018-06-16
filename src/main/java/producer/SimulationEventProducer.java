package producer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import connection.ExchangeConnector;
import event.Event;
import event.EventType;

/**
 * Class for generating and sending events
 * 
 * @author Geraj
 */
public class SimulationEventProducer {
	/** List of event to generate */
	private final ArrayList<EventType> eventTypeList = new ArrayList<EventType>(Arrays.asList(EventType.values()));;

	/** Events in future to add */
	private final SortedSet<Event> specialEvents = new TreeSet<>();

	/** Events in future to add */
	private final SortedSet<Event> eventsToSend = new TreeSet<>();

	/** nr of time to generate events */
	private int nrOfGeneration;

	/** Connection */
	private ExchangeConnector exchangeConnector = new ExchangeConnector();

	/***/
	private static long counter;

	Future<String> future = null;

	/**
	 * 
	 * Constructs a new instance.
	 * 
	 */
	public SimulationEventProducer() {
		super();
	}

	/**
	 * 
	 * Start evenet sending, sends nr
	 * 
	 * @param nrToSend
	 * @param generateSubeEvents 
	 */
	public void generateAndSend(int nrToSend, boolean generateSubeEvents) {
		future = null;
		synchronized (eventsToSend) {
			for (int i = 0; i < nrToSend; i++) {
				counter++;
				this.eventsToSend.addAll(generateEvents(counter, generateSubeEvents));
			}
		}
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		future = executorService.submit(() -> {
			return sendAllEvents();
		});

	}

	/**
	 * @return the future
	 */
	public Future<String> getFuture() {
		return future;
	}

	/**
	 * Send events in the event tree set
	 * 
	 * @return
	 */
	public String sendAllEvents() {
		StringBuilder resultBuilder = new StringBuilder();
		int counter = 0;
		exchangeConnector.startConnection();
		while (!eventsToSend.isEmpty()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			synchronized (eventsToSend) {
				Event firstEvent = eventsToSend.first();
				exchangeConnector.sendEventToExchange(firstEvent);
				eventsToSend.remove(firstEvent);
				counter++;
			}
		}
		resultBuilder.append("Sent nr of").append(counter).append(" events");
		exchangeConnector.closeConnection();
		return resultBuilder.toString();
	}

	/**
	 * 
	 * Generates time change event and sub events based on random number.
	 * 
	 * @param eventIndexNumber
	 *            - index for which to generate the events
	 * @param generateSubeEvents 
	 * 
	 * @return
	 */
	private SortedSet<Event> generateEvents(long eventIndexNumber, boolean generateSubeEvents) {
		
		SortedSet<Event> eventsInIteration = new TreeSet<>();
		if (generateSubeEvents) {
			double randomNumber = Math.random();
			for (EventType type : eventTypeList) {
				if (type.getProb() > randomNumber) {
					eventsInIteration.add(new Event(eventIndexNumber, type));
				}
			}
		} else {
			eventsInIteration.add(new Event(eventIndexNumber, EventType.TIME_CHANGE));
		}
		// check if we should add from special list
		List<Event> eventsAdded = new ArrayList<>();
		for (Event event : specialEvents) {
			if (event.getTimeStamp() <= eventIndexNumber) {
				eventsInIteration.add(event);
				eventsAdded.add(event);
			}
		}
		specialEvents.removeAll(eventsAdded);
		return eventsInIteration;
	}

	/**
	 * Get nrOfGeneration.
	 * 
	 * @return nrOfGeneration
	 */
	public int getNrOfGeneration() {
		return this.nrOfGeneration;
	}

	/**
	 * Set nrOfGeneration.
	 * 
	 * @param nrOfGeneration
	 */
	public void setNrOfGeneration(int nrOfGeneration) {
		this.nrOfGeneration = nrOfGeneration;
	}

	/**
	 * Get exchangeConnector.
	 * 
	 * @return exchangeConnector
	 */
	public ExchangeConnector getExchangeConnector() {
		return this.exchangeConnector;
	}

	/**
	 * Set exchangeConnector.
	 * 
	 * @param exchangeConnector
	 */
	public void setExchangeConnector(ExchangeConnector exchangeConnector) {
		this.exchangeConnector = exchangeConnector;
	}

	/**
	 * Get eventList.
	 * 
	 * @return eventList
	 */
	public ArrayList<EventType> getEventList() {
		return this.eventTypeList;
	}

	/**
	 * Add special event type to generation
	 */
	public void addSpecial(EventType selectedItem, long indexAt) {
		if (!EventType.TIME_CHANGE.equals(selectedItem)) {
			if (indexAt < counter) {
				indexAt = counter;
			}
			Event event = new Event(indexAt, selectedItem);
			specialEvents.add(event);
		}
	}

}
