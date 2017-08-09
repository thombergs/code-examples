package com.example.framework;

import au.com.dius.pact.provider.junit.PactRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration AutoConfiguration} that is activated when
 * the pact-jvm-provider-junit module is in the classpath.
 * </p>
 * <p>
 * This configuration provides a {@link DataSource} which allows to switch between multiple database states.
 * Each database state is defined by a name and a set of SQL scripts which set the database into the desired state.
 * The database states are configured via properties:
 * <pre>
 *   pact.databaseStates.&lt;NAME&gt;=/path/to/script1.sql,/path/to/script2.sql,...
 * </pre>
 * The NAME of the databaseState can be used with {@link DatabaseStateHolder#setCurrentDatabaseState(String)}
 * to set the {@link DataSource} into that state.
 * </p>
 */
@Configuration
@ConditionalOnClass(PactRunner.class)
@EnableConfigurationProperties(PactProperties.class)
public class PactDatabaseStatesAutoConfiguration {

  @Bean
  public DataSource dataSource(PactProperties pactProperties) {
    AbstractRoutingDataSource dataSource = new AbstractRoutingDataSource() {
      @Override
      protected Object determineCurrentLookupKey() {
        return DatabaseStateHolder.getCurrentDatabaseState();
      }
    };

    Map<Object, Object> targetDataSources = new HashMap<>();
    dataSource.setTargetDataSources(targetDataSources);

    // create a DataSource for each DatabaseState
    for (DatabaseState databaseState : pactProperties.getDatabaseStatesList()) {
      DataSource ds = DataSourceBuilder
              .create()
              .url(String.format("jdbc:h2:mem:%s;DB_CLOSE_ON_EXIT=FALSE", databaseState.getStateName()))
              .driverClassName("org.h2.Driver")
              .username("sa")
              .password("")
              .build();
      targetDataSources.put(databaseState.getStateName(), ds);
      DatabaseStateHolder.setCurrentDatabaseState(databaseState.getStateName());
    }

    return dataSource;
  }

  @Bean
  public DatabaseStatesInitializer databaseStatesInitializer(DataSource routingDataSource, PactProperties pactProperties) {
    return new DatabaseStatesInitializer(routingDataSource, pactProperties.getDatabaseStatesList());
  }


}
