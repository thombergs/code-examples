package io.reflectoring.featureflags.implementations.database;

import io.reflectoring.featureflags.implementations.FeatureFlagService;
import org.springframework.jdbc.core.JdbcTemplate;

public class DatabaseBackedFeatureFlagService implements FeatureFlagService {

    private JdbcTemplate jdbcTemplate;

    @Override
    public Boolean featureOne() {
        return jdbcTemplate.query("select value from features where feature_key='FEATURE_ONE'", resultSet -> {
            if(!resultSet.next()){
                return false;
            }

            boolean value = Boolean.parseBoolean(resultSet.getString(1));
            return value ? Boolean.TRUE : Boolean.FALSE;
        });
    }

    @Override
    public Integer featureTwo() {
        return jdbcTemplate.query("select value from features where feature_key='FEATURE_TWO'", resultSet -> {
            if(!resultSet.next()){
                return null;
            }

            return Integer.valueOf(resultSet.getString(1));
        });
    }
}
