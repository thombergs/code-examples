package io.reflectoring.configuration.mail;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Weight {

	private static final Pattern PATTERN = Pattern.compile("^([0-9]+)(g|kg|t)$");

	private long grams;

	private Weight(long grams){
		this.grams = grams;
	}

	public static Weight fromString(String string) {
		Matcher matcher = PATTERN.matcher(string);
		if (matcher.matches()) {
			Long amount = Long.valueOf(matcher.group(1));
			String unit = matcher.group(2);

			switch (unit) {
				case "g":
					return new Weight(amount);
				case "kg":
					return new Weight(amount * 1_000);
				case "t":
					return new Weight(amount * 1_000 * 1_000);
				default:
					throw new IllegalArgumentException(String.format("invalid weight unit %s", unit));
			}
		}
		throw new IllegalArgumentException(String.format("invalid weight string %s", string));
	}

	public long getGrams() {
		return grams;
	}
}
