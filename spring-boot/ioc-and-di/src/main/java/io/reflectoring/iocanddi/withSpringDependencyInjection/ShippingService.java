package io.reflectoring.iocanddi.withSpringDependencyInjection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ShippingService {

    RestTemplate restTemplate;

    @Autowired
    public ShippingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void ship(String shipmentId) {
        // do stuff
    }
}
