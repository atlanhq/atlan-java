/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.util;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.IndistinctAsset;
import com.atlan.model.core.AssetMutationResponse;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 * Utility class for managing bulk updates in batches.
 */
public class AssetBatch {

    public enum CustomMetadataHandling {
        IGNORE,
        OVERWRITE,
        MERGE
    }

    private final AtlanClient client;
    private List<Asset> _batch;
    private final int maxSize;
    private final boolean replaceAtlanTags;
    private final CustomMetadataHandling customMetadataHandling;
    private final boolean captureFailures;
    private final boolean updateOnly;

    /** Assets that were created (minimal info only). */
    @Getter
    private final List<Asset> created;

    /** Assets that were updated (minimal info only). */
    @Getter
    private final List<Asset> updated;

    /** Batches that failed to be committed (only populated when captureFailures is set to true). */
    @Getter
    private final List<FailedBatch> failures;

    /** Assets that were skipped, when updateOnly is requested and the asset does not exist in Atlan. */
    @Getter
    private final List<Asset> skipped;

    /** Map from placeholder GUID to resolved (actual) GUID, for all assets that were processed through the batch. */
    @Getter
    private final Map<String, String> resolvedGuids;

    /**
     * Create a new batch of assets to be bulk-saved.
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     */
    public AssetBatch(AtlanClient client, int maxSize) {
        this(client, maxSize, false, CustomMetadataHandling.IGNORE);
    }

    /**
     * Create a new batch of assets to be bulk-saved.
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param replaceAtlanTags if true, all Atlan tags on an existing asset will be overwritten; if false, all Atlan tags will be ignored
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     */
    public AssetBatch(
            AtlanClient client, int maxSize, boolean replaceAtlanTags, CustomMetadataHandling customMetadataHandling) {
        this(client, maxSize, replaceAtlanTags, customMetadataHandling, false);
    }

    /**
     * Create a new batch of assets to be bulk-saved.
     *
     * @param client connectivity to Atlan
     * @param typeName name of the type of assets to batch process (unused, for backwards compatibility only)
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @deprecated see constructor without typeName parameter
     */
    @Deprecated
    public AssetBatch(AtlanClient client, String typeName, int maxSize) {
        this(client, maxSize, false, CustomMetadataHandling.IGNORE);
    }

    /**
     * Create a new batch of assets to be bulk-saved.
     *
     * @param client connectivity to Atlan
     * @param typeName name of the type of assets to batch process (unused, for backwards compatibility only)
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param replaceAtlanTags if true, all Atlan tags on an existing asset will be overwritten; if false, all Atlan tags will be ignored
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     * @deprecated see constructor without typeName parameter
     */
    @Deprecated
    public AssetBatch(
            AtlanClient client,
            String typeName,
            int maxSize,
            boolean replaceAtlanTags,
            CustomMetadataHandling customMetadataHandling) {
        this(client, maxSize, replaceAtlanTags, customMetadataHandling, false);
    }

    /**
     * Create a new batch of assets to be bulk-saved.
     *
     * @param client connectivity to Atlan
     * @param typeName name of the type of assets to batch process (unused, for backwards compatibility only)
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param replaceAtlanTags if true, all Atlan tags on an existing asset will be overwritten; if false, all Atlan tags will be ignored
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     * @param captureFailures when true, any failed batches will be captured and retained rather than exceptions being raised (for large amounts of processing this could cause memory issues!)
     * @deprecated see constructor without typeName parameter
     */
    @Deprecated
    public AssetBatch(
            AtlanClient client,
            String typeName,
            int maxSize,
            boolean replaceAtlanTags,
            CustomMetadataHandling customMetadataHandling,
            boolean captureFailures) {
        this(client, maxSize, replaceAtlanTags, customMetadataHandling, captureFailures);
    }

    /**
     * Create a new batch of assets to be bulk-saved.
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param replaceAtlanTags if true, all Atlan tags on an existing asset will be overwritten; if false, all Atlan tags will be ignored
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     * @param captureFailures when true, any failed batches will be captured and retained rather than exceptions being raised (for large amounts of processing this could cause memory issues!)
     */
    public AssetBatch(
            AtlanClient client,
            int maxSize,
            boolean replaceAtlanTags,
            CustomMetadataHandling customMetadataHandling,
            boolean captureFailures) {
        this(client, maxSize, replaceAtlanTags, customMetadataHandling, captureFailures, false);
    }

    /**
     * Create a new batch of assets to be bulk-saved.
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param replaceAtlanTags if true, all Atlan tags on an existing asset will be overwritten; if false, all Atlan tags will be ignored
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     * @param captureFailures when true, any failed batches will be captured and retained rather than exceptions being raised (for large amounts of processing this could cause memory issues!)
     * @param updateOnly when true, only attempt to update existing assets and do not create any assets (note: this will incur a performance penalty)
     */
    public AssetBatch(
            AtlanClient client,
            int maxSize,
            boolean replaceAtlanTags,
            CustomMetadataHandling customMetadataHandling,
            boolean captureFailures,
            boolean updateOnly) {
        this.client = client;
        _batch = Collections.synchronizedList(new ArrayList<>());
        failures = Collections.synchronizedList(new ArrayList<>());
        resolvedGuids = new ConcurrentHashMap<>();
        this.maxSize = maxSize;
        this.replaceAtlanTags = replaceAtlanTags;
        this.customMetadataHandling = customMetadataHandling;
        this.created = Collections.synchronizedList(new ArrayList<>());
        this.updated = Collections.synchronizedList(new ArrayList<>());
        this.skipped = Collections.synchronizedList(new ArrayList<>());
        this.captureFailures = captureFailures;
        this.updateOnly = updateOnly;
    }

    /**
     * Add an asset to the batch to be processed.
     *
     * @param single the asset to add to a batch
     * @return the assets that were created or updated in this batch, or null if the batch is still queued
     * @throws AtlanException on any problems adding the asset to or processing the batch
     */
    public AssetMutationResponse add(Asset single) throws AtlanException {
        _batch.add(single);
        return process();
    }

    /**
     * If the number of entities we have queued up is equal to the batch size, process them and reset our queue;
     * otherwise do nothing.
     *
     * @return the assets that were created or updated in this batch, or null if the batch is still queued
     * @throws AtlanException on any problems processing the batch
     */
    private AssetMutationResponse process() throws AtlanException {
        // Once we reach our batch size, create them and then start a new batch
        if (_batch.size() == maxSize) {
            return flush();
        } else {
            return null;
        }
    }

    /**
     * Flush any remaining assets in the batch.
     *
     * @return the mutation response from the queued batch of assets that were flushed
     * @throws AtlanException on any problems flushing (submitting) the batch
     */
    public AssetMutationResponse flush() throws AtlanException {
        AssetMutationResponse response = null;
        if (!_batch.isEmpty()) {
            List<Asset> revised;
            if (updateOnly) {
                Set<String> found = new HashSet<>();
                List<String> qualifiedNames =
                        _batch.stream().map(Asset::getQualifiedName).collect(Collectors.toList());
                client.assets.select().where(Asset.QUALIFIED_NAME.in(qualifiedNames)).pageSize(maxSize).stream()
                        .forEach(asset -> found.add(asset.getTypeName() + "::" + asset.getQualifiedName()));
                revised = new ArrayList<>();
                _batch.forEach(asset -> {
                    if (found.contains(asset.getTypeName() + "::" + asset.getQualifiedName())) {
                        revised.add(asset);
                    } else {
                        track(skipped, asset);
                    }
                });
            } else {
                revised = _batch;
            }
            try {
                switch (customMetadataHandling) {
                    case IGNORE:
                        response = client.assets.save(revised, replaceAtlanTags);
                        break;
                    case OVERWRITE:
                        response = client.assets.saveReplacingCM(revised, replaceAtlanTags);
                        break;
                    case MERGE:
                        response = client.assets.saveMergingCM(revised, replaceAtlanTags);
                        break;
                }
            } catch (AtlanException e) {
                if (captureFailures) {
                    failures.add(new FailedBatch(_batch, e));
                } else {
                    throw e;
                }
            }
            _batch = Collections.synchronizedList(new ArrayList<>());
        }
        trackResponse(response);
        return response;
    }

    private void trackResponse(AssetMutationResponse response) {
        if (response != null) {
            response.getCreatedAssets().forEach(a -> track(created, a));
            response.getUpdatedAssets().forEach(a -> track(updated, a));
            if (response.getGuidAssignments() != null) {
                resolvedGuids.putAll(response.getGuidAssignments());
            }
        }
    }

    private void track(List<Asset> tracker, Asset candidate) {
        try {
            tracker.add(candidate.trimToRequired().name(candidate.getName()).build());
        } catch (InvalidRequestException e) {
            tracker.add(IndistinctAsset._internal()
                    .typeName(candidate.getTypeName())
                    .guid(candidate.getGuid())
                    .qualifiedName(candidate.getQualifiedName())
                    .build());
        }
    }

    /**
     * Internal class to capture batch failures.
     */
    @Getter
    public static final class FailedBatch {
        private final List<Asset> failedAssets;
        private final Exception failureReason;

        public FailedBatch(List<Asset> failedAssets, Exception failureReason) {
            this.failedAssets = Collections.unmodifiableList(failedAssets);
            this.failureReason = failureReason;
        }
    }
}
