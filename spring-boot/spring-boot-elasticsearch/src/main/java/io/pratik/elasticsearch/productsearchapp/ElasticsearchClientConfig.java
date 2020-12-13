/**
 * 
 */
package io.pratik.elasticsearch.productsearchapp;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author Pratik Das
 *
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "io.pratik.elasticsearch.repositories")
@ComponentScan(basePackages = { "io.pratik.elasticsearch" })
public class ElasticsearchClientConfig extends AbstractElasticsearchConfiguration {

	@Override
	@Bean
	public RestHighLevelClient elasticsearchClient() {

		final ClientConfiguration clientConfiguration = 
				ClientConfiguration
				.builder()
				.connectedTo("localhost:9200")
				.build();

		return RestClients
				.create(clientConfiguration)
				.rest();
	}
	

}
