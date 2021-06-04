/**
 * 
 */
package io.pratik.springcloudsqs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQSAsync;

import io.awspring.cloud.messaging.core.QueueMessageChannel;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;

/**
 * @author pratikdas
 *
 */
@Service
public class MessageSender {
	private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);

	private static final String QUEUE_NAME = "https://sqs.us-east-1.amazonaws.com/474715013863/testQueue";

	@Autowired
	private final AmazonSQSAsync amazonSqs;

	@Autowired
	public MessageSender(final AmazonSQSAsync amazonSQSAsync) {
		this.amazonSqs = amazonSQSAsync;
	}

	public boolean send(final String messagePayload) {
		MessageChannel messageChannel = new QueueMessageChannel(amazonSqs, QUEUE_NAME);

		Message<String> msg = MessageBuilder.withPayload(messagePayload)
				.setHeader("sender", "app1")
				.setHeaderIfAbsent("country", "AE")
				.build();
       
		long waitTimeoutMillis = 5000;
		boolean sentStatus = messageChannel.send(msg,waitTimeoutMillis);
		logger.info("message sent");
		return sentStatus;
	}

}
