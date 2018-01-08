package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("subscriber")
@Configuration
public class EventSubscriberConfiguration implements ApplicationListener<ApplicationReadyEvent> {

  private Logger logger = LoggerFactory.getLogger(EventSubscriberConfiguration.class);

  @Value("${subscriber.queue}")
  private String queueName;

  @Value("${subscriber.routingKey}")
  private String routingKey;

  @Bean
  public TopicExchange receiverExchange() {
    return new TopicExchange("eventExchange");
  }

  @Bean
  public Queue eventReceivingQueue() {
    if (queueName == null) {
      throw new IllegalStateException("No queue to listen to! Please specify the name of the queue to listen to with the property 'subscriber.queue'");
    }
    return new Queue(queueName);
  }

  @Bean
  public Binding binding(Queue eventReceivingQueue, TopicExchange receiverExchange) {
    if (routingKey == null) {
      throw new IllegalStateException("No events to listen to! Please specify the routing key for the events to listen to with the property 'subscriber.routingKey' (see EventPublisher for available routing keys).");
    }
    return BindingBuilder
            .bind(eventReceivingQueue)
            .to(receiverExchange)
            .with(routingKey);
  }

  @Bean
  public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                                  MessageListenerAdapter listenerAdapter) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(queueName);
    container.setMessageListener(listenerAdapter);
    return container;
  }

  @Bean
  public MessageListenerAdapter listenerAdapter(EventSubscriber eventSubscriber) {
    return new MessageListenerAdapter(eventSubscriber, "receive");
  }

  @Bean
  public EventSubscriber eventReceiver() {
    return new EventSubscriber();
  }

  @Override
  public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
    logger.info("SUBSCRIBING TO EVENTS MATCHING KEY '{}' FROM QUEUE '{}'!", routingKey, queueName);
  }
}
