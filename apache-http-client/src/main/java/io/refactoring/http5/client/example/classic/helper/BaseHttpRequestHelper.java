package io.refactoring.http5.client.example.classic.helper;

import io.refactoring.http5.client.example.classic.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;

/** Base HTTP request handler. */
@Slf4j
public abstract class BaseHttpRequestHelper {
    protected final JsonUtils jsonUtils = new JsonUtils();

}
