package io.reflectoring;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * Publishes a String message to RabbitMQ.
 */
class UserCreatedMessagePublisher {

	private RabbitTemplate rabbitTemplate;

	private TopicExchange topicExchange;

	UserCreatedMessagePublisher(RabbitTemplate rabbitTemplate, TopicExchange topicExchange) {
		this.rabbitTemplate = rabbitTemplate;
		this.topicExchange = topicExchange;
	}

	void publishMessage(String message, String routingKey) {
		rabbitTemplate.convertAndSend(topicExchange.getName(), routingKey, message);
	}

}
