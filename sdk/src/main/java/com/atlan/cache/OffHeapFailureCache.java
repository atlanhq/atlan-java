/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.serde.Serde;
import com.atlan.util.AssetBatch;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Generic class through which to cache any batch failures efficiently, off-heap, to avoid risking extreme
 * memory usage.
 */
public class OffHeapFailureCache extends AbstractOffHeapCache<String, AssetBatch.FailedBatch> {
    public OffHeapFailureCache(String name) {
        super(name);
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
            return Serde.allInclusiveMapper.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    /** {@inheritDoc}  */
    @Override
    public AssetBatch.FailedBatch deserializeValue(byte[] bytes) {
        try {
            return Serde.allInclusiveMapper.readValue(bytes, AssetBatch.FailedBatch.class);
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
