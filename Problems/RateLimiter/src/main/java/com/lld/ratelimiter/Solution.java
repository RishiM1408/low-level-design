package com.lld.ratelimiter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// --- Token Bucket Implementation ---
class TokenBucket {
    private final long capacity;
    private final double refillRate; // Tokens per second

    private double tokens;
    private long lastRefillTimestamp;

    public TokenBucket(long capacity, double refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = capacity;
        this.lastRefillTimestamp = System.currentTimeMillis();
    }

    // SDE-3 Focus: Synchronized Refill + Consume
    // Lazy Refill: We only update tokens when a request arrives.
    public synchronized boolean tryConsume() {
        refill();

        if (tokens >= 1) {
            tokens -= 1;
            return true;
        }
        return false;
    }

    private void refill() {
        long now = System.currentTimeMillis();
        long durationMs = now - lastRefillTimestamp;
        double tokensToAdd = (durationMs / 1000.0) * refillRate;

        if (tokensToAdd > 0) {
            tokens = Math.min(capacity, tokens + tokensToAdd);
            lastRefillTimestamp = now;
        }
    }
}

// --- Metrics Instrumentation (SDE-3 Enhancement) ---
class RateLimiterMetrics {
    private final java.util.concurrent.atomic.AtomicLong totalRequests = new java.util.concurrent.atomic.AtomicLong(0);
    private final java.util.concurrent.atomic.AtomicLong droppedRequests = new java.util.concurrent.atomic.AtomicLong(
            0);

    public void incrementTotal() {
        totalRequests.incrementAndGet();
    }

    public void incrementDropped() {
        droppedRequests.incrementAndGet();
    }

    public void printStats() {
        System.out.println("[Metrics] Total: " + totalRequests.get() + ", Dropped: " + droppedRequests.get());
    }
}

// --- Service ---
class RateLimiterService {
    private final Map<String, TokenBucket> userBuckets;
    private final int capacity;
    private final double refillRate;
    private final RateLimiterMetrics metrics;

    public RateLimiterService(int capacity, double refillRate) {
        this.userBuckets = new ConcurrentHashMap<>();
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.metrics = new RateLimiterMetrics();
    }

    public boolean allowRequest(String userId) {
        metrics.incrementTotal();
        userBuckets.putIfAbsent(userId, new TokenBucket(capacity, refillRate));
        boolean allowed = userBuckets.get(userId).tryConsume();

        if (!allowed) {
            metrics.incrementDropped();
        }
        return allowed;
    }

    public void showMetrics() {
        metrics.printStats();
    }
}

// --- Demo ---
public class Solution {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- Rate Limiter Demo (Token Bucket) ---");

        // Cap: 3, Rate: 1 token/sec
        RateLimiterService limiter = new RateLimiterService(3, 1.0);
        String user = "User1";

        // Burst 1: Consume 3 (Allowed)
        System.out.println("Req 1: " + limiter.allowRequest(user)); // true
        System.out.println("Req 2: " + limiter.allowRequest(user)); // true
        System.out.println("Req 3: " + limiter.allowRequest(user)); // true

        // Burst 2: Fail (Empty)
        System.out.println("Req 4 (Should Fail): " + limiter.allowRequest(user)); // false

        // Wait 1.1s -> Refill 1 token
        System.out.println("Waiting 1.1s...");
        Thread.sleep(1100);

        // Retry
        System.out.println("Req 5 (Should Pass): " + limiter.allowRequest(user)); // true
    }
}
