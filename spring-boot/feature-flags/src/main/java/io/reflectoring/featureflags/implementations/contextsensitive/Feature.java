package io.reflectoring.featureflags.implementations.contextsensitive;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Feature {

    public enum RolloutStrategy {
        GLOBAL,
        PERCENTAGE;
    }

    private final RolloutStrategy rolloutStrategy;

    private final int percentage;
    private final String value;
    private final String defaultValue;

    public Feature(RolloutStrategy rolloutStrategy, String value, String defaultValue, int percentage) {
        this.rolloutStrategy = rolloutStrategy;
        this.percentage = percentage;
        this.value = value;
        this.defaultValue = defaultValue;
    }

    public boolean evaluateBoolean(String userId) {
        switch (this.rolloutStrategy) {
            case GLOBAL:
                return this.getBooleanValue();
            case PERCENTAGE:
                if (percentageHashCode(userId) <= this.percentage) {
                    return this.getBooleanValue();
                } else {
                    return this.getBooleanDefaultValue();
                }
        }

        return this.getBooleanDefaultValue();
    }

    public Integer evaluateInt(String userId) {
        switch (this.rolloutStrategy) {
            case GLOBAL:
                return this.getIntValue();
            case PERCENTAGE:
                if (percentageHashCode(userId) <= this.percentage) {
                    return this.getIntValue();
                } else {
                    return this.getIntDefaultValue();
                }
        }

        return this.getIntDefaultValue();
    }

    double percentageHashCode(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(
                    text.getBytes(StandardCharsets.UTF_8));
            double INTEGER_RANGE = 1L << 32;
            return (((long) Arrays.hashCode(encodedhash) - Integer.MIN_VALUE) / INTEGER_RANGE) * 100;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public RolloutStrategy getTargeting() {
        return rolloutStrategy;
    }

    public int getPercentage() {
        return percentage;
    }

    public int getIntValue() {
        return Integer.parseInt(this.value);
    }

    public int getIntDefaultValue() {
        return Integer.parseInt(this.defaultValue);
    }


    public boolean getBooleanValue() {
        return Boolean.parseBoolean(this.value);
    }

    public boolean getBooleanDefaultValue() {
        return Boolean.parseBoolean(this.defaultValue);
    }

    public String getStringValue() {
        return value;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
