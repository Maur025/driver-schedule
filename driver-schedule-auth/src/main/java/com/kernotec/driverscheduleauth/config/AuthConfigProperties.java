package com.kernotec.driverscheduleauth.config;

import com.kernotec.driverscheduleauth.jpa.enums.RefreshTokenSecureEnum;
import com.kernotec.driverscheduleauth.jpa.enums.TimeMeasureEnum;
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
    private TimeMeasureEnum accessTokenExpType;
    private Integer refreshTokenExp;
    private TimeMeasureEnum refreshTokenExpType;
    private RefreshTokenSecureEnum refreshTokenSecure;
    private String adminPassword;
}
