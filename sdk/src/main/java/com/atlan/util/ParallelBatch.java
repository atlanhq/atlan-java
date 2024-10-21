/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.util;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Asset;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.AssetCreationHandling;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import lombok.Builder;

/**
 * Utility class for managing bulk updates across multiple parallel-running batches.
 */
@Builder
public class ParallelBatch {

    protected final ReadWriteLock lock = new ReentrantReadWriteLock();

    /** Connectivity to an Atlan tenant. */
    private AtlanClient client;

    /** Maximum number of assets to submit in each batch. */
    @Builder.Default
    private int maxSize = 20;

    /** Whether to replace Atlan tags (true), or ignore them (false). */
    @Builder.Default
    private boolean replaceAtlanTags = false;

    /** How to handle any custom metadata on assets (ignore, replace, or merge). */
    @Builder.Default
    private AssetBatch.CustomMetadataHandling customMetadataHandling = AssetBatch.CustomMetadataHandling.IGNORE;

    /** Whether to capture details about any failures (true) or throw exceptions for any failures (false). */
    @Builder.Default
    private boolean captureFailures = false;

    /** Whether to track the basic information about every asset that is created or updated (true) or only track counts (false). */
    @Builder.Default
    private boolean track = true;

    /** Whether to allow assets to be created (false) or only allow existing assets to be updated (true). */
    @Builder.Default
    private boolean updateOnly = false;

    /** When running with {@link #updateOnly} as true, whether to consider only exact matches (false) or ignore case (true). */
    @Builder.Default
    private boolean caseSensitive = true;

    /** When allowing assets to be created, how to handle those creations (full assets or partial assets). */
    @Builder.Default
    private AssetCreationHandling creationHandling = AssetCreationHandling.FULL;

    /** Whether tables and views should be treated interchangeably (an asset in the batch marked as a table will attempt to match a view if not found as a table, and vice versa). */
    @Builder.Default
    private boolean tableViewAgnostic = false;

    private final Map<Long, AssetBatch> batchMap = new ConcurrentHashMap<>();
    private final List<Asset> created = new ArrayList<>();
    private final List<Asset> updated = new ArrayList<>();
    private final List<Asset> restored = new ArrayList<>();
    private final List<AssetBatch.FailedBatch> failures = new ArrayList<>();
    private final List<Asset> skipped = new ArrayList<>();
    private final Map<String, String> resolvedGuids = new HashMap<>();
    private final Map<AssetBatch.AssetIdentity, String> resolvedQualifiedNames = new HashMap<>();

    /**
     * Create a new batch of assets to be bulk-saved, in parallel (across threads).
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     */
    public ParallelBatch(AtlanClient client, int maxSize) {
        this(client, maxSize, false, AssetBatch.CustomMetadataHandling.IGNORE);
    }

    /**
     * Create a new batch of assets to be bulk-saved, in parallel (across threads).
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param replaceAtlanTags if true, all Atlan tags on an existing asset will be overwritten; if false, all Atlan tags will be ignored
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     */
    public ParallelBatch(
            AtlanClient client,
            int maxSize,
            boolean replaceAtlanTags,
            AssetBatch.CustomMetadataHandling customMetadataHandling) {
        this(client, maxSize, replaceAtlanTags, customMetadataHandling, false);
    }

    /**
     * Create a new batch of assets to be bulk-saved, in parallel (across threads).
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param replaceAtlanTags if true, all Atlan tags on an existing asset will be overwritten; if false, all Atlan tags will be ignored
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     * @param captureFailures when true, any failed batches will be captured and retained rather than exceptions being raised (for large amounts of processing this could cause memory issues!)
     */
    public ParallelBatch(
            AtlanClient client,
            int maxSize,
            boolean replaceAtlanTags,
            AssetBatch.CustomMetadataHandling customMetadataHandling,
            boolean captureFailures) {
        this(client, maxSize, replaceAtlanTags, customMetadataHandling, captureFailures, false);
    }

    /**
     * Create a new batch of assets to be bulk-saved, in parallel (across threads).
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param replaceAtlanTags if true, all Atlan tags on an existing asset will be overwritten; if false, all Atlan tags will be ignored
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     * @param captureFailures when true, any failed batches will be captured and retained rather than exceptions being raised (for large amounts of processing this could cause memory issues!)
     * @param updateOnly when true, only attempt to update existing assets and do not create any assets (note: this will incur a performance penalty)
     */
    public ParallelBatch(
            AtlanClient client,
            int maxSize,
            boolean replaceAtlanTags,
            AssetBatch.CustomMetadataHandling customMetadataHandling,
            boolean captureFailures,
            boolean updateOnly) {
        this(client, maxSize, replaceAtlanTags, customMetadataHandling, captureFailures, updateOnly, true);
    }

    /**
     * Create a new batch of assets to be bulk-saved, in parallel (across threads).
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param replaceAtlanTags if true, all Atlan tags on an existing asset will be overwritten; if false, all Atlan tags will be ignored
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     * @param captureFailures when true, any failed batches will be captured and retained rather than exceptions being raised (for large amounts of processing this could cause memory issues!)
     * @param updateOnly when true, only attempt to update existing assets and do not create any assets (note: this will incur a performance penalty)
     * @param track when false, details about each created and updated asset will no longer be tracked (only an overall count of each) -- useful if you intend to send close to (or more than) 1 million assets through a batch
     */
    public ParallelBatch(
            AtlanClient client,
            int maxSize,
            boolean replaceAtlanTags,
            AssetBatch.CustomMetadataHandling customMetadataHandling,
            boolean captureFailures,
            boolean updateOnly,
            boolean track) {
        this(client, maxSize, replaceAtlanTags, customMetadataHandling, captureFailures, updateOnly, track, true);
    }

    /**
     * Create a new batch of assets to be bulk-saved, in parallel (across threads).
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param replaceAtlanTags if true, all Atlan tags on an existing asset will be overwritten; if false, all Atlan tags will be ignored
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     * @param captureFailures when true, any failed batches will be captured and retained rather than exceptions being raised (for large amounts of processing this could cause memory issues!)
     * @param updateOnly when true, only attempt to update existing assets and do not create any assets (note: this will incur a performance penalty)
     * @param track when false, details about each created and updated asset will no longer be tracked (only an overall count of each) -- useful if you intend to send close to (or more than) 1 million assets through a batch
     * @param caseSensitive (only applies when updateOnly is true) attempt to match assets case-sensitively (true) or case-insensitively (false)
     */
    public ParallelBatch(
            AtlanClient client,
            int maxSize,
            boolean replaceAtlanTags,
            AssetBatch.CustomMetadataHandling customMetadataHandling,
            boolean captureFailures,
            boolean updateOnly,
            boolean track,
            boolean caseSensitive) {
        this(
                client,
                maxSize,
                replaceAtlanTags,
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
     * @param replaceAtlanTags if true, all Atlan tags on an existing asset will be overwritten; if false, all Atlan tags will be ignored
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
            boolean replaceAtlanTags,
            AssetBatch.CustomMetadataHandling customMetadataHandling,
            boolean captureFailures,
            boolean updateOnly,
            boolean track,
            boolean caseSensitive,
            AssetCreationHandling creationHandling) {
        this(
                client,
                maxSize,
                replaceAtlanTags,
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
     * @param replaceAtlanTags if true, all Atlan tags on an existing asset will be overwritten; if false, all Atlan tags will be ignored
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
            boolean replaceAtlanTags,
            AssetBatch.CustomMetadataHandling customMetadataHandling,
            boolean captureFailures,
            boolean updateOnly,
            boolean track,
            boolean caseSensitive,
            AssetCreationHandling creationHandling,
            boolean tableViewAgnostic) {
        this.client = client;
        this.maxSize = maxSize;
        this.replaceAtlanTags = replaceAtlanTags;
        this.customMetadataHandling = customMetadataHandling;
        this.captureFailures = captureFailures;
        this.track = track;
        this.updateOnly = updateOnly;
        this.caseSensitive = caseSensitive;
        this.creationHandling = creationHandling;
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
        lock.writeLock().lock();
        try {
            long id = Thread.currentThread().getId();
            if (!batchMap.containsKey(id)) {
                batchMap.put(
                        id,
                        new AssetBatch(
                                client,
                                maxSize,
                                replaceAtlanTags,
                                customMetadataHandling,
                                captureFailures,
                                updateOnly,
                                track,
                                !caseSensitive,
                                creationHandling,
                                tableViewAgnostic));
            }
            return batchMap.get(id).add(single);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Flush any remaining assets in the parallel batches.
     *
     * @throws IllegalStateException on any problems flushing (submitting) any of the parallel batches
     */
    public void flush() throws AtlanException {
        lock.writeLock().lock();
        try {
            batchMap.values().parallelStream().forEach(batch -> {
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
     * Assets that were created (minimal info only).
     *
     * @return all created assets, across all parallel batches
     */
    public List<Asset> getCreated() {
        if (!track) {
            return null;
        }
        boolean empty;
        lock.readLock().lock();
        try {
            empty = created.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
        if (empty) {
            lock.writeLock().lock();
            try {
                for (AssetBatch batch : batchMap.values()) {
                    created.addAll(batch.getCreated());
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
    public List<Asset> getUpdated() {
        if (!track) {
            return null;
        }
        boolean empty;
        lock.readLock().lock();
        try {
            empty = updated.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
        if (empty) {
            lock.writeLock().lock();
            try {
                for (AssetBatch batch : batchMap.values()) {
                    updated.addAll(batch.getUpdated());
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
    public List<Asset> getRestored() {
        if (!track) {
            return null;
        }
        boolean empty;
        lock.readLock().lock();
        try {
            empty = restored.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
        if (empty) {
            lock.writeLock().lock();
            try {
                for (AssetBatch batch : batchMap.values()) {
                    restored.addAll(batch.getRestored());
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
    public List<AssetBatch.FailedBatch> getFailures() {
        boolean empty;
        lock.readLock().lock();
        try {
            empty = failures.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
        if (empty) {
            lock.writeLock().lock();
            try {
                for (AssetBatch batch : batchMap.values()) {
                    failures.addAll(batch.getFailures());
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
    public List<Asset> getSkipped() {
        boolean empty;
        lock.readLock().lock();
        try {
            empty = skipped.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
        if (empty) {
            lock.writeLock().lock();
            try {
                for (AssetBatch batch : batchMap.values()) {
                    skipped.addAll(batch.getSkipped());
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
}
