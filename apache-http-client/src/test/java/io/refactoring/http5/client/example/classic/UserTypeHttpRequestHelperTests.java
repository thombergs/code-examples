package io.refactoring.http5.client.example.classic;

import static org.assertj.core.api.Assertions.assertThat;

import io.refactoring.http5.client.example.model.User;
import io.refactoring.http5.client.example.model.UserPage;
import io.refactoring.http5.client.example.classic.helper.UserTypeHttpRequestHelper;
import java.util.Map;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** This example demonstrates how to process HTTP responses using a response handler. */
@Slf4j
public class UserTypeHttpRequestHelperTests extends BaseClassicExampleTests {

  private final UserTypeHttpRequestHelper userHttpRequestHelper = new UserTypeHttpRequestHelper();

  @Test
  void executeGetAllRequest() {
    try {
      // prepare
      final Map<String, String> params = Map.of("page", "1");

      // execute
      final UserPage paginatedUsers = userHttpRequestHelper.getPaginatedUsers(params);

      // verify
      assertThat(paginatedUsers).isNotNull();
      log.info("Got response: {}", jsonUtils.toJson(paginatedUsers));
    } catch (Exception e) {
      Assertions.fail("Failed to execute HTTP request.", e);
    }
  }

  @Test
  void executeGetUser() {
    try {
      // prepare
      final long userId = 2L;

      // execute
      final User existingUser = userHttpRequestHelper.getUser(userId);

      // verify
      final ThrowingConsumer<User> responseRequirements =
          user -> {
            assertThat(user).as("Created user cannot be null.").isNotNull();
            assertThat(user.getId()).as("ID should be positive number.").isEqualTo(userId);
            assertThat(user.getFirstName()).as("First name cannot be null.").isNotEmpty();
            assertThat(user.getLastName()).as("Last name cannot be null.").isNotEmpty();
            assertThat(user.getAvatar()).as("Avatar cannot be null.").isNotNull();
          };
      assertThat(existingUser).satisfies(responseRequirements);
    } catch (Exception e) {
      Assertions.fail("Failed to execute HTTP request.", e);
    }
  }

  @Test
  void executePostRequest() {
    try {
      // prepare
      @NonNull final User input = new User();
      input.setFirstName("DummyFirst");
      input.setLastName("DummyLast");

      // execute
      final User createdUser = userHttpRequestHelper.createUser(input);

      // verify
      final ThrowingConsumer<User> responseRequirements =
          user -> {
            assertThat(user).as("Created user cannot be null.").isNotNull();
            assertThat(user.getId()).as("ID should be positive number.").isPositive();
            assertThat(user.getFirstName())
                .as("First name does not match.")
                .isEqualTo(input.getFirstName());
            assertThat(user.getLastName())
                .as("Last name does not match.")
                .isEqualTo(input.getLastName());
            assertThat(user.getCreatedAt()).as("Created at cannot be null.").isNotNull();
          };
      assertThat(createdUser).satisfies(responseRequirements);
    } catch (Exception e) {
      Assertions.fail("Failed to execute HTTP request.", e);
    }
  }

  @Test
  void executePutRequest() {
    try {
      // prepare
      final int userId = 2;
      @NonNull final User existingUser = userHttpRequestHelper.getUser(userId);
      final String updatedFirstName = "UpdatedDummyFirst";
      existingUser.setFirstName(updatedFirstName);
      final String updatedLastName = "UpdatedDummyLast";
      existingUser.setLastName(updatedLastName);

      // execute
      final User updatedUser = userHttpRequestHelper.updateUser(existingUser);

      // verify
      final ThrowingConsumer<User> responseRequirements =
          user -> {
            assertThat(user).as("Updated user cannot be null.").isNotNull();
            assertThat(user.getId())
                .as("ID should be positive number.")
                .isPositive()
                .as("ID should not be updated.")
                .isEqualTo(existingUser.getId());
            assertThat(user.getFirstName())
                .as("First name does not match.")
                .isEqualTo(updatedFirstName);
            assertThat(user.getLastName())
                .as("Last name does not match.")
                .isEqualTo(updatedLastName);
            assertThat(user.getCreatedAt()).as("Created at cannot be null.").isNotNull();
          };
      assertThat(updatedUser).satisfies(responseRequirements);

    } catch (Exception e) {
      Assertions.fail("Failed to execute HTTP request.", e);
    }
  }
}
