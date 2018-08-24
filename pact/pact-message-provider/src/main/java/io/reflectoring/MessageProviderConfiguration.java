package io.reflectoring;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class MessageProviderConfiguration {

	@Bean
	public TopicExchange senderTopicExchange() {
		return new TopicExchange("myExchange");
	}


	@Bean
	public MessageProvider eventPublisher(RabbitTemplate rabbitTemplate, TopicExchange senderTopicExchange) {
		return new MessageProvider(rabbitTemplate, senderTopicExchange);
	}

}
