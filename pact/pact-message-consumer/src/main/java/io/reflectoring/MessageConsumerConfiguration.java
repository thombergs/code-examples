package io.reflectoring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConsumerConfiguration {

	private static final String QUEUE_NAME = "myQueue";

	private static final String EXCHANGE_NAME = "myExchange";

	@Bean
	public TopicExchange receiverExchange() {
		return new TopicExchange(EXCHANGE_NAME);
	}

	@Bean
	public Queue queue() {
		return new Queue(QUEUE_NAME);
	}

	@Bean
	public Binding binding(Queue eventReceivingQueue, TopicExchange receiverExchange) {
		return BindingBuilder
						.bind(eventReceivingQueue)
						.to(receiverExchange)
						.with("*.*");
	}

	@Bean
	public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
																									MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConsumerStartTimeout(1000); // we don't want to wait in this example project
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(QUEUE_NAME);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	public MessageListenerAdapter listenerAdapter(MessageConsumer messageConsumer) {
		return new MessageListenerAdapter(messageConsumer, "consumeStringMessage");
	}

	@Bean
	public MessageConsumer eventReceiver(ObjectMapper objectMapper) {
		return new MessageConsumer(objectMapper);
	}

}
