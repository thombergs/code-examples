package io.refactoring.http5.client.example.async.helper;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.concurrent.*;
import javax.net.ssl.SSLContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.async.methods.*;
import org.apache.hc.client5.http.config.TlsConfig;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.async.MinimalH2AsyncClient;
import org.apache.hc.client5.http.impl.async.MinimalHttpAsyncClient;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.config.Http1Config;
import org.apache.hc.core5.http.message.StatusLine;
import org.apache.hc.core5.http.nio.AsyncClientEndpoint;
import org.apache.hc.core5.http.nio.AsyncRequestProducer;
import org.apache.hc.core5.http.nio.entity.AsyncEntityProducers;
import org.apache.hc.core5.http.nio.entity.StringAsyncEntityConsumer;
import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
import org.apache.hc.core5.http.nio.support.BasicRequestProducer;
import org.apache.hc.core5.http.nio.support.BasicResponseConsumer;
import org.apache.hc.core5.http2.HttpVersionPolicy;
import org.apache.hc.core5.http2.config.H2Config;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.Timeout;

@Slf4j
public class AsyncClientH2Multiplexing {

  public static void main(final String[] args) throws Exception {

    final IOReactorConfig ioReactorConfig =
        IOReactorConfig.custom().setSoTimeout(Timeout.ofSeconds(5)).build();

    final CloseableHttpAsyncClient client =
        HttpAsyncClients.custom().setIOReactorConfig(ioReactorConfig).setH2Config(H2Config.DEFAULT).build();

    client.start();

    final HttpHost target = new HttpHost("nghttp2.org");
    final String requestUri = "/httpbin/post";

    final AsyncRequestProducer requestProducer =
        new BasicRequestProducer(
            Method.POST,
            target,
            requestUri,
            AsyncEntityProducers.create("stuff", ContentType.TEXT_PLAIN));
    System.out.println("Executing POST request to " + requestUri);
    final Future<Message<HttpResponse, String>> future =
        client.execute(
            requestProducer,
            new BasicResponseConsumer<String>(new StringAsyncEntityConsumer()),
            new FutureCallback<Message<HttpResponse, String>>() {

              @Override
              public void completed(final Message<HttpResponse, String> message) {
                System.out.println(requestUri + "->" + message.getHead().getCode());
                System.out.println(message.getBody());
              }

              @Override
              public void failed(final Exception ex) {
                System.out.println(requestUri + "->" + ex);
              }

              @Override
              public void cancelled() {
                System.out.println(requestUri + " cancelled");
              }
            });
    future.get();

    System.out.println("Shutting down");
    client.close(CloseMode.GRACEFUL);
  }

  private static void test1()
      throws NoSuchAlgorithmException,
          KeyManagementException,
          KeyStoreException,
          InterruptedException,
          ExecutionException,
          TimeoutException {
    MinimalH2AsyncClient minimalH2AsyncClient =
        HttpAsyncClients.createHttp2Minimal(
            H2Config.DEFAULT, IOReactorConfig.DEFAULT, getTlsStrategy());

    final MinimalHttpAsyncClient client =
        HttpAsyncClients.createMinimal(
            H2Config.DEFAULT,
            Http1Config.DEFAULT,
            IOReactorConfig.DEFAULT,
            PoolingAsyncClientConnectionManagerBuilder.create()
                .setTlsStrategy(getTlsStrategy())
                .setDefaultTlsConfig(
                    TlsConfig.custom().setVersionPolicy(HttpVersionPolicy.FORCE_HTTP_2).build())
                .build());

    client.start();

    final HttpHost target = new HttpHost("https", "nghttp2.org");
    final Future<AsyncClientEndpoint> leaseFuture = client.lease(target, null);
    final AsyncClientEndpoint endpoint = leaseFuture.get(30, TimeUnit.SECONDS);
    try {
      final String[] requestUris =
          new String[] {"/httpbin/ip", "/httpbin/user-agent", "/httpbin/headers"};

      final CountDownLatch latch = new CountDownLatch(requestUris.length);
      for (final String requestUri : requestUris) {
        final SimpleHttpRequest request =
            SimpleRequestBuilder.get().setHttpHost(target).setPath(requestUri).build();

        System.out.println("Executing request " + request);
        endpoint.execute(
            SimpleRequestProducer.create(request),
            SimpleResponseConsumer.create(),
            new FutureCallback<SimpleHttpResponse>() {

              @Override
              public void completed(final SimpleHttpResponse response) {
                latch.countDown();
                System.out.println(request + "->" + new StatusLine(response));
                System.out.println(response.getBodyText());
              }

              @Override
              public void failed(final Exception ex) {
                latch.countDown();
                System.out.println(request + "->" + ex);
              }

              @Override
              public void cancelled() {
                latch.countDown();
                System.out.println(request + " cancelled");
              }
            });
      }
      latch.await();
    } finally {
      endpoint.releaseAndReuse();
    }

    System.out.println("Shutting down");
    client.close(CloseMode.GRACEFUL);
  }

  private static TlsStrategy getTlsStrategy()
      throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
    // Trust standard CA and those trusted by our custom strategy
    final SSLContext sslContext =
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
                  final X509Certificate cert = chain[0];
                  log.warn(
                      "Bypassing SSL certificate validation for {}",
                      cert.getSubjectX500Principal().getName());
                  return true;
                })
            .build();

    return ClientTlsStrategyBuilder.create().setSslContext(sslContext).build();
  }
}
