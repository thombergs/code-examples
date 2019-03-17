package io.reflectoring.configuration.mail;

import org.springframework.core.convert.converter.Converter;

class WeightConverter implements Converter<String, Weight> {

	@Override
	public Weight convert(String source) {
		return Weight.fromString(source);
	}

}
