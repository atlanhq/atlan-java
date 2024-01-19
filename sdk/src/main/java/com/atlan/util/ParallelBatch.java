/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.util;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Asset;
import com.atlan.model.core.AssetMutationResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Builder;

/**
 * Utility class for managing bulk updates across multiple parallel-running batches.
 */
@Builder
public class ParallelBatch {

    private AtlanClient client;

    @Builder.Default
    private int maxSize = 20;

    @Builder.Default
    private boolean replaceAtlanTags = false;

    @Builder.Default
    private AssetBatch.CustomMetadataHandling customMetadataHandling = AssetBatch.CustomMetadataHandling.IGNORE;

    @Builder.Default
    private boolean captureFailures = false;

    @Builder.Default
    private boolean track = true;

    @Builder.Default
    private boolean updateOnly = false;

    @Builder.Default
    private boolean caseSensitive = true;

    @Builder.Default
    private AssetBatch.AssetCreationHandling creationHandling = AssetBatch.AssetCreationHandling.FULL;

    private final Map<Long, AssetBatch> batchMap = new ConcurrentHashMap<>();
    private final List<Asset> created = new ArrayList<>();
    private final List<Asset> updated = new ArrayList<>();
    private final List<AssetBatch.FailedBatch> failures = new ArrayList<>();
    private final List<Asset> skipped = new ArrayList<>();
    private final Map<String, String> resolvedGuids = new HashMap<>();

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
                AssetBatch.AssetCreationHandling.FULL);
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
            AssetBatch.AssetCreationHandling creationHandling) {
        this.client = client;
        this.maxSize = maxSize;
        this.replaceAtlanTags = replaceAtlanTags;
        this.customMetadataHandling = customMetadataHandling;
        this.captureFailures = captureFailures;
        this.track = track;
        this.updateOnly = updateOnly;
        this.caseSensitive = caseSensitive;
        this.creationHandling = creationHandling;
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
                            creationHandling));
        }
        return batchMap.get(id).add(single);
    }

    /**
     * Flush any remaining assets in the parallel batches.
     *
     * @throws IllegalStateException on any problems flushing (submitting) any of the parallel batches
     */
    public void flush() throws AtlanException {
        batchMap.values().parallelStream().forEach(batch -> {
            try {
                batch.flush();
            } catch (AtlanException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    /**
     * Number of assets that were created (no details, only a count).
     *
     * @return a count of the number of created assets, across all parallel batches
     */
    public long getNumCreated() {
        long count = 0;
        for (AssetBatch batch : batchMap.values()) {
            count += batch.getNumCreated().get();
        }
        return count;
    }

    /**
     * Number of assets that were updated (no details, only a count).
     *
     * @return a count of the number of updated assets, across all parallel batches
     */
    public long getNumUpdated() {
        long count = 0;
        for (AssetBatch batch : batchMap.values()) {
            count += batch.getNumUpdated().get();
        }
        return count;
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
        if (created.isEmpty()) {
            for (AssetBatch batch : batchMap.values()) {
                created.addAll(batch.getCreated());
            }
        }
        return created;
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
        if (updated.isEmpty()) {
            for (AssetBatch batch : batchMap.values()) {
                updated.addAll(batch.getUpdated());
            }
        }
        return updated;
    }

    /**
     * Batches that failed to be committed (only populated when captureFailures is set to true).
     *
     * @return all batches that failed, across all parallel batches
     */
    public List<AssetBatch.FailedBatch> getFailures() {
        if (failures.isEmpty()) {
            for (AssetBatch batch : batchMap.values()) {
                failures.addAll(batch.getFailures());
            }
        }
        return failures;
    }

    /**
     * Assets that were skipped, when updateOnly is requested and the asset does not exist in Atlan.
     *
     * @return all assets that were skipped, across all parallel batches
     */
    public List<Asset> getSkipped() {
        if (skipped.isEmpty()) {
            for (AssetBatch batch : batchMap.values()) {
                skipped.addAll(batch.getSkipped());
            }
        }
        return skipped;
    }

    /**
     * Map from placeholder GUID to resolved (actual) GUID, for all assets that were processed through the batch.
     *
     * @return all resolved GUIDs, across all parallel batches
     */
    public Map<String, String> getResolvedGuids() {
        if (resolvedGuids.isEmpty()) {
            for (AssetBatch batch : batchMap.values()) {
                resolvedGuids.putAll(batch.getResolvedGuids());
            }
        }
        return resolvedGuids;
    }
}
