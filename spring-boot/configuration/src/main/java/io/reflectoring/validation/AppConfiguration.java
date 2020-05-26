package io.reflectoring.validation;

import io.reflectoring.validation.thirdparty.ThirdPartyComponentProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@EnableConfigurationProperties(AppProperties.class)
class AppConfiguration {

    @Bean
    public static ReportEmailAddressValidator configurationPropertiesValidator() {
        return new ReportEmailAddressValidator();
    }

    @Bean
    @Validated
    @ConfigurationProperties(prefix = "app.third-party.properties")
    public ThirdPartyComponentProperties thirdPartyComponentProperties() {
        return new ThirdPartyComponentProperties();
    }

}
