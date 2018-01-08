package com.example.demo.connectionchecking.junit5;

import java.util.Optional;

import com.example.demo.connectionchecking.ConnectionChecker;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;

public class AssumeConnectionCondition implements ExecutionCondition {

  @Override
  public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
    Optional<AssumeConnection> annotation = findAnnotation(context.getElement(), AssumeConnection.class);
    if (annotation.isPresent()) {
      String uri = annotation.get().uri();
      ConnectionChecker checker = new ConnectionChecker(uri);
      if (!checker.connect()) {
        return ConditionEvaluationResult.disabled(String.format("Could not connect to '%s'. Skipping test!", uri));
      } else {
        return ConditionEvaluationResult.enabled(String.format("Successfully connected to '%s'. Continuing test!", uri));
      }
    }
    return ConditionEvaluationResult.enabled("No AssumeConnection annotation found. Continuing test.");
  }

}
