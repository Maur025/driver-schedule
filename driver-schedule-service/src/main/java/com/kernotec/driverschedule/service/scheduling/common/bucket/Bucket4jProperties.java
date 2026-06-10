package com.kernotec.driverschedule.service.scheduling.common.bucket;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "bucket-4j-config")
@Getter
@Setter
public class Bucket4jProperties {

    private Long capacity;
    private Long refillTokens;
    private Long refillPeriodInSeconds;
    private Long bucketCacheExpireInMinutes;
    private Long bucketCacheMaxSize;
}
