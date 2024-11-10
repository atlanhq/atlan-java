/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.model.assets.Asset;
import com.atlan.util.AssetBatch;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Predicate;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

/**
 * Generic class through which to cache any assets efficiently, off-heap, to avoid risking extreme
 * memory usage.
 */
public class OffHeapAssetCache implements Closeable {

    private final Path backingStore;
    private final ChronicleMap<UUID, Asset> internal;

    /**
     * Construct new asset cache.
     *
     * @param name must be unique across the running code
     * @param anticipatedSize number of entries we expect to put in the cache
     */
    public OffHeapAssetCache(String name, int anticipatedSize) {
        this(name, anticipatedSize, AssetBatch.EXEMPLAR_COLUMN);
    }

    /**
     * Construct new asset cache.
     *
     * @param name must be unique across the running code
     * @param anticipatedSize number of entries we expect to put in the cache
     * @param exemplar sample asset value for what will be stored in the cache
     */
    public OffHeapAssetCache(String name, int anticipatedSize, Asset exemplar) {
        try {
            backingStore = Files.createTempFile(name, ".dat");
            ChronicleMapBuilder<UUID, Asset> builder =
                    ChronicleMap.of(UUID.class, Asset.class).name(name).constantKeySizeBySample(UUID.randomUUID()).checksumEntries(false);
            if (exemplar != null) {
                builder.averageValue(exemplar);
            }
            if (anticipatedSize > 0) {
                builder.entries(anticipatedSize);
            } else {
                builder.entries(1_000_000);
            }
            internal = builder.createPersistedTo(backingStore.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Unable to create off-heap cache for tracking.", e);
        }
    }

    /**
     * Add a new asset to the cache.
     * Note: the asset MUST have a real (not a placeholder) GUID to be cached.
     *
     * @param asset to add to the cache
     * @return any asset that was previously cached with the same GUID, or null if no such GUID has ever been cached
     */
    public Asset add(Asset asset) {
        return put(asset.getGuid(), asset);
    }

    /**
     * Retrieve an asset from the cache by its GUID.
     *
     * @param guid of the asset to retrieve
     * @return the asset with that GUID, or null if it is not in the cache
     */
    public Asset get(String guid) {
        return internal.get(UUID.fromString(guid));
    }

    /**
     * Put an asset into the cache by its GUID.
     *
     * @param guid of the asset to put into the cache
     * @param asset to put into the cache
     * @return any asset that was previously cached with the same GUID, or null if no such GUID has ever been cached
     */
    private Asset put(String guid, Asset asset) {
        return internal.put(UUID.fromString(guid), asset);
    }

    /**
     * Check whether the cache has an asset in it with the provided GUID.
     *
     * @param guid of the asset to check exists in the cache
     * @return true if and only if the cache has an asset with this GUID in it
     */
    public boolean containsKey(String guid) {
        return internal.containsKey(UUID.fromString(guid));
    }

    /**
     * Retrieve the number of assets currently held in the cache.
     *
     * @return the number of assets currently in the cache
     */
    public int size() {
        return internal.size();
    }

    /**
     * Retrieve the number of assets currently held in the cache.
     *
     * @return the number of assets currently in the cache
     */
    public int getSize() {
        return size();
    }

    /**
     * Indicates whether the cache has no entries.
     *
     * @return true if the cache has no entries, otherwise false
     */
    public boolean isEmpty() {
        return internal.isEmpty();
    }

    /**
     * Indicates whether the cache has any entries.
     *
     * @return true if the cache has at least one entry, otherwise false
     */
    public boolean isNotEmpty() {
        return !isEmpty();
    }

    /**
     * Retrieve all the keys held in the cache.
     *
     * @return a collection of all keys held in the cache
     */
    public Collection<UUID> keys() {
        return internal.keySet();
    }

    /**
     * Retrieve all the assets held in the cache.
     *
     * @return a collection of all assets held in the cache
     */
    public Collection<Asset> values() {
        return internal.values();
    }

    /**
     * Indicates whether the cache has already been closed.
     *
     * @return true if the cache has been closed, otherwise false
     */
    public boolean isClosed() {
        return internal.isClosed();
    }

    /**
     * Clean up the cache, once it is no longer needed.
     *
     * @throws IOException if unable to remove the temporary file holding the cache
     */
    @Override
    public void close() throws IOException {
        internal.close();
        if (!backingStore.toFile().delete()) {
            throw new IOException("Unable to delete off-heap cache: " + backingStore);
        }
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
        OffHeapAssetCache combined =
                new OffHeapAssetCache(this.internal.name(), this.size() + other.size(), AssetBatch.EXEMPLAR_COLUMN);
        combined.internal.putAll(this.internal);
        combined.internal.putAll(other.internal);
        this.close();
        other.close();
        return combined;
    }

    /**
     * Extend this cache with all the entries from the provided cache.
     *
     * @param other other cache with which to extend this one
     */
    public void extendedWith(OffHeapAssetCache other) {
        if (other != null) {
            this.internal.putAll(other.internal);
        }
    }

    /**
     * Extend this cache with all the entries from the provided cache.
     *
     * @param other other cache with which to extend this one
     * @param isValid boolean method that takes a single asset as an argument, and only when evaluated to true for
     *                an asset in the other cache will that entry from the other cache be included in this one
     */
    public void extendedWith(OffHeapAssetCache other, Predicate<Asset> isValid) {
        if (other != null) {
            for (Asset one : other.internal.values()) {
                if (isValid.test(one)) {
                    put(one.getGuid(), one);
                }
            }
        }
    }
}
