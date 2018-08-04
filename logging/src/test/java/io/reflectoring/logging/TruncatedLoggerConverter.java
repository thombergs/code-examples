package io.reflectoring.logging;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import ch.qos.logback.classic.pattern.LoggerConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class TruncatedLoggerConverter extends LoggerConverter {

	@Override
	public String convert(ILoggingEvent event) {
		String maxLengthString = getFirstOption();
		int maxLength = Integer.parseInt(maxLengthString);
		String loggerName = super.convert(event);
		if (loggerName.length() <= maxLength) {
			return loggerName + generateSpaces(maxLength - loggerName.length());
		} else {
			return "..." + loggerName.substring(loggerName.length() - maxLength + 3);
		}
	}

	private String generateSpaces(int count) {
		return Stream.generate(() -> " ").limit(count).collect(Collectors.joining());
	}

}
