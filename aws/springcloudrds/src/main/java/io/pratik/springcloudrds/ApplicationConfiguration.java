/**
 * 
 */
package io.pratik.springcloudrds;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.awspring.cloud.jdbc.config.annotation.RdsInstanceConfigurer;
import io.awspring.cloud.jdbc.datasource.TomcatJdbcDataSourceFactory;

/**
 * @author pratikdas
 *
 */
@Configuration
public class ApplicationConfiguration {
	@Bean
	public RdsInstanceConfigurer instanceConfigurer() {
		return ()-> {
				TomcatJdbcDataSourceFactory dataSourceFactory = new TomcatJdbcDataSourceFactory();
				dataSourceFactory.setInitialSize(10);
				dataSourceFactory.setValidationQuery("SELECT 1 FROM DUAL");
				return dataSourceFactory;
		};
	}
}
