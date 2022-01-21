package io.reflectoring.featureflags.launchdarkly;

import com.launchdarkly.sdk.LDUser;
import com.launchdarkly.sdk.server.LDClient;
import io.reflectoring.featureflags.FeatureFlagService;
import io.reflectoring.featureflags.web.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Component("launchdarkly")
@Primary
public class LaunchDarklyFeatureFlagService implements FeatureFlagService {

    private final Logger logger = LoggerFactory.getLogger(LaunchDarklyFeatureFlagService.class);
    private final LDClient launchdarklyClient;
    private final UserSession userSession;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public LaunchDarklyFeatureFlagService(LDClient launchdarklyClient, UserSession userSession) {
        this.launchdarklyClient = launchdarklyClient;
        this.userSession = userSession;
    }

    /**
     * Initializing LaunchDarkly users so that they are in the expected state at the start of the app.
     * This is for testing purposes only.
     */
    @PostConstruct
    public void initUsers() {
        launchdarklyClient.identify(new LDUser.Builder("alice")
                .custom("clicked", true)
                .build());
        launchdarklyClient.identify(new LDUser.Builder("bob")
                .custom("clicked", true)
                .build());
        launchdarklyClient.identify(new LDUser.Builder("charlie")
                .custom("clicked", true)
                .build());
    }

    @Override
    public Boolean isGlobalBooleanFeatureActive() {
        return launchdarklyClient.boolVariation("global-boolean-flag", getLaunchdarklyUserFromSession(), false);
    }

    @Override
    public Boolean isPercentageRolloutActive() {
        return launchdarklyClient.boolVariation("user-based-percentage-rollout", getLaunchdarklyUserFromSession(), false);
    }

    @Override
    public Integer getNumberFlag() {
        return null;
    }

    @Override
    public Boolean isUserActionTargetedFeatureActive() {
        return launchdarklyClient.boolVariation("user-clicked-flag", getLaunchdarklyUserFromSession(), false);
    }

    @Override
    public Boolean isNewServiceEnabled() {
        return true;
    }

    @Override
    public Optional<LocalDateTime> currentDateForWelcomeMessage() {
        String stringValue = launchdarklyClient.stringVariation("current-date-for-welcome-message", getLaunchdarklyUserFromSession(), "false");

        if ("false".equals(stringValue)) {
            return Optional.empty();
        }

        if ("now".equals(stringValue)) {
            return Optional.of(LocalDateTime.now());
        }

        try {
            return Optional.of(LocalDateTime.parse(stringValue, dateFormatter));
        } catch (DateTimeParseException e) {
            logger.warn("could not parse date ... falling back to current date", e);
            return Optional.of(LocalDateTime.now());
        }
    }

    @Override
    public Optional<LocalDateTime> currentDateForWelcomeEmails() {
        String stringValue = launchdarklyClient.stringVariation("current-date-for-welcome-email", getLaunchdarklyUserFromSession(), "false");

        if ("false".equals(stringValue)) {
            return Optional.empty();
        }

        if ("now".equals(stringValue)) {
            return Optional.of(LocalDateTime.now());
        }

        try {
            return Optional.of(LocalDateTime.parse(stringValue, dateFormatter));
        } catch (DateTimeParseException e) {
            logger.warn("could not parse date ... falling back to current date", e);
            return Optional.of(LocalDateTime.now());
        }
    }

    @Override
    public Optional<LocalDateTime> currentDateForWelcomeMessage(String username) {
        String stringValue = launchdarklyClient.stringVariation("current-date-for-welcome-message", new LDUser.Builder(username).build(), "false");

        if ("false".equals(stringValue)) {
            return Optional.empty();
        }

        if ("now".equals(stringValue)) {
            return Optional.of(LocalDateTime.now());
        }

        try {
            return Optional.of(LocalDateTime.parse(stringValue, dateFormatter));
        } catch (DateTimeParseException e) {
            logger.warn("could not parse date ... falling back to current date", e);
            return Optional.of(LocalDateTime.now());
        }
    }

    @Override
    public Optional<LocalDateTime> currentDateForWelcomeEmails(String username) {
        String stringValue = launchdarklyClient.stringVariation("current-date-for-welcome-email", new LDUser.Builder(username).build(), "false");

        if ("false".equals(stringValue)) {
            return Optional.empty();
        }

        if ("now".equals(stringValue)) {
            return Optional.of(LocalDateTime.now());
        }

        try {
            return Optional.of(LocalDateTime.parse(stringValue, dateFormatter));
        } catch (DateTimeParseException e) {
            logger.warn("could not parse date ... falling back to current date", e);
            return Optional.of(LocalDateTime.now());
        }
    }

    private LDUser getLaunchdarklyUserFromSession() {
        return new LDUser.Builder(userSession.getUsername())
                .custom("clicked", userSession.hasClicked())
                .build();
    }


}
