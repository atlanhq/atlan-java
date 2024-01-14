/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.util;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Asset;
import com.atlan.model.core.AssetMutationResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParallelBatch {

    private final AtlanClient client;
    private final int maxSize;
    private final boolean replaceAtlanTags;
    private final AssetBatch.CustomMetadataHandling customMetadataHandling;
    private final boolean captureFailures;
    private final boolean track;
    private final boolean updateOnly;
    private final Map<Long, AssetBatch> batchMap;

    private List<Asset> created = null;
    private List<Asset> updated = null;
    private List<AssetBatch.FailedBatch> failures = null;
    private List<Asset> skipped = null;
    private Map<String, String> resolvedGuids = null;

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
        this.client = client;
        this.maxSize = maxSize;
        this.replaceAtlanTags = replaceAtlanTags;
        this.customMetadataHandling = customMetadataHandling;
        this.captureFailures = captureFailures;
        this.track = track;
        this.updateOnly = updateOnly;
        this.batchMap = new ConcurrentHashMap<>();
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
                            track));
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
     * Assets that were created (minimal info only).
     *
     * @return all created assets, across all parallel batches
     */
    public List<Asset> getCreated() {
        if (created == null) {
            List<Asset> list = new ArrayList<>();
            for (AssetBatch batch : batchMap.values()) {
                list.addAll(batch.getCreated());
            }
            created = Collections.unmodifiableList(list);
        }
        return created;
    }

    /**
     * Assets that were updated (minimal info only).
     *
     * @return all updated assets, across all parallel batches
     */
    public List<Asset> getUpdated() {
        if (updated == null) {
            List<Asset> list = new ArrayList<>();
            for (AssetBatch batch : batchMap.values()) {
                list.addAll(batch.getUpdated());
            }
            updated = Collections.unmodifiableList(list);
        }
        return updated;
    }

    /**
     * Batches that failed to be committed (only populated when captureFailures is set to true).
     *
     * @return all batches that failed, across all parallel batches
     */
    public List<AssetBatch.FailedBatch> getFailures() {
        if (failures == null) {
            List<AssetBatch.FailedBatch> list = new ArrayList<>();
            for (AssetBatch batch : batchMap.values()) {
                list.addAll(batch.getFailures());
            }
            failures = Collections.unmodifiableList(list);
        }
        return failures;
    }

    /**
     * Assets that were skipped, when updateOnly is requested and the asset does not exist in Atlan.
     *
     * @return all assets that were skipped, across all parallel batches
     */
    public List<Asset> getSkipped() {
        if (skipped == null) {
            List<Asset> list = new ArrayList<>();
            for (AssetBatch batch : batchMap.values()) {
                list.addAll(batch.getSkipped());
            }
            skipped = Collections.unmodifiableList(list);
        }
        return skipped;
    }

    /**
     * Map from placeholder GUID to resolved (actual) GUID, for all assets that were processed through the batch.
     *
     * @return all resolved GUIDs, across all parallel batches
     */
    public Map<String, String> getResolvedGuids() {
        if (resolvedGuids == null) {
            Map<String, String> map = new HashMap<>();
            for (AssetBatch batch : batchMap.values()) {
                map.putAll(batch.getResolvedGuids());
            }
            resolvedGuids = Collections.unmodifiableMap(map);
        }
        return resolvedGuids;
    }
}
