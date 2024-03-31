package io.refactoring.http5.client.example.async.helper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.apache.hc.client5.http.impl.async.MinimalHttpAsyncClient;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** User async http request helper tests. */
class UserAsyncHttpRequestHelperTests extends BaseAsyncExampleTests {

  private final UserAsyncHttpRequestHelper userHttpRequestHelper = new UserAsyncHttpRequestHelper();

  private final Condition<String> getUserErrorCheck =
      new Condition<String>("Check failure response.") {
        @Override
        public boolean matches(String value) {
          // value should not be null
          // value should not be failure message
          return value != null && !value.startsWith("Failed to get user");
        }
      };

  /** Tests get user. */
  @Test
  void getUserWithCallback() {
    try {
      userHttpRequestHelper.startHttpAsyncClient();

      // Send 10 requests in parallel
      // call the delayed endpoint
      final List<Long> userIdList = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);
      final Map<Long, String> responseBodyMap =
          userHttpRequestHelper.getUserWithCallback(userIdList, 3);

      // verify
      assertThat(responseBodyMap)
          .hasSameSizeAs(userIdList)
          .doesNotContainKey(null)
          .doesNotContainValue(null)
          .hasValueSatisfying(getUserErrorCheck);

    } catch (Exception e) {
      Assertions.fail("Failed to execute HTTP request.", e);
    } finally {
      userHttpRequestHelper.stopHttpAsyncClient();
    }
  }

  /** Tests get user with stream. */
  @Test
  void getUserWithStream() {
    try {
      userHttpRequestHelper.startHttpAsyncClient();

      // Send 10 requests in parallel
      // call the delayed endpoint
      final List<Long> userIdList = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);
      final Map<Long, String> responseBodyMap =
          userHttpRequestHelper.getUserWithStreams(userIdList, 3);

      // verify
      assertThat(responseBodyMap)
          .hasSameSizeAs(userIdList)
          .doesNotContainKey(null)
          .doesNotContainValue(null)
          .hasValueSatisfying(getUserErrorCheck);

    } catch (Exception e) {
      Assertions.fail("Failed to execute HTTP request.", e);
    } finally {
      userHttpRequestHelper.stopHttpAsyncClient();
    }
  }

  /** Tests get user with pipelining. */
  @Test
  void getUserWithPipelining() {
    MinimalHttpAsyncClient minimalHttpAsyncClient = null;
    try {
      minimalHttpAsyncClient = userHttpRequestHelper.startMinimalHttp1AsyncClient();

      // Send 10 requests in parallel
      // call the delayed endpoint
      final List<Long> userIdList = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);
      final Map<Long, String> responseBodyMap =
          userHttpRequestHelper.getUserWithPipelining(minimalHttpAsyncClient, userIdList, 3);

      // verify
      assertThat(responseBodyMap)
          .hasSameSizeAs(userIdList)
          .doesNotContainKey(null)
          .doesNotContainValue(null)
          .hasValueSatisfying(getUserErrorCheck);

    } catch (Exception e) {
      Assertions.fail("Failed to execute HTTP request.", e);
    } finally {
      userHttpRequestHelper.stopMinimalHttpAsyncClient(minimalHttpAsyncClient);
    }
  }

  /** Tests get user with multiplexing. */
  @Test
  void getUserWithMultiplexing() {
    MinimalHttpAsyncClient minimalHttpAsyncClient = null;
    try {
      minimalHttpAsyncClient = userHttpRequestHelper.startMinimalHttp2AsyncClient();

      // Send 10 requests in parallel
      // call the delayed endpoint
      final List<Long> userIdList = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);
      final Map<Long, String> responseBodyMap =
          userHttpRequestHelper.getUserWithMultiplexing(minimalHttpAsyncClient, userIdList, 3);

      // verify
      assertThat(responseBodyMap)
          .hasSameSizeAs(userIdList)
          .doesNotContainKey(null)
          .doesNotContainValue(null)
          .hasValueSatisfying(getUserErrorCheck);

    } catch (Exception e) {
      Assertions.fail("Failed to execute HTTP request.", e);
    } finally {
      userHttpRequestHelper.stopMinimalHttpAsyncClient(minimalHttpAsyncClient);
    }
  }
}
