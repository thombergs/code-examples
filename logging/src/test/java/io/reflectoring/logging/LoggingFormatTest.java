package io.reflectoring.logging;

import io.reflectoring.descriptivelogger.LoggerFactory;
import org.junit.jupiter.api.Test;

public class LoggingFormatTest {

	private MyLogger logger = LoggerFactory.getLogger(MyLogger.class, LoggingFormatTest.class);

	@Test
	public void testLogPattern(){
		Thread.currentThread().setName("very-long-thread-name");
		logger.logDebugMessage();
		Thread.currentThread().setName("short");
		logger.logInfoMessage();
		logger.logMessageWithLongId();
	}
}
