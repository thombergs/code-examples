package com.example.framework;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;

/**
 * Initializes a {@link DataSource} to a specified List of {@link DatabaseState}s. It is assumed that the {@link DataSource}
 * can switch between several states.
 */
public class DatabaseStatesInitializer {

  private final DataSource dataSource;

  private final List<DatabaseState> databaseStates;

  /**
   * Constructor.
   *
   * @param dataSource     the {@link DataSource} to execute SQL scripts against.
   * @param databaseStates the {@link DatabaseState}s to create within the {@link DataSource}.
   */
  public DatabaseStatesInitializer(DataSource dataSource, List<DatabaseState> databaseStates) {
    this.dataSource = dataSource;
    this.databaseStates = databaseStates;
  }

  /**
   * Executes SQL scripts to initialize the {@link DataSource} with several states.
   * <p/>
   * For each {@link DatabaseState}, the {@link DatabaseStateHolder} will be called to set the {@link DataSource}
   * into that state. Then, the SQL scripts of that {@link DatabaseState} are executed against the {@link DataSource}
   * to initialize that state.
   */
  @PostConstruct
  public void initialize() {
    for (DatabaseState databaseState : this.databaseStates) {
      DatabaseStateHolder.setCurrentDatabaseState(databaseState.getStateName());
      ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
      for (String script : databaseState.getSqlscripts()) {
        populator.addScript(new ClassPathResource(script));
      }
      populator.execute(this.dataSource);
    }
  }

}
