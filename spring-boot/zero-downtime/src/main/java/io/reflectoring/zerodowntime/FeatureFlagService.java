package io.reflectoring.zerodowntime;

import com.launchdarkly.sdk.LDUser;
import com.launchdarkly.sdk.server.LDClient;
import org.springframework.stereotype.Component;

@Component
public class FeatureFlagService {

    private final LDClient launchdarklyClient;

    public FeatureFlagService(LDClient launchdarklyClient) {
        this.launchdarklyClient = launchdarklyClient;
    }

    public Boolean writeToNewCustomerSchema() {
        return true;
    }

    public Boolean readFromNewCustomerSchema() {
        return false;
    }

}