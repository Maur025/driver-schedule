package com.kernotec.driverscheduleservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigApp {

    private static final AntPathRequestMatcher[] AUTH_WHITELIST = {
        new AntPathRequestMatcher("/v3/api-docs/**"),
        new AntPathRequestMatcher("/swagger-ui/**"),
        new AntPathRequestMatcher("/swagger-ui.html"),
        new AntPathRequestMatcher("/webjars/**"),
        new AntPathRequestMatcher("/*/v3/api-docs/**"),
        new AntPathRequestMatcher("/*/swagger-ui/**"),
        new AntPathRequestMatcher("/*/swagger-ui.html"),
        new AntPathRequestMatcher("/actuator/**"),
        new AntPathRequestMatcher("/ws-driver-schedule/**")
    };

    @Autowired
    @Qualifier("webAuthenticationEntryPoint")
    private AuthenticationEntryPoint webAuthenticationEntryPoint;

    @Autowired
    @Qualifier("webAccessDeniedHandler")
    private AccessDeniedHandler webAccessDeniedHandler;

    @Bean
    @ConditionalOnWebApplication(type = Type.SERVLET)
    @Primary
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain apiFilterChain(HttpSecurity httpSecurity,
        CorsConfigurationSource configurationSource) throws Exception
    {
        httpSecurity.cors(configurer -> configurer.configurationSource(configurationSource))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(registry -> registry.requestMatchers(AUTH_WHITELIST)
                .permitAll()
                .anyRequest()
                .authenticated())
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
            .exceptionHandling(configurer -> {
                configurer.authenticationEntryPoint(webAuthenticationEntryPoint);
                configurer.accessDeniedHandler(webAccessDeniedHandler);
            });

        return httpSecurity.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("scope");
        authoritiesConverter.setAuthorityPrefix("SCOPE_");

        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        return jwtAuthenticationConverter;
    }
}
