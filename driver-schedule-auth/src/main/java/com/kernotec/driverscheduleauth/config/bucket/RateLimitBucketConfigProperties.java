package com.kernotec.driverscheduleauth.config.bucket;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rate-limit-bucket-config")
@Getter
@Setter
public class RateLimitBucketConfigProperties {

    private Long capacity;
    private Long refillTokens;
    private Long refillPeriodInSeconds;
    private Long bucketCacheExpireInMinutes;
    private Long bucketCacheMaxSize;
}
