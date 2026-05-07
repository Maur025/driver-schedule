package com.kernotec.driverschedule.service.webflux.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "modules-app")
@Getter
@Setter
public class ModuleAppProperties {

    private ServiceUri appAuth;

    public record ServiceUri(String scheme, String host, String port) {

    }
}
