package io.reflectoring.springwebflux.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import javax.net.ssl.SSLException;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebclientConfig {

    @Bean
    public WebClient getWebClientBuilder() throws SSLException {

        // Define SSLContext in order to connect to secure REST API network
        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        // Create an HTTP Client. Overload with timeout config and LogLevel settings.
        // Override the above SSLContext into the client
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 120 * 1000)
                .doOnConnected(connection -> connection.addHandlerLast(new ReadTimeoutHandler(120 * 1000, TimeUnit.MILLISECONDS)))
                .wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
                .followRedirect(true)
                .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));

        // Add the above connector to the WebClient instance
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
