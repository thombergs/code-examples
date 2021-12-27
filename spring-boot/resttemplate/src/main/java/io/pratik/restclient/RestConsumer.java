/**
 * 
 */
package io.pratik.restclient;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.pratik.apis.models.Product;

/**
 * @author pratikdas
 *
 */
public class RestConsumer {
	
	public void getProductAsJson() {
		RestTemplate restTemplate = new RestTemplate();
		String resourceUrl
		  = "http://localhost:8080/products";
		ResponseEntity<String> response
		  = restTemplate.getForEntity(resourceUrl, String.class);
		
		String productsJson = response.getBody();
		
		System.out.println(productsJson);
	}
	
	public void getProducts() {
		//RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
		RestTemplate restTemplate = new RestTemplate();
		String resourceUrl
		  = "http://localhost:8080/products";
		ResponseEntity<List> response
		  = restTemplate.getForEntity(resourceUrl, List.class);
		
		List<Product> products = response.getBody();
		System.out.println(products);
	}
	
	public void getProductObjects() {
		//RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
		RestTemplate restTemplate = new RestTemplate();
		String resourceUrl
		  = "http://localhost:8080/products";
		List<?> products
		  = restTemplate.getForObject(resourceUrl, List.class);
		
		System.out.println(products);
	}
	
	public void getHeaders() {
		RestTemplate restTemplate = new RestTemplate();
		String resourceUrl
		  = "http://localhost:8080/products";
		HttpHeaders httpHeaders = restTemplate.headForHeaders(resourceUrl);
		System.out.println("headers "+httpHeaders);
	}
	
	public void createProduct() {
		RestTemplate restTemplate = new RestTemplate();
		String resourceUrl
		  = "http://localhost:8080/products";
		HttpEntity<Product> request = new HttpEntity<Product>(new Product("Television", "Samsung",1145.67,"S001"));
		String productCreateResponse = restTemplate.postForObject(resourceUrl, request, String.class);
		
		
		
		System.out.println(productCreateResponse);
	}
	
	public void createProductWithExchange() {
		
		RestTemplate restTemplate = new RestTemplate();
		String resourceUrl
		  = "http://localhost:8080/products";
		HttpEntity<Product> request = new HttpEntity<Product>(new Product("Television", "Samsung",1145.67,"S001"));
		ResponseEntity<String> productCreateResponse = restTemplate.exchange(resourceUrl, HttpMethod.POST, request, String.class);
		
		
		
		System.out.println(productCreateResponse);
	}
	
	public void updateProductWithExchange() {
		RestTemplate restTemplate = new RestTemplate();
		
		 HttpHeaders headers = new HttpHeaders();
		 headers.setContentType(MediaType.APPLICATION_JSON);
		 headers.set("Authorization", "Bearer ...");
		 
		String resourceUrl
		  = "http://localhost:8080/products";
		HttpEntity<Product> request = new HttpEntity<Product>(new Product("Television", "Samsung",1145.67,"S001"));
	    restTemplate.exchange(resourceUrl, HttpMethod.PUT, request, Void.class);
		
		
	}
	

	
	public void getProductAsStream() {
		final Product fetchProductRequest = new Product("Television", "Samsung",1145.67,"S001");
		RestTemplate restTemplate = new RestTemplate();
		String resourceUrl
		  = "http://localhost:8080/products";
	
		
		RequestCallback requestCallback = request -> {
	        ObjectMapper mapper = new ObjectMapper();
	            mapper.writeValue(request.getBody(), 
	            		fetchProductRequest);

		        request.getHeaders()
			     .setAccept(Arrays.asList(
			    		 MediaType.APPLICATION_OCTET_STREAM, 
			    		 MediaType.ALL));
		        };

		ResponseExtractor<Void> responseExtractor = response -> {
			     Path path = Paths.get("some/path");
			     Files.copy(response.getBody(), path);
			     return null;
			 };
		restTemplate.execute(resourceUrl, 
				HttpMethod.GET, 
				requestCallback, 
				responseExtractor );
		
		
	}
	
	
	public void submitProductForm() {
		RestTemplate restTemplate = new RestTemplate();
		String resourceUrl
		  = "http://localhost:8080/products";
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
		map.add("id", "1");
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
		
		ResponseEntity<String> response = restTemplate.postForEntity(
				  resourceUrl+"/form", request , String.class);
		
		
		
		
		System.out.println(response.getBody());
	}
	
	private ClientHttpRequestFactory getClientHttpRequestFactory() {
	    int connectTimeout = 5000;
	    int readTimeout = 5000;
	    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
	      = new HttpComponentsClientHttpRequestFactory();
	    clientHttpRequestFactory.setConnectTimeout(connectTimeout);
	    clientHttpRequestFactory.setReadTimeout(readTimeout);
	    return clientHttpRequestFactory;
	}
	
	public void createProductWithLocation() {
		RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
		String resourceUrl
		  = "http://localhost:8080/products";
		HttpEntity<Product> request = new HttpEntity<Product>(new Product("Television", "Samsung",1145.67,"S001"));
		URI location = restTemplate.postForLocation(resourceUrl, request);
		
		
		
		System.out.println(location);
	}
	
	public void getAllowedOps() {
		RestTemplate restTemplate = new RestTemplate();
		
		String resourceUrl
		  = "http://localhost:8080/products";
		Set<HttpMethod> optionsForAllow = restTemplate.optionsForAllow(resourceUrl);
		HttpMethod[] supportedMethods
		  = {HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE};	
		System.out.println(optionsForAllow);
	}
	
	public void getProductWithError() {
		RestTemplate restTemplate = new RestTemplate();
		
		RestTemplateCustomizer customizers = new RestTemplateCustomizer() {
			
			@Override
			public void customize(RestTemplate restTemplate) {
				restTemplate.setErrorHandler(new CustomErrorHandler());
				
				
				
			}
		};
		RestTemplateBuilder builder = new RestTemplateBuilder(customizers );
		// restTemplate = new RestTemplate();// builder.build();
		restTemplate =  builder.build();
		
		System.out.println("Default error handler::" + restTemplate.getErrorHandler());
		String resourceUrl
		  = "http://localhost:8080/product/error";
		try {
			Product product
			  = restTemplate.getForObject(resourceUrl, Product.class);

		}catch(RestServiceException ex) {
			System.out.println("error occured: ["+ ex.getError() + "] in service:: "+ ex.getServiceName());
		}
	
	}
	
	public void getProductWithMessageTransformer() {
		RestTemplate restTemplate = new RestTemplate();
		
		RestTemplateCustomizer customizers = new RestTemplateCustomizer() {
			
			@Override
			public void customize(RestTemplate restTemplate) {
				restTemplate.setErrorHandler(new CustomErrorHandler());
				
			}
		};
		RestTemplateBuilder builder = new RestTemplateBuilder(customizers );
		// restTemplate = new RestTemplate();// builder.build();
		restTemplate =  builder.build();
		
		System.out.println("Default error handler::" + restTemplate.getErrorHandler());
		String resourceUrl
		  = "http://localhost:8080/product/error";
		try {
			Product product
			  = restTemplate.getForObject(resourceUrl, Product.class);

		}catch(RestServiceException ex) {
			System.out.println("error occured: ["+ ex.getError() + "] in service:: "+ ex.getServiceName());
		}
	
	}
	
	public void getProductAsXML() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setMessageConverters(getXmlMessageConverter());
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
	    HttpEntity<String> entity = new HttpEntity<>(headers);
	    
	    String productID = "P123445";

	    String resourceUrl
		  = "http://localhost:8080/products/"+productID;
	    ResponseEntity<Product> response = 
	      restTemplate.exchange(resourceUrl, HttpMethod.GET, entity, Product.class, "1");
	    Product resource = response.getBody();
	}
	
	private List<HttpMessageConverter<?>> getXmlMessageConverter() {
	    XStreamMarshaller marshaller = new XStreamMarshaller();
	    marshaller.setAnnotatedClasses(Product.class);
	    MarshallingHttpMessageConverter marshallingConverter = 
	      new MarshallingHttpMessageConverter(marshaller);

	    List<HttpMessageConverter<?>> converters = new ArrayList<>();
	    converters.add(marshallingConverter);
	    return converters;
	}
	
	
	public static void main(String[] args) {
		RestConsumer restConsumer = new RestConsumer();
		
		/*restConsumer.getProductAsJson();
		restConsumer.getProducts();
		restConsumer.getProductObjects();
		
		restConsumer.getHeaders(); restConsumer.createProduct();
		restConsumer.getAllowedOps(); restConsumer.updateProductWithExchange();
		restConsumer.getProductAsStream();
		restConsumer.createProductWithLocation();
		*/
		//restConsumer.getProductWithError();
		restConsumer.getProductAsXML();
		
	}

}
