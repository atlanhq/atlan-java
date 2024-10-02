/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.cache;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.atlan.model.core.AtlanObject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for reusable components that are common to all caches, where
 * a cache is populated en-masse through batch refreshing.
 */
@Slf4j
public abstract class AbstractMassCache<T extends AtlanObject> {

    protected final Map<String, String> mapIdToName = new ConcurrentHashMap<>();
    protected final Map<String, String> mapNameToId = new ConcurrentHashMap<>();
    protected final Map<String, T> mapIdToObject = new ConcurrentHashMap<>();

    /** Whether to refresh the cache by retrieving all objects up-front (true) or lazily, on-demand (false). */
    @Getter
    protected AtomicBoolean bulkRefresh = new AtomicBoolean(true);

    /**
     * Logic to refresh the cache of objects from Atlan.
     *
     * @throws AtlanException on any error communicating with Atlan to refresh the cache of objects
     */
    public synchronized void refreshCache() throws AtlanException {
        mapIdToName.clear();
        mapNameToId.clear();
        mapIdToObject.clear();
    }

    /**
     * Logic to look up a single object for the cache.
     *
     * @param id unique internal identifier for the object
     * @throws AtlanException on any error communicating with Atlan
     */
    public abstract void lookupById(String id) throws AtlanException;

    /**
     * Logic to look up a single object for the cache.
     *
     * @param name unique name for the object
     * @throws AtlanException on any error communicating with Atlan
     */
    public abstract void lookupByName(String name) throws AtlanException;

    /**
     * Add an entry to the cache
     *
     * @param id Atlan-internal ID
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
     * Checks whether the provided human-readable name is known.
     * (Note: will not refresh the cache itself to determine this.)
     *
     * @param name human-readable name of the object
     * @return true if the object is known, false otherwise
     */
    public boolean isNameKnown(String name) {
        return mapNameToId.containsKey(name);
    }

    /**
     * Checks whether the provided Atlan-internal ID string is known.
     * (Note: will not refresh the cache itself to determine this.)
     *
     * @param id Atlan-internal ID string of the object
     * @return true if the object is known, false otherwise
     */
    public boolean isIdKnown(String id) {
        return mapIdToName.containsKey(id);
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
            String id = mapNameToId.get(name);
            if (id == null && allowRefresh) {
                // If not found, refresh the cache and look again (could be stale)
                if (bulkRefresh.get()) {
                    refreshCache();
                } else {
                    lookupByName(name);
                }
                id = mapNameToId.get(name);
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
            String name = mapIdToName.get(id);
            if (name == null && allowRefresh) {
                // If not found, refresh the cache and look again (could be stale)
                if (bulkRefresh.get()) {
                    refreshCache();
                } else {
                    lookupById(id);
                }
                name = mapIdToName.get(id);
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
            T result = mapIdToObject.get(id);
            if (result == null && allowRefresh) {
                // If not found, refresh the cache and look again (could be stale)
                if (bulkRefresh.get()) {
                    refreshCache();
                } else {
                    lookupById(id);
                }
                result = mapIdToObject.get(id);
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
