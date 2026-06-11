package com.kernotec.driverschedule.common.properties;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kernotec")
@Getter
@Setter
public class KernotecApiProperties {

    private OpenApiInfo info;
    private List<OpenApiServer> servers;

    @Getter
    @Setter
    public static class OpenApiInfo {

        private String title;
        private String description;
        private String version;
    }

    @Getter
    @Setter
    public static class OpenApiServer {

        private String url;
        private String description;
    }
}

