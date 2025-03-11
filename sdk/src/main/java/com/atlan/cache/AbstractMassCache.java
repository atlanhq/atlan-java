/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.AtlanObject;
import java.io.Closeable;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for reusable components that are common to all caches, where
 * a cache is populated en-masse through batch refreshing.
 */
@Slf4j
public abstract class AbstractMassCache<T extends AtlanObject> implements Closeable {

    protected final AtlanClient client;
    private final String cacheName;

    private volatile Map<String, String> mapIdToName = new ConcurrentHashMap<>();
    private volatile Map<String, String> mapNameToId = new ConcurrentHashMap<>();
    private volatile Map<String, String> mapIdToSid = new ConcurrentHashMap<>();
    private volatile Map<String, String> mapSidToId = new ConcurrentHashMap<>();
    private volatile OffHeapObjectCache<T> mapIdToObject;

    protected final ReadWriteLock lock = new ReentrantReadWriteLock();
    protected final ReentrantLock refreshLock = new ReentrantLock();
    protected final AtomicBoolean needsRefresh = new AtomicBoolean(true);
    protected final AtomicLong lastRefresh = new AtomicLong(Long.MIN_VALUE);

    /** Whether to refresh the cache by retrieving all objects up-front (true) or lazily, on-demand (false). */
    @Getter
    protected AtomicBoolean bulkRefresh = new AtomicBoolean(true);

    /**
     * Define a new mass cache with the provided details.
     * Note: ideally, before using, also set the total capacity.
     *
     * @param cacheName name of the cache
     */
    public AbstractMassCache(AtlanClient client, String cacheName) {
        this.client = client;
        this.cacheName = cacheName;
        this.mapIdToObject = new OffHeapObjectCache<>(client, cacheName);
    }

    /**
     * Initializes a new off-heap cache for the objects themselves.
     * This will be automatically called either when an entry is first added or the cache as a whole is refreshed.
     */
    private void resetOffHeap() {
        if (mapIdToObject != null) mapIdToObject.close();
        mapIdToObject = new OffHeapObjectCache<>(client, cacheName);
    }

    /**
     * Wraps the cache refresh with necessary concurrency controls, and forces a refresh.
     * Always call this method to actually update a cache, not the directly-implemented, cache-specific
     * {@code refreshCache}.
     *
     * @throws AtlanException on any error communicating with Atlan to refresh the cache of objects
     */
    public void forceRefresh() throws AtlanException {
        refreshIfNeeded(true);
    }

    /**
     * Wraps the cache refresh with necessary concurrency controls, but only refreshes the cache if it
     * has not already been refreshed.
     *
     * @throws AtlanException on any error communicating with Atlan to refresh the cache of objects
     */
    public void refreshIfNeeded() throws AtlanException {
        refreshIfNeeded(false);
    }

    /**
     * Actually handle refreshing the cache, blocking all concurrent reads or writes while it is being refreshed.
     * @param force if true, force the cache to be refreshed
     *
     * @throws AtlanException on any error communicating with Atlan to refresh the cache of objects
     */
    private void refreshIfNeeded(boolean force) throws AtlanException {
        if (force || needsRefresh.compareAndSet(true, false)) {
            refreshLock.lock();
            try {
                long now = System.currentTimeMillis();
                lock.writeLock().lock();
                try {
                    mapIdToName.clear();
                    mapNameToId.clear();
                    mapIdToSid.clear();
                    mapSidToId.clear();
                    resetOffHeap();
                    refreshCache();
                } finally {
                    lock.writeLock().unlock();
                }
                lastRefresh.set(now);
            } finally {
                refreshLock.unlock();
            }
        } else {
            refreshLock.lock();
            try {
                // Just wait until the first thread refreshes the cache before proceeding
            } finally {
                refreshLock.unlock();
            }
        }
    }

    /**
     * Wraps a single object lookup for the cache with necessary concurrency controls.
     *
     * @param id unique internal identifier for the object
     * @throws AtlanException on any error communicating with Atlan
     */
    public void cacheById(String id) throws AtlanException {
        if (bulkRefresh.get()) {
            refreshIfNeeded();
        } else {
            lock.writeLock().lock();
            try {
                lookupById(id);
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    /**
     * Wraps a single object lookup for the cache with necessary concurrency controls.
     *
     * @param sid unique secondary internal identifier for the object
     * @throws AtlanException on any error communicating with Atlan
     */
    public void cacheBySid(String sid) throws AtlanException {
        if (bulkRefresh.get()) {
            refreshIfNeeded();
        } else {
            lock.writeLock().lock();
            try {
                lookupBySid(sid);
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    /**
     * Wraps a single object lookup for the cache with necessary concurrency controls.
     *
     * @param name unique name for the object
     * @throws AtlanException on any error communicating with Atlan
     */
    public void cacheByName(String name) throws AtlanException {
        if (bulkRefresh.get()) {
            refreshIfNeeded();
        } else {
            lock.writeLock().lock();
            try {
                lookupByName(name);
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    /**
     * Logic to refresh a specific cache en-masse (must be implemented).
     *
     * @throws AtlanException on any error communicating with Atlan to refresh the cache of objects
     */
    protected abstract void refreshCache() throws AtlanException;

    /**
     * Logic to look up a single object for the cache.
     *
     * @param id unique internal identifier for the object
     * @throws AtlanException on any error communicating with Atlan
     */
    protected abstract void lookupById(String id) throws AtlanException;

    /**
     * Logic to look up a single object for the cache.
     * Note: by default this is not implemented (and will immediately error), so override it if you intend the cache
     * to be populated by secondary ID lookups.
     *
     * @param sid unique secondary internal identifier for the object
     * @throws AtlanException on any error communicating with Atlan
     * @throws InvalidRequestException if not overridden with logic to update cache by secondary ID lookups
     */
    protected void lookupBySid(String sid) throws AtlanException {
        throw new InvalidRequestException(ErrorCode.CANNOT_CACHE_REFRESH_BY_SID);
    }

    /**
     * Logic to look up a single object for the cache.
     *
     * @param name unique name for the object
     * @throws AtlanException on any error communicating with Atlan
     */
    protected abstract void lookupByName(String name) throws AtlanException;

    /**
     * Add an entry to the cache.
     * This should only be called by the lookup methods, which themselves should never directly
     * be invoked.
     *
     * @param id Atlan-internal ID (UUID)
     * @param name human-readable name
     * @param object the object to cache (if any)
     */
    protected void cache(String id, String name, T object) {
        mapIdToName.put(id, name);
        mapNameToId.put(name, id);
        mapIdToObject.put(id, object);
    }

    /**
     * Add an entry to the cache.
     * This should only be called by the lookup methods, which themselves should never directly
     * be invoked.
     *
     * @param id Atlan-internal ID (UUID)
     * @param sid secondary Atlan-internal ID (hashed-string / nanoID)
     * @param name human-readable name
     * @param object the object to cache (if any)
     */
    protected void cache(String id, String sid, String name, T object) {
        cache(id, name, object);
        mapIdToSid.put(id, sid);
        mapSidToId.put(sid, id);
    }

    /**
     * Whether the object-storing portion of the cache is empty (true) or not (false).
     *
     * @return true if no objects are cached, otherwise false
     */
    protected boolean isEmpty() {
        lock.readLock().lock();
        try {
            if (mapIdToObject == null) return true;
            return mapIdToObject.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Retrieve an iterable set of entries for the object-storing portion of the cache.
     *
     * @return an iterable set of entries of objects that are cached
     */
    protected Stream<Map.Entry<String, T>> entrySet() {
        lock.readLock().lock();
        try {
            if (mapIdToObject == null) return Stream.empty();
            return mapIdToObject.entrySet();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Checks whether the provided human-readable name is known.
     * (Note: will not refresh the cache itself to determine this.)
     *
     * @param name human-readable name of the object
     * @return true if the object is known, false otherwise
     */
    public boolean isNameKnown(String name) {
        lock.readLock().lock();
        try {
            String id = mapNameToId.get(name);
            if (id == null) return false;
            return mapIdToObject.containsKey(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Checks whether the provided Atlan-internal ID string is known.
     * (Note: will not refresh the cache itself to determine this.)
     *
     * @param id Atlan-internal ID string of the object
     * @return true if the object is known, false otherwise
     */
    public boolean isIdKnown(String id) {
        lock.readLock().lock();
        try {
            return mapIdToObject.containsKey(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Thread-safe cache retrieval of the ID of an object by its name.
     *
     * @param name of the object
     * @return the ID of the object (if cached), or null
     */
    protected String getIdFromName(String name) {
        if (name == null) return null;
        lock.readLock().lock();
        try {
            return mapNameToId.get(name);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Thread-safe cache retrieval of the name of an object by its ID.
     *
     * @param id of the object
     * @return the name of the object (if cached), or null
     */
    protected String getNameFromId(String id) {
        if (id == null) return null;
        return getNameFromId(id, false);
    }

    /**
     * Thread-safe cache retrieval of the name of an object by its ID, when not bypassing the read lock.
     * Note: this allows you to bypass the read lock, in order to avoid potential deadlock situations,
     * however you should ONLY do this if you know PRECISELY that you are still controlling the ordering
     * of reads and writes (as concurrency safety will be bypassed when the read lock is bypassed) -- if
     * you are not careful you may get a cache miss which would otherwise have been a cache hit.
     *
     * @param id of the object
     * @param bypassReadLock whether to bypass the read lock (necessary if we're reading while inside a write lock)
     * @return the name of the object (if cached), or null
     */
    protected String getNameFromId(String id, boolean bypassReadLock) {
        if (id == null) return null;
        if (bypassReadLock) {
            return getNameFromIdWithoutLock(id);
        } else {
            return getNameFromIdWithLock(id);
        }
    }

    private String getNameFromIdWithoutLock(String id) {
        return mapIdToName.get(id);
    }

    private String getNameFromIdWithLock(String id) {
        lock.readLock().lock();
        try {
            return mapIdToName.get(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Thread-safe cache retrieval of the ID of an object by its secondary ID.
     *
     * @param sid of the object
     * @return the ID of the object (if cached), or null
     */
    protected String getIdFromSid(String sid) {
        if (sid == null) return null;
        lock.readLock().lock();
        try {
            return mapSidToId.get(sid);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Thread-safe cache retrieval of the secondary ID of an object by its ID.
     *
     * @param id of the object
     * @return the secondary ID of the object (if cached), or null
     */
    protected String getSidFromId(String id) {
        if (id == null) return null;
        lock.readLock().lock();
        try {
            return mapIdToSid.get(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Thread-safe cache retrieval of the secondary ID of an object by its name.
     *
     * @param name of the object
     * @return the secondary ID of the object (if cached), or null
     */
    protected String getSidFromName(String name) {
        String id = getIdFromName(name);
        return getSidFromId(id);
    }

    /**
     * Thread-safe cache retrieval of the name of an object by its secondary ID.
     *
     * @param sid of the object
     * @return the name of the object (if cached), or null
     */
    protected String getNameFromSid(String sid) {
        String id = getIdFromSid(sid);
        return getNameFromId(id);
    }

    /**
     * Thread-safe cache retrieval of the object itself, by its ID.
     *
     * @param id of the object
     * @return the object itself (if cached), or null
     */
    protected T getObjectById(String id) {
        if (id == null) return null;
        lock.readLock().lock();
        try {
            if (mapIdToObject == null) return null;
            return mapIdToObject.get(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Translate the provided human-readable name to its Atlan-internal ID string.
     *
     * @param name human-readable name of the object
     * @return unique Atlan-internal ID string for the object
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the object to retrieve
     */
    public String getIdForName(String name) throws AtlanException {
        return getIdForName(name, true);
    }

    /**
     * Translate the provided human-readable name to its Atlan-internal ID string.
     *
     * @param name human-readable name of the object
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return unique Atlan-internal ID string for the object
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the object to retrieve
     */
    public String getIdForName(String name, boolean allowRefresh) throws AtlanException {
        if (name == null || name.isEmpty()) throw new InvalidRequestException(ErrorCode.MISSING_NAME);
        if (allowRefresh) {
            String id = getIdFromName(name);
            if (id == null) {
                // If not found, refresh the cache and look again (could be stale)
                cacheByName(name);
                id = getIdFromName(name);
            }
            return checkIdForName(id, name);
        } else {
            return checkIdForName(getIdFromName(name), name);
        }
    }

    private String checkIdForName(String id, String name) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ID_NOT_FOUND_BY_NAME, cacheName, name);
        }
        return id;
    }

    /**
     * Translate the provided Atlan-internal secondary ID to its Atlan-internal ID string.
     *
     * @param sid Atlan-internal secondary ID
     * @return unique Atlan-internal ID string for the object
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the object to retrieve
     */
    public String getIdForSid(String sid) throws AtlanException {
        return getIdForSid(sid, true);
    }

    /**
     * Translate the provided Atlan-internal secondary ID to its Atlan-internal ID string.
     *
     * @param sid Atlan-internal secondary ID
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return unique Atlan-internal ID string for the object
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the object to retrieve
     */
    public String getIdForSid(String sid, boolean allowRefresh) throws AtlanException {
        if (sid == null || sid.isEmpty()) throw new InvalidRequestException(ErrorCode.MISSING_ID);
        if (allowRefresh) {
            String id = getIdFromSid(sid);
            if (id == null) {
                // If not found, refresh the cache and look again (could be stale)
                cacheBySid(sid);
                id = getIdFromSid(sid);
            }
            return checkIdForSid(id, sid);
        } else {
            return checkIdForSid(getIdFromSid(sid), sid);
        }
    }

    private String checkIdForSid(String id, String sid) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ID_NOT_FOUND_BY_SID, cacheName, sid);
        }
        return id;
    }

    /**
     * Translate the provided human-readable name to its Atlan-internal secondary ID string.
     *
     * @param name human-readable name of the object
     * @return unique Atlan-internal secondary ID string for the object
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the object to retrieve
     */
    public String getSidForName(String name) throws AtlanException {
        return getSidForName(name, true);
    }

    /**
     * Translate the provided human-readable name to its Atlan-internal secondary ID string.
     *
     * @param name human-readable name of the object
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return unique Atlan-internal secondary ID string for the object
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the object to retrieve
     */
    public String getSidForName(String name, boolean allowRefresh) throws AtlanException {
        if (name == null || name.isEmpty()) throw new InvalidRequestException(ErrorCode.MISSING_NAME);
        if (allowRefresh) {
            String sid = getSidFromName(name);
            if (sid == null) {
                // If not found, refresh the cache and look again (could be stale)
                cacheByName(name);
                sid = getSidFromName(name);
            }
            return checkSidForName(sid, name);
        } else {
            return checkSidForName(getSidFromName(name), name);
        }
    }

    private String checkSidForName(String sid, String name) throws AtlanException {
        if (sid == null) {
            throw new NotFoundException(ErrorCode.ID_NOT_FOUND_BY_NAME, cacheName, name);
        }
        return sid;
    }

    /**
     * Translate the provided Atlan-internal ID string to the human-readable name for the object.
     *
     * @param id Atlan-internal ID string
     * @return human-readable name of the object
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the object to retrieve
     */
    public String getNameForId(String id) throws AtlanException {
        return getNameForId(id, true);
    }

    /**
     * Translate the provided Atlan-internal ID string to the human-readable name for the object.
     *
     * @param id Atlan-internal ID string
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return human-readable name of the object
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the object to retrieve
     */
    public String getNameForId(String id, boolean allowRefresh) throws AtlanException {
        if (id == null || id.isEmpty()) throw new InvalidRequestException(ErrorCode.MISSING_ID);
        if (allowRefresh) {
            String name = getNameFromId(id);
            if (name == null) {
                // If not found, refresh the cache and look again (could be stale)
                cacheById(id);
                name = getNameFromId(id);
            }
            return checkNameForId(name, id);
        } else {
            return checkNameForId(getNameFromId(id), id);
        }
    }

    private String checkNameForId(String name, String id) throws AtlanException {
        if (name == null) {
            throw new NotFoundException(ErrorCode.NAME_NOT_FOUND_BY_ID, cacheName, id);
        }
        return name;
    }

    /**
     * Translate the provided Atlan-internal secondary ID string to the human-readable name for the object.
     *
     * @param sid Atlan-internal secondary ID string
     * @return human-readable name of the object
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the object to retrieve
     */
    public String getNameForSid(String sid) throws AtlanException {
        return getNameForSid(sid, true);
    }

    /**
     * Translate the provided Atlan-internal secondary ID string to the human-readable name for the object.
     *
     * @param sid Atlan-internal secondary ID string
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return human-readable name of the object
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the object to retrieve
     */
    public String getNameForSid(String sid, boolean allowRefresh) throws AtlanException {
        if (sid == null || sid.isEmpty()) throw new InvalidRequestException(ErrorCode.MISSING_ID);
        if (allowRefresh) {
            String name = getNameFromSid(sid);
            if (name == null) {
                // If not found, refresh the cache and look again (could be stale)
                cacheBySid(sid);
                name = getNameFromSid(sid);
            }
            return checkNameForSid(name, sid);
        } else {
            return checkNameForSid(getNameFromSid(sid), sid);
        }
    }

    private String checkNameForSid(String name, String sid) throws AtlanException {
        if (name == null) {
            throw new NotFoundException(ErrorCode.NAME_NOT_FOUND_BY_ID, cacheName, sid);
        }
        return name;
    }

    /**
     * Retrieve the actual object by Atlan-internal ID string.
     *
     * @param id Atlan-internal ID string
     * @return the object with that ID
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no ID was provided for the object to retrieve
     */
    public T getById(String id) throws AtlanException {
        return getById(id, true);
    }

    /**
     * Retrieve the actual object by Atlan-internal ID string.
     *
     * @param id Atlan-internal ID string
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return the object with that ID
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no ID was provided for the object to retrieve
     */
    public T getById(String id, boolean allowRefresh) throws AtlanException {
        if (id == null || id.isEmpty()) throw new InvalidRequestException(ErrorCode.MISSING_ID);
        if (allowRefresh) {
            T result = getObjectById(id);
            if (result == null) {
                // If not found, refresh the cache and look again (could be stale)
                cacheById(id);
                result = getObjectById(id);
            }
            return checkById(result, id);
        } else {
            return checkById(getObjectById(id), id);
        }
    }

    private T checkById(T result, String id) throws AtlanException {
        if (result == null) {
            throw new NotFoundException(ErrorCode.NAME_NOT_FOUND_BY_ID, cacheName, id);
        }
        return result;
    }

    /**
     * Retrieve the actual object by Atlan-internal secondary ID string.
     *
     * @param sid Atlan-internal secondary ID string
     * @return the object with that secondary ID
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no ID was provided for the object to retrieve
     */
    public T getBySid(String sid) throws AtlanException {
        return getBySid(sid, true);
    }

    /**
     * Retrieve the actual object by Atlan-internal secondary ID string.
     *
     * @param sid Atlan-internal secondary ID string
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return the object with that secondary ID
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no ID was provided for the object to retrieve
     */
    public T getBySid(String sid, boolean allowRefresh) throws AtlanException {
        String id = getIdForSid(sid, allowRefresh);
        return getById(id, false);
    }

    /**
     * Retrieve the actual object by human-readable name.
     *
     * @param name human-readable name of the object
     * @return the object with that name
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the object to retrieve
     */
    public T getByName(String name) throws AtlanException {
        return getByName(name, true);
    }

    /**
     * Retrieve the actual object by human-readable name.
     *
     * @param name human-readable name of the object
     * @param allowRefresh whether to allow a refresh of the cache (true) or not (false)
     * @return the object with that name
     * @throws AtlanException on any API communication problem if the cache needs to be refreshed
     * @throws NotFoundException if the object cannot be found (does not exist) in Atlan
     * @throws InvalidRequestException if no name was provided for the object to retrieve
     */
    public T getByName(String name, boolean allowRefresh) throws AtlanException {
        String id = getIdForName(name, allowRefresh);
        return getById(id, false);
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        lock.writeLock().lock();
        try {
            if (mapIdToObject != null) {
                mapIdToObject.close();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}
