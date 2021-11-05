package io.reflectoring.featureflags.launchdarkly;

import com.launchdarkly.sdk.LDUser;
import com.launchdarkly.sdk.server.LDClient;
import io.reflectoring.featureflags.FeatureFlagService;
import io.reflectoring.featureflags.web.UserSession;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component("launchdarkly")
@Primary
public class LaunchDarklyFeatureFlagService implements FeatureFlagService {

    private final LDClient launchdarklyClient;
    private final UserSession userSession;

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

    private LDUser getLaunchdarklyUserFromSession() {
        return new LDUser.Builder(userSession.getUsername())
                .custom("clicked", userSession.hasClicked())
                .build();
    }
}
