/**
 * 
 */
package io.pratik.restclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.pratik.restclient.models.RestTemplateError;

/**
 * @author pratikdas
 *
 */
public class CustomErrorHandler implements ResponseErrorHandler{

	@Override
	public boolean hasError(ClientHttpResponse response) 
			throws IOException {
		return (
		          response.getStatusCode().series() ==
		              HttpStatus.Series.CLIENT_ERROR 
		              
		          || response.getStatusCode().series() == 
		              HttpStatus.Series.SERVER_ERROR
		       );
		    
	}

	@Override
	public void handleError(ClientHttpResponse response) 
			throws IOException {

	    if (response.getStatusCode().is4xxClientError() 
	    		|| response.getStatusCode().is5xxServerError()) {


	    	try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()))) {
	          String httpBodyResponse = reader.lines().collect(Collectors.joining(""));
	          
	  		  ObjectMapper mapper = new ObjectMapper();
			  RestTemplateError restTemplateError = mapper.readValue(httpBodyResponse, RestTemplateError.class);

			  
	          throw new RestServiceException(restTemplateError.getPath(), response.getStatusCode(), restTemplateError.getError());
	        }	
	    
	    }
		
	
	}
}
