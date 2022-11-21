package io.reflectoring.iocanddi.withDependencyInjection;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.sql.DataSource;
import java.time.Duration;

public class ShippingService {
    private RestTemplate restTemplate;
    private DataSource dataSource;

    public ShippingService(RestTemplate restTemplate, DataSource dataSource) {
       this.restTemplate = restTemplate;
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
