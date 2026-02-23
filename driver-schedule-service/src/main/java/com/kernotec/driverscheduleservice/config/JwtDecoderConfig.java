package com.kernotec.driverscheduleservice.config;

import java.util.List;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtDecoderConfig {

    private final AuthConfigProperties authConfigProperties;

    public JwtDecoderConfig(AuthConfigProperties authConfigProperties) {
        this.authConfigProperties = authConfigProperties;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        var secretKeySpec = new SecretKeySpec(
            authConfigProperties.getSecretKey()
                .getBytes(), "HmacSHA256"
        );

        var jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
            .build();

        OAuth2TokenValidator<Jwt> audienceValidator = new JwtClaimValidator<List<String>>(
            JwtClaimNames.AUD,
            aud -> aud != null && aud.contains(authConfigProperties.getAudience())
        );

        OAuth2TokenValidator<Jwt> withDefaultValidators = new DelegatingOAuth2TokenValidator<>(
            new JwtTimestampValidator(), audienceValidator);

        jwtDecoder.setJwtValidator(withDefaultValidators);

        return jwtDecoder;
    }
}
