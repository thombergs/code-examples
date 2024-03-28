package io.refactoring.http5.client.example.classic.helper;

import io.refactoring.http5.client.example.RequestProcessingException;
import io.refactoring.http5.client.example.helper.BaseHttpRequestHelper;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;

/**
 * Utility to handle HTTP requests for user entities. It uses built in types for HTTP processing.
 */
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
      final URI uri = new URIBuilder("/api/users/" + userId).build();
      final HttpGet httpGetRequest = new HttpGet(uri);
      log.debug(
          "Executing {} request: {} on host {}",
          httpGetRequest.getMethod(),
          httpGetRequest.getUri(),
          httpHost);

      // Create a response handler
      final BasicHttpClientResponseHandler handler = new BasicHttpClientResponseHandler();
      final String responseBody = httpClient.execute(httpHost, httpGetRequest, handler);

      log.info("Got response: {}", responseBody);

      return responseBody;
    } catch (Exception e) {
      throw new RequestProcessingException(
          MessageFormat.format("Failed to get user for ID: {0}", userId), e);
    }
  }

  /**
   * Gets user status for given user id.
   *
   * @param userId user id
   * @return response if user is found
   * @throws RequestProcessingException if failed to execute request
   */
  public Integer getUserStatus(final long userId) throws RequestProcessingException {
    try (final CloseableHttpClient httpClient = HttpClients.createDefault()) {
      // Create request
      final HttpHost httpHost = HttpHost.create("https://reqres.in");
      final URI uri = new URIBuilder("/api/users/" + userId).build();
      final HttpHead httpHeadRequest = new HttpHead(uri);
      log.debug(
          "Executing {} request: {} on host {}",
          httpHeadRequest.getMethod(),
          httpHeadRequest.getUri(),
          httpHost);

      // Create a response handler
      // Implement HttpClientResponseHandler::handleResponse(ClassicHttpResponse response)
      final HttpClientResponseHandler<Integer> handler = HttpResponse::getCode;
      final Integer code = httpClient.execute(httpHost, httpHeadRequest, handler);

      log.info("Got response status code: {}", code);

      return code;
    } catch (Exception e) {
      throw new RequestProcessingException(
          MessageFormat.format("Failed to get user status for ID: {0}", userId), e);
    }
  }

  /**
   * Gets paginated users.
   *
   * @param requestParameters request parameters
   * @return response paginated users
   * @throws RequestProcessingException if failed to execute request
   */
  public String getPaginatedUsers(final Map<String, String> requestParameters)
      throws RequestProcessingException {
    try (final CloseableHttpClient httpClient = HttpClients.createDefault()) {
      // Create request
      final HttpHost httpHost = HttpHost.create("https://reqres.in");
      final List<NameValuePair> nameValuePairs =
          requestParameters.entrySet().stream()
              .map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue()))
              .map(entry -> (NameValuePair) entry)
              .toList();
      final HttpGet httpGetRequest =
          new HttpGet(new URIBuilder("/api/users/").addParameters(nameValuePairs).build());
      log.debug(
          "Executing {} request: {} on host {}",
          httpGetRequest.getMethod(),
          httpGetRequest.getUri(),
          httpHost);

      // Create a response handler
      final BasicHttpClientResponseHandler handler = new BasicHttpClientResponseHandler();
      final String responseBody = httpClient.execute(httpHost, httpGetRequest, handler);

      log.info("Got response: {}", responseBody);
      return responseBody;
    } catch (Exception e) {
      throw new RequestProcessingException("Failed to get paginated users.", e);
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
        final URI uri = new URIBuilder("/api/users/").build();
        final HttpPost httpPostRequest = new HttpPost(uri);
        httpPostRequest.setEntity(entity);
        log.debug(
            "Executing {} request: {} on host {}",
            httpPostRequest.getMethod(),
            httpPostRequest.getUri(),
            httpHost);

        // Create a response handler
        final BasicHttpClientResponseHandler handler = new BasicHttpClientResponseHandler();
        final String responseBody = httpClient.execute(httpHost, httpPostRequest, handler);
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
        final URI uri = new URIBuilder("/api/users/" + userId).build();
        final HttpPut httpPutRequest = new HttpPut(uri);
        httpPutRequest.setEntity(entity);
        log.debug(
            "Executing {} request: {} on host {}",
            httpPutRequest.getMethod(),
            httpPutRequest.getUri(),
            httpHost);

        // Create a response handler
        final BasicHttpClientResponseHandler handler = new BasicHttpClientResponseHandler();
        final String responseBody = httpClient.execute(httpHost, httpPutRequest, handler);
        log.info("Got response: {}", responseBody);

        return responseBody;
      }
    } catch (Exception e) {
      throw new RequestProcessingException("Failed to update user.", e);
    }
  }

  /**
   * Patches user for given input.
   *
   * @param userId existing user id
   * @param firstName first name
   * @param lastName last name
   * @return patched user
   * @throws RequestProcessingException if failed to execute request
   */
  public String patchUser(
      final long userId, @NonNull final String firstName, @NonNull final String lastName)
      throws RequestProcessingException {
    try (final CloseableHttpClient httpClient = HttpClients.createDefault()) {
      log.debug("Patch user using input: first name {}, last name {}", firstName, lastName);
      // Prepare request
      final List<NameValuePair> formParams = new ArrayList<NameValuePair>();
      formParams.add(new BasicNameValuePair("first_name", firstName));
      formParams.add(new BasicNameValuePair("last_name", lastName));

      try (final UrlEncodedFormEntity entity =
          new UrlEncodedFormEntity(formParams, StandardCharsets.UTF_8)) {
        final HttpHost httpHost = HttpHost.create("https://reqres.in");
        final URI uri = new URIBuilder("/api/users/" + userId).build();
        final HttpPatch httpPatchRequest = new HttpPatch(uri);
        httpPatchRequest.setEntity(entity);
        log.debug(
            "Executing {} request: {} on host {}",
            httpPatchRequest.getMethod(),
            httpPatchRequest.getUri(),
            httpHost);

        // Create a response handler
        final BasicHttpClientResponseHandler handler = new BasicHttpClientResponseHandler();
        final String responseBody = httpClient.execute(httpHost, httpPatchRequest, handler);
        log.info("Got response: {}", responseBody);

        return responseBody;
      }
    } catch (Exception e) {
      throw new RequestProcessingException("Failed to patch user.", e);
    }
  }

  /**
   * Deletes given user.
   *
   * @param userId existing user id
   * @throws RequestProcessingException if failed to execute request
   */
  public void deleteUser(final long userId) throws RequestProcessingException {
    try (final CloseableHttpClient httpClient = HttpClients.createDefault()) {
      log.info("Delete user with ID: {}", userId);
      final HttpHost httpHost = HttpHost.create("https://reqres.in");
      final URI uri = new URIBuilder("/api/users/" + userId).build();
      final HttpDelete httpDeleteRequest = new HttpDelete(uri);
      log.debug(
          "Executing {} request: {} on host {}",
          httpDeleteRequest.getMethod(),
          httpDeleteRequest.getUri(),
          httpHost);

      // Create a response handler
      final BasicHttpClientResponseHandler handler = new BasicHttpClientResponseHandler();
      final String responseBody = httpClient.execute(httpHost, httpDeleteRequest, handler);
      log.info("Got response: {}", responseBody);
    } catch (Exception e) {
      throw new RequestProcessingException("Failed to update user.", e);
    }
  }

  /**
   * Executes HTTP OPTIONS method on the server.
   *
   * @return headers
   * @throws RequestProcessingException if failed to execute request
   */
  public Map<String, String> executeOptions() throws RequestProcessingException {
    try (final CloseableHttpClient httpClient = HttpClients.createDefault()) {
      final HttpHost httpHost = HttpHost.create("https://reqres.in");
      final URI uri = new URIBuilder("/api/users/").build();
      final HttpOptions httpOptionsRequest = new HttpOptions(uri);
      log.debug(
          "Executing {} request: {} on host {}",
          httpOptionsRequest.getMethod(),
          httpOptionsRequest.getUri(),
          httpHost);

      // Create a response handler
      // Implement HttpClientResponseHandler::handleResponse(ClassicHttpResponse response)
      final HttpClientResponseHandler<Map<String, String>> handler =
          response ->
              StreamSupport.stream(
                      Spliterators.spliteratorUnknownSize(
                          response.headerIterator(), Spliterator.ORDERED),
                      false)
                  .collect(Collectors.toMap(Header::getName, Header::getValue));
      final Map<String, String> headers = httpClient.execute(httpHost, httpOptionsRequest, handler);
      log.info("Got headers: {}", headers);
      return headers;
    } catch (Exception e) {
      throw new RequestProcessingException("Failed to execute the request.", e);
    }
  }
}
