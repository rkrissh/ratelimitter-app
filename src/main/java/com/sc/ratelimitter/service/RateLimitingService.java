package com.sc.ratelimitter.service;

import com.sc.ratelimitter.repository.UserPlanRepository;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class RateLimitingService {

    private final Map<UUID, Bucket> bucketCache = new ConcurrentHashMap<UUID, Bucket>();
    private final UserPlanRepository userPlanRepository;

    public Bucket resolveBucket(final UUID userId) {
        return bucketCache.computeIfAbsent(userId, this::newBucket);
    }

    public void deleteIfExists(final UUID userId) {
        bucketCache.remove(userId);
    }

    private Bucket newBucket(UUID userId) {
        final var plan = userPlanRepository.findByUserIdAndIsActive(userId, true).get().getPlan();
        final Integer limitPerHour = plan.getLimitPerHour();
        return Bucket.builder()
                .addLimit(Bandwidth.classic(limitPerHour, Refill.intervally(limitPerHour, Duration.ofHours(1))))
                .build();
    }
}