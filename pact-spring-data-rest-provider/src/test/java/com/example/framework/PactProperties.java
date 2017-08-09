package com.example.framework;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Loads the properties "pact.databaseStates.&lt;NAME&gt;" into the Spring environment.
 */
@ConfigurationProperties("pact")
public class PactProperties {

  private Map<String, String> databaseStates;

  /**
   * Retrieves a map with the names of the configured database states as keys and {@link DatabaseState} objects
   * as values.
   */
  public List<DatabaseState> getDatabaseStatesList() {
    List<DatabaseState> databaseStatesList = new ArrayList<>();
    for (Map.Entry<String, String> entry : databaseStates.entrySet()) {
      String stateName = entry.getKey();

      // When reading a property as a Map, as is done for databaseStates, Spring Boot automatically adds numeric
      // keys and moves the actual key into the value, separated by a comma. Thus, we have all entries duplicated
      // and have to remove the entries with numeric keys.

      if (!stateName.matches("^[0-9]+$")) {
        String sqlScriptsString = entry.getValue();
        String[] sqlScripts = sqlScriptsString.split(",");
        databaseStatesList.add(new DatabaseState(stateName, sqlScripts));
      }
    }
    return databaseStatesList;
  }


  static List<String> toCommandLineArguments(List<DatabaseState> databaseStates) {
    List<String> args = new ArrayList<>();
    for (DatabaseState databaseState : databaseStates) {
      String argString = String.format("--pact.databaseStates.%s=", databaseState.getStateName());
      int i = 0;
      for (String scriptPath : databaseState.getSqlscripts()) {
        argString += String.format("%s", scriptPath);
        if (i < databaseState.getSqlscripts().length - 1) {
          argString += ",";
        }
        i++;
      }
      args.add(argString);
    }
    return args;
  }

  public Map<String, String> getDatabaseStates() {
    return databaseStates;
  }

  public void setDatabaseStates(Map<String, String> databaseStates) {
    this.databaseStates = databaseStates;
  }
}
