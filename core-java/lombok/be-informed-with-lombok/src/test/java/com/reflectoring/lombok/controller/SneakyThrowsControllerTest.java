package com.reflectoring.lombok.controller;

import com.reflectoring.lombok.service.SneakyThrowsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SneakyThrowsControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private SneakyThrowsService sneakyThrowsService;

    @Test
    public void contextLoads() {
        assertThat(sneakyThrowsService).isNotNull();
    }

    @Test
    public void getFileData_errorResponse() throws Exception {

        Map<String, String> apiResponse = testRestTemplate.exchange(
                RequestEntity.get(new URI("http://localhost:"+port+"/api/sneakyThrows")).build(),
                new ParameterizedTypeReference<Map<String, String>>() {}).getBody();
        assertThat(apiResponse).isEqualTo(Map.of("status", "FAIL"));

    }


}
