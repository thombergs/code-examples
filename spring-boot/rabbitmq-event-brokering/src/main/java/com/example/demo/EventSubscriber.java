package com.example.demo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventSubscriber {

  private Logger logger = LoggerFactory.getLogger(EventSubscriber.class);

  public void receive(String message) {
    logger.info("Received message '{}'", message);
  }


}
