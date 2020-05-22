package io.reflectoring.validation;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AppProperties.class)
class AppConfiguration {

    @Bean
    public static ReportEmailAddressValidator configurationPropertiesValidator() {
        return new ReportEmailAddressValidator();
    }

}
