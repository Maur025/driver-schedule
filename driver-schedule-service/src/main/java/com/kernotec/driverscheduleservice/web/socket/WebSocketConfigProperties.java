package com.kernotec.driverscheduleservice.web.socket;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "web-socket-config")
@Getter
@Setter
public class WebSocketConfigProperties {

    private String brokerPrefix;
    private String appPrefix;
    private String privateMessagePrefix;
    private String endpoint;
    private String userDestinationPrefix;
}
