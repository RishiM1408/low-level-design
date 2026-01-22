package com.lld.ratelimiter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RateLimiterTest {

    @Test
    public void testTokenBucketConsumption() throws InterruptedException {
        // Assume TokenBucket constructed with Capacity 10, Refill Rate 1/sec
        RateLimiter limiter = new TokenBucketRateLimiter(10, 1);

        // Consume 5
        assertTrue(limiter.allowRequest("user1"), "Request 1 should be allowed");
        assertTrue(limiter.allowRequest("user1"), "Request 2 should be allowed");

        // Simulate depletion (mocking or just logic flow)
        // For real SDE-3 code, we'd mock the TimeProvider.
    }
}
