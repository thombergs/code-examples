package io.refactoring.http5.client.example.config.helper;

import io.refactoring.http5.client.example.config.interceptor.CustomHttpExecutionInterceptor;
import io.refactoring.http5.client.example.config.interceptor.CustomHttpRequestInterceptor;
import io.refactoring.http5.client.example.config.interceptor.CustomHttpResponseInterceptor;
import io.refactoring.http5.client.example.helper.BaseHttpRequestHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.HttpRoute;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.cache.CacheConfig;
import org.apache.hc.client5.http.impl.cache.CachingHttpClientBuilder;
import org.apache.hc.client5.http.impl.cache.CachingHttpClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

/** Utility to handle HTTP client configurations. */
@Slf4j
public class HttpConfigurationHelper extends BaseHttpRequestHelper {

  /**
   * Populates request config.
   *
   * @param httpClientBuilder the http client builder
   * @param requestTimeoutMillis the request timeout millis
   * @param responseTimeoutMillis the response timeout millis
   * @param connectionKeepAliveMillis the connection keep alive millis
   * @return http client builder
   */
  public HttpClientBuilder populateRequestConfig(
      HttpClientBuilder httpClientBuilder,
      final long requestTimeoutMillis,
      final long responseTimeoutMillis,
      final long connectionKeepAliveMillis) {
    final Timeout requestTimeout = Timeout.ofMilliseconds(requestTimeoutMillis);
    final Timeout responseTimeout = Timeout.ofMilliseconds(responseTimeoutMillis);
    final TimeValue connectionKeepAlive = TimeValue.ofMilliseconds(connectionKeepAliveMillis);

    final RequestConfig requestConfig =
        RequestConfig.custom()
            .setConnectionRequestTimeout(requestTimeout)
            .setResponseTimeout(responseTimeout)
            .setConnectionKeepAlive(connectionKeepAlive)
            .build();
    return httpClientBuilder.setDefaultRequestConfig(requestConfig);
  }

  /**
   * Populates caching config.
   *
   * @param maxCacheEntries the max cache entries
   * @param maxObjectSize the max object size
   * @return the caching http client builder
   */
  public CachingHttpClientBuilder populateCachingConfig(
      final int maxCacheEntries, final int maxObjectSize) {
    final CacheConfig cacheConfig =
        CacheConfig.custom()
            .setMaxCacheEntries(maxCacheEntries)
            .setMaxObjectSize(maxObjectSize)
            .build();
    return CachingHttpClients.custom().setCacheConfig(cacheConfig);
  }

  /**
   * Gets pooled closeable http client.
   *
   * @param host the host
   * @return the pooled closeable http client
   */
  public CloseableHttpClient getPooledCloseableHttpClient(final String host) {
    // Increase max total connection to 200
    // Increase default max per route connection per route to 20
    return getPooledCloseableHttpClient(host, 80, 200, 20, 1000, 1000, 1000);
  }

  /**
   * Gets pooled closeable http client.
   *
   * @param host the host
   * @param port the port
   * @param maxTotalConnections the max total connections
   * @param defaultMaxPerRoute the default max per route
   * @param requestTimeoutMillis the request timeout millis
   * @param responseTimeoutMillis the response timeout millis
   * @param connectionKeepAliveMillis the connection keep alive millis
   * @return the pooled closeable http client
   */
  public CloseableHttpClient getPooledCloseableHttpClient(
      final String host,
      int port,
      int maxTotalConnections,
      int defaultMaxPerRoute,
      long requestTimeoutMillis,
      long responseTimeoutMillis,
      long connectionKeepAliveMillis) {
    final PoolingHttpClientConnectionManager connectionManager =
        new PoolingHttpClientConnectionManager();
    connectionManager.setMaxTotal(maxTotalConnections);
    connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);

    final HttpHost httpHost = new HttpHost(host, port);
    connectionManager.setMaxPerRoute(new HttpRoute(httpHost), 50);
    HttpClientBuilder httpClientBuilder = HttpClients.custom();
    httpClientBuilder =
        populateRequestConfig(
            httpClientBuilder,
            requestTimeoutMillis,
            responseTimeoutMillis,
            connectionKeepAliveMillis);
    return httpClientBuilder.setConnectionManager(connectionManager).build();
  }

  /**
   * Gets cached closeable http client.
   *
   * @param maxCacheEntries the max cache entries
   * @param maxObjectSize the max object size
   * @return the cached closeable http client
   */
  public CloseableHttpClient getCachedCloseableHttpClient(
      final int maxCacheEntries, final int maxObjectSize) {
    return populateCachingConfig(maxCacheEntries, maxObjectSize).build();
  }

  /**
   * Gets request intercepting closeable http client.
   *
   * @return the request intercepting closeable http client
   */
  public CloseableHttpClient getRequestInterceptingCloseableHttpClient() {
    return HttpClients.custom()
        .addRequestInterceptorFirst(new CustomHttpRequestInterceptor())
        .build();
  }

  /**
   * Gets response intercepting closeable http client.
   *
   * @return the response intercepting closeable http client
   */
  public CloseableHttpClient getResponseInterceptingCloseableHttpClient() {
    return HttpClients.custom()
        .addResponseInterceptorFirst(new CustomHttpResponseInterceptor())
        .build();
  }

  /**
   * Gets execution intercepting closeable http client.
   *
   * @return the exec intercepting closeable http client
   */
  public CloseableHttpClient getExecInterceptingCloseableHttpClient() {
    return HttpClients.custom()
        .addExecInterceptorFirst("customExecInterceptor", new CustomHttpExecutionInterceptor())
        .build();
  }
}
