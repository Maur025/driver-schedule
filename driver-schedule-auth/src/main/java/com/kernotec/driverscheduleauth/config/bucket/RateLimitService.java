package com.kernotec.driverscheduleauth.config.bucket;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bucket;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

@Service
public class RateLimitService {

    private final RateLimitBucketConfigProperties rateLimitBucketConfigProperties;

    private final Cache<String, Bucket> bucketCache;

    public RateLimitService(RateLimitBucketConfigProperties rateLimitBucketConfigProperties) {
        this.rateLimitBucketConfigProperties = rateLimitBucketConfigProperties;
        this.bucketCache = Caffeine.newBuilder()
            .maximumSize(rateLimitBucketConfigProperties.getBucketCacheMaxSize())
            .expireAfterAccess(
                rateLimitBucketConfigProperties.getBucketCacheExpireInMinutes(), TimeUnit.MINUTES)
            .build();
    }

    public Bucket resolveBucket(String key) {
        return bucketCache.get(key, this::newBucket);
    }

    private Bucket newBucket(String key) {
        return Bucket.builder()
            .addLimit(limit -> limit.capacity(rateLimitBucketConfigProperties.getCapacity())
                .refillGreedy(
                    rateLimitBucketConfigProperties.getRefillTokens(),
                    Duration.ofSeconds(rateLimitBucketConfigProperties.getRefillPeriodInSeconds())
                ))
            .build();
    }
}
