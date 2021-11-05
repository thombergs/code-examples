/**
 * 
 */
package io.pratik;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author pratikdas
 *
 */
public class ApacheHttpClientApp {

	
    public void invoke() {
        
            try(
            		CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();) {
            		client.start();
                    
                    final SimpleHttpRequest request = SimpleRequestBuilder.get()
                    		.setUri(URLConstants.URL)
                    		.addHeader(URLConstants.API_KEY_NAME, URLConstants.API_KEY_VALUE)
                            .build();
                    
                    Future<SimpleHttpResponse> future = 
                    		client.execute(request, 
                    				new FutureCallback<SimpleHttpResponse>() {

						@Override
						public void completed(SimpleHttpResponse result) {
							String response = result.getBodyText();
							System.out.println("response::"+response);
						}

						@Override
						public void failed(Exception ex) {
							System.out.println("response::"+ex);
						}

						@Override
						public void cancelled() {
							// TODO Auto-generated method stub
							
						}
                    	
                    });
                    HttpResponse response = future.get();
                    
	                // Get HttpResponse Status
	                System.out.println("version "+ response.getVersion());              // HTTP/1.1
	                System.out.println(response.getCode());   // 200
	                System.out.println(response.getReasonPhrase()); // OK
	
            } catch (InterruptedException | ExecutionException | IOException e) {
				e.printStackTrace();
			} 
    }
    
    public void invokePost() {
    	
    	StringEntity stringEntity = new StringEntity(prepareRequest());
    	HttpPost httpPost = new HttpPost("https://reqbin.com/echo/post/json");

        httpPost.setEntity(stringEntity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        try(
        		CloseableHttpClient httpClient = HttpClients.createDefault();
        		
                CloseableHttpResponse response = httpClient.execute(httpPost);) {

                // Get HttpResponse Status
                System.out.println("version "+ response.getVersion()); // HTTP/1.1
                System.out.println(response.getCode());   // 200
                System.out.println(response.getReasonPhrase()); // OK

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    String result = EntityUtils.toString(entity);
                    System.out.println(result);
                }

        } catch (ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
    }
    
	/**
	 * @return
	 * @throws JsonProcessingException
	 */
	private String prepareRequest() {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return requestBody;
	}
	public static void main(String[] args) throws ClientProtocolException, IOException, ParseException {
		new ApacheHttpClientApp().invoke();

	}

}
