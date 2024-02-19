package io.refactoring.http5.client.example.util;

import lombok.extern.slf4j.Slf4j;

/** Base HTTP request handler. */
@Slf4j
public abstract class BaseHttpRequestHelper {
    protected final JsonUtils jsonUtils = new JsonUtils();

}
