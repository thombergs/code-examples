package io.reflectoring.client.registration.rpc;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@ConditionalOnProperty(
        value = "scheduling.enable",
        havingValue = "true",
        matchIfMissing = true
)
@Configuration
@EnableScheduling
public class SchedulingConfiguration {
}
