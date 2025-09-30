/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.model.core.AtlanCloseable;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import org.rocksdb.WriteBatch;
import org.rocksdb.WriteOptions;

/**
 * Generic class through which to cache any key-value pairs efficiently, off-heap, to avoid
 * risking extreme memory usage.
 */
@Slf4j
public abstract class AbstractOffHeapCache<K, V> implements AtlanCloseable {

    private final Path backingStore;
    private volatile RocksDB internal;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Getter
    private final String name;

    /**
     * Construct new object cache.
     *
     * @param name to distinguish which cache is which
     */
    public AbstractOffHeapCache(String name) {
        this.name = name;
        lock.writeLock().lock();
        try {
            backingStore = Files.createTempDirectory("rdb_" + name + "_");
            log.debug("Opening off-heap cache ({}): {}", this.name, this.backingStore);
            internal = RocksDB.open(backingStore.toString());
        } catch (IOException | RocksDBException e) {
            throw new RuntimeException("Unable to create off-heap cache for tracking.", e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    protected abstract byte[] serializeKey(K key);

    protected abstract K deserializeKey(byte[] bytes) throws IOException;

    protected abstract byte[] serializeValue(V value);

    protected abstract V deserializeValue(byte[] bytes) throws IOException;

    /**
     * Retrieve a value from the cache by its key.
     *
     * @param key of the value to retrieve
     * @return the value with that key, or null if it is not in the cache
     */
    public V get(K key) {
        if (internal.isClosed()) return null;
        byte[] kb = serializeKey(key);
        byte[] value;
        lock.readLock().lock();
        try {
            value = internal.get(kb);
        } catch (RocksDBException e) {
            throw new IllegalStateException("Unable to get value for key: " + key, e);
        } finally {
            lock.readLock().unlock();
        }
        try {
            if (value == null || value.length == 0) {
                log.warn("Null or empty value retrieved for ID: {} -- short-circuiting.", key);
                return null;
            }
            return deserializeValue(value);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to translate value for key: " + key, e);
        }
    }

    /**
     * Put a value into the cache by its key.
     *
     * @param key of the value to put into the cache
     * @param value to put into the cache
     */
    public void put(K key, V value) {
        if (internal.isClosed())
            throw new IllegalStateException("Off-heap cache is closed -- cannot add a key/value to it: " + key);
        byte[] kb = serializeKey(key);
        byte[] vb = serializeValue(value);
        if (vb == null || vb.length == 0)
            log.warn(" ... zero-length serialized object being added ({}): {}", key, value);
        lock.writeLock().lock();
        try {
            internal.put(kb, vb);
        } catch (RocksDBException e) {
            throw new IllegalStateException("Unable to put value for key: " + key, e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Put all the provided entries into the cache.
     *
     * @param other cache of entries to add to the cache
     */
    public void putAll(AbstractOffHeapCache<K, V> other) {
        if (other != null) {
            if (internal.isClosed())
                throw new IllegalStateException("Off-heap cache is closed -- cannot bulk-add keys and values to it.");
            try (WriteBatch batch = new WriteBatch();
                    WriteOptions options = new WriteOptions()) {
                try (RocksIterator iterator = other.internal.newIterator()) {
                    for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
                        batch.put(iterator.key(), iterator.value());
                    }
                }
                lock.writeLock().lock();
                try {
                    internal.write(options, batch);
                } finally {
                    lock.writeLock().unlock();
                }
            } catch (RocksDBException e) {
                throw new IllegalStateException("Error putting all values into cache.", e);
            }
        }
    }

    /**
     * Check whether the cache has a value in it with the provided key.
     *
     * @param key of the value to check exists in the cache
     * @return true if and only if the cache has a value with this key in it
     */
    public boolean containsKey(K key) {
        if (internal.isClosed()) return false;
        byte[] kb = serializeKey(key);
        lock.readLock().lock();
        try {
            return internal.keyExists(kb);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Retrieve the number of entries currently held in the cache.
     *
     * @return the number of entries currently in the cache
     */
    public long size() {
        return entrySet().count();
    }

    /**
     * Retrieve the number of entries currently held in the cache.
     *
     * @return the number of entries currently in the cache
     */
    public long getSize() {
        return size();
    }

    /**
     * Indicates whether the cache has no entries.
     *
     * @return true if the cache has no entries, otherwise false
     */
    public boolean isEmpty() {
        return size() == 0;
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
     * Retrieve all the values held in the cache.
     *
     * @return a stream of all values held in the cache
     */
    public Stream<V> values() {
        if (internal.isClosed()) return Stream.empty();
        return new EntryIterator<>(this, internal.newIterator()).stream().map(Map.Entry::getValue);
    }

    /**
     * Retrieve all entries held in the cache.
     *
     * @return an entry set of all keys (and their values) held in the cache
     */
    public Stream<Map.Entry<K, V>> entrySet() {
        if (internal.isClosed()) return Stream.empty();
        return new EntryIterator<>(this, internal.newIterator()).stream();
    }

    /**
     * Indicates whether the cache has already been closed.
     *
     * @return true if the cache has been closed, otherwise false
     */
    public boolean isNotClosed() {
        return !internal.isClosed();
    }

    /** Clean up the cache, once it is no longer needed. */
    @Override
    public void close() {
        log.debug("Closing off-heap cache ({}): {}", getName(), backingStore);
        lock.writeLock().lock();
        try {
            internal.close();
            File file = backingStore.toFile();
            if (file.exists() && file.isDirectory()) {
                deleteDirectory(backingStore);
            }
        } catch (IOException e) {
            log.warn("Unable to remove backing store for off-heap cache -- leaving it behind.");
            log.debug("Full details: ", e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void deleteDirectory(Path directory) throws IOException {
        Files.walkFileTree(directory, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Iterator and streaming over entries in the cache, since it could be too large to fit into memory.
     *
     * @param <K> type of the keys in the cache
     * @param <V> type of the values in the cache
     */
    private static final class EntryIterator<K, V> implements Iterator<Map.Entry<K, V>>, AtlanCloseable {
        private final AbstractOffHeapCache<K, V> cache;
        private final RocksIterator iterator;

        public EntryIterator(AbstractOffHeapCache<K, V> cache, RocksIterator iterator) {
            this.cache = cache;
            this.iterator = iterator;
            this.iterator.seekToFirst(); // Start from the first entry
        }

        /** {@inheritDoc} */
        @Override
        public boolean hasNext() {
            return iterator.isValid();
        }

        /** {@inheritDoc} */
        @Override
        public Map.Entry<K, V> next() {
            if (!hasNext()) {
                throw new IllegalStateException("No more elements in the cache.");
            }
            byte[] key = iterator.key();
            byte[] value = iterator.value();
            try {
                Map.Entry<K, V> entry =
                        new AbstractMap.SimpleEntry<>(cache.deserializeKey(key), cache.deserializeValue(value));
                iterator.next(); // Move to the next entry
                return entry;
            } catch (IOException e) {
                throw new IllegalStateException("Unable to deserialize value.", e);
            }
        }

        /** {@inheritDoc} */
        @Override
        public void close() {
            iterator.close();
        }

        /** Convert this iterator into a stream of cache entries. */
        public Stream<Map.Entry<K, V>> stream() {
            return StreamSupport.stream(
                            Spliterators.spliteratorUnknownSize(this, Spliterator.ORDERED | Spliterator.NONNULL), false)
                    .onClose(this::close);
        }
    }
}
