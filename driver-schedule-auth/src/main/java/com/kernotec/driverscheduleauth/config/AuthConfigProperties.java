package com.kernotec.driverscheduleauth.config;

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
    private String audience;
    private Integer accessTokenExp;
    private String accessTokenExpType;
    private Integer refreshTokenExp;
    private String refreshTokenExpType;
    private String refreshTokenSecure;
}
