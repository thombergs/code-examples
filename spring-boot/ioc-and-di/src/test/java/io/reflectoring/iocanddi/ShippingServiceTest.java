package io.reflectoring.iocanddi;

import io.reflectoring.iocanddi.withDependencyInjection.ShippingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

import static org.mockito.Mockito.when;

public class ShippingServiceTest {

    @Test
    void testShipping() {
        RestTemplate restTemplateMock = Mockito.mock(RestTemplate.class);
        DataSource dataSourceMock = Mockito.mock(DataSource.class);

        when(restTemplateMock.getForEntity("url", String.class))
                .thenReturn(ResponseEntity.ok("What Ever"));

        ShippingService shippingService = new ShippingService(restTemplateMock, dataSourceMock);
        shippingService.ship("some Id");

        // assert stuff
    }
}
