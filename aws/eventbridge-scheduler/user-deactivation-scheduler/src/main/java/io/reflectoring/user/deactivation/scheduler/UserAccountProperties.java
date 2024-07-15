package io.reflectoring.user.deactivation.scheduler;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "io.reflectoring.user.account")
class UserAccountProperties {

    @NotNull(message = "User account deactivation delay must be configured")
    private Duration deactivationDelay;

}