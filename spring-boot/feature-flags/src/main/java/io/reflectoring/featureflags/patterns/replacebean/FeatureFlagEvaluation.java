package io.reflectoring.featureflags.patterns.replacebean;

@FunctionalInterface
public interface FeatureFlagEvaluation {

    boolean evaluate();

}
