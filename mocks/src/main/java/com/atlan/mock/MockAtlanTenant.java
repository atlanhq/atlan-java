/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.mock;

import com.atlan.AtlanClient;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockAtlanTenant {

    public static volatile AtlanClient client = null;
    private static final AtomicBoolean initialized = new AtomicBoolean(false);
    private static final AtomicBoolean initializing = new AtomicBoolean(false);
    private static final CountDownLatch latch = new CountDownLatch(1);

    /**
     * Allow multiple threads to attempt to initialize the client, but have them wait until
     * whichever wins the race completes, then all others short-circuit to reuse the same client.
     * @throws InterruptedException on any interruption
     */
    public static void initializeClient() throws InterruptedException {
        if (initialized.get()) {
            return;
        }

        if (initializing.compareAndSet(false, true)) {
            try {
                client = new AtlanClient("http://localhost:8765", "unused");
                initialized.set(true);
            } finally {
                // Signal waiting threads
                latch.countDown();
            }
        } else {
            // Wait for initialization to complete
            latch.await();
        }
    }

    public static void closeClient() {
        try {
            AtlanClient c = client;
            if (c != null) {
                c.close();
            }
            client = null;
            initialized.set(false);
            initializing.set(false);
        } catch (Exception e) {
            log.error("Unable to close client used for unit tests.", e);
        }
    }
}
