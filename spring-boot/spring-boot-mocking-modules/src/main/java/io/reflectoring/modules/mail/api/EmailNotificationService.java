package io.reflectoring.modules.mail.api;

public interface EmailNotificationService {

    void sendEmail(String to, String subject, String text);

}
