package io.refactoring.http5.client.example.util;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** The Http configuration helper tests. */
class HttpConfigurationHelperTests {
    private final HttpConfigurationHelper httpConfigurationHelper = new HttpConfigurationHelper();
  @Test
  void getPooledCloseableHttpClient() {
      final CloseableHttpClient httpClient = httpConfigurationHelper.getPooledCloseableHttpClient("localhost");

  }
}
