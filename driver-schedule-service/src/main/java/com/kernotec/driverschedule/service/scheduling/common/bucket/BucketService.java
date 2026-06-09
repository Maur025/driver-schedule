package com.kernotec.driverschedule.service.scheduling.common.bucket;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bucket;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

@Service
public class BucketService {

    private final Bucket4jProperties bucket4jProperties;
    private final Cache<String, Bucket> bucketCache;

    public BucketService(Bucket4jProperties bucket4jProperties) {
        this.bucket4jProperties = bucket4jProperties;
        this.bucketCache = Caffeine.newBuilder()
            .maximumSize(bucket4jProperties.getBucketCacheMaxSize())
            .expireAfterAccess(bucket4jProperties.getBucketCacheExpireInMinutes(), TimeUnit.MINUTES)
            .build();
    }

    public Bucket getBucket(String key) {
        return bucketCache.get(key, this::newBucket);
    }

    private Bucket newBucket(String key) {
        return Bucket.builder()
            .addLimit(limit -> limit.capacity(bucket4jProperties.getCapacity())
                .refillGreedy(
                    bucket4jProperties.getRefillTokens(),
                    Duration.ofSeconds(bucket4jProperties.getRefillPeriodInSeconds())
                ))
            .build();
    }
}
