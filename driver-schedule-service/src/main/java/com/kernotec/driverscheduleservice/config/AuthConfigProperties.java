package com.kernotec.driverscheduleservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "oauth2-config")
@Getter
@Setter
public class AuthConfigProperties {

    private String secretKey;
    private String realm;
    private String resource;
    private String audience;
}
