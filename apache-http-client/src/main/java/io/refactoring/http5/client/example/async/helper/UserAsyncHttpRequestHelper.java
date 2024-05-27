package io.refactoring.http5.client.example.async.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.refactoring.http5.client.example.RequestProcessingException;
import io.refactoring.http5.client.example.config.interceptor.UserResponseAsyncExecChainHandler;
import io.refactoring.http5.client.example.helper.BaseHttpRequestHelper;
import io.refactoring.http5.client.example.model.User;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.*;
import javax.net.ssl.SSLContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.async.methods.*;
import org.apache.hc.client5.http.config.TlsConfig;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.async.MinimalHttpAsyncClient;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.config.Http1Config;
import org.apache.hc.core5.http.nio.AsyncClientEndpoint;
import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
import org.apache.hc.core5.http.nio.support.BasicRequestProducer;
import org.apache.hc.core5.http2.HttpVersionPolicy;
import org.apache.hc.core5.http2.config.H2Config;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.reactive.ReactiveEntityProducer;
import org.apache.hc.core5.reactive.ReactiveResponseConsumer;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.Timeout;
import org.reactivestreams.Publisher;

/** Handles HTTP requests for user entities. It uses built in types for HTTP processing. */
@Slf4j
public class UserAsyncHttpRequestHelper extends BaseHttpRequestHelper {

  private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private CloseableHttpAsyncClient httpClient;

  private MinimalHttpAsyncClient minimalHttp1Client;
  private MinimalHttpAsyncClient minimalHttp2Client;
  private CloseableHttpAsyncClient httpAsyncInterceptingClient;

  /** Starts http async client. */
  public void startHttpAsyncClient() {
    if (httpClient == null) {
      try {
        PoolingAsyncClientConnectionManager cm =
            PoolingAsyncClientConnectionManagerBuilder.create()
                .setTlsStrategy(getTlsStrategy())
                .build();
        IOReactorConfig ioReactorConfig =
            IOReactorConfig.custom().setSoTimeout(Timeout.ofSeconds(5)).build();
        httpClient =
            HttpAsyncClients.custom()
                .setIOReactorConfig(ioReactorConfig)
                .setConnectionManager(cm)
                .build();
        httpClient.start();
        log.debug("Started HTTP async client.");
      } catch (Exception e) {
        String errorMsg = "Failed to start HTTP async client.";
        log.error(errorMsg, e);
        throw new RuntimeException(errorMsg, e);
      }
    }
  }

  private TlsStrategy getTlsStrategy()
      throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
    // Trust standard CA and those trusted by our custom strategy
    SSLContext sslContext =
        SSLContexts.custom()
            // Custom TrustStrategy implementations are intended for verification
            // of certificates whose CA is not trusted by the system, and where specifying
            // a custom truststore containing the certificate chain is not an option.
            .loadTrustMaterial(
                (chain, authType) -> {
                  // Please note that validation of the server certificate without validation
                  // of the entire certificate chain in this example is preferred to completely
                  // disabling trust verification, however, this still potentially allows
                  // for man-in-the-middle attacks.
                  X509Certificate cert = chain[0];
                  log.warn(
                      "Bypassing SSL certificate validation for {}",
                      cert.getSubjectX500Principal().getName());
                  return true;
                })
            .build();

    return ClientTlsStrategyBuilder.create().setSslContext(sslContext).build();
  }

  /**
   * Starts http 1 async client.
   *
   * @return the minimal http async client
   */
  public MinimalHttpAsyncClient startMinimalHttp1AsyncClient() {
    if (minimalHttp1Client == null) {
      minimalHttp1Client = startMinimalHttpAsyncClient(HttpVersionPolicy.FORCE_HTTP_1);
    }
    return minimalHttp1Client;
  }

  /**
   * Starts http 2 async client.
   *
   * @return minimal http async client
   */
  public MinimalHttpAsyncClient startMinimalHttp2AsyncClient() {
    if (minimalHttp2Client == null) {
      minimalHttp2Client = startMinimalHttpAsyncClient(HttpVersionPolicy.FORCE_HTTP_2);
    }
    return minimalHttp2Client;
  }

  /**
   * Starts http async client.
   *
   * @return minimal Http client;
   */
  private MinimalHttpAsyncClient startMinimalHttpAsyncClient(HttpVersionPolicy httpVersionPolicy) {
    try {
      MinimalHttpAsyncClient minimalHttpClient =
          HttpAsyncClients.createMinimal(
              H2Config.DEFAULT,
              Http1Config.DEFAULT,
              IOReactorConfig.DEFAULT,
              PoolingAsyncClientConnectionManagerBuilder.create()
                  .setTlsStrategy(getTlsStrategy())
                  .setDefaultTlsConfig(
                      TlsConfig.custom().setVersionPolicy(httpVersionPolicy).build())
                  .build());
      minimalHttpClient.start();
      log.debug("Started minimal HTTP async client for {}.", httpVersionPolicy);
      return minimalHttpClient;
    } catch (Exception e) {
      String errorMsg = "Failed to start minimal HTTP async client.";
      log.error(errorMsg, e);
      throw new RuntimeException(errorMsg, e);
    }
  }

  /**
   * Starts http async intercepting client.
   *
   * @return closeable http async client
   */
  public CloseableHttpAsyncClient startHttpAsyncInterceptingClient() {
    try {
      if (httpAsyncInterceptingClient == null) {
        PoolingAsyncClientConnectionManager cm =
            PoolingAsyncClientConnectionManagerBuilder.create()
                .setTlsStrategy(getTlsStrategy())
                .build();
        IOReactorConfig ioReactorConfig =
            IOReactorConfig.custom().setSoTimeout(Timeout.ofSeconds(5)).build();
        httpAsyncInterceptingClient =
            HttpAsyncClients.custom()
                .setIOReactorConfig(ioReactorConfig)
                .setConnectionManager(cm)
                .addExecInterceptorFirst("custom", new UserResponseAsyncExecChainHandler())
                .build();
        httpAsyncInterceptingClient.start();
        log.debug("Started HTTP async client with requests interceptors.");
      }
      return httpAsyncInterceptingClient;
    } catch (Exception e) {
      String errorMsg = "Failed to start HTTP async client.";
      log.error(errorMsg, e);
      throw new RuntimeException(errorMsg, e);
    }
  }

  /** Stops http async client. */
  public void stopHttpAsyncClient() {
    if (httpClient != null) {
      log.info("Shutting down http async client.");
      httpClient.close(CloseMode.GRACEFUL);
      httpClient = null;
    }
  }

  /**
   * Stops minimal http async client.
   *
   * @param minimalHttpClient the minimal http client
   */
  public void stopMinimalHttpAsyncClient(MinimalHttpAsyncClient minimalHttpClient) {
    if (minimalHttpClient != null) {
      log.info("Shutting down minimal http async client.");
      minimalHttpClient.close(CloseMode.GRACEFUL);
      minimalHttpClient = null;
    }
  }

  /**
   * Gets all users for given ids using async callback.
   *
   * @param userIdList user id list
   * @param delayInSec the delay in seconds by which server will send the response
   * @return response if user is found
   * @throws RequestProcessingException if failed to execute request
   */
  public Map<String, String> getUserWithCallback(List<String> userIdList, int delayInSec)
      throws RequestProcessingException {
    Objects.requireNonNull(httpClient, "Make sure that HTTP Async client is started.");
    Map<String, String> userResponseMap = new HashMap<>();
    Map<String, Future<SimpleHttpResponse>> futuresMap = new HashMap<>();
    for (String userId : userIdList) {
      try {
        // Create request
        HttpHost httpHost = HttpHost.create("https://reqres.in");
        URI uri = new URIBuilder("/api/users/" + userId + "?delay=" + delayInSec).build();
        SimpleHttpRequest httpGetRequest =
            SimpleRequestBuilder.get().setHttpHost(httpHost).setPath(uri.getPath()).build();
        log.debug(
            "Executing {} request: {} on host {}",
            httpGetRequest.getMethod(),
            httpGetRequest.getUri(),
            httpHost);

        Future<SimpleHttpResponse> future =
            httpClient.execute(
                SimpleRequestProducer.create(httpGetRequest),
                SimpleResponseConsumer.create(),
                new SimpleHttpResponseCallback(
                    httpGetRequest,
                    MessageFormat.format("Failed to get user for ID: {0}", userId)));
        futuresMap.put(userId, future);
      } catch (Exception e) {
        String message = MessageFormat.format("Failed to get user for ID: {0}", userId);
        log.error(message, e);
        userResponseMap.put(userId, message);
      }
    }

    log.debug("Got {} futures.", futuresMap.size());

    for (Map.Entry<String, Future<SimpleHttpResponse>> futureEntry : futuresMap.entrySet()) {
      String userId = futureEntry.getKey();
      try {
        userResponseMap.put(userId, futureEntry.getValue().get().getBodyText());
      } catch (Exception e) {
        String message = MessageFormat.format("Failed to get user for ID: {0}", userId);
        log.error(message, e);
        userResponseMap.put(userId, message);
      }
    }

    return userResponseMap;
  }

  /**
   * Gets all users for given ids using streams.
   *
   * @param userIdList user id list
   * @param delayInSec the delay in seconds by which server will send the response
   * @return response if user is found
   * @throws RequestProcessingException if failed to execute request
   */
  public Map<Long, String> getUserWithStreams(List<Long> userIdList, int delayInSec)
      throws RequestProcessingException {
    Objects.requireNonNull(httpClient, "Make sure that HTTP Async client is started.");
    Map<Long, String> userResponseMap = new HashMap<>();
    Map<Long, Future<SimpleHttpResponse>> futuresMap = new HashMap<>();
    for (Long userId : userIdList) {
      try {
        // Create request
        HttpHost httpHost = HttpHost.create("https://reqres.in");
        URI uri = new URIBuilder("/api/users/" + userId + "?delay=" + delayInSec).build();
        SimpleHttpRequest httpGetRequest =
            SimpleRequestBuilder.get().setHttpHost(httpHost).setPath(uri.getPath()).build();
        log.debug(
            "Executing {} request: {} on host {}",
            httpGetRequest.getMethod(),
            httpGetRequest.getUri(),
            httpHost);

        Future<SimpleHttpResponse> future =
            httpClient.execute(
                new BasicRequestProducer(httpGetRequest, null),
                new SimpleCharResponseConsumer(
                    httpGetRequest, MessageFormat.format("Failed to get user for ID: {0}", userId)),
                null);
        futuresMap.put(userId, future);
      } catch (Exception e) {
        String message = MessageFormat.format("Failed to get user for ID: {0}", userId);
        log.error(message, e);
        userResponseMap.put(userId, message);
      }
    }

    log.debug("Got {} futures.", futuresMap.size());
    for (Map.Entry<Long, Future<SimpleHttpResponse>> futureEntry : futuresMap.entrySet()) {
      Long userId = futureEntry.getKey();
      try {
        userResponseMap.put(userId, futureEntry.getValue().get().getBodyText());
      } catch (Exception e) {
        String message = MessageFormat.format("Failed to get user for ID: {0}", userId);
        log.error(message, e);
        userResponseMap.put(userId, message);
      }
    }

    return userResponseMap;
  }

  /**
   * Gets all users for given ids using pipelining.
   *
   * @param minimalHttpClient the minimal http client
   * @param userIdList user id list
   * @param delayInSec the delay in seconds by which server will send the response
   * @param scheme the scheme
   * @param hostname the hostname
   * @return response if user is found
   * @throws RequestProcessingException if failed to execute request
   */
  public Map<Long, String> getUserWithPipelining(
      MinimalHttpAsyncClient minimalHttpClient,
      List<Long> userIdList,
      int delayInSec,
      String scheme,
      String hostname)
      throws RequestProcessingException {
    return getUserWithParallelRequests(minimalHttpClient, userIdList, delayInSec, scheme, hostname);
  }

  /**
   * Gets all users for given ids using multiplexing.
   *
   * @param minimalHttpClient the minimal http client
   * @param userIdList user id list
   * @param delayInSec the delay in seconds by which server will send the response
   * @param scheme the scheme
   * @param hostname the hostname
   * @return response if user is found
   * @throws RequestProcessingException if failed to execute request
   */
  public Map<Long, String> getUserWithMultiplexing(
      MinimalHttpAsyncClient minimalHttpClient,
      List<Long> userIdList,
      int delayInSec,
      String scheme,
      String hostname)
      throws RequestProcessingException {
    return getUserWithParallelRequests(minimalHttpClient, userIdList, delayInSec, scheme, hostname);
  }

  private Map<Long, String> getUserWithParallelRequests(
      MinimalHttpAsyncClient minimalHttpClient,
      List<Long> userIdList,
      int delayInSec,
      String scheme,
      String hostname)
      throws RequestProcessingException {

    Objects.requireNonNull(
        minimalHttpClient, "Make sure that minimal HTTP Async client is started.");
    Map<Long, String> userResponseMap = new HashMap<>();
    Map<Long, Future<SimpleHttpResponse>> futuresMap = new LinkedHashMap<>();
    AsyncClientEndpoint endpoint = null;
    Long userId = null;

    try {
      HttpHost httpHost = new HttpHost(scheme, hostname);
      Future<AsyncClientEndpoint> leaseFuture = minimalHttpClient.lease(httpHost, null);
      endpoint = leaseFuture.get(30, TimeUnit.SECONDS);
      CountDownLatch latch = new CountDownLatch(userIdList.size());

      for (Long currentUserId : userIdList) {
        userId = currentUserId;
        // Create request
        URI uri = new URIBuilder("/api/users/" + userId + "?delay=" + delayInSec).build();
        SimpleHttpRequest httpGetRequest =
            SimpleRequestBuilder.get().setHttpHost(httpHost).setPath(uri.getPath()).build();
        log.debug(
            "Executing {} request: {} on host {}",
            httpGetRequest.getMethod(),
            httpGetRequest.getUri(),
            httpHost);

        Future<SimpleHttpResponse> future =
            minimalHttpClient.execute(
                SimpleRequestProducer.create(httpGetRequest),
                SimpleResponseConsumer.create(),
                new CustomHttpResponseCallback(
                    httpGetRequest,
                    MessageFormat.format("Failed to get user for ID: {0}", userId),
                    latch));
        futuresMap.put(userId, future);
      }

      latch.await();
    } catch (RequestProcessingException e) {
      userResponseMap.put(userId, e.getMessage());
    } catch (Exception e) {
      if (userId != null) {
        String message = MessageFormat.format("Failed to get user for ID: {0}", userId);
        log.error(message, e);
        userResponseMap.put(userId, message);
      } else {
        throw new RequestProcessingException("Failed to process request.", e);
      }
    } finally {
      if (endpoint != null) {
        endpoint.releaseAndReuse();
      }
    }

    handleFutureResults(futuresMap, userResponseMap);

    return userResponseMap;
  }

  private void handleFutureResults(
      Map<Long, Future<SimpleHttpResponse>> futuresMap, Map<Long, String> userResponseMap) {
    log.debug("Got {} futures.", futuresMap.size());

    for (Map.Entry<Long, Future<SimpleHttpResponse>> futureEntry : futuresMap.entrySet()) {
      Long currentUserId = futureEntry.getKey();
      try {
        userResponseMap.put(currentUserId, futureEntry.getValue().get().getBodyText());
      } catch (Exception e) {
        String message;
        if (e.getCause() instanceof ConnectionClosedException) {
          message = "Server does not support HTTP/2 multiplexing.";
        } else {
          message = MessageFormat.format("Failed to get user for ID: {0}", currentUserId);
        }
        log.error(message, e);
        userResponseMap.put(currentUserId, message);
      }
    }
  }

  /**
   * Execute requests with interceptors.
   *
   * @param closeableHttpAsyncClient the closeable http async client
   * @param userId the user id
   * @param count the request execution count
   * @param baseNumber the base number
   * @return the map
   * @throws RequestProcessingException the request processing exception
   */
  public Map<Integer, String> executeRequestsWithInterceptors(
      CloseableHttpAsyncClient closeableHttpAsyncClient, Long userId, int count, int baseNumber)
      throws RequestProcessingException {
    Objects.requireNonNull(
        closeableHttpAsyncClient, "Make sure that HTTP Async client is started.");
    Map<Integer, String> userResponseMap = new HashMap<>();
    Map<Integer, Future<SimpleHttpResponse>> futuresMap = new LinkedHashMap<>();

    try {
      HttpHost httpHost = HttpHost.create("https://reqres.in");
      URI uri = new URIBuilder("/api/users/" + userId).build();
      String path = uri.getPath();
      SimpleHttpRequest httpGetRequest =
          SimpleRequestBuilder.get()
              .setHttpHost(httpHost)
              .setPath(path)
              .addHeader("x-base-number", String.valueOf(baseNumber))
              .build();
      for (int i = 0; i < count; i++) {
        try {
          Future<SimpleHttpResponse> future =
              executeInterceptorRequest(closeableHttpAsyncClient, httpGetRequest, i, httpHost);
          futuresMap.put(i, future);
        } catch (RequestProcessingException e) {
          userResponseMap.put(i, e.getMessage());
        }
      }
    } catch (Exception e) {
      String message = MessageFormat.format("Failed to get user for ID: {0}", userId);
      log.error(message, e);
      throw new RequestProcessingException(message, e);
    }

    handleInterceptorFutureResults(futuresMap, userResponseMap);

    return userResponseMap;
  }

  private Future<SimpleHttpResponse> executeInterceptorRequest(
      CloseableHttpAsyncClient closeableHttpAsyncClient,
      SimpleHttpRequest httpGetRequest,
      int i,
      HttpHost httpHost)
      throws URISyntaxException {
    // Update request
    httpGetRequest.removeHeaders("x-req-exec-number");
    httpGetRequest.addHeader("x-req-exec-number", String.valueOf(i));
    log.debug(
        "Executing {} request: {} on host {}",
        httpGetRequest.getMethod(),
        httpGetRequest.getUri(),
        httpHost);

    return closeableHttpAsyncClient.execute(
        httpGetRequest, new SimpleHttpResponseCallback(httpGetRequest, ""));
  }

  private void handleInterceptorFutureResults(
      Map<Integer, Future<SimpleHttpResponse>> futuresMap, Map<Integer, String> userResponseMap) {
    log.debug("Got {} futures.", futuresMap.size());

    for (Map.Entry<Integer, Future<SimpleHttpResponse>> futureEntry : futuresMap.entrySet()) {
      Integer currentRequestId = futureEntry.getKey();
      try {
        userResponseMap.put(currentRequestId, futureEntry.getValue().get().getBodyText());
      } catch (Exception e) {
        String message =
            MessageFormat.format("Failed to get user for request id: {0}", currentRequestId);
        log.error(message, e);
        userResponseMap.put(currentRequestId, message);
      }
    }
  }

  /**
   * Creates user with reactive processing.
   *
   * @param minimalHttpClient the minimal http client
   * @param userName the username
   * @param userJob the user job
   * @param scheme the scheme
   * @param hostname the hostname
   * @return the user with reactive processing
   * @throws RequestProcessingException the request processing exception
   */
  public User createUserWithReactiveProcessing(
      MinimalHttpAsyncClient minimalHttpClient,
      String userName,
      String userJob,
      String scheme,
      String hostname)
      throws RequestProcessingException {
    try {
      HttpHost httpHost = new HttpHost(scheme, hostname);
      URI uri = new URIBuilder(httpHost.toURI() + "/api/users/").build();
      String payloadStr = preparePayload(userName, userJob);
      ReactiveResponseConsumer consumer = new ReactiveResponseConsumer();
      Future<Void> requestFuture = executeRequest(minimalHttpClient, consumer, uri, payloadStr);

      Message<HttpResponse, Publisher<ByteBuffer>> streamingResponse =
          consumer.getResponseFuture().get();
      printHeaders(streamingResponse);
      return prepareResult(streamingResponse, requestFuture);
    } catch (Exception e) {
      throw new RequestProcessingException("Failed to create user. Error: " + e.getMessage(), e);
    }
  }

  private void printHeaders(Message<HttpResponse, Publisher<ByteBuffer>> streamingResponse) {
    log.debug("Head: {}", streamingResponse.getHead());
    for (Header header : streamingResponse.getHead().getHeaders()) {
      log.debug("Header : {}", header);
    }
  }

  private String preparePayload(String userName, String userJob) throws JsonProcessingException {
    Map<String, String> payload = new HashMap<>();
    payload.put("name", userName);
    payload.put("job", userJob);
    return OBJECT_MAPPER.writeValueAsString(payload);
  }

  private User prepareResult(
      Message<HttpResponse, Publisher<ByteBuffer>> streamingResponse, Future<Void> requestFuture)
      throws InterruptedException, ExecutionException, TimeoutException, JsonProcessingException {
    StringBuilder result = new StringBuilder();
    Observable.fromPublisher(streamingResponse.getBody())
        .map(
            byteBuffer -> {
              byte[] bytes = new byte[byteBuffer.remaining()];
              byteBuffer.get(bytes);
              return new String(bytes);
            })
        .materialize()
        .forEach(
            stringNotification -> {
              String value = stringNotification.getValue();
              if (value != null) {
                result.append(value);
              }
            });

    requestFuture.get(1, TimeUnit.MINUTES);
    return OBJECT_MAPPER.readerFor(User.class).readValue(result.toString());
  }

  private Future<Void> executeRequest(
      MinimalHttpAsyncClient minimalHttpClient,
      ReactiveResponseConsumer consumer,
      URI uri,
      String payloadStr) {
    byte[] bs = payloadStr.getBytes(StandardCharsets.UTF_8);
    ReactiveEntityProducer reactiveEntityProducer =
        new ReactiveEntityProducer(
            Flowable.just(ByteBuffer.wrap(bs)), bs.length, ContentType.TEXT_PLAIN, null);

    return minimalHttpClient.execute(
        new BasicRequestProducer("POST", uri, reactiveEntityProducer), consumer, null);
  }
}
