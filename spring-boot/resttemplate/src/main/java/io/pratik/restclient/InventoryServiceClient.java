/**
 * 
 */
package io.pratik.restclient;

import java.util.Collections;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author pratikdas
 *
 */
@Service
public class InventoryServiceClient {
	
	private RestTemplate restTemplate;
	
	public InventoryServiceClient(RestTemplateBuilder builder) {
		restTemplate = builder.errorHandler(
				new CustomErrorHandler())
				.build();
		
		restTemplate.setMessageConverters(Collections.singletonList(
		        new MappingJackson2HttpMessageConverter()));
		
		restTemplate = new RestTemplate(
		        Collections.singletonList(new MappingJackson2HttpMessageConverter()));
		
	
	}
	
	



}
