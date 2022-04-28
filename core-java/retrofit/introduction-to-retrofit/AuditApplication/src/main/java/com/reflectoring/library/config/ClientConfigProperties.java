package com.reflectoring.library.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "library")
@Getter
@Setter
public class ClientConfigProperties {

    private String endpoint;

    private String username;

    private String password;

    private Integer connectionTimeout;

    private Integer readWriteTimeout;
}
