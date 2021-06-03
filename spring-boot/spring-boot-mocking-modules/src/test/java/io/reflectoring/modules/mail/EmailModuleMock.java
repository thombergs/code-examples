package io.reflectoring.modules.mail;

import io.reflectoring.modules.mail.api.EmailNotificationService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

@TestConfiguration
public class EmailModuleMock {

    private final EmailNotificationService emailNotificationServiceMock = Mockito.mock(EmailNotificationService.class);

    @Bean
    @Primary
    EmailNotificationService emailNotificationServiceMock() {
        return emailNotificationServiceMock;
    }

    public void givenSendMailSucceeds() {
        // nothing to do, the mock will simply return 0
    }

    public void givenSendMailThrowsError() {
        doThrow(new RuntimeException("error when sending mail"))
                .when(emailNotificationServiceMock).sendEmail(anyString(), anyString(), anyString());
    }

    public void assertSentMailContains(String repositoryUrl) {
        verify(emailNotificationServiceMock).sendEmail(anyString(), anyString(), contains(repositoryUrl));
    }

    public void assertNoMailSent() {
        verify(emailNotificationServiceMock, never()).sendEmail(anyString(), anyString(), anyString());
    }

}
