package io.refactoring.http5.client.example.util;

import io.refactoring.http5.client.example.RequestProcessingException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;

/** Utility to handle HTTP requests for user entities.It uses built in types for HTTP processing */
@Slf4j
public class UserSimpleHttpRequestHelper extends BaseHttpRequestHelper {

  /**
   * Gets user for given user id.
   *
   * @param userId user id
   * @return response if user is found
   * @throws RequestProcessingException if failed to execute request
   */
  public String getUser(final long userId) throws RequestProcessingException {
    try (final CloseableHttpClient httpClient = HttpClients.createDefault()) {
      // Create request
      final HttpHost httpHost = HttpHost.create("https://reqres.in");
      final HttpGet httpGetRequest = new HttpGet(new URIBuilder("/api/users/" + userId).build());
      log.debug(
          "Executing {} request: {} on host {}",
          httpGetRequest.getMethod(),
          httpGetRequest.getUri(),
          httpHost);

      // Create a response handler
      final BasicHttpClientResponseHandler responseHandler = new BasicHttpClientResponseHandler();
      final String responseBody = httpClient.execute(httpHost, httpGetRequest, responseHandler);

      log.info("Got response: {}", responseBody);

      return responseBody;
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
      final HttpHost httpHost = HttpHost.create("https://reqres.in");
      final HttpGet httpGetRequest = new HttpGet(new URIBuilder("/api/users/").build());
      log.debug(
          "Executing {} request: {} on host {}",
          httpGetRequest.getMethod(),
          httpGetRequest.getUri(),
          httpHost);

      // Create a response handler
      final BasicHttpClientResponseHandler responseHandler = new BasicHttpClientResponseHandler();
      final String responseBody = httpClient.execute(httpHost, httpGetRequest, responseHandler);

      log.info("Got response: {}", responseBody);
      return responseBody;
    } catch (Exception e) {
      throw new RequestProcessingException("Failed to get all users.", e);
    }
  }

  /**
   * Creates user for given input.
   *
   * @param firstName first name
   * @param lastName last name
   * @param email email
   * @param avatar avatar
   * @return newly created user
   * @throws RequestProcessingException if failed to execute request
   */
  public String createUser(
      @NonNull final String firstName,
      @NonNull final String lastName,
      @NonNull final String email,
      @NonNull final String avatar)
      throws RequestProcessingException {
    try (final CloseableHttpClient httpClient = HttpClients.createDefault()) {
      log.debug(
          "Create user using input: first name {}, last name {}, email {}, avatar {}",
          firstName,
          lastName,
          email,
          avatar);
      // Create request
      final List<NameValuePair> formParams = new ArrayList<NameValuePair>();
      formParams.add(new BasicNameValuePair("first_name", firstName));
      formParams.add(new BasicNameValuePair("last_name", lastName));
      formParams.add(new BasicNameValuePair("email", email));
      formParams.add(new BasicNameValuePair("avatar", avatar));
      try (final UrlEncodedFormEntity entity =
          new UrlEncodedFormEntity(formParams, StandardCharsets.UTF_8)) {
        final HttpHost httpHost = HttpHost.create("https://reqres.in");
        final HttpPost httpPostRequest = new HttpPost(new URIBuilder("/api/users/").build());
        httpPostRequest.setEntity(entity);
        log.debug(
            "Executing {} request: {} on host {}",
            httpPostRequest.getMethod(),
            httpPostRequest.getUri(),
            httpHost);

        // Create a response handler
        final BasicHttpClientResponseHandler responseHandler = new BasicHttpClientResponseHandler();
        final String responseBody = httpClient.execute(httpHost, httpPostRequest, responseHandler);
        log.info("Got response: {}", responseBody);

        return responseBody;
      }
    } catch (Exception e) {
      throw new RequestProcessingException("Failed to create user.", e);
    }
  }

  /**
   * Updates user for given input.
   *
   * @param userId existing user id
   * @param firstName first name
   * @param lastName last name
   * @param email email
   * @param avatar avatar
   * @return updated user
   * @throws RequestProcessingException if failed to execute request
   */
  public String updateUser(
      final long userId,
      @NonNull final String firstName,
      @NonNull final String lastName,
      @NonNull final String email,
      @NonNull final String avatar)
      throws RequestProcessingException {
    try (final CloseableHttpClient httpClient = HttpClients.createDefault()) {
      log.debug(
          "Update user using input: first name {}, last name {}, email {}, avatar {}",
          firstName,
          lastName,
          email,
          avatar);
      // Update request
      final List<NameValuePair> formParams = new ArrayList<NameValuePair>();
      formParams.add(new BasicNameValuePair("first_name", firstName));
      formParams.add(new BasicNameValuePair("last_name", lastName));
      formParams.add(new BasicNameValuePair("email", email));
      formParams.add(new BasicNameValuePair("avatar", avatar));

      try (final UrlEncodedFormEntity entity =
          new UrlEncodedFormEntity(formParams, StandardCharsets.UTF_8)) {
        final HttpHost httpHost = HttpHost.create("https://reqres.in");
        final HttpPut httpPutRequest = new HttpPut(new URIBuilder("/api/users/" + userId).build());
        httpPutRequest.setEntity(entity);
        log.debug(
            "Executing {} request: {} on host {}",
            httpPutRequest.getMethod(),
            httpPutRequest.getUri(),
            httpHost);

        // Create a response handler
        final BasicHttpClientResponseHandler responseHandler = new BasicHttpClientResponseHandler();
        final String responseBody = httpClient.execute(httpHost, httpPutRequest, responseHandler);
        log.info("Got response: {}", responseBody);

        return responseBody;
      }
    } catch (Exception e) {
      throw new RequestProcessingException("Failed to update user.", e);
    }
  }
}
