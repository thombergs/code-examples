package io.refactoring.http5.client.example.classic.util;

import java.net.URISyntaxException;

import io.refactoring.http5.client.example.config.ConfigurationUtils;
import io.refactoring.http5.client.example.util.JsonUtils;
import lombok.NonNull;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.entity.StringEntity;

/** Utility for HTTP client interactions. */
public abstract class RequestProcessingUtils {

  protected final JsonUtils jsonUtils = new JsonUtils();

  protected final ConfigurationUtils configurationUtils = new ConfigurationUtils();

  /**
   * Converts a source object into a string entity.
   *
   * @param source source object
   * @return string entity
   */
  public StringEntity toJsonStringEntity(@NonNull final Object source) {
    return new StringEntity(jsonUtils.toJson(source), ContentType.APPLICATION_JSON);
  }

  /**
   * Gets API host.
   *
   * @return API host
   * @throws URISyntaxException if failed to get API host
   */
  public HttpHost getApiHost() throws URISyntaxException {
    return HttpHost.create(configurationUtils.getString("app.prop.api-host"));
  }
}
