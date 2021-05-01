package io.reflectoring.modules.mail.internal;

import io.reflectoring.modules.mail.api.EmailNotificationService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(EmailModuleConfigurationProperties.class)
public class EmailModuleConfiguration {

    private final EmailModuleConfigurationProperties configurationProperties;

    public EmailModuleConfiguration(EmailModuleConfigurationProperties configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    @Bean
    EmailNotificationService emailNotificationService(MailClient mailClient) {
        return new EmailNotificationServiceImpl(mailClient);
    }

    @Bean
    MailClient mailClient() {
        return new MailClient(configurationProperties.isEnabled());
    }

}
