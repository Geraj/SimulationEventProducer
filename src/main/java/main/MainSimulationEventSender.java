/*
 * Copyright (C) TBA BV
 * All rights reserved.
 * www.tba.nl
 */
package main;

import java.awt.Window;
import java.util.concurrent.TimeoutException;

import producer.SimulationEventProducer;


/**
 * TODO DESCRIPTION
 * 
 * @author Geraj
 */
public class MainSimulationEventSender {
  private final static String EXCHANGE_NAME = "event_exchange";

  /**
   * TODO DESCRIPTION
   * 
   * @param args
   * @throws TimeoutException
   */
  public static void main(String[] args) {


    SimulationEventProducer producer = new SimulationEventProducer(10);
    producer.startAndSend();

    // ConnectionFactory factory = new ConnectionFactory();
    // factory.setHost("localhost");
    // Connection connection = factory.newConnection();
    // Channel channel = connection.createChannel();
    // channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
    //
    //
    // //channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    // //String message = "Hello!";
    // Event simEvent = new Event(1, EventType.TIME_CHANGE);
    // channel.basicPublish( EXCHANGE_NAME , "", null,
    // EventSerializationUtil.toByteArray(simEvent));
    // //channel.basicPublish(EXCHANGE_NAME, "", null,
    // EventSerializationUtil.toByteArray(simEvent));
    // System.out.println(" [x] Sent '" + simEvent + "'");
    //
    //
    //
    //
    // channel.close();
    // connection.close();
  }

}
