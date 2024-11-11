/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.LogicException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.AtlanObject;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for reusable components that are common to all caches, where
 * a cache is populated en-masse through batch refreshing.
 */
@Slf4j
public abstract class AbstractMassCache<T extends AtlanObject> {

    private volatile Map<String, String> mapIdToName = new ConcurrentHashMap<>();
    private volatile Map<String, String> mapNameToId = new ConcurrentHashMap<>();
    private volatile Map<String, String> mapIdToSid = new ConcurrentHashMap<>();
    private volatile Map<String, String> mapSidToId = new ConcurrentHashMap<>();
    private volatile AbstractOffHeapCache<T> mapIdToObject;

    protected final ReadWriteLock lock = new ReentrantReadWriteLock();

    /** Whether to refresh the cache by retrieving all objects up-front (true) or lazily, on-demand (false). */
    @Getter
    protected AtomicBoolean bulkRefresh = new AtomicBoolean(true);

    /**
     * Initializes a new off-heap cache for the objects themselves.
     * This must be implemented, to include an efficient initial query for an exemplar value and anticipated
     * overall size of the cache, through which to initialize the off-heap cache.
     *
     * @param name of the off-heap cache
     * @param totalCapacity maximum number of objects expected in the cache
     * @param exemplar object that is a representative example of what will be in the cache
     * @param valueClazz class of objects that will be in the cache
     * @throws AtlanException on any error communicating with Atlan
     */
    protected void initializeOffHeap(String name, int totalCapacity, T exemplar, Class<T> valueClazz)
            throws AtlanException {
        if (mapIdToObject != null) {
            try {
                mapIdToObject.close();
            } catch (IOException e) {
                throw new LogicException(ErrorCode.ERROR_PASSTHROUGH, e);
            }
        }
        mapIdToObject = new AbstractOffHeapCache<>(name, totalCapacity, exemplar, valueClazz);
    }

    /**
     * Wraps the cache refresh with necessary concurrency controls.
     * Always call this method to actually update a cache, not the directly-implemented, cache-specific
     * {@code refreshCache}.
     *
     * @throws AtlanException on any error communicating with Atlan to refresh the cache of objects
     */
    public void refresh() throws AtlanException {
        lock.writeLock().lock();
        try {
            mapIdToName.clear();
            mapNameToId.clear();
            refreshCache();
        } finally {
            lock.writeLock().unlock();
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
            refresh();
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
            refresh();
        } else {
            throw new InvalidRequestException(ErrorCode.CANNOT_CACHE_REFRESH_BY_SID);
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
            refresh();
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
        if (object != null) {
            mapIdToObject.put(id, object);
        }
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
        mapIdToName.put(id, name);
        mapNameToId.put(name, id);
        mapIdToSid.put(id, sid);
        mapSidToId.put(sid, id);
        if (object != null) {
            mapIdToObject.put(id, object);
        }
    }

    /**
     * Whether the object-storing portion of the cache is empty (true) or not (false).
     *
     * @return true if no objects are cached, otherwise false
     */
    protected boolean isEmpty() {
        lock.readLock().lock();
        try {
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
    protected Set<Map.Entry<UUID, T>> entrySet() {
        lock.readLock().lock();
        try {
            return mapIdToObject.internal.entrySet();
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
            return mapNameToId.containsKey(name);
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
            return mapIdToName.containsKey(id);
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
        lock.readLock().lock();
        try {
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
        if (name != null && !name.isEmpty()) {
            String id = getIdFromName(name);
            if (id == null && allowRefresh) {
                // If not found, refresh the cache and look again (could be stale)
                cacheByName(name);
                id = getIdFromName(name);
            }
            if (id == null) {
                throw new NotFoundException(ErrorCode.ID_NOT_FOUND_BY_NAME, name);
            }
            return id;
        } else {
            throw new InvalidRequestException(ErrorCode.MISSING_NAME);
        }
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
        if (name != null && !name.isEmpty()) {
            String id = getSidFromName(name);
            if (id == null && allowRefresh) {
                // If not found, refresh the cache and look again (could be stale)
                cacheByName(name);
                id = getSidFromName(name);
            }
            if (id == null) {
                throw new NotFoundException(ErrorCode.ID_NOT_FOUND_BY_NAME, name);
            }
            return id;
        } else {
            throw new InvalidRequestException(ErrorCode.MISSING_NAME);
        }
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
        if (id != null && !id.isEmpty()) {
            String name = getNameFromId(id);
            if (name == null && allowRefresh) {
                // If not found, refresh the cache and look again (could be stale)
                cacheById(id);
                name = getNameFromId(id);
            }
            if (name == null) {
                throw new NotFoundException(ErrorCode.NAME_NOT_FOUND_BY_ID, id);
            }
            return name;
        } else {
            throw new InvalidRequestException(ErrorCode.MISSING_ID);
        }
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
        if (sid != null && !sid.isEmpty()) {
            String name = getNameFromSid(sid);
            if (name == null && allowRefresh) {
                // If not found, refresh the cache and look again (could be stale)
                cacheBySid(sid);
                name = getNameFromSid(sid);
            }
            if (name == null) {
                throw new NotFoundException(ErrorCode.NAME_NOT_FOUND_BY_ID, sid);
            }
            return name;
        } else {
            throw new InvalidRequestException(ErrorCode.MISSING_ID);
        }
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
        if (id != null && !id.isEmpty()) {
            T result = getObjectById(id);
            if (result == null && allowRefresh) {
                // If not found, refresh the cache and look again (could be stale)
                cacheById(id);
                result = getObjectById(id);
            }
            if (result == null) {
                throw new NotFoundException(ErrorCode.NAME_NOT_FOUND_BY_ID, id);
            }
            return result;
        } else {
            throw new InvalidRequestException(ErrorCode.MISSING_ID);
        }
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
        if (name != null && !name.isEmpty()) {
            String id = getIdForName(name, allowRefresh);
            return getById(id, false);
        } else {
            throw new InvalidRequestException(ErrorCode.MISSING_NAME);
        }
    }
}
