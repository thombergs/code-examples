package com.example.framework;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;

public class DatabaseStatesInitializer {

  private final DataSource dataSource;

  private final List<DatabaseState> databaseStates;

  public DatabaseStatesInitializer(DataSource dataSource, List<DatabaseState> databaseStates) {
    this.dataSource = dataSource;
    this.databaseStates = databaseStates;
  }

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
