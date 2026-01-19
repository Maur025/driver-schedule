package com.kernotec.driverscheduleauth.config;

import com.kernotec.driverscheduleauth.config.bucket.ThrottlingFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final DriverScheduleAuthProperties driverScheduleAuthProperties;
    private final ThrottlingFilter throttlingFilter;

    private final AccessDeniedHandler webAccessDeniedHandler;
    private final AuthenticationEntryPoint webAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ConditionalOnWebApplication(type = Type.SERVLET)
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
            .cors(configurer -> configurer.configurationSource(getCorsConfigurationSource()))
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth.requestMatchers(
                    "/realms/driver-schedule-auth/account/**")
                .authenticated()
                .anyRequest()
                .permitAll())
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
            .addFilterBefore(throttlingFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(configurer -> {
                configurer.authenticationEntryPoint(webAuthenticationEntryPoint);
                configurer.accessDeniedHandler(webAccessDeniedHandler);
            })
            .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("roles");
        authoritiesConverter.setAuthorityPrefix("");

        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    public CorsConfigurationSource getCorsConfigurationSource() {
        var corsConfigurationSource = new CorsConfiguration();

        corsConfigurationSource.setAllowedOriginPatterns(
            driverScheduleAuthProperties.getAllowedOriginsList());
        corsConfigurationSource.setAllowedMethods(
            List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        corsConfigurationSource.setAllowedHeaders(List.of("*"));
        corsConfigurationSource.setAllowCredentials(true);
        corsConfigurationSource.setExposedHeaders(List.of("*"));

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfigurationSource);

        return source;
    }
}
