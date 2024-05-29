package io.refactoring.http5.client.example.classic;

import static org.assertj.core.api.Assertions.assertThat;

import io.refactoring.http5.client.example.classic.helper.UserSimpleHttpRequestHelper;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** This example demonstrates how to process HTTP responses using a response handler. */
@Slf4j
public class UserSimpleHttpRequestHelperTests extends BaseClassicExampleTests {

  private final UserSimpleHttpRequestHelper userHttpRequestHelper =
      new UserSimpleHttpRequestHelper();

  /** Execute get paginated request. */
  @Test
  void executeGetPaginatedRequest() {
    try {
      // prepare
      final Map<String, String> params = Map.of("page", "1");

      // execute
      final String responseBody = userHttpRequestHelper.getPaginatedUsers(params);

      // verify
      assertThat(responseBody).isNotEmpty();
      log.info("Got response: {}", responseBody);
    } catch (Exception e) {
      Assertions.fail("Failed to execute HTTP request.", e);
    }
  }

  /** Execute get specific request. */
  @Test
  void executeGetSpecificRequest() {
    try {
      // prepare
      final long userId = 2L;

      // execute
      final String existingUser = userHttpRequestHelper.getUser(userId);

      // verify
      assertThat(existingUser).isNotEmpty();
    } catch (Exception e) {
      Assertions.fail("Failed to execute HTTP request.", e);
    }
  }

  /** Execute get specific request. */
  @Test
  void executeUserStatus() {
    try {
      // prepare
      final long userId = 2L;

      // execute
      final Integer userStatus = userHttpRequestHelper.getUserStatus(userId);

      // verify
      assertThat(userStatus).isEqualTo(HttpStatus.SC_OK);
    } catch (Exception e) {
      Assertions.fail("Failed to execute HTTP request.", e);
    }
  }

  /** Execute post request. */
  @Test
  void executePostRequest() {
    try {
      // prepare
      // execute
      final String createdUser =
          userHttpRequestHelper.createUser(
              "DummyFirst", "DummyLast", "DummyEmail@example.com", "DummyAvatar");

      // verify
      assertThat(createdUser).isNotEmpty();
    } catch (Exception e) {
      Assertions.fail("Failed to execute HTTP request.", e);
    }
  }

  /** Execute put request. */
  @Test
  void executePutRequest() {
    try {
      // prepare
      final int userId = 2;

      // execute
      final String updatedUser =
          userHttpRequestHelper.updateUser(
              userId,
              "UpdatedDummyFirst",
              "UpdatedDummyLast",
              "UpdatedDummyEmail@example.com",
              "UpdatedDummyAvatar");

      // verify
      assertThat(updatedUser).isNotEmpty();

    } catch (Exception e) {
      Assertions.fail("Failed to execute HTTP request.", e);
    }
  }

  /** Execute put request. */
  @Test
  void executePatchRequest() {
    try {
      // prepare
      final int userId = 2;

      // execute
      final String patchedUser =
          userHttpRequestHelper.patchUser(userId, "UpdatedDummyFirst", "UpdatedDummyLast");

      // verify
      assertThat(patchedUser).isNotEmpty();
    } catch (Exception e) {
      Assertions.fail("Failed to execute HTTP request.", e);
    }
  }

  /** Execute delete request. */
  @Test
  void executeDeleteRequest() {
    try {
      // prepare
      final int userId = 2;

      // execute
      userHttpRequestHelper.deleteUser(userId);
    } catch (Exception e) {
      Assertions.fail("Failed to execute HTTP request.", e);
    }
  }

  /** Execute options request. */
  @Test
  void executeOptions() {
    try {
      // execute
      final Map<String, String> headers = userHttpRequestHelper.executeOptions();
      assertThat(headers.keySet())
          .as("Headers do not contain allow header")
          .containsAnyOf("Allow", "Access-Control-Allow-Methods");

    } catch (Exception e) {
      Assertions.fail("Failed to execute HTTP request.", e);
    }
  }
}
