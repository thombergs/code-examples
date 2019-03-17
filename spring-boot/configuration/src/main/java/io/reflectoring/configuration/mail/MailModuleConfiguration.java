package io.reflectoring.configuration.mail;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MailModuleProperties.class)
class MailModuleConfiguration {

	@Bean
	@ConfigurationPropertiesBinding
	public WeightConverter weightConverter() {
		return new WeightConverter();
	}

}
