package com.example.framework;

public class DatabaseState {

  private final String stateName;

  private final String[] sqlscripts;

  public DatabaseState(String stateName, String... sqlscripts) {
    this.stateName = stateName;
    this.sqlscripts = sqlscripts;
  }

  public String getStateName() {
    return stateName;
  }

  public String[] getSqlscripts() {
    return sqlscripts;
  }

}
