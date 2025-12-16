package com.kernotec.driverscheduleauth.config;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kernotec")
@Getter
@Setter
public class DriverScheduleAuthProperties {

    private List<Server> servers;
    private String allowedOrigins;

    public List<String> getAllowedOriginsList() {
        if (allowedOrigins == null || allowedOrigins.isBlank()) {
            return List.of();
        }

        return List.of(allowedOrigins.replaceAll(" ", "")
            .split(","));
    }

    public record Server(String url, String description) {

    }
}
