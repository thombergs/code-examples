package org.example.silenum.mockito.infrastructure.configuration.persistence;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "zonedDateTimeProvider")
@EntityScan(basePackages = "org.example.silenum.mockito.infrastructure.database.entity")
@EnableJpaRepositories(basePackages = "org.example.silenum.mockito.infrastructure.database.repository")
public class PersistenceConfiguration {

    @Bean(name = "zonedDateTimeProvider")
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(ZonedDateTime.now());
    }

}
