package io.refactoring.http5.client.example.classic.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;

/** Utility for HTTP client interactions for user's domain. */
public class UserRequestProcessingUtils extends RequestProcessingUtils {

  /**
   * Gets users API URI.
   *
   * @return users API URI
   */
  protected URI getUsersApiUri() {
    return URI.create(configurationUtils.getString("app.prop.uri-users-api"));
  }

  /**
   * Gets users API page size.
   *
   * @return users API page size
   */
  public long getUsersApiPageSize() {
    return configurationUtils.getLong("app.prop.uri-users-api-page-size");
  }

  /**
   * Prepares URI for API to operate on users.
   *
   * @return users API URI, for example, /api/users
   * @throws URISyntaxException if failed to prepare users API URI
   */
  public URI prepareUsersApiUri() throws URISyntaxException {
    return new URIBuilder(getUsersApiUri()).build();
  }

  /**
   * Prepares URI for API to get user by ID.
   *
   * @param userId ID of user to be fetched
   * @return get user by id API URI, for example, /api/users/123
   * @throws URISyntaxException if failed to prepare API URI
   */
  public URI prepareUsersApiUri(final long userId) throws URISyntaxException {
    return new URIBuilder(getUsersApiUri() + "/" + userId).build();
  }

  /**
   * Prepares URI for API to fetch users using request parameters.
   *
   * @param parameters request parameters
   * @return users API URI, for example, /api/users?page=1
   * @throws URISyntaxException if failed to prepare API URI
   */
  public URI prepareUsersApiUri(@NonNull final Map<String, String> parameters)
      throws URISyntaxException {
    final List<NameValuePair> nameValuePairs =
        parameters.entrySet().stream()
            .map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue()))
            .map(entry -> (NameValuePair) entry)
            .toList();
    return new URIBuilder(getUsersApiUri()).addParameters(nameValuePairs).build();
  }
}
