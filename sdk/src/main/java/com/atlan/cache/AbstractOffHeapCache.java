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
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

/**
 * Generic class through which to cache any objects efficiently, off-heap, to avoid risking extreme
 * memory usage.
 */
@Slf4j
class AbstractOffHeapCache<T extends AtlanObject> implements Closeable {

    public final int DEFAULT_INIT_SIZE = 10_000;
    private final double FILL_FACTOR = 0.70;
    private final Path backingStore;
    volatile ChronicleMap<UUID, T> internal;
    private final long maxSize;

    @Getter
    private final String name;

    /**
     * Construct new object cache.
     *
     * @param name must be unique across the running code
     * @param anticipatedSize number of entries we expect to put in the cache
     * @param exemplar sample object value for what will be stored in the cache
     * @param valueClass class of all objects that will be in the cache
     */
    public AbstractOffHeapCache(String name, int anticipatedSize, T exemplar, Class<T> valueClass) {
        try {
            this.name = name;
            backingStore = Files.createTempFile(name, ".dat");
            int initSize = Math.max(DEFAULT_INIT_SIZE, anticipatedSize);
            internal = createNew(name, exemplar, valueClass, initSize, backingStore);
            maxSize = initSize;
        } catch (IOException e) {
            throw new RuntimeException("Unable to create off-heap cache for tracking.", e);
        }
    }

    /**
     * Create a new off-heap object cache.
     *
     * @param name must be unique across the running code
     * @param exemplar sample object value for what will be stored in the cache
     * @param valueClass class of all objects that will be in the cache
     * @param initSize maximum size for the new object cache
     * @param store where to store it on disk
     * @return the new off-heap cache, ready-to-use
     * @throws IOException on any error creating the new off-heap cache
     */
    private ChronicleMap<UUID, T> createNew(String name, T exemplar, Class<T> valueClass, int initSize, Path store)
            throws IOException {
        ChronicleMapBuilder<UUID, T> builder = ChronicleMap.of(UUID.class, valueClass)
                .name(name)
                .constantKeySizeBySample(UUID.randomUUID())
                .checksumEntries(false)
                .averageValue(exemplar)
                .entries(initSize)
                .maxBloatFactor(2.0);
        ChronicleMap<UUID, T> map = builder.createPersistedTo(store.toFile());
        log.debug("Opening off-heap cache ({}): {}", map.name(), store);
        return map;
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
        if (internal.size() + 1 >= (maxSize * FILL_FACTOR)) {
            autoExtend(internal.size() * 0.5);
        }
        return internal.put(UUID.fromString(id), object);
    }

    /**
     * Put all the provided entries into the cache.
     *
     * @param other cache of entries to add to the cache
     */
    protected void putAll(AbstractOffHeapCache<T> other) {
        if (internal.size() + other.internal.size() >= (maxSize * FILL_FACTOR)) {
            autoExtend(internal.size() + other.internal.size());
        }
        internal.putAll(other.internal);
    }

    /**
     * Check and force-extend (if necessary) the off-heap cache.
     *
     * @param newSize new size to use for the off-heap cache
     */
    private void autoExtend(double newSize) {
        if (internal.remainingAutoResizes() <= 1) {
            int newMax;
            if (newSize > Integer.MAX_VALUE) {
                newMax = Integer.MAX_VALUE;
            } else if (newSize < Integer.MIN_VALUE) {
                newMax = DEFAULT_INIT_SIZE;
            } else {
                newMax = (int) newSize;
            }
            log.info("Force auto-extending off-heap cache {} to: {} entries.", internal.name(), newSize);
            try {
                Path newStore = Files.createTempFile(internal.name(), ".dat");
                UUID first = internal.keySet().stream().findFirst().get();
                ChronicleMap<UUID, T> newMap =
                        createNew(internal.name(), internal.get(first), internal.valueClass(), newMax, newStore);
                newMap.putAll(internal);
                internal.close();
                internal = newMap;
            } catch (IOException e) {
                throw new IllegalStateException("Unable to auto-extend off-heap cache for tracking.", e);
            }
        }
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
     * Retrieve all entries held in the cache.
     *
     * @return an entry set of all objects (and keys) held in the cache
     */
    public Set<Map.Entry<UUID, T>> entrySet() {
        return internal.entrySet();
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
        log.debug("Closing off-heap cache ({}): {}", internal.name(), backingStore);
        internal.close();
        File file = backingStore.toFile();
        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException("Unable to delete off-heap cache: " + backingStore);
            }
            log.debug(" ... file deleted.");
        } else {
            log.debug(" ... file already deleted.");
        }
    }
}
