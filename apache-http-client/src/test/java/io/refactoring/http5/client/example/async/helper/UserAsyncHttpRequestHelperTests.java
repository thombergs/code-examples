package io.refactoring.http5.client.example.async.helper;

import static org.assertj.core.api.Assertions.assertThat;

import io.refactoring.http5.client.example.model.User;
import java.util.List;
import java.util.Map;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
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
          return value != null
              && (!value.startsWith("Failed to get user")
                  || value.equals("Server does not support HTTP/2 multiplexing."));
        }
      };

  /** Tests get user. */
  @Test
  void getUserWithCallback() {
    try {
      userHttpRequestHelper.startHttpAsyncClient();

      // Send 10 requests in parallel
      // call the delayed endpoint
      final List<String> userIdList =
          List.of("/httpbin/ip", "/httpbin/user-agent", "/httpbin/headers");
      final Map<String, String> responseBodyMap =
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
          userHttpRequestHelper.getUserWithPipelining(
              minimalHttpAsyncClient, userIdList, 3, "https", "reqres.in");

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
          userHttpRequestHelper.getUserWithMultiplexing(
              minimalHttpAsyncClient, userIdList, 3, "https", "reqres.in");

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

  /** Tests get user with async client with interceptor. */
  @Test
  void getUserWithInterceptors() {
    try (final CloseableHttpAsyncClient closeableHttpAsyncClient =
        userHttpRequestHelper.startHttpAsyncInterceptingClient()) {

      final int baseNumber = 3;
      final int requestExecCount = 5;
      final Map<Integer, String> responseBodyMap =
          userHttpRequestHelper.executeRequestsWithInterceptors(
              closeableHttpAsyncClient, 1L, requestExecCount, baseNumber);

      // verify
      assertThat(responseBodyMap)
          .hasSize(requestExecCount)
          .doesNotContainKey(null)
          .doesNotContainValue(null)
          .hasValueSatisfying(getUserErrorCheck);

      final String expectedResponse = "Multiple of " + baseNumber;
      for (Integer i : responseBodyMap.keySet()) {
        if (i % baseNumber == 0) {
          assertThat(responseBodyMap).containsEntry(i, expectedResponse);
        }
      }
    } catch (Exception e) {
      Assertions.fail("Failed to execute HTTP request.", e);
    }
  }

  @Test
  void createUserWithReactiveProcessing() {
    MinimalHttpAsyncClient minimalHttpAsyncClient = null;
    try {
      minimalHttpAsyncClient = userHttpRequestHelper.startMinimalHttp1AsyncClient();

      final User responseBody =
          userHttpRequestHelper.createUserWithReactiveProcessing(
              minimalHttpAsyncClient, "RxMan", "Manager", "https", "reqres.in");

      // verify
      assertThat(responseBody).extracting("id", "createdAt").isNotNull();

    } catch (Exception e) {
      Assertions.fail("Failed to execute HTTP request.", e);
    } finally {
      userHttpRequestHelper.stopMinimalHttpAsyncClient(minimalHttpAsyncClient);
    }
  }
}
