package com.example.demo;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringfoxConfiguration {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .tags(new Tag("Address Entity", "Repository for Address entities"))
                .apiInfo(new ApiInfo("Customer Service API", "REST API of the Customer Service", "v42", null, null, null, null, Lists.newArrayList()));
    }

}
