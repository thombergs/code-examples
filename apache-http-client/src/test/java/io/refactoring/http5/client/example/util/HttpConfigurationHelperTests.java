package io.refactoring.http5.client.example.util;

import io.refactoring.http5.client.example.config.helper.HttpConfigurationHelper;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.junit.jupiter.api.Test;

/** The Http configuration helper tests. */
class HttpConfigurationHelperTests {
    private final HttpConfigurationHelper httpConfigurationHelper = new HttpConfigurationHelper();
  @Test
  void getPooledCloseableHttpClient() {
      final CloseableHttpClient httpClient = httpConfigurationHelper.getPooledCloseableHttpClient("localhost");

  }
}
