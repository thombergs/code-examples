package io.pratik.springcloudsqs;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringcloudsqsApplicationTests {
	private static final Logger logger = LoggerFactory.getLogger(SpringcloudsqsApplicationTests.class.getName());
	
	@Autowired
	private MessageSender messageSender;
	
	@Autowired
	private MessageSenderWithTemplate messageSenderWithTemplate;

	@Test
	void contextLoads() {
	}
	
	void send_message_with_message_template() {
		logger.info("test with message template.");
		messageSenderWithTemplate.send("Test Message1");
	}
	
	@Test
	void send_message_with_message_channel() {
		logger.info("test with message channel.");
		messageSender.send("Test msg");
	}

}
