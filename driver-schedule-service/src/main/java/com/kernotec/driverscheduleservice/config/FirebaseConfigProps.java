package com.kernotec.driverscheduleservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "firebase-config-props")
@Getter
@Setter
public class FirebaseConfigProps {

    private String keyPath;
}
