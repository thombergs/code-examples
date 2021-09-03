package io.reflectoring.featureflags.togglz;

import io.reflectoring.featureflags.web.UserSession;
import org.springframework.stereotype.Component;
import org.togglz.core.user.FeatureUser;
import org.togglz.core.user.UserProvider;

@Component
public class TogglzUserProvider implements UserProvider {

    private final UserSession userSession;

    public TogglzUserProvider(UserSession userSession) {
        this.userSession = userSession;
    }

    @Override
    public FeatureUser getCurrentUser() {
        return new FeatureUser() {
            @Override
            public String getName() {
                return userSession.getUsername();
            }

            @Override
            public boolean isFeatureAdmin() {
                return false;
            }

            @Override
            public Object getAttribute(String attributeName) {
                if (attributeName.equals("clicked")) {
                    return userSession.hasClicked();
                }
                return null;
            }
        };
    }

}
