package io.reflectoring.iocanddi.withDependencyInjection;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.sql.DataSource;
import java.time.Duration;

public class DIContainer {
    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate =
                new RestTemplateBuilder()
                        .setConnectTimeout(Duration.ofMillis(1000))
                        .setReadTimeout(Duration.ofMillis(2000))
                        .build();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("http://payment-service-uri:8080"));

        return restTemplate;
    }

    private DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.h2.Driver");
        dataSourceBuilder.url("jdbc:h2:file:C:/temp/test");
        dataSourceBuilder.username("shipping-user");
        dataSourceBuilder.password("superSecretPassword");
       return dataSourceBuilder.build();
    }

    public ShippingService getShipmentService() {
        return new ShippingService(getRestTemplate(), getDataSource());
    }
}
