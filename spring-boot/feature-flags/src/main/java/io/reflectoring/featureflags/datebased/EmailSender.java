package io.reflectoring.featureflags.datebased;

import io.reflectoring.featureflags.FeatureFlagService;
import io.reflectoring.featureflags.web.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class EmailSender {

    private final Logger logger = LoggerFactory.getLogger(EmailSender.class);
    private final FeatureFlagService featureFlagService;

    public EmailSender(FeatureFlagService featureFlagService, UserSession userSession) {
        this.featureFlagService = featureFlagService;
    }

    @Scheduled(fixedDelay = 10000)
    public void sendWelcomeEmails() {
        for (User user : getUsers()) {
            Optional<LocalDateTime> now = featureFlagService.currentDateForWelcomeEmails(user.name);
            if (now.isEmpty()) {
                logger.info("not sending email to user {}", user.name);
                continue;
            }
            if (user.registrationDate.isBefore(now.get().minusDays(14L).toLocalDate())) {
                sendEmail(user, "Welcome email after 14 days");
            } else if (user.registrationDate.isBefore(now.get().minusDays(7L).toLocalDate())) {
                sendEmail(user, "Welcome email after 7 days");
            } else if (user.registrationDate.isBefore(now.get().minusDays(1L).toLocalDate())) {
                sendEmail(user, "Welcome email after 1 day");
            }
        }
    }

    private void sendEmail(User user, String message) {
        logger.info("sending email to user {} with message {}", user.name, message);
    }

    private List<User> getUsers() {
        return Arrays.asList(
                new User("alice", LocalDate.now().minusDays(1)),
                new User("bob", LocalDate.now().minusDays(2)),
                new User("charlie", LocalDate.now().minusDays(3)),
                new User("ben", LocalDate.now().minusDays(4)),
                new User("tom", LocalDate.now().minusDays(5)),
                new User("hugo", LocalDate.now().minusDays(6))
        );
    }

    static class User {

        private final String name;
        private final LocalDate registrationDate;

        User(String name, LocalDate registrationDate) {
            this.name = name;
            this.registrationDate = registrationDate;
        }
    }

}
