package io.refactoring.http5.client.example.util;

import com.fasterxml.jackson.core.type.TypeReference;
import io.refactoring.http5.client.example.RequestProcessingException;
import io.refactoring.http5.client.example.model.ResponseDataObject;
import io.refactoring.http5.client.example.model.User;
import io.refactoring.http5.client.example.model.UserPage;
import java.text.MessageFormat;
import java.util.Map;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHost;

/**
 * Utility to handle HTTP requests for {@linkplain User} entities. It uses the user-defined types
 * for HTTP processing
 */
@Slf4j
public class UserTypeHttpRequestHelper extends BaseHttpRequestHelper {

  private final UserRequestProcessingUtils userRequestProcessingUtils =
      new UserRequestProcessingUtils();

  /**
   * Gets user for given user id.
   *
   * @param userId user id
   * @return user if found
   * @throws RequestProcessingException if failed to execute request
   */
  public User getUser(final long userId) throws RequestProcessingException {
    try (final CloseableHttpClient httpClient = HttpClients.createDefault()) {
      // Create request
      final HttpHost httpHost = userRequestProcessingUtils.getApiHost();
      final HttpGet httpGetRequest =
          new HttpGet(userRequestProcessingUtils.prepareUsersApiUri(userId));
      log.debug(
          "Executing {} request: {} on host {}",
          httpGetRequest.getMethod(),
          httpGetRequest.getUri(),
          httpHost);

      // Create a response handler
      final BasicHttpClientResponseHandler responseHandler = new BasicHttpClientResponseHandler();
      final String responseBody = httpClient.execute(httpHost, httpGetRequest, responseHandler);

      log.info("Got response: {}", jsonUtils.makePretty(responseBody));

      final ResponseDataObject<User> userResponseDataObject =
          jsonUtils.fromJson(responseBody, new TypeReference<ResponseDataObject<User>>() {});
      return userResponseDataObject.getData();
    } catch (Exception e) {
      throw new RequestProcessingException(
          MessageFormat.format("Failed to get user for ID: {0}", userId), e);
    }
  }

  /**
   * Gets all users.
   *
   * @param requestParameters request parameters
   * @return response
   * @throws RequestProcessingException if failed to execute request
   */
  public String getAllUsers(final Map<String, String> requestParameters)
      throws RequestProcessingException {
    try (final CloseableHttpClient httpClient = HttpClients.createDefault()) {
      // Create request
      final HttpHost httpHost = userRequestProcessingUtils.getApiHost();
      final HttpGet httpGetRequest =
          new HttpGet(userRequestProcessingUtils.prepareUsersApiUri(requestParameters));
      log.debug(
          "Executing {} request: {} on host {}",
          httpGetRequest.getMethod(),
          httpGetRequest.getUri(),
          httpHost);

      // Create a response handler
      final BasicHttpClientResponseHandler responseHandler = new BasicHttpClientResponseHandler();
      final String responseBody = httpClient.execute(httpHost, httpGetRequest, responseHandler);

      log.info("Got response: {}", jsonUtils.makePretty(responseBody));
      return responseBody;
    } catch (Exception e) {
      throw new RequestProcessingException("Failed to get all users.", e);
    }
  }

  /**
   * Gets all users.
   *
   * @param requestParameters request parameters
   * @return response
   * @throws RequestProcessingException if failed to execute request
   */
  public UserPage getAllUsersUsingTypedResponseHandler(final Map<String, String> requestParameters)
      throws RequestProcessingException {
    try (final CloseableHttpClient httpClient = HttpClients.createDefault()) {
      // Create request
      final HttpHost httpHost = userRequestProcessingUtils.getApiHost();
      final HttpGet httpGetRequest =
          new HttpGet(userRequestProcessingUtils.prepareUsersApiUri(requestParameters));
      log.debug(
          "Executing {} request: {} on host {}",
          httpGetRequest.getMethod(),
          httpGetRequest.getUri(),
          httpHost);

      // Create a response handler
      final DataObjectResponseHandler<UserPage> responseHandler =
          new DataObjectResponseHandler<>(UserPage.class);
      final UserPage responseBody = httpClient.execute(httpHost, httpGetRequest, responseHandler);
      log.info("Got response: {}", jsonUtils.toJson(responseBody));
      return responseBody;
    } catch (Exception e) {
      throw new RequestProcessingException("Failed to get all users.", e);
    }
  }

  /**
   * Creates user for given input.
   *
   * @param input user creation input
   * @return newly created user
   * @throws RequestProcessingException if failed to execute request
   */
  public User createUser(@NonNull final User input) throws RequestProcessingException {
    try (final CloseableHttpClient httpClient = HttpClients.createDefault()) {
      log.debug("Create user using input: {}", jsonUtils.toJson(input));
      // Create request
      final HttpHost httpHost = userRequestProcessingUtils.getApiHost();
      final HttpPost httpPostRequest =
          new HttpPost(userRequestProcessingUtils.prepareUsersApiUri());
      httpPostRequest.setEntity(userRequestProcessingUtils.toJsonStringEntity(input));
      log.debug(
          "Executing {} request: {} on host {}",
          httpPostRequest.getMethod(),
          httpPostRequest.getUri(),
          httpHost);

      // Create a response handler
      final BasicHttpClientResponseHandler responseHandler = new BasicHttpClientResponseHandler();
      final String responseBody = httpClient.execute(httpHost, httpPostRequest, responseHandler);
      log.info("Got response: {}", jsonUtils.makePretty(responseBody));

      return jsonUtils.fromJson(responseBody, User.class);

    } catch (Exception e) {
      throw new RequestProcessingException("Failed to create user.", e);
    }
  }

  /**
   * Updates user for given input.
   *
   * @param input user update input
   * @return updated user
   * @throws RequestProcessingException if failed to execute request
   */
  public User updateUser(@NonNull final User input) throws RequestProcessingException {
    try (final CloseableHttpClient httpClient = HttpClients.createDefault()) {
      log.debug("Update user using input: {}", jsonUtils.toJson(input));
      // Update request
      final HttpHost httpHost = userRequestProcessingUtils.getApiHost();
      final HttpPut httpPutRequest = new HttpPut(userRequestProcessingUtils.prepareUsersApiUri());
      httpPutRequest.setEntity(userRequestProcessingUtils.toJsonStringEntity(input));
      log.debug(
          "Executing {} request: {} on host {}",
          httpPutRequest.getMethod(),
          httpPutRequest.getUri(),
          httpHost);

      // Create a response handler
      final BasicHttpClientResponseHandler responseHandler = new BasicHttpClientResponseHandler();
      final String responseBody = httpClient.execute(httpHost, httpPutRequest, responseHandler);
      log.info("Got response: {}", jsonUtils.makePretty(responseBody));

      return jsonUtils.fromJson(responseBody, User.class);

    } catch (Exception e) {
      throw new RequestProcessingException("Failed to update user.", e);
    }
  }
}
