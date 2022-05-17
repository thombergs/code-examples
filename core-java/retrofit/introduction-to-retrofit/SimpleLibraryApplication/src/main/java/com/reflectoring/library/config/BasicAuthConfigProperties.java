package com.reflectoring.library.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth")
@Getter
@Setter
public class BasicAuthConfigProperties {
    private String username;

    private String password;

}

