/**
 * 
 */
package io.pratik.healthcheck.usersignup;

import reactor.netty.http.client.HttpClient;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.jayway.jsonpath.JsonPath;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.core.publisher.Mono;
import reactor.netty.tcp.TcpClient;

/**
 * @author Pratik Das
 *
 */
public final class URLHelper {
	public static String shortenURL(final String longURL) {
		WebClient webClient = getWebClient("https://cleanuri.com");
		Mono<String> apiResponse = webClient.post().uri("/api/v1/shorten")
				                     .body(BodyInserters.fromMultipartData("url", longURL))
					                 .retrieve()
					                 .bodyToMono(String.class);
		String jsonResponse = apiResponse.block();
		
		String shortenedURL = JsonPath.read(jsonResponse, "$.result_url");
		return shortenedURL;
	}

	private static WebClient getWebClient(final String baseUrl) {
		TcpClient tcpClient = TcpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
				.doOnConnected(connection -> {
					connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
					connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
				});

		WebClient client = WebClient.builder()
				.baseUrl(baseUrl)
		        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient))).build();
		return client;
	}

}
