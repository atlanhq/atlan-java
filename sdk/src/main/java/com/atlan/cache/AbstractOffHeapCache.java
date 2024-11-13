/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.AtlanClient;
import com.atlan.model.core.AtlanObject;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
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
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import org.rocksdb.WriteBatch;
import org.rocksdb.WriteOptions;

/**
 * Generic class through which to cache any objects efficiently, off-heap, to avoid risking extreme
 * memory usage.
 */
@Slf4j
class AbstractOffHeapCache<T extends AtlanObject> implements Closeable {

    private final AtlanClient client;
    private final Path backingStore;
    private volatile RocksDB internal;
    private volatile AtomicLong count = new AtomicLong(0L);

    @Getter
    private final String name;

    static {
        RocksDB.loadLibrary();
    }

    /**
     * Construct new object cache.
     *
     * @param client connectivity to the Atlan tenant
     * @param name to distinguish which cache is which
     */
    public AbstractOffHeapCache(AtlanClient client, String name) {
        this.client = client;
        this.name = name;
        try (Options options = new Options().setCreateIfMissing(true)) {
            backingStore = Files.createTempDirectory("rdb_" + name + "_");
            internal = RocksDB.open(options, backingStore.toString());
        } catch (IOException | RocksDBException e) {
            throw new RuntimeException("Unable to create off-heap cache for tracking.", e);
        }
    }

    private static byte[] serializeKey(String key) {
        return key.getBytes(StandardCharsets.UTF_8);
    }

    private static String deserializeKey(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private byte[] serializeValue(T value) {
        byte[] typeName = value.getClass().getCanonicalName().getBytes(StandardCharsets.UTF_8);
        int typeNameLength = typeName.length;
        try {
            byte[] json = client.writeValueAsBytes(value);
            ByteBuffer buffer = ByteBuffer.allocate(typeNameLength + 4 + json.length);
            buffer.putInt(typeNameLength);
            buffer.put(typeName);
            buffer.put(json);
            return buffer.array();
        } catch (IOException e) {
            throw new RuntimeException("Unable to serialize value.", e);
        }
    }

    private static Object _deserializeValue(AtlanClient client, byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int typeNameLength = buffer.getInt();
        byte[] typeNameBytes = new byte[typeNameLength];
        buffer.get(typeNameBytes);
        String typeName = new String(typeNameBytes, StandardCharsets.UTF_8);
        try {
            Class<?> type = Class.forName(typeName);
            byte[] json = new byte[buffer.remaining()];
            buffer.get(json);
            return client.readValue(json, type);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to find type: " + typeName + ".", e);
        } catch (IOException e) {
            throw new RuntimeException("Unable to deserialize value.", e);
        }
    }

    @SuppressWarnings("unchecked")
    private T deserializeValue(byte[] bytes) throws IOException {
        return (T) _deserializeValue(client, bytes);
    }

    /**
     * Retrieve an object from the cache by its ID.
     *
     * @param id of the object to retrieve
     * @return the object with that UUID, or null if it is not in the cache
     */
    public T get(String id) {
        byte[] key = serializeKey(id);
        try {
            byte[] value = internal.get(key);
            return deserializeValue(value);
        } catch (RocksDBException e) {
            throw new IllegalStateException("Unable to get value for key: " + id, e);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to translate value for key: " + id, e);
        }
    }

    /**
     * Put an object into the cache by its ID.
     *
     * @param id of the object to put into the cache
     * @param object to put into the cache
     * @return any object that was previously cached with the same UUID, or null if no such UUID has ever been cached
     */
    protected T put(String id, T object) {
        byte[] key = serializeKey(id);
        byte[] value = serializeValue(object);
        try {
            byte[] existing = internal.get(key);
            internal.put(key, value);
            if (existing == null) {
                count.getAndIncrement();
            } else {
                return deserializeValue(existing);
            }
        } catch (RocksDBException e) {
            throw new IllegalStateException("Unable to put value for key: " + id, e);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to translate existing value for key: " + id, e);
        }
        return null;
    }

    /**
     * Put all the provided entries into the cache.
     *
     * @param other cache of entries to add to the cache
     */
    protected void putAll(AbstractOffHeapCache<T> other) {
        try (WriteBatch batch = new WriteBatch();
                WriteOptions options = new WriteOptions()) {
            try (RocksIterator iterator = other.internal.newIterator()) {
                for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
                    batch.put(iterator.key(), iterator.value());
                }
            }
            internal.write(options, batch);
        } catch (RocksDBException e) {
            throw new IllegalStateException("Error putting all values into cache.", e);
        }
    }

    /**
     * Check whether the cache has an object in it with the provided UUID.
     *
     * @param id of the object to check exists in the cache
     * @return true if and only if the cache has an object with this UUID in it
     */
    public boolean containsKey(String id) {
        byte[] key = serializeKey(id);
        return internal.keyExists(key);
    }

    /**
     * Retrieve the number of objects currently held in the cache.
     *
     * @return the number of objects currently in the cache
     */
    public long size() {
        return count.get();
    }

    /**
     * Retrieve the number of objects currently held in the cache.
     *
     * @return the number of objects currently in the cache
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
        return count.get() == 0;
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
     * Retrieve all the objects held in the cache.
     *
     * @return a collection of all objects held in the cache
     */
    public Stream<T> values() {
        return new EntryIterator<T>(client, internal.newIterator()).stream().map(Map.Entry::getValue);
    }

    /**
     * Retrieve all entries held in the cache.
     *
     * @return an entry set of all objects (and keys) held in the cache
     */
    public Stream<Map.Entry<String, T>> entrySet() {
        return new EntryIterator<T>(client, internal.newIterator()).stream();
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
        log.debug("Closing off-heap cache ({}): {}", getName(), backingStore);
        internal.close();
        File file = backingStore.toFile();
        if (file.exists() && file.isDirectory()) {
            deleteDirectory(backingStore);
            log.debug(" ... cache deleted.");
        } else {
            log.debug(" ... cache already deleted.");
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
     * @param <T> object type to iterate over
     */
    static final class EntryIterator<T extends AtlanObject> implements Iterator<Map.Entry<String, T>>, AutoCloseable {
        private final AtlanClient client;
        private final RocksIterator iterator;

        public EntryIterator(AtlanClient client, RocksIterator iterator) {
            this.client = client;
            this.iterator = iterator;
            this.iterator.seekToFirst(); // Start from the first entry
        }

        @SuppressWarnings("unchecked")
        private T deserializeValue(byte[] bytes) throws IOException {
            return (T) _deserializeValue(client, bytes);
        }

        /** {@inheritDoc} */
        @Override
        public boolean hasNext() {
            return iterator.isValid();
        }

        /** {@inheritDoc} */
        @Override
        public Map.Entry<String, T> next() {
            if (!hasNext()) {
                throw new IllegalStateException("No more elements in the cache.");
            }
            byte[] key = iterator.key();
            byte[] value = iterator.value();
            try {
                Map.Entry<String, T> entry =
                        new AbstractMap.SimpleEntry<>(deserializeKey(key), deserializeValue(value));
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
        public Stream<Map.Entry<String, T>> stream() {
            return StreamSupport.stream(
                            Spliterators.spliteratorUnknownSize(this, Spliterator.ORDERED | Spliterator.NONNULL), false)
                    .onClose(this::close);
        }
    }
}
