
package connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import event.Event;
import util.EventSerializationUtil;

/**
 * TODO DESCRIPTION
 * 
 * @author Geraj
 */
public class ExchangeConnector {
  
  /** EXCHANGE_NAME */
  private final static String EXCHANGE_NAME = "event_exchange";
  
  /** Exchange */
  private String exchange;

  /** host */
  private String host;
  
  /**
   */
  private Channel channel;
  
  /** connection */
  private Connection connection; 
  
  /**
   * 
   * Constructs a new instance.
   */
  public ExchangeConnector() {
    exchange = EXCHANGE_NAME;
    host = "localhost";
  }
  /**
   * 
   * Constructs a new instance.
   * @param exchangeName
   * @param host
   */
  public ExchangeConnector(String exchangeName, String host) {
    this.exchange = exchangeName;
    this.host = host;
  }
  
  /**
   * 
   * TODO DESCRIPTION
   */
  public void startConnection()  {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(host);
    try {
      Connection connection = factory.newConnection();
      Channel channel = connection.createChannel();
      this.channel = channel;
      this.connection = connection;
      channel.exchangeDeclare(exchange, "fanout");
    } catch (IOException | TimeoutException ex) {
      ex.printStackTrace();
      System.out.println(ex);
    }

  }
  
  /**
   * 
   * TODO DESCRIPTION

   */
  public void closeConnection()  {
    try {
      this.channel.close();
      this.connection.close();
    } catch (IOException | TimeoutException e) {
      // TODO Add your own exception handling here, consider logging
      e.printStackTrace();
    }
  
  }
  
  /**
   * 
   * TODO DESCRIPTION
   * @param event
   */
  public void sendEventToExchange(Event event) {
    try {
    	this.channel.basicPublish( exchange , "", null, EventSerializationUtil.toByteArray(event));
    } catch (IOException ex) {
      ex.printStackTrace();
      System.out.println(ex);
    }
  }
  
}
