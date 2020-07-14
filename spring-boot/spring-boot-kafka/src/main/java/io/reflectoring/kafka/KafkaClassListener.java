package io.reflectoring.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(id = "class-level", topics = "reflectoring-1")
class KafkaClassListener {

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@KafkaHandler
	void listen(String message) {
		LOG.info("ClassLevel KafkaHandler[String] {}", message);
	}

	@KafkaHandler(isDefault = true)
	void listenDefault(Object object) {
		LOG.info("ClassLevel KafkaHandler[Default] {}", object);
	}
}
