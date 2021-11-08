/**
 * 
 */
package io.pratik;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author pratikdas
 *
 */
public class HttpClientApp {

	public void invoke() throws URISyntaxException {

		HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).followRedirects(Redirect.NORMAL).build();

		HttpRequest request = HttpRequest.newBuilder().uri(new URI(URLConstants.URL)).GET()
				.header(URLConstants.API_KEY_NAME, URLConstants.API_KEY_VALUE).timeout(Duration.ofSeconds(10)).build();

		client.sendAsync(request, BodyHandlers.ofString())
		      .thenApply(HttpResponse::body)
		      .thenAccept(System.out::println)
			  .join();
	}

	public void invokePost() {
		
		try {
			String requestBody = prepareRequest();
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://reqbin.com/echo/post/json"))
					.POST(HttpRequest.BodyPublishers.ofString(requestBody))
					.header("Accept", "application/json")
					.build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			System.out.println(response.body());
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 * @throws JsonProcessingException
	 */
	private String prepareRequest() throws JsonProcessingException {
		var values = new HashMap<String, String>() {
			{
				put("Id", "12345");
				put("Customer", "Roger Moose");
				put("Quantity", "3");
				put("Price","167.35");
			}
		};

		var objectMapper = new ObjectMapper();
		String requestBody = objectMapper.writeValueAsString(values);
		return requestBody;
	}

	public static void main(String[] args) throws URISyntaxException {
		new HttpClientApp().invoke();
	}

}
