package io.reflectoring.configuration.unknownfield;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@SpringBootConfiguration
@EnableConfigurationProperties(UnkownFieldProperties.class)
class UnknownFieldModule {
}
