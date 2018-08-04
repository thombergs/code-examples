package io.reflectoring.logging;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class TruncatedThreadConverter extends ClassicConverter {

	@Override
	public String convert(ILoggingEvent event) {
		String maxLengthString = getFirstOption();
		int maxLength = Integer.parseInt(maxLengthString);
		String threadName = event.getThreadName();

		if (threadName.length() <= maxLength) {
			return threadName + generateSpaces(maxLength - threadName.length());
		} else {
			return "..." + threadName.substring(threadName.length() - maxLength + 3);
		}
	}

	private String generateSpaces(int count) {
		return Stream.generate(() -> " ").limit(count).collect(Collectors.joining());
	}

}
