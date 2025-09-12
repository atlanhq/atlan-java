/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.AtlanClient;
import com.atlan.util.AssetBatch;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Generic class through which to cache any deferred assets efficiently, off-heap, to avoid risking extreme
 * memory usage.
 */
public class OffHeapDeferredCache extends AbstractOffHeapCache<String, AssetBatch.RelatedAssetHold> {

    private final AtlanClient client;

    /**
     * Construct new cache for failures.
     *
     * @param client connectivity to the Atlan tenant
     * @param name to distinguish which cache is which
     */
    public OffHeapDeferredCache(AtlanClient client, String name) {
        super(name);
        this.client = client;
    }

    /** {@inheritDoc} */
    @Override
    public byte[] serializeKey(String key) {
        return key.getBytes(StandardCharsets.UTF_8);
    }

    /** {@inheritDoc}  */
    @Override
    public String deserializeKey(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /** {@inheritDoc}  */
    @Override
    public byte[] serializeValue(AssetBatch.RelatedAssetHold value) {
        try {
            return client.writeValueAsBytes(value);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /** {@inheritDoc}  */
    @Override
    public AssetBatch.RelatedAssetHold deserializeValue(byte[] bytes) {
        try {
            return client.readValue(bytes, AssetBatch.RelatedAssetHold.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
