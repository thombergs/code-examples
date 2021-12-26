/**
 * 
 */
package io.pratik;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author pratikdas
 *
 */
public class OkHttpClientApp {
	
	public void invoke() throws URISyntaxException, IOException {
	    OkHttpClient client = new OkHttpClient.Builder()
			    .readTimeout(1000, TimeUnit.MILLISECONDS)
			    .writeTimeout(1000, TimeUnit.MILLISECONDS)
			    .build();

		Request request = new Request.Builder()
			.url(URLConstants.URL)
			.get()
			.addHeader(URLConstants.API_KEY_NAME, URLConstants.API_KEY_VALUE)
			.build();

		Call call = client.newCall(request);
		call.enqueue(new Callback() {
	        public void onResponse(Call call, Response response) 
	          throws IOException {
	        	System.out.println(response.body().string());
	        }
	        
	        public void onFailure(Call call, IOException e) {
	            // error
	        }
	    });
		
	}
	
	public void invokePost() throws URISyntaxException, IOException {
	    OkHttpClient client = new OkHttpClient.Builder()
			    .readTimeout(1000, TimeUnit.MILLISECONDS)
			    .writeTimeout(1000, TimeUnit.MILLISECONDS)
			    .build();
	    
		String requestBody = prepareRequest();

	    RequestBody body = RequestBody.create(
	      requestBody,
	      MediaType.parse("application/json"));
	    
		Request request = new Request.Builder()
			.url("https://reqbin.com/echo/post/json")
			.post(body)
			.addHeader(URLConstants.API_KEY_NAME, URLConstants.API_KEY_VALUE)
			.build();

		Response response = client.newCall(request).execute();
		System.out.println(response.body().string());
	}

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
	
	public static void main(String[] args) throws URISyntaxException, IOException {
		new OkHttpClientApp().invokePost();
	}

}
