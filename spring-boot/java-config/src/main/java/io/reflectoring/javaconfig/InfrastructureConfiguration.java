package io.reflectoring.javaconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({DatabaseConfiguration.class, AppCacheConfiguration.class, SecurityConfiguration.class})
public class InfrastructureConfiguration {
}
