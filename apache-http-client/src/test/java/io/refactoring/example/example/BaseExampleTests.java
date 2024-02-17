package io.refactoring.example.example;

import org.json.JSONObject;

/**
 * Base class for all examples.
 */
public abstract class BaseExampleTests {

    protected String makePretty(final String jsonString) {
        return new JSONObject(jsonString).toString(2);
    }
}
