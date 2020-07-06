package io.reflectoring.kafka;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;

/**
 * KafkaProducerConfig
 */
@Configuration
class KafkaProducerConfig {

	@Value("${io.reflectoring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Bean
	Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return props;
	}

	@Bean
	ProducerFactory<String, String> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	KafkaTemplate<String, String> kafkaTemplate() {
		KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory());
		kafkaTemplate.setMessageConverter(new StringJsonMessageConverter());
		kafkaTemplate.setDefaultTopic("reflectoring-user");
		return kafkaTemplate;
	}

	@Bean
	public RoutingKafkaTemplate routingTemplate(GenericApplicationContext context) {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
		DefaultKafkaProducerFactory<Object, Object> bytesPF = new DefaultKafkaProducerFactory<>(props);
		context.registerBean(DefaultKafkaProducerFactory.class, "bytesPF", bytesPF);

		Map<Pattern, ProducerFactory<Object, Object>> map = new LinkedHashMap<>();
		map.put(Pattern.compile(".*-bytes"), bytesPF);
		return new RoutingKafkaTemplate(map);
	}

	@Bean
	public ProducerFactory<String, User> userProducerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public KafkaTemplate<String, User> userKafkaTemplate() {
		return new KafkaTemplate<>(userProducerFactory());
	}
}