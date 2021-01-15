package io.reflectoring.modules.mail.internal;

import io.reflectoring.modules.mail.api.EmailNotificationService;
import org.springframework.stereotype.Component;

class EmailNotificationServiceImpl implements EmailNotificationService {

    private final MailClient mailClient;

    public EmailNotificationServiceImpl(MailClient mailClient) {
        this.mailClient = mailClient;
    }

    @Override
    public void sendEmail(String to, String subject, String text) {
        // send an email         
    }

}
