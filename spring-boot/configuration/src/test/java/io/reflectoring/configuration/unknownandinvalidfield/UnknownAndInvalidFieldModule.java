package io.reflectoring.configuration.unknownandinvalidfield;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootConfiguration
@EnableConfigurationProperties(UnkownAndInvalidFieldProperties.class)
class UnknownAndInvalidFieldModule {
}
