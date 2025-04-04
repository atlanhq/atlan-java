/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.AtlanClient;
import com.atlan.model.assets.Asset;
import java.util.function.Predicate;

/**
 * Generic class through which to cache any assets efficiently, off-heap, to avoid risking extreme
 * memory usage.
 */
public class OffHeapAssetCache extends OffHeapObjectCache<Asset> {

    /**
     * Construct new asset cache.
     *
     * @param client connectivity to the Atlan tenant
     * @param name must be unique across the running code
     */
    public OffHeapAssetCache(AtlanClient client, String name) {
        super(client, name);
    }

    /**
     * Add an asset into the cache.
     * Note: the object MUST have a real (not a placeholder) UUID to be cached.
     *
     * @param asset to add to the cache
     */
    public void add(Asset asset) {
        put(asset.getGuid(), asset);
    }

    /**
     * Extend this cache with all the entries from the provided cache.
     *
     * @param closeOriginal if true, close the provided cache after the extension is complete
     * @param other other cache with which to extend this one
     */
    public void extendedWith(OffHeapAssetCache other, boolean closeOriginal) {
        if (other != null) {
            this.putAll(other);
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
     */
    public void extendedWith(OffHeapAssetCache other, boolean closeOriginal, Predicate<Asset> isValid) {
        if (other != null) {
            other.values().forEach(one -> {
                if (isValid.test(one)) {
                    put(one.getGuid(), one);
                }
            });
            if (closeOriginal) {
                other.close();
            }
        }
    }
}
