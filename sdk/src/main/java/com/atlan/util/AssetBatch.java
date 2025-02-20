/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.util;

import com.atlan.AtlanClient;
import com.atlan.cache.OffHeapAssetCache;
import com.atlan.cache.OffHeapFailureCache;
import com.atlan.cache.ReflectionCache;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.LogicException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.Column;
import com.atlan.model.assets.IndistinctAsset;
import com.atlan.model.assets.MaterializedView;
import com.atlan.model.assets.SnowflakeDynamicTable;
import com.atlan.model.assets.Table;
import com.atlan.model.assets.View;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.core.AsyncCreationResponse;
import com.atlan.model.core.AtlanCloseable;
import com.atlan.model.core.AtlanTag;
import com.atlan.model.enums.AssetCreationHandling;
import com.atlan.model.enums.AtlanTagHandling;
import com.atlan.model.enums.CustomMetadataHandling;
import com.atlan.model.relations.Reference;
import com.atlan.model.search.FluentSearch;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.serde.Serde;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

/**
 * Utility class for managing bulk updates in batches.
 */
public class AssetBatch implements AtlanCloseable {

    private static final Set<String> TABLE_LEVEL_ASSETS =
            Set.of(Table.TYPE_NAME, View.TYPE_NAME, MaterializedView.TYPE_NAME, SnowflakeDynamicTable.TYPE_NAME);

    /** Connectivity to an Atlan tenant. */
    private final AtlanClient client;

    /** Maximum number of assets to submit in each batch. */
    private final int maxSize;

    /** How to handle any Atlan tags on assets (ignore, append, replace, or remove). */
    private final AtlanTagHandling atlanTagHandling;

    /** How to handle any custom metadata on assets (ignore, replace, or merge). */
    private final CustomMetadataHandling customMetadataHandling;

    /** Whether to capture details about any failures (true) or throw exceptions for any failures (false). */
    private final boolean captureFailures;

    /** Whether to allow assets to be created (false) or only allow existing assets to be updated (true). */
    private final boolean updateOnly;

    /** Whether to track the basic information about every asset that is created or updated (true) or only track counts (false). */
    private final boolean track;

    /** When running with {@link #updateOnly} as true, whether to consider only exact matches (false) or ignore case (true). */
    private final boolean caseInsensitive;

    /** When allowing assets to be created, how to handle those creations (full assets or partial assets). */
    private final AssetCreationHandling creationHandling;

    /** Whether tables and views should be treated interchangeably (an asset in the batch marked as a table will attempt to match a view if not found as a table, and vice versa). */
    private final boolean tableViewAgnostic;

    /** Internal queue for building up assets to be saved. */
    private final List<Asset> _batch = Collections.synchronizedList(new ArrayList<>());

    /** Number of assets that were created (no details, just a count). */
    @Getter
    private final AtomicLong numCreated = new AtomicLong(0);

    /** Number of assets that were updated (no details, just a count). */
    @Getter
    private final AtomicLong numUpdated = new AtomicLong(0);

    /**
     * Number of assets that were potentially restored from being archived, or otherwise touched
     * without actually being updated (no details, just a count).
     */
    @Getter
    private final AtomicLong numRestored = new AtomicLong(0);

    /** Number of assets that were skipped during processing (no details, just a count). */
    @Getter
    private final AtomicLong numSkipped = new AtomicLong(0);

    /** Assets that were created (minimal info only). */
    @Getter
    private final OffHeapAssetCache created;

    /** Assets that were updated (minimal info only). */
    @Getter
    private final OffHeapAssetCache updated;

    /**
     * Assets that were potentially restored from being archived, or otherwise touched without actually
     * being updated (minimal info only).
     */
    @Getter
    private final OffHeapAssetCache restored;

    /** Batches that failed to be committed (only populated when captureFailures is set to true). */
    @Getter
    private final OffHeapFailureCache failures;

    /** Assets that were skipped, when updateOnly is requested and the asset does not exist in Atlan. */
    @Getter
    private final OffHeapAssetCache skipped;

    /** Map from placeholder GUID to resolved (actual) GUID, for all assets that were processed through the batch. */
    @Getter
    private final Map<String, String> resolvedGuids = new ConcurrentHashMap<>();

    /**
     * Map from all-lowercase qualifiedName (case-insensitive) to case-sensitive qualifiedName,
     * for all assets that were processed through the batch.
     * Note: This is only produced when caseInsensitive is true, otherwise it will be empty.
     */
    @Getter
    private final Map<AssetIdentity, String> resolvedQualifiedNames = new ConcurrentHashMap<>();

    /**
     * Create a new batch of assets to be bulk-saved.
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     */
    public AssetBatch(AtlanClient client, int maxSize) {
        this(client, maxSize, AtlanTagHandling.IGNORE, CustomMetadataHandling.IGNORE);
    }

    /**
     * Create a new batch of assets to be bulk-saved.
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param atlanTagHandling how to handle Atlan tags (ignore them, append them (leaving any pre-existing), replace them (wiping out any pre-existing), or remove them)
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     */
    public AssetBatch(
            AtlanClient client,
            int maxSize,
            AtlanTagHandling atlanTagHandling,
            CustomMetadataHandling customMetadataHandling) {
        this(client, maxSize, atlanTagHandling, customMetadataHandling, false);
    }

    /**
     * Create a new batch of assets to be bulk-saved.
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param atlanTagHandling how to handle Atlan tags (ignore them, append them (leaving any pre-existing), replace them (wiping out any pre-existing), or remove them)
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     * @param captureFailures when true, any failed batches will be captured and retained rather than exceptions being raised (for large amounts of processing this could cause memory issues!)
     */
    public AssetBatch(
            AtlanClient client,
            int maxSize,
            AtlanTagHandling atlanTagHandling,
            CustomMetadataHandling customMetadataHandling,
            boolean captureFailures) {
        this(client, maxSize, atlanTagHandling, customMetadataHandling, captureFailures, false);
    }

    /**
     * Create a new batch of assets to be bulk-saved.
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param atlanTagHandling how to handle Atlan tags (ignore them, append them (leaving any pre-existing), replace them (wiping out any pre-existing), or remove them)
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     * @param captureFailures when true, any failed batches will be captured and retained rather than exceptions being raised (for large amounts of processing this could cause memory issues!)
     * @param updateOnly when true, only attempt to update existing assets and do not create any assets (note: this will incur a performance penalty)
     */
    public AssetBatch(
            AtlanClient client,
            int maxSize,
            AtlanTagHandling atlanTagHandling,
            CustomMetadataHandling customMetadataHandling,
            boolean captureFailures,
            boolean updateOnly) {
        this(client, maxSize, atlanTagHandling, customMetadataHandling, captureFailures, updateOnly, true);
    }

    /**
     * Create a new batch of assets to be bulk-saved.
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param atlanTagHandling how to handle Atlan tags (ignore them, append them (leaving any pre-existing), replace them (wiping out any pre-existing), or remove them)
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     * @param captureFailures when true, any failed batches will be captured and retained rather than exceptions being raised (for large amounts of processing this could cause memory issues!)
     * @param updateOnly when true, only attempt to update existing assets and do not create any assets (note: this will incur a performance penalty)
     * @param track when false, details about each created and updated asset will no longer be tracked (only an overall count of each) -- useful if you intend to send close to (or more than) 1 million assets through a batch
     */
    public AssetBatch(
            AtlanClient client,
            int maxSize,
            AtlanTagHandling atlanTagHandling,
            CustomMetadataHandling customMetadataHandling,
            boolean captureFailures,
            boolean updateOnly,
            boolean track) {
        this(client, maxSize, atlanTagHandling, customMetadataHandling, captureFailures, updateOnly, track, false);
    }

    /**
     * Create a new batch of assets to be bulk-saved.
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param atlanTagHandling how to handle Atlan tags (ignore them, append them (leaving any pre-existing), replace them (wiping out any pre-existing), or remove them)
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     * @param captureFailures when true, any failed batches will be captured and retained rather than exceptions being raised (for large amounts of processing this could cause memory issues!)
     * @param updateOnly when true, only attempt to update existing assets and do not create any assets (note: this will incur a performance penalty)
     * @param track when false, details about each created and updated asset will no longer be tracked (only an overall count of each) -- useful if you intend to send close to (or more than) 1 million assets through a batch
     * @param caseInsensitive (only applies when updateOnly is true) when matching assets, search for their qualifiedName in a case-insensitive way
     */
    public AssetBatch(
            AtlanClient client,
            int maxSize,
            AtlanTagHandling atlanTagHandling,
            CustomMetadataHandling customMetadataHandling,
            boolean captureFailures,
            boolean updateOnly,
            boolean track,
            boolean caseInsensitive) {
        this(
                client,
                maxSize,
                atlanTagHandling,
                customMetadataHandling,
                captureFailures,
                updateOnly,
                track,
                caseInsensitive,
                AssetCreationHandling.FULL);
    }

    /**
     * Create a new batch of assets to be bulk-saved.
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param atlanTagHandling how to handle Atlan tags (ignore them, append them (leaving any pre-existing), replace them (wiping out any pre-existing), or remove them)
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     * @param captureFailures when true, any failed batches will be captured and retained rather than exceptions being raised (for large amounts of processing this could cause memory issues!)
     * @param updateOnly when true, only attempt to update existing assets and do not create any assets (note: this will incur a performance penalty)
     * @param track when false, details about each created and updated asset will no longer be tracked (only an overall count of each) -- useful if you intend to send close to (or more than) 1 million assets through a batch
     * @param caseInsensitive (only applies when updateOnly is true) when matching assets, search for their qualifiedName in a case-insensitive way
     * @param creationHandling if assets are to be created, how they should be created (as full assets or only partial assets)
     */
    public AssetBatch(
            AtlanClient client,
            int maxSize,
            AtlanTagHandling atlanTagHandling,
            CustomMetadataHandling customMetadataHandling,
            boolean captureFailures,
            boolean updateOnly,
            boolean track,
            boolean caseInsensitive,
            AssetCreationHandling creationHandling) {
        this(
                client,
                maxSize,
                atlanTagHandling,
                customMetadataHandling,
                captureFailures,
                updateOnly,
                track,
                caseInsensitive,
                creationHandling,
                false);
    }

    /**
     * Create a new batch of assets to be bulk-saved.
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param atlanTagHandling how to handle Atlan tags (ignore them, append them (leaving any pre-existing), replace them (wiping out any pre-existing), or remove them)
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     * @param captureFailures when true, any failed batches will be captured and retained rather than exceptions being raised (for large amounts of processing this could cause memory issues!)
     * @param updateOnly when true, only attempt to update existing assets and do not create any assets (note: this will incur a performance penalty)
     * @param track when false, details about each created and updated asset will no longer be tracked (only an overall count of each) -- useful if you intend to send close to (or more than) 1 million assets through a batch
     * @param caseInsensitive (only applies when updateOnly is true) when matching assets, search for their qualifiedName in a case-insensitive way
     * @param creationHandling if assets are to be created, how they should be created (as full assets or only partial assets)
     * @param tableViewAgnostic if true, tables and views will be treated interchangeably (an asset in the batch marked as a table will attempt to match a view if not found as a table, and vice versa)
     */
    public AssetBatch(
            AtlanClient client,
            int maxSize,
            AtlanTagHandling atlanTagHandling,
            CustomMetadataHandling customMetadataHandling,
            boolean captureFailures,
            boolean updateOnly,
            boolean track,
            boolean caseInsensitive,
            AssetCreationHandling creationHandling,
            boolean tableViewAgnostic) {
        this(
                client,
                maxSize,
                atlanTagHandling,
                customMetadataHandling,
                captureFailures,
                updateOnly,
                track,
                caseInsensitive,
                creationHandling,
                tableViewAgnostic,
                new OffHeapAssetCache(
                        client, "created_" + Thread.currentThread().getId()),
                new OffHeapAssetCache(
                        client, "updated_" + Thread.currentThread().getId()),
                new OffHeapAssetCache(
                        client, "restored_" + Thread.currentThread().getId()),
                new OffHeapAssetCache(
                        client, "skipped_" + Thread.currentThread().getId()),
                new OffHeapFailureCache(
                        client, "failed_" + Thread.currentThread().getId()));
    }

    /**
     * Create a new batch of assets to be bulk-saved.
     *
     * @param client connectivity to Atlan
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param atlanTagHandling how to handle Atlan tags (ignore them, append them (leaving any pre-existing), replace them (wiping out any pre-existing), or remove them)
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     * @param captureFailures when true, any failed batches will be captured and retained rather than exceptions being raised (for large amounts of processing this could cause memory issues!)
     * @param updateOnly when true, only attempt to update existing assets and do not create any assets (note: this will incur a performance penalty)
     * @param track when false, details about each created and updated asset will no longer be tracked (only an overall count of each) -- useful if you intend to send close to (or more than) 1 million assets through a batch
     * @param caseInsensitive (only applies when updateOnly is true) when matching assets, search for their qualifiedName in a case-insensitive way
     * @param creationHandling if assets are to be created, how they should be created (as full assets or only partial assets)
     * @param tableViewAgnostic if true, tables and views will be treated interchangeably (an asset in the batch marked as a table will attempt to match a view if not found as a table, and vice versa)
     * @param created off-heap asset cache tracking assets that have been created
     * @param updated off-heap asset cache tracking assets that have been updated
     * @param restored off-heap asset cache tracking assets that have been restored
     * @param skipped off-heap asset cache tracking assets that have been skipped
     * @param failed off-heap cache tracking batches of assets that have failed
     */
    public AssetBatch(
            AtlanClient client,
            int maxSize,
            AtlanTagHandling atlanTagHandling,
            CustomMetadataHandling customMetadataHandling,
            boolean captureFailures,
            boolean updateOnly,
            boolean track,
            boolean caseInsensitive,
            AssetCreationHandling creationHandling,
            boolean tableViewAgnostic,
            OffHeapAssetCache created,
            OffHeapAssetCache updated,
            OffHeapAssetCache restored,
            OffHeapAssetCache skipped,
            OffHeapFailureCache failed) {
        this.client = client;
        this.maxSize = maxSize;
        this.atlanTagHandling = atlanTagHandling;
        this.customMetadataHandling = customMetadataHandling;
        this.creationHandling = creationHandling;
        this.track = track;
        this.captureFailures = captureFailures;
        this.updateOnly = updateOnly;
        this.caseInsensitive = caseInsensitive;
        this.tableViewAgnostic = tableViewAgnostic;
        this.created = created;
        this.updated = updated;
        this.restored = restored;
        this.skipped = skipped;
        this.failures = failed;
    }

    /**
     * Add an asset to the batch to be processed.
     *
     * @param single the asset to add to a batch
     * @return the assets that were created or updated in this batch, or null if the batch is still queued
     * @throws AtlanException on any problems adding the asset to or processing the batch
     */
    public AssetMutationResponse add(Asset single) throws AtlanException {
        if (single != null) {
            if (single.getAtlanTags() != null && !single.getAtlanTags().isEmpty()) {
                _batch.add(handleTags(single));
            } else {
                _batch.add(single);
            }
        }
        return process();
    }

    private Asset handleTags(Asset asset) throws LogicException {
        Reference.ReferenceBuilder<?, ?> assetBuilder = asset.toBuilder();
        Method setAtlanTags = ReflectionCache.getSetter(assetBuilder.getClass(), "atlanTags");
        try {
            Set<AtlanTag> existing = asset.getAtlanTags();
            Set<AtlanTag> revisedTags = new TreeSet<>();
            for (AtlanTag tag : existing) {
                AtlanTag revised =
                        switch (atlanTagHandling) {
                            case APPEND -> tag.toBuilder()
                                    .semantic(Reference.SaveSemantic.APPEND)
                                    .build();
                            case REPLACE -> tag.toBuilder()
                                    .semantic(Reference.SaveSemantic.REPLACE)
                                    .build();
                            case REMOVE -> tag.toBuilder()
                                    .semantic(Reference.SaveSemantic.REMOVE)
                                    .build();
                            default -> null;
                        };
                if (revised != null) {
                    revisedTags.add(revised);
                }
            }
            setAtlanTags.invoke(assetBuilder, revisedTags);
            return (Asset) assetBuilder.build();
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new LogicException(ErrorCode.ASSET_MODIFICATION_ERROR, e, Asset.ATLAN_TAGS.getAtlanFieldName());
        }
    }

    /**
     * Close the batch by freeing up any resources it has used.
     * Note: this will clear any internal caches of results, so only call this after you have processed those!
     */
    @Override
    public void close() {
        AtlanCloseable.close(created);
        AtlanCloseable.close(updated);
        AtlanCloseable.close(restored);
        AtlanCloseable.close(skipped);
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
    public synchronized AssetMutationResponse flush() throws AtlanException {
        AsyncCreationResponse response = null;
        List<Asset> revised = null;
        if (!_batch.isEmpty()) {
            boolean fuzzyMatch = false;
            if (tableViewAgnostic) {
                Set<String> typesInBatch =
                        _batch.stream().map(Asset::getTypeName).collect(Collectors.toSet());
                typesInBatch.retainAll(TABLE_LEVEL_ASSETS);
                fuzzyMatch = !typesInBatch.isEmpty();
            }
            if (updateOnly || creationHandling != AssetCreationHandling.FULL || fuzzyMatch) {
                Map<AssetIdentity, String> found = new HashMap<>();
                List<String> qualifiedNames =
                        _batch.stream().map(Asset::getQualifiedName).collect(Collectors.toList());
                FluentSearch.FluentSearchBuilder<?, ?> builder;
                if (caseInsensitive) {
                    builder = client.assets.select(true).minSomes(1);
                    for (String qn : qualifiedNames) {
                        builder.whereSome(Asset.QUALIFIED_NAME.eq(qn, caseInsensitive));
                    }
                } else {
                    builder = client.assets.select(true).where(Asset.QUALIFIED_NAME.in(qualifiedNames));
                }
                builder.pageSize(Math.max(maxSize * 2, IndexSearchDSL.DEFAULT_PAGE_SIZE)).stream()
                        .forEach(asset -> {
                            AssetIdentity assetId =
                                    new AssetIdentity(asset.getTypeName(), asset.getQualifiedName(), caseInsensitive);
                            found.put(assetId, asset.getQualifiedName());
                        });
                revised = new ArrayList<>();
                for (Asset asset : _batch) {
                    AssetIdentity assetId =
                            new AssetIdentity(asset.getTypeName(), asset.getQualifiedName(), caseInsensitive);
                    // If found, with a type match, go ahead and update it
                    if (found.containsKey(assetId)) {
                        // Replace the actual qualifiedName on the asset before adding it to the batch
                        // (in case it matched case-insensitively, we need the proper case-sensitive name we
                        // found to ensure it's an update, not a create)
                        addFuzzyMatched(asset, asset.getTypeName(), found.get(assetId), revised);
                    } else if (tableViewAgnostic && TABLE_LEVEL_ASSETS.contains(asset.getTypeName())) {
                        // If found as a different (but acceptable) type, update that instead
                        AssetIdentity asTable =
                                new AssetIdentity(Table.TYPE_NAME, asset.getQualifiedName(), caseInsensitive);
                        AssetIdentity asView =
                                new AssetIdentity(View.TYPE_NAME, asset.getQualifiedName(), caseInsensitive);
                        AssetIdentity asMaterializedView = new AssetIdentity(
                                MaterializedView.TYPE_NAME, asset.getQualifiedName(), caseInsensitive);
                        AssetIdentity asDynamicTable = new AssetIdentity(
                                SnowflakeDynamicTable.TYPE_NAME, asset.getQualifiedName(), caseInsensitive);
                        if (found.containsKey(asTable)) {
                            addFuzzyMatched(asset, Table.TYPE_NAME, found.get(asTable), revised);
                        } else if (found.containsKey(asView)) {
                            addFuzzyMatched(asset, View.TYPE_NAME, found.get(asView), revised);
                        } else if (found.containsKey(asMaterializedView)) {
                            addFuzzyMatched(asset, MaterializedView.TYPE_NAME, found.get(asMaterializedView), revised);
                        } else if (found.containsKey(asDynamicTable)) {
                            addFuzzyMatched(asset, SnowflakeDynamicTable.TYPE_NAME, found.get(asDynamicTable), revised);
                        } else if (creationHandling == AssetCreationHandling.PARTIAL) {
                            // Still create it (partial), if not found and partial asset creation is allowed
                            addPartialAsset(asset, revised);
                        } else if (creationHandling == AssetCreationHandling.FULL) {
                            // Still create it (full), if not found and full asset creation is allowed
                            revised.add(asset);
                        } else {
                            // Otherwise, if it still does not match any fallback and cannot be created, skip it
                            track(skipped, asset);
                            numSkipped.getAndIncrement();
                        }
                    } else if (creationHandling == AssetCreationHandling.PARTIAL) {
                        // Append isPartial(true) onto the asset before adding it to the batch, to ensure only
                        // a partial (and not a full) asset is created
                        addPartialAsset(asset, revised);
                    } else {
                        track(skipped, asset);
                        numSkipped.getAndIncrement();
                    }
                }
            } else {
                // Otherwise create it (full)
                revised = new ArrayList<>(_batch);
            }
            if (!revised.isEmpty()) {
                try {
                    response = switch (customMetadataHandling) {
                        case IGNORE -> client.assets.save(revised);
                        case OVERWRITE -> client.assets.saveReplacingCM(revised);
                        case MERGE -> client.assets.saveMergingCM(revised);};
                    if (response != null) {
                        response.block();
                    }
                } catch (AtlanException e) {
                    if (captureFailures) {
                        track(failures, _batch, e);
                    } else {
                        throw e;
                    }
                }
            }
            _batch.clear();
        }
        trackResponse(response, revised);
        return response;
    }

    private void addFuzzyMatched(Asset asset, String typeName, String actualQN, List<Asset> revised)
            throws LogicException {
        Reference.ReferenceBuilder<?, ?> assetBuilder = asset.toBuilder();
        Method setQualifiedName =
                ReflectionCache.getSetter(assetBuilder.getClass(), Asset.QUALIFIED_NAME.getAtlanFieldName());
        Method setTypeName = ReflectionCache.getSetter(assetBuilder.getClass(), Asset.TYPE_NAME.getAtlanFieldName());
        try {
            setTypeName.invoke(assetBuilder, typeName);
            setQualifiedName.invoke(assetBuilder, actualQN);
            revised.add((Asset) assetBuilder.build());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new LogicException(
                    ErrorCode.ASSET_MODIFICATION_ERROR,
                    e,
                    Asset.QUALIFIED_NAME.getAtlanFieldName() + " or " + Asset.TYPE_NAME.getAtlanFieldName());
        }
    }

    private void addPartialAsset(Asset asset, List<Asset> revised) throws LogicException {
        Reference.ReferenceBuilder<?, ?> assetBuilder = asset.toBuilder();
        Method setIsPartial = ReflectionCache.getSetter(assetBuilder.getClass(), Asset.IS_PARTIAL.getAtlanFieldName());
        try {
            setIsPartial.invoke(assetBuilder, true);
            revised.add((Asset) assetBuilder.build());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new LogicException(ErrorCode.ASSET_MODIFICATION_ERROR, e, Asset.QUALIFIED_NAME.getAtlanFieldName());
        }
    }

    private void trackResponse(AssetMutationResponse response, List<Asset> sent) {
        if (response != null) {
            if (track) {
                response.getCreatedAssets().forEach(a -> track(created, a));
                response.getUpdatedAssets().forEach(a -> track(updated, a));
            }
            // Always track the counts and resolved GUIDs...
            numCreated.getAndAdd(response.getCreatedAssets().size());
            numUpdated.getAndAdd(response.getUpdatedAssets().size());
            if (response.getGuidAssignments() != null) {
                resolvedGuids.putAll(response.getGuidAssignments());
            }
            if (sent != null) {
                for (Asset one : sent) {
                    String guid = one.getGuid();
                    if (guid != null
                            && (response.getGuidAssignments() == null
                                    || !response.getGuidAssignments().containsKey(guid))) {
                        // Ensure any assets that were sent with GUIDs that were used as-is
                        // are added to the resolved GUIDs map
                        resolvedGuids.put(guid, guid);
                    }
                    String mappedGuid = resolvedGuids.getOrDefault(guid, guid);
                    if (!created.containsKey(mappedGuid) && !updated.containsKey(mappedGuid)) {
                        // Ensure any assets that do not show as either created or updated are still tracked
                        // as possibly restored (and inject the mapped GUID in case it had a placeholder)
                        track(restored, (Asset) one.toBuilder().guid(mappedGuid).build());
                        numRestored.getAndIncrement();
                    }
                    if (caseInsensitive) {
                        String typeName = one.getTypeName();
                        String qualifiedName = one.getQualifiedName();
                        AssetIdentity id = new AssetIdentity(typeName, qualifiedName, caseInsensitive);
                        resolvedQualifiedNames.put(id, qualifiedName);
                    }
                }
            }
        }
    }

    private void track(OffHeapAssetCache tracker, Asset candidate) {
        try {
            tracker.add(buildCacheable(candidate.trimToRequired(), candidate));
        } catch (InvalidRequestException e) {
            try {
                Class<?> assetClass = Serde.getAssetClassForType(candidate.getTypeName());
                Method method = assetClass.getMethod("_internal");
                Object result = method.invoke(null);
                tracker.add(buildCacheable((Asset.AssetBuilder<?, ?>) result, candidate));
            } catch (ClassNotFoundException
                    | NoSuchMethodException
                    | InvocationTargetException
                    | IllegalAccessException eRef) {
                tracker.add(buildCacheable(IndistinctAsset._internal().typeName(candidate.getTypeName()), candidate));
            }
        }
    }

    private void track(OffHeapFailureCache tracker, List<Asset> batch, Exception failureReason) {
        List<Asset> minimal = new ArrayList<>();
        for (Asset asset : batch) {
            try {
                minimal.add(asset.trimToRequired()
                        .guid(asset.getGuid())
                        .qualifiedName(asset.getQualifiedName())
                        .build());
            } catch (InvalidRequestException e) {
                minimal.add(IndistinctAsset._internal()
                        .typeName(asset.getTypeName())
                        .guid(asset.getGuid())
                        .qualifiedName(asset.getQualifiedName())
                        .build());
            }
        }
        tracker.put(UUID.randomUUID().toString(), new FailedBatch(minimal, failureReason));
    }

    /**
     * Construct the minimal asset representation necessary for the asset to be included in a
     * persistent connection cache.
     *
     * @param builder for the asset
     * @param candidate from which to draw any additional details
     * @return the minimally-complete, cacheable asset
     */
    private Asset buildCacheable(Asset.AssetBuilder<?, ?> builder, Asset candidate) {
        builder.guid(candidate.getGuid())
                .qualifiedName(candidate.getQualifiedName())
                .connectionQualifiedName(candidate.getConnectionQualifiedName())
                .name(candidate.getName())
                .tenantId(candidate.getTenantId());
        if (candidate instanceof Column) {
            Integer order = ((Column) candidate).getOrder();
            if (order != null) {
                ((Column.ColumnBuilder<?, ?>) builder).order(order);
            }
        }
        return builder.build();
    }

    /**
     * Internal class to capture batch failures.
     */
    @Getter
    @Builder
    @Jacksonized
    @EqualsAndHashCode
    public static final class FailedBatch {
        private final List<Asset> failedAssets;
        private final Exception failureReason;

        public FailedBatch(List<Asset> failedAssets, Exception failureReason) {
            this.failedAssets = failedAssets;
            this.failureReason = failureReason;
        }
    }

    /**
     * Class to uniquely identify an asset by its type and qualifiedName.
     */
    @Getter
    @EqualsAndHashCode
    public static final class AssetIdentity {
        private final String typeName;
        private final String qualifiedName;

        public AssetIdentity(String typeName, String qualifiedName) {
            this(typeName, qualifiedName, false);
        }

        public AssetIdentity(String typeName, String qualifiedName, boolean caseInsensitive) {
            this.typeName = typeName;
            if (caseInsensitive) {
                this.qualifiedName = qualifiedName.toLowerCase(Locale.ROOT);
            } else {
                this.qualifiedName = qualifiedName;
            }
        }

        /**
         * Reverse-engineer an asset identity from a string representation.
         *
         * @param combined the combined (serialized) asset identity
         * @return the actual asset identity
         */
        public static AssetIdentity fromString(String combined) {
            String[] tokens = combined.split("::");
            if (tokens.length != 2) {
                throw new IllegalArgumentException("Invalid asset identity: " + combined);
            }
            return new AssetIdentity(tokens[0], tokens[1]);
        }

        /**
         * Simplify an asset identity down to a string representation.
         *
         * @return the combined (serialized) asset identity
         */
        @Override
        public String toString() {
            return typeName + "::" + qualifiedName;
        }
    }
}
