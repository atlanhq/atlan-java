/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.model.core.AtlanObject;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.UUID;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

/**
 * Generic class through which to cache any objects efficiently, off-heap, to avoid risking extreme
 * memory usage.
 */
class AbstractOffHeapCache<T extends AtlanObject> implements Closeable {

    private final Path backingStore;
    protected final ChronicleMap<UUID, T> internal;

    /**
     * Construct new object cache.
     *
     * @param name must be unique across the running code
     * @param anticipatedSize number of entries we expect to put in the cache
     * @param exemplar sample object value for what will be stored in the cache
     * @param valueClazz class of all objects that will be in the cache
     */
    public AbstractOffHeapCache(String name, int anticipatedSize, T exemplar, Class<T> valueClazz) {
        try {
            backingStore = Files.createTempFile(name, ".dat");
            ChronicleMapBuilder<UUID, T> builder = ChronicleMap.of(UUID.class, valueClazz)
                    .name(name)
                    .constantKeySizeBySample(UUID.randomUUID())
                    .checksumEntries(false)
                    .averageValue(exemplar)
                    .entries(anticipatedSize > 0 ? anticipatedSize : 10_000)
                    .maxBloatFactor(2.0);
            internal = builder.createPersistedTo(backingStore.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Unable to create off-heap cache for tracking.", e);
        }
    }

    /**
     * Retrieve an object from the cache by its ID.
     *
     * @param id of the object to retrieve
     * @return the object with that UUID, or null if it is not in the cache
     */
    public T get(String id) {
        return internal.get(UUID.fromString(id));
    }

    /**
     * Put an object into the cache by its ID.
     *
     * @param id of the object to put into the cache
     * @param object to put into the cache
     * @return any object that was previously cached with the same UUID, or null if no such UUID has ever been cached
     */
    protected T put(String id, T object) {
        return internal.put(UUID.fromString(id), object);
    }

    /**
     * Check whether the cache has an object in it with the provided UUID.
     *
     * @param id of the object to check exists in the cache
     * @return true if and only if the cache has an object with this UUID in it
     */
    public boolean containsKey(String id) {
        return internal.containsKey(UUID.fromString(id));
    }

    /**
     * Retrieve the number of objects currently held in the cache.
     *
     * @return the number of objects currently in the cache
     */
    public int size() {
        return internal.size();
    }

    /**
     * Retrieve the number of objects currently held in the cache.
     *
     * @return the number of objects currently in the cache
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
     * Retrieve all the objects held in the cache.
     *
     * @return a collection of all objects held in the cache
     */
    public Collection<T> values() {
        return internal.values();
    }

    /**
     * Indicates whether the cache has already been closed.
     *
     * @return true if the cache has been closed, otherwise false
     */
    public boolean isNotClosed() {
        return !internal.isClosed();
    }

    /**
     * Clean up the cache, once it is no longer needed.
     *
     * @throws IOException if unable to remove the temporary file holding the cache
     */
    @Override
    public void close() throws IOException {
        internal.close();
        File file = backingStore.toFile();
        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException("Unable to delete off-heap cache: " + backingStore);
            }
        }
    }
}
