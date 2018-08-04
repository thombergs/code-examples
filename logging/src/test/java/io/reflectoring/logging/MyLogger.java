package io.reflectoring.logging;

import io.reflectoring.descriptivelogger.DescriptiveLogger;
import io.reflectoring.descriptivelogger.LogMessage;
import org.slf4j.event.Level;

@DescriptiveLogger
public interface MyLogger {

	@LogMessage(level=Level.DEBUG, message="This is a DEBUG message.", id=14556)
	void logDebugMessage();

	@LogMessage(level=Level.INFO, message="This is an INFO message.", id=5456)
	void logInfoMessage();

	@LogMessage(level=Level.ERROR, message="This is an ERROR message with a very long ID.", id=1548654)
	void logMessageWithLongId();

}
