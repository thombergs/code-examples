package io.reflectoring.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
class KafkaTopicConfig {

	@Value("${io.reflectoring.kafka.topic-1}")
	private String topic1;

	@Value("${io.reflectoring.kafka.topic-2}")
	private String topic2;
	
	@Value("${io.reflectoring.kafka.topic-3}")
	private String topic3;
	
	@Value("${io.reflectoring.kafka.topic-4}")
	private String topic4;

	@Bean
	NewTopic topic1() {
		return TopicBuilder.name(topic1).build();
	}

	@Bean
	NewTopic topic2() {
		return TopicBuilder.name(topic2).build();
	}
	
	@Bean
	NewTopic topic3() {
		return TopicBuilder.name(topic3).build();
	}
	
	@Bean
	NewTopic topicUser() {
		return TopicBuilder.name(topic4).build();
	}
	
	@Bean
	NewTopic topicBytes() {
		return TopicBuilder.name("reflectoring-bytes").build();
	}
	
	@Bean
	NewTopic others() {
		return TopicBuilder.name("reflectoring-others").build();
	}
}
