package com.example.demo.connectionchecking.junit4;

import com.example.demo.connectionchecking.ConnectionChecker;
import org.junit.AssumptionViolatedException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class AssumingConnection implements TestRule {

  private ConnectionChecker checker;

  public AssumingConnection(ConnectionChecker checker) {
    this.checker = checker;
  }

  @Override
  public Statement apply(Statement base, Description description) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        if (!checker.connect()) {
          throw new AssumptionViolatedException("Could not connect. Skipping test!");
        } else {
          base.evaluate();
        }
      }
    };
  }

}
