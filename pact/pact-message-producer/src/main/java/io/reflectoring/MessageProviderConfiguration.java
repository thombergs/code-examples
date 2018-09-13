package io.reflectoring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
class MessageProviderConfiguration {

	@Bean
	TopicExchange topicExchange() {
		return new TopicExchange("myExchange");
	}


	@Bean
	MessageProducer messageProvider(ObjectMapper objectMapper, MessagePublisher publisher) {
		return new MessageProducer(objectMapper, publisher);
	}

	@Bean
	MessagePublisher messagePublisher(RabbitTemplate rabbitTemplate, TopicExchange topicExchange) {
		return new MessagePublisher(rabbitTemplate, topicExchange);
	}

	@Bean
	SendMessageJob job(MessageProducer messageProducer) {
		return new SendMessageJob(messageProducer);
	}


}
