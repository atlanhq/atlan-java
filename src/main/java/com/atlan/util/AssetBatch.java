/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.util;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Asset;
import com.atlan.model.core.AssetMutationResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for managing bulk updates in batches.
 */
@Slf4j
public class AssetBatch {

    public enum CustomMetadataHandling {
        IGNORE,
        OVERWRITE,
        MERGE
    }

    private List<Asset> _batch;
    private final String typeName;
    private final int maxSize;
    private final boolean replaceAtlanTags;
    private final CustomMetadataHandling customMetadataHandling;

    /**
     * Create a new batch of assets to be bulk-upserted.
     *
     * @param typeName name of the type of assets to batch process (used only for logging)
     * @param maxSize maximum size of each batch that should be processed (per API call)
     */
    public AssetBatch(String typeName, int maxSize) {
        this(typeName, maxSize, false, CustomMetadataHandling.IGNORE);
    }

    /**
     * Create a new batch of assets to be bulk-upserted.
     *
     * @param typeName name of the type of assets to batch process (used only for logging)
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param replaceAtlanTags if true, all Atlan tags on an existing asset will be overwritten; if false, all Atlan tags will be ignored
     * @param customMetadataHandling how to handle custom metadata (ignore it, replace it (wiping out anything pre-existing), or merge it)
     */
    public AssetBatch(
            String typeName, int maxSize, boolean replaceAtlanTags, CustomMetadataHandling customMetadataHandling) {
        _batch = new ArrayList<>();
        this.typeName = typeName;
        this.maxSize = maxSize;
        this.replaceAtlanTags = replaceAtlanTags;
        this.customMetadataHandling = customMetadataHandling;
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
            log.info("... upserting next batch of ({}) {}s...", _batch.size(), typeName);
            switch (customMetadataHandling) {
                case IGNORE:
                    response = Atlan.getDefaultClient().assets().save(_batch, replaceAtlanTags);
                    break;
                case OVERWRITE:
                    response = Atlan.getDefaultClient().assets().saveReplacingCM(_batch, replaceAtlanTags);
                    break;
                case MERGE:
                    response = Atlan.getDefaultClient().assets().saveMergingCM(_batch, replaceAtlanTags);
                    break;
            }
            _batch = new ArrayList<>();
        }
        return response;
    }
}
