/*
 * Copyright (C) TBA BV
 * All rights reserved.
 * www.tba.nl
 */
package producer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

import connection.ExchangeConnector;
import event.Event;
import event.EventType;

/**
 * TODO DESCRIPTION
 * 
 * @author Geraj
 */
public class SimulationEventProducer {
  /** List of event to generate */
  private final ArrayList<EventType> eventList;

  /** nr of time to generate events */
  private int nrOfGeneration;

  /** Connection */
  private ExchangeConnector exchangeConnector = new ExchangeConnector();

  /**
   * 
   * Constructs a new instance.
   * 
   * @param nrOfGeneration
   */
  public SimulationEventProducer(int nrOfGeneration) {
    this.nrOfGeneration = nrOfGeneration;
    this.eventList = new ArrayList<EventType>(Arrays.asList(EventType.values()));
  }

  /**
   * 
   * Constructs a new instance.
   * 
   * @param nrOfGeneration
   * @param eventList
   */
  public SimulationEventProducer(int nrOfGeneration, ArrayList<EventType> eventList) {
    this.nrOfGeneration = nrOfGeneration;
    this.eventList = eventList;
  }

  /**
   * 
   * TODO DESCRIPTION
   */
  public void startAndSend() {
    exchangeConnector.startConnection();
    for (int i = 0; i < nrOfGeneration; i++) {
      for (Event event : generateEvents(i)) {
        exchangeConnector.sendEventToExchange(event);
      }
    }
    exchangeConnector.closeConnection();
  }

  /**
   * 
   * TODO DESCRIPTION
   * @param orderNumber 
   * 
   * @return
   */
  private SortedSet<Event> generateEvents(int orderNumber) {
    double randomNumber = Math.random();
    SortedSet<Event> eventsInIteration = new TreeSet<>();
    for (EventType type : eventList) {
      if (type.getProb() > randomNumber) {
        eventsInIteration.add(new Event(orderNumber, type));
      }
    }    
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
    return this.eventList;
  }

}
