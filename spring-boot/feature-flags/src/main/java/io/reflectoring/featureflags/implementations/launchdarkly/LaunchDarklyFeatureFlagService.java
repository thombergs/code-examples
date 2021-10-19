package io.reflectoring.featureflags.implementations.launchdarkly;

import com.launchdarkly.sdk.LDUser;
import com.launchdarkly.sdk.server.LDClient;
import io.reflectoring.featureflags.implementations.FeatureFlagService;
import io.reflectoring.featureflags.web.UserSession;

public class LaunchDarklyFeatureFlagService implements FeatureFlagService {

    private final LDClient launchdarklyClient;
    private final UserSession userSession;

    public LaunchDarklyFeatureFlagService(LDClient launchdarklyClient, UserSession userSession) {
        this.launchdarklyClient = launchdarklyClient;
        this.userSession = userSession;
    }

    @Override
    public Boolean featureOne() {
        return launchdarklyClient.boolVariation("feature-one", getLaunchdarklyUserFromSession(), false);
    }

    @Override
    public Integer featureTwo() {
        return launchdarklyClient.intVariation("feature-two", getLaunchdarklyUserFromSession(), 0);
    }

    private LDUser getLaunchdarklyUserFromSession() {
        return new LDUser.Builder(userSession.getUsername())
                .build();
    }
}
