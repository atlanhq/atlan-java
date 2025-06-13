/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.net;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;

/**
 * Cross-thread, global rate limiting mechanism -- so if at any point the SDK receives
 * a 429 response that it is being rate-limited (throttled) then we can centrally pause
 * and coordinate retries across all threads, rather than allowing individual threads to
 * further overwhelm the throttling via their own independent requests.
 */
@Slf4j
public class GlobalRateLimiter {
    private static final GlobalRateLimiter INSTANCE = new GlobalRateLimiter();

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition rateAvailable = lock.newCondition();
    private final AtomicBoolean isRateLimited = new AtomicBoolean(false);
    private long resumeTimeMillis = 0;

    private GlobalRateLimiter() {}

    public static GlobalRateLimiter getInstance() {
        return INSTANCE;
    }

    /**
     * Check if we should wait due to a rate limit, and if so actually do that wait.
     *
     * @throws InterruptedException on any interruption
     */
    public void waitIfRateLimited() throws InterruptedException {
        if (!isRateLimited.get()) {
            return; // Fast path if not rate limited
        }

        lock.lock();
        try {
            // Check again after acquiring lock
            while (isRateLimited.get()) {
                long waitTime = resumeTimeMillis - System.currentTimeMillis();
                if (waitTime <= 0) {
                    // Time has passed, we can resume
                    isRateLimited.set(false);
                    rateAvailable.signalAll(); // Wake up all waiting threads
                    break;
                }
                // Wait until signaled or timeout
                log.debug(" ... pausing for {}ms before retrying, given the rate-limit", waitTime);
                rateAvailable.await(waitTime, TimeUnit.MILLISECONDS);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Record that a rate limit is effective.
     *
     * @param retryAfterMillis amount of time to wait (in milliseconds) before resuming
     */
    public void setRateLimit(long retryAfterMillis) {
        lock.lock();
        try {
            isRateLimited.set(true);
            resumeTimeMillis = System.currentTimeMillis() + retryAfterMillis;
            // No need to signal as all threads will check the resumeTimeMillis
        } finally {
            lock.unlock();
        }
    }
}
