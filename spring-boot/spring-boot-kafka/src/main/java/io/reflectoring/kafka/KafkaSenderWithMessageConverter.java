package io.reflectoring.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class KafkaSenderWithMessageConverter {

	private final Logger LOG = LoggerFactory.getLogger(KafkaSenderWithMessageConverter.class);

	@Autowired
	private KafkaTemplate<String, ?> kafkaTemplate;

	void sendMessageWithConverter(Message<?> user) {
		LOG.info("Sending With Message Converter : {}", user);
		LOG.info("--------------------------------");

		kafkaTemplate.send(user);
	}

}
