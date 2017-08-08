package com.example.framework;

import java.util.ArrayList;
import java.util.List;

public class SpringBootStarterBuilder {

  private Class<?> applicationClass;

  private List<String> args = new ArrayList<>();

  private List<DatabaseState> databaseStates = new ArrayList<>();

  public SpringBootStarterBuilder withApplicationClass(Class<?> clazz) {
    this.applicationClass = clazz;
    return this;
  }

  public SpringBootStarterBuilder withArgument(String argument) {
    this.args.add(argument);
    return this;
  }

  public SpringBootStarterBuilder withDatabaseState(String stateName, String... sqlScripts) {
    this.databaseStates.add(new DatabaseState(stateName, sqlScripts));
    return this;
  }

  public SpringBootStarter build() {
    return new SpringBootStarter(this.applicationClass, this.databaseStates, args);
  }


}
