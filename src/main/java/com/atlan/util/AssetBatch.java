/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.util;

import com.atlan.api.EntityBulkEndpoint;
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

    private List<Asset> _batch;
    private final String typeName;
    private final int maxSize;
    private final boolean replaceClassifications;
    private final boolean replaceCustomMetadata;

    /**
     * Create a new batch of assets to be bulk-upserted.
     *
     * @param typeName name of the type of assets to batch process (used only for logging)
     * @param maxSize maximum size of each batch that should be processed (per API call)
     */
    public AssetBatch(String typeName, int maxSize) {
        this(typeName, maxSize, false, false);
    }

    /**
     * Create a new batch of assets to be bulk-upserted.
     *
     * @param typeName name of the type of assets to batch process (used only for logging)
     * @param maxSize maximum size of each batch that should be processed (per API call)
     * @param replaceClassifications if true, all classifications on an existing asset will be overwritten; if false, all classifications will be ignored
     * @param replaceCustomMetadata if true, all custom metadata on an existing asset will be overwritten; if false, all custom metadata will be ignored
     */
    public AssetBatch(String typeName, int maxSize, boolean replaceClassifications, boolean replaceCustomMetadata) {
        _batch = new ArrayList<>();
        this.typeName = typeName;
        this.maxSize = maxSize;
        this.replaceClassifications = replaceClassifications;
        this.replaceCustomMetadata = replaceCustomMetadata;
    }

    /**
     * Add an asset to the batch to be processed.
     *
     * @param single the asset to add to a batch
     * @return the assets that were created or updated in this batch, or null if the batch is still queued
     */
    public AssetMutationResponse add(Asset single) {
        _batch.add(single);
        return process();
    }

    /**
     * If the number of entities we have queued up is equal to the batch size, process them and reset our queue;
     * otherwise do nothing.
     *
     * @return the assets that were created or updated in this batch, or null if the batch is still queued
     */
    private AssetMutationResponse process() {
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
     */
    public AssetMutationResponse flush() {
        AssetMutationResponse response = null;
        if (!_batch.isEmpty()) {
            try {
                log.info("... upserting next batch of ({}) {}s...", _batch.size(), typeName);
                response = EntityBulkEndpoint.upsert(_batch, replaceClassifications, replaceCustomMetadata);
            } catch (AtlanException e) {
                log.error("Unexpected exception while trying to upsert: {}", _batch, e);
            }
            _batch = new ArrayList<>();
        }
        return response;
    }
}
