/* SPDX-License-Identifier: Apache-2.0
   Copyright 2026 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

import mu.KotlinLogging
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.IntStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * `ParallelBatch` keeps one `AssetBatch` per thread, so the related-asset pass must reuse a bounded
 * set of threads. Parallelism 1 is the case `CompletableFuture.runAsync` without an executor turns
 * into one new thread per item (CONNECT-3976).
 */
class ForEachParallelTest {
    private val logger = KotlinLogging.logger {}

    private fun threadsUsed(parallelism: Int): Set<Long> {
        val threads = ConcurrentHashMap.newKeySet<Long>()
        val processed = ConcurrentHashMap.newKeySet<Int>()
        IntStream.range(0, 200).boxed().forEachParallel(logger, parallelism) {
            threads.add(Thread.currentThread().id)
            processed.add(it)
        }
        assertEquals(200, processed.size)
        return threads
    }

    @Test
    fun singleThreadWhenParallelismIsOne() {
        assertEquals(1, threadsUsed(1).size)
    }

    @Test
    fun neverMoreThreadsThanParallelism() {
        assertTrue(threadsUsed(4).size <= 4)
    }
}
