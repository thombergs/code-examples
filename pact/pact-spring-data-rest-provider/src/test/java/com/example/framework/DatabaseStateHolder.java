package com.example.framework;

/**
 * Holds the current database state.
 * <p/>
 * TODO: replace the static state variable with a thread-safe alternative. Potentially use a special HTTP header
 * that is intercepted by a Bean defined in {@link PactDatabaseStatesAutoConfiguration} and sets the database
 * state in a {@link ThreadLocal} variable.
 */
public class DatabaseStateHolder {

  private static String currentDatabaseState;

  /**
   * Sets the database to the state with the specified name.
   * <p/>
   * <strong>WARNING:</strong> the database state is not thread safe. If there are multiple threads accessing
   * the database in different states at the same time, apocalypse will come!
   *
   * @param databaseStateName the name of the {@link DatabaseState} to put the database in.
   */
  public static void setCurrentDatabaseState(String databaseStateName) {
    currentDatabaseState = databaseStateName;
  }

  /**
   * Returns the name of the current {@link DatabaseState}.
   * <p/>
   * <strong>WARNING:</strong> the database state is not thread safe. If there are multiple threads accessing
   * the database in different states at the same time, apocalypse will come!
   */
  public static String getCurrentDatabaseState() {
    return currentDatabaseState;
  }

}
