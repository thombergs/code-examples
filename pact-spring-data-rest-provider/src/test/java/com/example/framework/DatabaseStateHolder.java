package com.example.framework;

public class DatabaseStateHolder {

  private static String currentDatabaseState;

  public static void setCurrentDatabaseState(String databaseStateName) {
    currentDatabaseState = databaseStateName;
  }

  public static String getCurrentDatabaseState() {
    return currentDatabaseState;
  }

}
