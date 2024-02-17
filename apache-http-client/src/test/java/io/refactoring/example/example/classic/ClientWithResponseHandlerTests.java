package io.refactoring.example.example.classic;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This example demonstrates how to process HTTP responses using a response handler.<br><br>
 * <p>
 * This is the recommended approach for sending and receiving HTTP requests and responses. With this approach, the
 * caller can focus on processing HTTP responses and delegate the task of cleaning up system resources to HttpClient.
 * The underlying HTTP connection will always be automatically released back to the connection manager when an HTTP
 * response handler is used.
 */
@Slf4j
public class ClientWithResponseHandlerTests extends BaseClassicExampleTests {

    @Test
    void executeGetRequest() {
        try (final CloseableHttpClient httpclient = HttpClientBuilder.create().build()) {
            final HttpGet httpget = new HttpGet("https://reqres.in/api/users?page=1");
            log.debug("Executing request: {}", httpget.getURI());

            // Create a response handler
            final ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String responseBody = httpclient.execute(httpget, responseHandler);

            // verify
            assertThat(responseBody).isNotEmpty();
            log.info("Got response: {}", makePretty(responseBody));
        } catch (IOException e) {
            Assertions.fail(e);
        }
    }
}
