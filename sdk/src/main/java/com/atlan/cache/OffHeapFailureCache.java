/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.AtlanClient;
import com.atlan.util.AssetBatch;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Generic class through which to cache any batch failures efficiently, off-heap, to avoid risking extreme
 * memory usage.
 */
public class OffHeapFailureCache extends AbstractOffHeapCache<String, AssetBatch.FailedBatch> {

    private final AtlanClient client;

    /**
     * Construct new cache for failures.
     *
     * @param client connectivity to the Atlan tenant
     * @param name to distinguish which cache is which
     */
    public OffHeapFailureCache(AtlanClient client, String name) {
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
    public byte[] serializeValue(AssetBatch.FailedBatch value) {
        try {
            return client.writeValueAsBytes(value);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /** {@inheritDoc}  */
    @Override
    public AssetBatch.FailedBatch deserializeValue(byte[] bytes) {
        try {
            return client.readValue(bytes, AssetBatch.FailedBatch.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Extend this cache with all the entries from the provided cache.
     *
     * @param closeOriginal if true, close the provided cache after the extension is complete
     * @param other other cache with which to extend this one
     */
    public void extendedWith(OffHeapFailureCache other, boolean closeOriginal) {
        if (other != null) {
            this.putAll(other);
            if (closeOriginal) {
                other.close();
            }
        }
    }
}
