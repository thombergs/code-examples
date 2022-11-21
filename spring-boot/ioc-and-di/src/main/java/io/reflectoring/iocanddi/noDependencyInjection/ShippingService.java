package io.reflectoring.iocanddi.noDependencyInjection;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.sql.DataSource;
import java.time.Duration;

public class ShippingService {
    private RestTemplate restTemplate;
    private DataSource dataSource;

    public ShippingService() {
        RestTemplate restTemplate =
                new RestTemplateBuilder()
                        .setConnectTimeout(Duration.ofMillis(1000))
                        .setReadTimeout(Duration.ofMillis(2000))
                        .build();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("http://payment-service-uri:8080"));

        this.restTemplate = restTemplate;

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.h2.Driver");
        dataSourceBuilder.url("jdbc:h2:file:C:/temp/test");
        dataSourceBuilder.username("shipping-user");
        dataSourceBuilder.password("superSecretPassword");
        DataSource dataSource = dataSourceBuilder.build();

        this.dataSource = dataSource;
    }

    private boolean packageIsShippable(String id) {
        // business logic that make REST and database calls
        return true;
    }

    public void ship(String shipmentId) {
        if (packageIsShippable(shipmentId)) {
            // ship the thing
        }
    }
}
