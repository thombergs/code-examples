/**
 * 
 */
package io.pratik;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.core5.http.ParseException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author pratikdas
 *
 */
public class WebClientApp {
	
	public void invoke() {
		WebClient client = WebClient.create();
		client
		.get()
		.uri(URLConstants.URL)
        .header(URLConstants.API_KEY_NAME, URLConstants.API_KEY_VALUE)
        .retrieve()
        .bodyToMono(String.class)
        .subscribe(result->System.out.println(result));
	}
	
	public void invokePost() {
		WebClient client = WebClient.create();
		
		String result = client
		.post()
        .uri("https://reqbin.com/echo/post/json")
        .body(BodyInserters.fromValue(prepareRequest()))
        .exchange()
        .flatMap(response -> response.bodyToMono(String.class))
        .block();
		
		System.out.println("result::"+result);
	}
	
	private String prepareRequest()  {
		var values = new HashMap<String, String>() {
			{
				put("Id", "12345");
				put("Customer", "Roger Moose");
				put("Quantity", "3");
				put("Price","167.35");
			}
		};

		var objectMapper = new ObjectMapper();
		String requestBody;
		try {
			requestBody = objectMapper.writeValueAsString(values);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
		return requestBody;
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException, ParseException {
		new WebClientApp().invoke();

	}

}
