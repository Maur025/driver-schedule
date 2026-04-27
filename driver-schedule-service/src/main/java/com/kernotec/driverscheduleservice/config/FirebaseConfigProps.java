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

    private String type;
    private String projectId;
    private String privateKeyId;
    private String privateKey;
    private String client_email;
    private String clientId;
    private String authUri;
    private String tokenUri;
    private String authProviderX509CertUrl;
    private String clientX509CertUrl;
    private String universeDomain;
}
