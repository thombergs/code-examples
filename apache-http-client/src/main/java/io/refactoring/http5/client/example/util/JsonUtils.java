package io.refactoring.http5.client.example.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.json.JSONObject;

/** Utility to handle JSON. */
public class JsonUtils {
  private ObjectMapper objectMapper;

  private ObjectMapper getObjectMapper() {
    if (objectMapper == null) {
      objectMapper = new ObjectMapper();
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    return objectMapper;
  }

  /**
   * Converts a source object into JSON string.
   *
   * @param source source object
   * @return JSON representation of the object, {@code null} if {@code source} is {@code null}
   * @throws RuntimeException if fails to convert the {@code source} to JSON
   */
  public String toJson(final Object source) {
    if (source == null) {
      return null;
    }
    try {
      return getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(source);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Converts a source string into an object of target class type.
   *
   * @param jsonString source JSON
   * @param targetClassType class type of object
   * @return object of a {@code targetClassType}
   * @param <T> type of target object
   * @throws JsonProcessingException if it fails to process the JSON
   */
  public <T> T fromJson(@NonNull final String jsonString, @NonNull final Class<T> targetClassType)
      throws JsonProcessingException {
    return getObjectMapper().readValue(jsonString, targetClassType);
  }

  /**
   * Converts a source string into an object of target type.
   *
   * @param jsonString source JSON
   * @param targetTpeReference type of object
   * @return object of a {@code targetTpeReference}
   * @param <T> type of target object
   * @throws JsonProcessingException if it fails to process the JSON
   */
  public <T> T fromJson(
      @NonNull final String jsonString, @NonNull final TypeReference<T> targetTpeReference)
      throws JsonProcessingException {
    return getObjectMapper().readValue(jsonString, targetTpeReference);
  }

  /**
   * Converts source JSON into pretty formatter JSON.
   *
   * @param source source JSON
   * @return formatted JSON
   */
  public String makePretty(final String source) {
    return new JSONObject(source).toString(2);
  }
}
