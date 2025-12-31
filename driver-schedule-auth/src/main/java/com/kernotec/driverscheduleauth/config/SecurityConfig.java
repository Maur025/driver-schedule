package com.kernotec.driverscheduleauth.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.kernotec.driverscheduleauth.config.bucket.ThrottlingFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
            .cors(configurer -> configurer.configurationSource(getCorsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth.requestMatchers(
                    "/realms/driver-schedule-auth/account/**")
                .authenticated()
                .anyRequest()
                .permitAll())
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
            .addFilterBefore(throttlingFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
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
