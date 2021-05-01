/**
 * 
 */
package io.pratik.springcloudsqs;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.buffered.AmazonSQSBufferedAsyncClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.awspring.cloud.messaging.config.QueueMessageHandlerFactory;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;

/**
 * @author pratikdas
 *
 */
@Configuration
public class CustomSqsConfiguration {
	
	@Bean
	public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync) {
		return new QueueMessagingTemplate(amazonSQSAsync);
	}

	@Bean
	public QueueMessageHandlerFactory queueMessageHandlerFactory(final ObjectMapper mapper,
			final AmazonSQSAsync amazonSQSAsync) {
		final QueueMessageHandlerFactory queueHandlerFactory = new QueueMessageHandlerFactory();
		queueHandlerFactory.setAmazonSqs(amazonSQSAsync);

		queueHandlerFactory.setArgumentResolvers(
				Collections.singletonList(new PayloadMethodArgumentResolver(jackson2MessageConverter(mapper))));
		return queueHandlerFactory;
	}

	private MessageConverter jackson2MessageConverter(final ObjectMapper mapper) {

		final MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();

		// set strict content type match to false to enable the listener to handle AWS events
		converter.setStrictContentTypeMatch(false);
		converter.setObjectMapper(mapper);
		return converter;
	}
}
