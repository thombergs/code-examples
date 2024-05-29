package io.refactoring.http5.client.example.classic;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/** This example demonstrates how to process HTTP responses using the HTTP client. */
@Slf4j
public class BasicClientTests extends BaseClassicExampleTests {

  @Test
  void executeGetRequest() {
//    CloseableHttpResponse closeableHttpResponse = null;
//    try (final CloseableHttpClient httpclient = HttpClientBuilder.create().build()) {
//      final ClassicHttpRequest httpGet = new HttpGet("https://reqres.in/api/users?page=1");
//      log.debug("Executing request: {}", httpGet.getRequestUri());
//
//      // Create a response
//      closeableHttpResponse = httpclient.execute(httpGet);
//
//      // verify
//      final Consumer<CloseableHttpResponse> responseRequirements =
//          response -> {
//            assertThat(response.getEntity()).as("Failed to get response.").isNotNull();
//            assertThat(response.getProtocolVersion())
//                .as("Invalid protocol version.")
//                .isEqualTo(HttpVersion.HTTP_1_1);
//            assertThat(response.getStatusLine().getProtocolVersion())
//                .as("Invalid protocol version in status line.")
//                .isEqualTo(HttpVersion.HTTP_1_1);
//            assertThat(response.getStatusLine().getStatusCode())
//                .as("Invalid HTTP status in status line.")
//                .isEqualTo(HttpStatus.SC_OK);
//            assertThat(response.getStatusLine().getReasonPhrase())
//                .as("Invalid reason phrase in status line.")
//                .isEqualTo("OK");
//          };
//      assertThat(closeableHttpResponse).satisfies(responseRequirements);
//
//      final HttpEntity respEntity = closeableHttpResponse.getEntity();
//      final String respStr = EntityUtils.toString(respEntity);
//      log.info("Got response: {}", makePretty(respStr));
//    } catch (IOException e) {
//      fail("Failed to execute HTTP request.", e);
//    } finally {
//      if (closeableHttpResponse != null) {
//        try {
//          closeableHttpResponse.close();
//        } catch (IOException e) {
//          fail("Failed to close the HTTP response.", e);
//        }
//      }
//    }
  }
}
