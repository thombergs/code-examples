package com.example.framework;

/**
 * Defines a state of the database, which is defined by a set of SQL scripts.
 */
public class DatabaseState {

  private final String stateName;

  private final String[] sqlscripts;

  /**
   * Constructor.
   *
   * @param stateName  unique name of this database state.
   * @param sqlscripts paths to SQL scripts within the classpath. These scripts will be executed to put the
   *                   database into the database state described by this object.
   */
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
