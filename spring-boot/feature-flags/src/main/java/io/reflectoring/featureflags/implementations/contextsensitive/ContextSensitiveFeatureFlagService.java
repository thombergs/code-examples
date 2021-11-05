package io.reflectoring.featureflags.implementations.contextsensitive;

import io.reflectoring.featureflags.implementations.FeatureFlagService;
import io.reflectoring.featureflags.implementations.contextsensitive.Feature.RolloutStrategy;
import io.reflectoring.featureflags.web.UserSession;
import org.jetbrains.annotations.Nullable;
import org.springframework.jdbc.core.JdbcTemplate;

public class ContextSensitiveFeatureFlagService implements FeatureFlagService {

    private final JdbcTemplate jdbcTemplate;
    private final UserSession userSession;

    public ContextSensitiveFeatureFlagService(JdbcTemplate jdbcTemplate, UserSession userSession) {
        this.jdbcTemplate = jdbcTemplate;
        this.userSession = userSession;
    }

    @Override
    public Boolean featureOne() {
        Feature feature = getFeatureFromDatabase();
        if (feature == null) {
            return Boolean.FALSE;
        }
        return feature.evaluateBoolean(userSession.getUsername());
    }

    @Override
    public Integer featureTwo() {
        Feature feature = getFeatureFromDatabase();
        if (feature == null) {
            return null;
        }
        return feature.evaluateInt(userSession.getUsername());
    }

    @Nullable
    private Feature getFeatureFromDatabase() {
        return jdbcTemplate.query("select targeting, value, defaultValue, percentage from features where feature_key='FEATURE_ONE'", resultSet -> {
            if (!resultSet.next()) {
                return null;
            }

            RolloutStrategy rolloutStrategy = Enum.valueOf(RolloutStrategy.class, resultSet.getString(1));
            String value = resultSet.getString(2);
            String defaultValue = resultSet.getString(3);
            int percentage = resultSet.getInt(4);

            return new Feature(rolloutStrategy, value, defaultValue, percentage);
        });
    }
}
