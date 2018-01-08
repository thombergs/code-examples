package com.example.framework;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Starts a Spring Boot application.
 * </p>
 * <p>
 * When included in a JUnit test with the {@link org.junit.ClassRule} annotation as in the example below, the Spring Boot application will be
 * started before any of the test methods are run.
 * <pre>
 * public class MyTest {
 *
 *   &#064;ClassRule
 *   public static SpringBootStarter starter = SpringBootStarter.builder()
 *     .withApplicationClass(MyApplication.class)
 *     ...
 *     .build();
 *
 *   &#064;Test
 *   public void test(){
 *     ...
 *   }
 *
 * }
 * </pre>
 * </p>
 */
public class SpringBootStarter implements TestRule {

  private final Class<?> applicationClass;

  private final List<String> args;

  private final List<DatabaseState> databaseStates;

  /**
   * Constructor.
   *
   * @param applicationClass the Spring Boot application class.
   * @param databaseStates   list containing {@link DatabaseState} objects, each describing a database state
   *                         in form of one or more SQL scripts.
   * @param args             the command line arguments the application should be started with.
   */
  public SpringBootStarter(Class<?> applicationClass, List<DatabaseState> databaseStates, List<String> args) {
    this.args = args;
    this.applicationClass = applicationClass;
    this.databaseStates = databaseStates;
  }

  @Override
  public Statement apply(Statement base, Description description) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        List<String> args = new ArrayList<>();
        args.addAll(SpringBootStarter.this.args);
        args.addAll(PactProperties.toCommandLineArguments(SpringBootStarter.this.databaseStates));
        ApplicationContext context = SpringApplication.run(SpringBootStarter.this.applicationClass, args.toArray(new String[]{}));
        base.evaluate();
        SpringApplication.exit(context);
      }
    };
  }

  /**
   * Creates a builder that provides a fluent API to create a new {@link SpringBootStarter} instance.
   */
  public static SpringBootStarterBuilder builder() {
    return new SpringBootStarterBuilder();
  }

  public static class SpringBootStarterBuilder {

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


}
