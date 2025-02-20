/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.util;

import com.atlan.AtlanClient;
import com.atlan.cache.OffHeapAssetCache;
import com.atlan.cache.OffHeapFailureCache;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Asset;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.core.AtlanCloseable;
import com.atlan.model.enums.AssetCreationHandling;
import com.atlan.model.enums.AtlanTagHandling;
import com.atlan.model.enums.CustomMetadataHandling;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Utility class for managing bulk updates across multiple parallel-running batches.
 */
public class ParallelBatch implements AtlanCloseable {

    protected final ReadWriteLock lock = new ReentrantReadWriteLock();

    /** Connectivity to an Atlan tenant. */
    private final AtlanClient client;

    /** Maximum number of assets to submit in each batch. */
    private final int maxSize;

    /** How to handle Atlan tags (ignore them, append them (leaving any pre-existing), replace them (wiping out any pre-existing), or remove them) */
    private final AtlanTagHandling atlanTagHandling;

    /** How to handle any custom metadata on assets (ignore, replace, or merge). */
    private final CustomMetadataHandling customMetadataHandling;

    /** Whether to capture details about any failures (true) or throw exceptions for any failures (false). */
    private final boolean captureFailures;

    /** Whether to track the basic information about every asset that is created or updated (true) or only track counts (false). */
    private final boolean track;

    /** Whether to allow assets to be created (false) or only allow existing assets to be updated (true). */
    private final boolean updateOnly;

    /** When running with {@link #updateOnly} as true, whether to consider only exact matches (false) or ignore case (true). */
    private final boolean caseSensitive;

    /** When allowing assets to be created, how to handle those creations (full assets or partial assets). */
    private final AssetCreationHandling creationHandling;

    /** Whether tables and views should be treated interchangeably (an asset in the batch marked as a table will attempt to match a view if not found as a table, and vice versa). */
    private final boolean tableViewAgnostic;

    private final ConcurrentHashMap<Long, AssetBatch> batchMap = new ConcurrentHashMap<>();
    private final Map<String, String> resolvedGuids = new ConcurrentHashMap<>();
    private final Map<AssetBatch.AssetIdentity, String> resolvedQualifiedNames = new ConcurrentHashMap<>();

    private OffHeapAssetCache created = null;
    private OffHeapAssetCache updated = null;
    private OffHeapAssetCache restored = null;
    private OffHeapAssetCache skipped = null;
    private OffHeapFailureCache failures = null;

    /**
     * Create a new batch of assets to be bulk-saved, in parallel (across threads).
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     */
    public ParallelBatch(AtlanClient client, int maxSize) {
        this(client, maxSize, AtlanTagHandling.IGNORE, CustomMetadataHandling.IGNORE);
    }

    /**
     * Create a new batch of assets to be bulk-saved, in parallel (across threads).
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param atlanTagHandling how to handle Atlan tags (ignore them, append them (leaving any pre-existing), replace them (wiping out any pre-existing), or remove them)
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     */
    public ParallelBatch(
            AtlanClient client,
            int maxSize,
            AtlanTagHandling atlanTagHandling,
            CustomMetadataHandling customMetadataHandling) {
        this(client, maxSize, atlanTagHandling, customMetadataHandling, false);
    }

    /**
     * Create a new batch of assets to be bulk-saved, in parallel (across threads).
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param atlanTagHandling how to handle Atlan tags (ignore them, append them (leaving any pre-existing), replace them (wiping out any pre-existing), or remove them)
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     * @param captureFailures when true, any failed batches will be captured and retained rather than exceptions being raised (for large amounts of processing this could cause memory issues!)
     */
    public ParallelBatch(
            AtlanClient client,
            int maxSize,
            AtlanTagHandling atlanTagHandling,
            CustomMetadataHandling customMetadataHandling,
            boolean captureFailures) {
        this(client, maxSize, atlanTagHandling, customMetadataHandling, captureFailures, false);
    }

    /**
     * Create a new batch of assets to be bulk-saved, in parallel (across threads).
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param atlanTagHandling how to handle Atlan tags (ignore them, append them (leaving any pre-existing), replace them (wiping out any pre-existing), or remove them)
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     * @param captureFailures when true, any failed batches will be captured and retained rather than exceptions being raised (for large amounts of processing this could cause memory issues!)
     * @param updateOnly when true, only attempt to update existing assets and do not create any assets (note: this will incur a performance penalty)
     */
    public ParallelBatch(
            AtlanClient client,
            int maxSize,
            AtlanTagHandling atlanTagHandling,
            CustomMetadataHandling customMetadataHandling,
            boolean captureFailures,
            boolean updateOnly) {
        this(client, maxSize, atlanTagHandling, customMetadataHandling, captureFailures, updateOnly, true);
    }

    /**
     * Create a new batch of assets to be bulk-saved, in parallel (across threads).
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param atlanTagHandling how to handle Atlan tags (ignore them, append them (leaving any pre-existing), replace them (wiping out any pre-existing), or remove them)
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     * @param captureFailures when true, any failed batches will be captured and retained rather than exceptions being raised (for large amounts of processing this could cause memory issues!)
     * @param updateOnly when true, only attempt to update existing assets and do not create any assets (note: this will incur a performance penalty)
     * @param track when false, details about each created and updated asset will no longer be tracked (only an overall count of each) -- useful if you intend to send close to (or more than) 1 million assets through a batch
     */
    public ParallelBatch(
            AtlanClient client,
            int maxSize,
            AtlanTagHandling atlanTagHandling,
            CustomMetadataHandling customMetadataHandling,
            boolean captureFailures,
            boolean updateOnly,
            boolean track) {
        this(client, maxSize, atlanTagHandling, customMetadataHandling, captureFailures, updateOnly, track, true);
    }

    /**
     * Create a new batch of assets to be bulk-saved, in parallel (across threads).
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param atlanTagHandling how to handle Atlan tags (ignore them, append them (leaving any pre-existing), replace them (wiping out any pre-existing), or remove them)
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     * @param captureFailures when true, any failed batches will be captured and retained rather than exceptions being raised (for large amounts of processing this could cause memory issues!)
     * @param updateOnly when true, only attempt to update existing assets and do not create any assets (note: this will incur a performance penalty)
     * @param track when false, details about each created and updated asset will no longer be tracked (only an overall count of each) -- useful if you intend to send close to (or more than) 1 million assets through a batch
     * @param caseSensitive (only applies when updateOnly is true) attempt to match assets case-sensitively (true) or case-insensitively (false)
     */
    public ParallelBatch(
            AtlanClient client,
            int maxSize,
            AtlanTagHandling atlanTagHandling,
            CustomMetadataHandling customMetadataHandling,
            boolean captureFailures,
            boolean updateOnly,
            boolean track,
            boolean caseSensitive) {
        this(
                client,
                maxSize,
                atlanTagHandling,
                customMetadataHandling,
                captureFailures,
                updateOnly,
                track,
                caseSensitive,
                AssetCreationHandling.FULL);
    }

    /**
     * Create a new batch of assets to be bulk-saved, in parallel (across threads).
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param atlanTagHandling how to handle Atlan tags (ignore them, append them (leaving any pre-existing), replace them (wiping out any pre-existing), or remove them)
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     * @param captureFailures when true, any failed batches will be captured and retained rather than exceptions being raised (for large amounts of processing this could cause memory issues!)
     * @param updateOnly when true, only attempt to update existing assets and do not create any assets (note: this will incur a performance penalty)
     * @param track when false, details about each created and updated asset will no longer be tracked (only an overall count of each) -- useful if you intend to send close to (or more than) 1 million assets through a batch
     * @param caseSensitive (only applies when updateOnly is true) attempt to match assets case-sensitively (true) or case-insensitively (false)
     * @param creationHandling if assets are to be created, how they should be created (as full assets or only partial assets)
     */
    public ParallelBatch(
            AtlanClient client,
            int maxSize,
            AtlanTagHandling atlanTagHandling,
            CustomMetadataHandling customMetadataHandling,
            boolean captureFailures,
            boolean updateOnly,
            boolean track,
            boolean caseSensitive,
            AssetCreationHandling creationHandling) {
        this(
                client,
                maxSize,
                atlanTagHandling,
                customMetadataHandling,
                captureFailures,
                updateOnly,
                track,
                caseSensitive,
                creationHandling,
                false);
    }

    /**
     * Create a new batch of assets to be bulk-saved, in parallel (across threads).
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param atlanTagHandling how to handle Atlan tags (ignore them, append them (leaving any pre-existing), replace them (wiping out any pre-existing), or remove them)
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     * @param captureFailures when true, any failed batches will be captured and retained rather than exceptions being raised (for large amounts of processing this could cause memory issues!)
     * @param updateOnly when true, only attempt to update existing assets and do not create any assets (note: this will incur a performance penalty)
     * @param track when false, details about each created and updated asset will no longer be tracked (only an overall count of each) -- useful if you intend to send close to (or more than) 1 million assets through a batch
     * @param caseSensitive (only applies when updateOnly is true) attempt to match assets case-sensitively (true) or case-insensitively (false)
     * @param creationHandling if assets are to be created, how they should be created (as full assets or only partial assets)
     * @param tableViewAgnostic if true, tables and views will be treated interchangeably (an asset in the batch marked as a table will attempt to match a view if not found as a table, and vice versa)
     */
    public ParallelBatch(
            AtlanClient client,
            int maxSize,
            AtlanTagHandling atlanTagHandling,
            CustomMetadataHandling customMetadataHandling,
            boolean captureFailures,
            boolean updateOnly,
            boolean track,
            boolean caseSensitive,
            AssetCreationHandling creationHandling,
            boolean tableViewAgnostic) {
        this.client = client;
        this.maxSize = maxSize;
        this.atlanTagHandling = atlanTagHandling;
        this.customMetadataHandling = customMetadataHandling;
        this.creationHandling = creationHandling;
        this.track = track;
        this.captureFailures = captureFailures;
        this.updateOnly = updateOnly;
        this.caseSensitive = caseSensitive;
        this.tableViewAgnostic = tableViewAgnostic;
    }

    /**
     * Add an asset to the batch to be processed.
     *
     * @param single the asset to add to a batch
     * @return the assets that were created or updated in this batch, or null if the batch is still queued
     * @throws AtlanException on any problems adding the asset to or processing the batch
     */
    public AssetMutationResponse add(Asset single) throws AtlanException {
        long id = Thread.currentThread().getId();
        // Note: these are thread-specific operations, so not explicitly locked or synchronized
        AssetBatch batch = batchMap.computeIfAbsent(
                id,
                k -> new AssetBatch(
                        client,
                        maxSize,
                        atlanTagHandling,
                        customMetadataHandling,
                        captureFailures,
                        updateOnly,
                        track,
                        !caseSensitive,
                        creationHandling,
                        tableViewAgnostic));
        return batch.add(single);
    }

    /**
     * Flush any remaining assets in the parallel batches.
     *
     * @throws IllegalStateException on any problems flushing (submitting) any of the parallel batches
     */
    public void flush() throws AtlanException {
        lock.writeLock().lock();
        try {
            batchMap.values().forEach(batch -> {
                try {
                    batch.flush();
                } catch (AtlanException e) {
                    throw new IllegalStateException(e);
                }
            });
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Number of assets that were created (no details, only a count).
     *
     * @return a count of the number of created assets, across all parallel batches
     */
    public long getNumCreated() {
        lock.readLock().lock();
        try {
            long count = 0;
            for (AssetBatch batch : batchMap.values()) {
                count += batch.getNumCreated().get();
            }
            return count;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Number of assets that were updated (no details, only a count).
     *
     * @return a count of the number of updated assets, across all parallel batches
     */
    public long getNumUpdated() {
        lock.readLock().lock();
        try {
            long count = 0;
            for (AssetBatch batch : batchMap.values()) {
                count += batch.getNumUpdated().get();
            }
            return count;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Number of assets that were potentially restored from being archived, or otherwise touched
     * without actually being updated (no details, just a count).
     *
     * @return a count of the number of potentially restored assets, across all parallel batches
     */
    public long getNumRestored() {
        lock.readLock().lock();
        try {
            long count = 0;
            for (AssetBatch batch : batchMap.values()) {
                count += batch.getNumRestored().get();
            }
            return count;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Number of assets that were skipped during processing (no details, just a count).
     *
     * @return a count of the number of skipped assets, across all parallel batches
     */
    public long getNumSkipped() {
        lock.readLock().lock();
        try {
            long count = 0;
            for (AssetBatch batch : batchMap.values()) {
                count += batch.getNumSkipped().get();
            }
            return count;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Assets that were created (minimal info only).
     *
     * @return all created assets, across all parallel batches
     */
    public OffHeapAssetCache getCreated() {
        if (!track) return null;
        if (created == null) {
            lock.writeLock().lock();
            try {
                created = new OffHeapAssetCache(client, "p-created");
                for (AssetBatch batch : batchMap.values()) {
                    if (batch.getCreated().isNotClosed()) {
                        created.extendedWith(batch.getCreated(), true);
                    }
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
        lock.readLock().lock();
        try {
            return created;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Assets that were updated (minimal info only).
     *
     * @return all updated assets, across all parallel batches
     */
    public OffHeapAssetCache getUpdated() {
        if (!track) return null;
        if (updated == null) {
            lock.writeLock().lock();
            try {
                updated = new OffHeapAssetCache(client, "p-updated");
                for (AssetBatch batch : batchMap.values()) {
                    if (batch.getUpdated().isNotClosed()) {
                        updated.extendedWith(batch.getUpdated(), true);
                    }
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
        lock.readLock().lock();
        try {
            return updated;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Assets that were potentially restored from being archived, or otherwise touched without actually
     * being updated (minimal info only).
     *
     * @return all potentially restored assets, across all parallel batches
     */
    public OffHeapAssetCache getRestored() {
        if (!track) return null;
        if (restored == null) {
            lock.writeLock().lock();
            try {
                restored = new OffHeapAssetCache(client, "p-restored");
                for (AssetBatch batch : batchMap.values()) {
                    if (batch.getRestored().isNotClosed()) {
                        restored.extendedWith(batch.getRestored(), true);
                    }
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
        lock.readLock().lock();
        try {
            return restored;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Batches that failed to be committed (only populated when captureFailures is set to true).
     *
     * @return all batches that failed, across all parallel batches
     */
    public OffHeapFailureCache getFailures() {
        if (!track) return null;
        if (failures == null) {
            lock.writeLock().lock();
            try {
                failures = new OffHeapFailureCache(client, "p-failed");
                for (AssetBatch batch : batchMap.values()) {
                    if (batch.getFailures().isNotClosed()) {
                        failures.extendedWith(batch.getFailures(), true);
                    }
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
        lock.readLock().lock();
        try {
            return failures;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Assets that were skipped, when updateOnly is requested and the asset does not exist in Atlan.
     *
     * @return all assets that were skipped, across all parallel batches
     */
    public OffHeapAssetCache getSkipped() {
        if (!track) return null;
        if (skipped == null) {
            lock.writeLock().lock();
            try {
                skipped = new OffHeapAssetCache(client, "p-skipped");
                for (AssetBatch batch : batchMap.values()) {
                    if (batch.getSkipped().isNotClosed()) {
                        skipped.extendedWith(batch.getSkipped(), true);
                    }
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
        lock.readLock().lock();
        try {
            return skipped;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Map from placeholder GUID to resolved (actual) GUID, for all assets that were processed through the batch.
     *
     * @return all resolved GUIDs, across all parallel batches
     */
    public Map<String, String> getResolvedGuids() {
        boolean empty;
        lock.readLock().lock();
        try {
            empty = resolvedGuids.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
        if (empty) {
            lock.writeLock().lock();
            try {
                for (AssetBatch batch : batchMap.values()) {
                    resolvedGuids.putAll(batch.getResolvedGuids());
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
        lock.readLock().lock();
        try {
            return resolvedGuids;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Map from case-insensitive qualifiedName to resolved (actual) qualifiedName,
     * for all assets that were processed through the batch.
     * Note: this is only populated when caseSensitive is false, and will otherwise be empty
     *
     * @return all resolved qualifiedNames, across all parallel batches
     */
    public Map<AssetBatch.AssetIdentity, String> getResolvedQualifiedNames() {
        boolean empty;
        lock.readLock().lock();
        try {
            empty = resolvedQualifiedNames.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
        if (empty) {
            lock.writeLock().lock();
            try {
                for (AssetBatch batch : batchMap.values()) {
                    resolvedQualifiedNames.putAll(batch.getResolvedQualifiedNames());
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
        lock.readLock().lock();
        try {
            return resolvedQualifiedNames;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Close the batch by freeing up any resources it has used.
     * Note: this will clear any internal caches of results, so only call this after you have processed those!
     */
    @Override
    public void close() {
        lock.writeLock().lock();
        try {
            for (AssetBatch batch : batchMap.values()) {
                AtlanCloseable.close(batch);
            }
        } finally {
            lock.writeLock().unlock();
        }
        AtlanCloseable.close(created);
        AtlanCloseable.close(updated);
        AtlanCloseable.close(restored);
        AtlanCloseable.close(skipped);
        AtlanCloseable.close(failures);
    }
}
