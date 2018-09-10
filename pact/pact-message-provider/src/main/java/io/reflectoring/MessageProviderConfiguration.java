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
	UserCreatedMessageProvider messageProvider(ObjectMapper objectMapper, UserCreatedMessagePublisher publisher) {
		return new UserCreatedMessageProvider(objectMapper, publisher);
	}

	@Bean
	UserCreatedMessagePublisher messagePublisher(RabbitTemplate rabbitTemplate, TopicExchange topicExchange) {
		return new UserCreatedMessagePublisher(rabbitTemplate, topicExchange);
	}

	@Bean
	SendMessageJob job(UserCreatedMessageProvider messageProvider) {
		return new SendMessageJob(messageProvider);
	}


}
