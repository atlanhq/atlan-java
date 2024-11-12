/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.model.assets.Asset;
import com.atlan.model.assets.Column;
import java.io.IOException;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * Generic class through which to cache any assets efficiently, off-heap, to avoid risking extreme
 * memory usage.
 */
public class OffHeapAssetCache extends AbstractOffHeapCache<Asset> {

    public static final Column EXEMPLAR_COLUMN = Column._internal()
            .guid(UUID.randomUUID().toString())
            .qualifiedName("default/somewhere/1234567890/database/schema/table/column_name")
            .connectionQualifiedName("default/somewhere/1234567890")
            .name("column_name")
            .tenantId("default")
            .order(10)
            .build();

    /**
     * Construct new asset cache.
     *
     * @param name must be unique across the running code
     * @param anticipatedSize number of entries we expect to put in the cache
     */
    public OffHeapAssetCache(String name, int anticipatedSize) {
        this(name, anticipatedSize, EXEMPLAR_COLUMN);
    }

    /**
     * Construct new asset cache.
     *
     * @param name must be unique across the running code
     * @param anticipatedSize number of entries we expect to put in the cache
     * @param exemplar sample asset value for what will be stored in the cache
     */
    public OffHeapAssetCache(String name, int anticipatedSize, Asset exemplar) {
        super(name, anticipatedSize, exemplar, Asset.class);
    }

    /**
     * Add an asset into the cache.
     * Note: the object MUST have a real (not a placeholder) UUID to be cached.
     *
     * @param asset to add to the cache
     * @return any asset that was previously cached with the same UUID, or null if no such UUID has ever been cached
     */
    public Asset add(Asset asset) {
        return put(asset.getGuid(), asset);
    }

    /**
     * Create a copy of this cache and return it.
     * Note: this will not close the original cache (so you need to handle that yourself!)
     *
     * @param closeOriginal if true, close the original cache from which we copied after the copy is complete
     * @return a copy of this cache
     * @throws IOException if unable to close the original caches
     */
    public OffHeapAssetCache copy(boolean closeOriginal) throws IOException {
        OffHeapAssetCache copy = new OffHeapAssetCache(this.getName(), this.size());
        copy.putAll(this);
        if (closeOriginal) {
            this.close();
        }
        return copy;
    }

    /**
     * Combine two caches together to form a single, new cache.
     * Note: the original caches will be closed and deleted after the combined cache is created.
     *
     * @param other other cache with which to combine this one
     * @return a new cache with the combined entries of the original cache and the one provided
     * @throws IOException if unable to clean up either of the original caches
     */
    public OffHeapAssetCache combinedWith(OffHeapAssetCache other) throws IOException {
        if (other == null) {
            return this;
        }
        OffHeapAssetCache combined = new OffHeapAssetCache(this.getName(), this.size() + other.size());
        combined.putAll(this);
        combined.putAll(other);
        IOException exception = null;
        try {
            this.close();
        } catch (IOException e) {
            exception = e;
        }
        try {
            other.close();
        } catch (IOException e) {
            if (exception == null) {
                exception = e;
            } else {
                exception.addSuppressed(e);
            }
        }
        if (exception != null) {
            throw exception;
        }
        return combined;
    }

    /**
     * Extend this cache with all the entries from the provided cache.
     *
     * @param closeOriginal if true, close the provided cache after the extension is complete
     * @param other other cache with which to extend this one
     * @throws IOException on any error closing the provided cache
     */
    public void extendedWith(OffHeapAssetCache other, boolean closeOriginal) throws IOException {
        if (other != null) {
            this.internal.putAll(other.internal);
            if (closeOriginal) {
                other.close();
            }
        }
    }

    /**
     * Extend this cache with all the entries from the provided cache.
     *
     * @param other other cache with which to extend this one
     * @param closeOriginal if true, close the provided cache after the extension is complete
     * @param isValid boolean method that takes a single asset as an argument, and only when evaluated to true for
     *                an asset in the other cache will that entry from the other cache be included in this one
     * @throws IOException on any error closing the provided cache
     */
    public void extendedWith(OffHeapAssetCache other, boolean closeOriginal, Predicate<Asset> isValid)
            throws IOException {
        if (other != null) {
            for (Asset one : other.values()) {
                if (isValid.test(one)) {
                    put(one.getGuid(), one);
                }
            }
            if (closeOriginal) {
                other.close();
            }
        }
    }
}
